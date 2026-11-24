import { execFileSync } from "node:child_process";
import { readFileSync, writeFileSync, mkdirSync } from "node:fs";
import { dirname, join, relative } from "node:path";
import { fileURLToPath } from "node:url";
import { Agent, CursorAgentError } from "@cursor/sdk";

const __dirname = dirname(fileURLToPath(import.meta.url));
const repoRoot = join(__dirname, "..", "..");
const instructionsPath = join(__dirname, "INSTRUCTIONS.md");

const SOURCE_EXT = /\.(java|kt|kts)$/i;
const ZERO_SHA = /^0+$/;

function sh(args, options = {}) {
  return execFileSync("git", args, {
    cwd: repoRoot,
    encoding: "utf8",
    stdio: ["ignore", "pipe", "pipe"],
    ...options,
  }).trim();
}

function resolveRange() {
  const head = (process.env.REVIEW_HEAD || process.env.GITHUB_SHA || "").trim() || sh(["rev-parse", "HEAD"]);
  let base = (process.env.REVIEW_BASE || "").trim();
  if (ZERO_SHA.test(base)) {
    base = "";
  }

  if (!base && process.env.GITHUB_EVENT_BEFORE && !ZERO_SHA.test(process.env.GITHUB_EVENT_BEFORE)) {
    base = process.env.GITHUB_EVENT_BEFORE.trim();
  }

  if (!base) {
    try {
      base = sh(["rev-parse", "HEAD~1"]);
    } catch {
      base = "";
    }
  }

  return { base, head };
}

function listChangedSources(base, head) {
  if (!base) {
    return sh(["ls-tree", "-r", "--name-only", head])
      .split("\n")
      .map((line) => line.trim())
      .filter((line) => SOURCE_EXT.test(line));
  }

  return sh(["diff", "--name-only", "--diff-filter=ACMR", `${base}...${head}`])
    .split("\n")
    .map((line) => line.trim())
    .filter((line) => line && SOURCE_EXT.test(line));
}

function buildDiff(base, head, files) {
  if (files.length === 0) {
    return "";
  }

  if (!base) {
    const chunks = files.map((file) => {
      const content = sh(["show", `${head}:${file}`]);
      return `--- /dev/null\n+++ b/${file}\n${content
        .split("\n")
        .map((line) => `+${line}`)
        .join("\n")}`;
    });
    return chunks.join("\n\n");
  }

  return sh(["diff", "--unified=3", `${base}...${head}`, "--", ...files]);
}

function parseVerdict(text) {
  if (!text) {
    return null;
  }
  const matches = [...text.matchAll(/VERDICT:\s*(PASS|FAIL)\b/gi)];
  if (matches.length === 0) {
    return null;
  }
  return matches[matches.length - 1][1].toUpperCase();
}

async function main() {
  const apiKey = process.env.CURSOR_API_KEY;
  if (!apiKey) {
    console.error("Falta CURSOR_API_KEY. Agrégalo como secret del repositorio.");
    process.exit(1);
  }

  const { base, head } = resolveRange();
  const files = listChangedSources(base, head);
  const relativeFiles = files.map((file) => relative(repoRoot, join(repoRoot, file)) || file);

  console.log(`Base: ${base || "(sin commit previo)"}`);
  console.log(`Head: ${head}`);
  console.log(`Archivos fuente a revisar: ${relativeFiles.length}`);
  relativeFiles.forEach((file) => console.log(` - ${file}`));

  if (relativeFiles.length === 0) {
    console.log("Sin cambios Java/Kotlin en el commit. VERDICT: PASS");
    process.exit(0);
  }

  const diff = buildDiff(base, head, relativeFiles);
  if (!diff.trim()) {
    console.log("Diff vacío tras filtrar. VERDICT: PASS");
    process.exit(0);
  }

  const maxChars = Number(process.env.REVIEW_MAX_DIFF_CHARS || 180000);
  const clipped = diff.length > maxChars
    ? `${diff.slice(0, maxChars)}\n\n...[DIFF TRUNCADO A ${maxChars} CARACTERES]...`
    : diff;

  const instructions = readFileSync(instructionsPath, "utf8");
  const prompt = [
    instructions,
    "",
    "## DIFF a revisar",
    "Rango:",
    base ? `${base}...${head}` : `commit completo ${head}`,
    "",
    "Archivos:",
    relativeFiles.map((file) => `- ${file}`).join("\n"),
    "",
    "```diff",
    clipped,
    "```",
    "",
    "Revisa únicamente este DIFF según las reglas. No edites el repositorio.",
    "Cierra con VERDICT: PASS o VERDICT: FAIL.",
  ].join("\n");

  let result;
  try {
    result = await Agent.prompt(prompt, {
      apiKey,
      model: { id: process.env.CURSOR_MODEL || "composer-2.5" },
      local: {
        cwd: repoRoot,
        settingSources: [],
      },
    });
  } catch (error) {
    if (error instanceof CursorAgentError) {
      console.error(`Error al iniciar el agente: ${error.message}`);
      if (error.requestId) {
        console.error(`requestId=${error.requestId}`);
      }
      process.exit(1);
    }
    throw error;
  }

  const output = typeof result.result === "string" ? result.result : String(result.result ?? "");
  const outDir = join(repoRoot, "ci", "code-review", "out");
  mkdirSync(outDir, { recursive: true });
  writeFileSync(join(outDir, "review-output.md"), output, "utf8");

  console.log("--- Respuesta del agente ---");
  console.log(output);
  console.log("--- Fin respuesta ---");
  console.log(`status=${result.status}`);

  if (result.status === "error") {
    console.error("El agente terminó en error. Pipeline cortado.");
    process.exit(2);
  }

  const verdict = parseVerdict(output);
  if (verdict === "PASS") {
    console.log("Gate de nomenclatura: PASS");
    process.exit(0);
  }
  if (verdict === "FAIL") {
    console.error("Gate de nomenclatura: FAIL — corrige nombrado antes de desplegar.");
    process.exit(3);
  }

  console.error("No se encontró VERDICT: PASS|FAIL en la respuesta del agente.");
  process.exit(3);
}

main().catch((error) => {
  console.error(error);
  process.exit(1);
});
