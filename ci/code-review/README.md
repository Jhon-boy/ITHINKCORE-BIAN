# Gate de revisión por agente (CI)

Job de GitHub Actions que usa el **Cursor SDK** para revisar nomenclatura
(clases, métodos, variables, parámetros, paquetes) al estilo SonarQube
**solo sobre el diff del commit**. Si el veredicto es `FAIL`, el pipeline
no despliega.

## Secret requerido

En el repositorio de GitHub:

**Settings → Secrets and variables → Actions → New repository secret**

| Name | Valor |
|------|--------|
| `CURSOR_API_KEY` | API key de Cursor (Dashboard → Integrations) o service account del team |

Sin este secret el job `code-review` falla y corta el deploy.

## Cómo corre

1. `build-test` (Maven verify + ArchUnit)
2. `code-review` (agente Cursor sobre el diff Java/Kotlin)
3. `deploy` (solo si los dos anteriores pasan)

Rama: la definida en `.github/workflows/ci-cd.yml` (hoy `master`).

## Prueba local

Requiere Node.js **22.13+**.

```bash
cd ci/code-review
npm install
export CURSOR_API_KEY=cursor_...
# opcional: REVIEW_BASE y REVIEW_HEAD (SHAs)
node review.mjs
```

## Instrucciones del agente

Edita `INSTRUCTIONS.md` para ajustar reglas de nombrado sin tocar el workflow.
