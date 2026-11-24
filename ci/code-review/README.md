# Gate de revisión por agente (CI)

Job de GitHub Actions que usa la **Cloud Agents API** de Cursor (HTTP)
para revisar nomenclatura estilo SonarQube **solo sobre el diff del commit**.

Misma `CURSOR_API_KEY`. **Sin Node, sin SDK, sin `node_modules`.**

Si el veredicto es `FAIL`, el pipeline no despliega.

## Secret requerido

En GitHub: **Settings → Secrets and variables → Actions**

| Name | Valor |
|------|--------|
| `CURSOR_API_KEY` | API key de [Cursor Dashboard → API Keys](https://cursor.com/dashboard) |

## Cómo corre

1. `build-test` (Maven verify + ArchUnit) — esto es lo que más tarda por Java
2. `code-review` — `python3 review.py` llama `POST https://api.cursor.com/v1/agents` (agente **sin repo**: solo prompt + diff)
3. `deploy` — solo si el agente responde `VERDICT: PASS`

## Prueba local

```bash
cd ci/code-review
export CURSOR_API_KEY=crsr_...
# opcional: REVIEW_BASE y REVIEW_HEAD (SHAs)
python3 review.py
```

## Instrucciones del agente

Edita `INSTRUCTIONS.md` para ajustar reglas de nombrado.
