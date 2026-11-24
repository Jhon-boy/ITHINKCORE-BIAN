#!/usr/bin/env python3
"""Gate de nomenclatura vía Cloud Agents API (sin SDK ni node_modules)."""

from __future__ import annotations

import json
import os
import re
import subprocess
import sys
import time
import urllib.error
import urllib.request
from pathlib import Path

REPO_ROOT = Path(__file__).resolve().parents[2]
INSTRUCTIONS = Path(__file__).resolve().parent / "INSTRUCTIONS.md"
OUT_DIR = Path(__file__).resolve().parent / "out"
API_BASE = "https://api.cursor.com/v1"
SOURCE_EXT = re.compile(r"\.(java|kt|kts)$", re.I)
ZERO_SHA = re.compile(r"^0+$")
TERMINAL = {"FINISHED", "ERROR", "CANCELLED", "EXPIRED"}


def sh(args: list[str]) -> str:
    return subprocess.check_output(["git", *args], cwd=REPO_ROOT, text=True).strip()


def resolve_range() -> tuple[str, str]:
    head = (os.environ.get("REVIEW_HEAD") or os.environ.get("GITHUB_SHA") or "").strip()
    if not head:
        head = sh(["rev-parse", "HEAD"])

    base = (os.environ.get("REVIEW_BASE") or "").strip()
    if ZERO_SHA.match(base or ""):
        base = ""

    before = (os.environ.get("GITHUB_EVENT_BEFORE") or "").strip()
    if not base and before and not ZERO_SHA.match(before):
        base = before

    if not base:
        try:
            base = sh(["rev-parse", "HEAD~1"])
        except subprocess.CalledProcessError:
            base = ""

    return base, head


def list_changed_sources(base: str, head: str) -> list[str]:
    if not base:
        files = sh(["ls-tree", "-r", "--name-only", head]).splitlines()
    else:
        files = sh(["diff", "--name-only", "--diff-filter=ACMR", f"{base}...{head}"]).splitlines()
    return [f.strip() for f in files if f.strip() and SOURCE_EXT.search(f)]


def build_diff(base: str, head: str, files: list[str]) -> str:
    if not files:
        return ""
    if not base:
        chunks = []
        for file in files:
            content = sh(["show", f"{head}:{file}"])
            body = "\n".join(f"+{line}" for line in content.splitlines())
            chunks.append(f"--- /dev/null\n+++ b/{file}\n{body}")
        return "\n\n".join(chunks)
    return sh(["diff", "--unified=3", f"{base}...{head}", "--", *files])


def api_request(method: str, path: str, api_key: str, body: dict | None = None) -> dict:
    data = None if body is None else json.dumps(body).encode("utf-8")
    req = urllib.request.Request(
        f"{API_BASE}{path}",
        data=data,
        method=method,
        headers={
            "Authorization": f"Bearer {api_key}",
            "Content-Type": "application/json",
            "Accept": "application/json",
        },
    )
    try:
        with urllib.request.urlopen(req, timeout=120) as resp:
            raw = resp.read().decode("utf-8")
            return json.loads(raw) if raw else {}
    except urllib.error.HTTPError as exc:
        detail = exc.read().decode("utf-8", errors="replace")
        raise RuntimeError(f"HTTP {exc.code} {path}: {detail}") from exc


def parse_verdict(text: str) -> str | None:
    matches = re.findall(r"VERDICT:\s*(PASS|FAIL)\b", text or "", flags=re.I)
    if not matches:
        return None
    return matches[-1].upper()


def main() -> int:
    api_key = os.environ.get("CURSOR_API_KEY", "").strip()
    if not api_key:
        print("Falta CURSOR_API_KEY. Agrégalo como secret del repositorio.", file=sys.stderr)
        return 1

    base, head = resolve_range()
    files = list_changed_sources(base, head)
    print(f"Base: {base or '(sin commit previo)'}")
    print(f"Head: {head}")
    print(f"Archivos fuente a revisar: {len(files)}")
    for file in files:
        print(f" - {file}")

    if not files:
        print("Sin cambios Java/Kotlin en el commit. VERDICT: PASS")
        return 0

    diff = build_diff(base, head, files)
    if not diff.strip():
        print("Diff vacío tras filtrar. VERDICT: PASS")
        return 0

    max_chars = int(os.environ.get("REVIEW_MAX_DIFF_CHARS", "180000"))
    if len(diff) > max_chars:
        diff = f"{diff[:max_chars]}\n\n...[DIFF TRUNCADO A {max_chars} CARACTERES]..."

    instructions = INSTRUCTIONS.read_text(encoding="utf-8")
    prompt = "\n".join(
        [
            instructions,
            "",
            "## DIFF a revisar",
            f"Rango: {base + '...' + head if base else 'commit completo ' + head}",
            "",
            "Archivos:",
            *[f"- {f}" for f in files],
            "",
            "```diff",
            diff,
            "```",
            "",
            "Revisa únicamente este DIFF según las reglas. No edites el repositorio.",
            "No uses herramientas. Responde solo con el informe.",
            "Cierra con VERDICT: PASS o VERDICT: FAIL.",
        ]
    )

    create_body = {
        "name": "corebank-nomenclature-gate",
        "prompt": {"text": prompt},
        # Sin repos ni env: agente "no-repo" (solo el prompt). Misma API key.
    }
    model = (os.environ.get("CURSOR_MODEL") or "").strip()
    if model:
        create_body["model"] = {"id": model}

    print("Creando agente Cloud (sin SDK)...")
    created = api_request("POST", "/agents", api_key, create_body)
    agent = created.get("agent") or created
    run = created.get("run") or {}
    agent_id = agent.get("id")
    run_id = run.get("id") or agent.get("latestRunId")
    if not agent_id or not run_id:
        print(f"Respuesta inesperada al crear agente: {json.dumps(created)[:1000]}", file=sys.stderr)
        return 1

    print(f"agentId={agent_id}")
    print(f"runId={run_id}")

    timeout_s = int(os.environ.get("REVIEW_TIMEOUT_SECONDS", "600"))
    deadline = time.time() + timeout_s
    result_text = ""
    status = "CREATING"

    while time.time() < deadline:
        run_info = api_request("GET", f"/agents/{agent_id}/runs/{run_id}", api_key)
        status = (run_info.get("status") or "").upper()
        print(f"Estado del run: {status}")
        if status in TERMINAL:
            result_text = run_info.get("result") or ""
            break
        time.sleep(5)
    else:
        print(f"Timeout esperando el agente ({timeout_s}s).", file=sys.stderr)
        return 2

    OUT_DIR.mkdir(parents=True, exist_ok=True)
    (OUT_DIR / "review-output.md").write_text(result_text or f"(sin result; status={status})", encoding="utf-8")

    print("--- Respuesta del agente ---")
    print(result_text)
    print("--- Fin respuesta ---")

    if status != "FINISHED":
        print(f"El agente terminó en {status}. Pipeline cortado.", file=sys.stderr)
        return 2

    verdict = parse_verdict(result_text)
    if verdict == "PASS":
        print("Gate de nomenclatura: PASS")
        return 0
    if verdict == "FAIL":
        print("Gate de nomenclatura: FAIL — corrige nombrado antes de desplegar.", file=sys.stderr)
        return 3

    print("No se encontró VERDICT: PASS|FAIL en la respuesta del agente.", file=sys.stderr)
    return 3


if __name__ == "__main__":
    try:
        raise SystemExit(main())
    except Exception as exc:  # noqa: BLE001 — CI gate: cualquier fallo corta el deploy
        print(exc, file=sys.stderr)
        raise SystemExit(1) from exc
