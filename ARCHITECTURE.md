# Arquitectura Corebank (demo)

## Visión

Corebank es un **proyecto de muestra** con arquitectura hexagonal (ports & adapters)
y un facade BIAN generado desde OpenAPI. Hay dos superficies HTTP:

1. **API interna** — Consultar (`/api/consultar/**`) y Mantenimiento (`/api/mantenimiento/**`).
2. **API BIAN** — Service Domains Party y Current Account (solo operaciones implementadas).

```
 Controllers (consultar / mantenimiento / BIAN)
        │
        ▼
 Application services  ──►  domain ports  ◄──  infrastructure adapters  ──►  JPA
        │                         │
        ▼                         ▼
   application.dto          domain.entities / domain.models
```

## Paquetes

| Paquete | Rol |
|---------|-----|
| `application.controllers.consultar` | Lectura REST — un `*ConsultaController` por entidad |
| `application.controllers.mantenimiento` | Escritura REST — un `*MantenimientoController` por entidad |
| `application.services` | Casos de uso internos + ledger / AmountBlock de dominio |
| `application.dto` | Contratos HTTP (`*Dto`, `ResponseModel`) |
| `application.bian.*` | Facade BIAN (controllers, services, mappers, support) |
| `domain.entities` | Modelo de dominio (ver trade-off JPA abajo) |
| `domain.ports` | Contratos de salida (repositorios) |
| `domain.models` | Requests/value objects de dominio (p. ej. `LedgerPostingRequest`) |
| `infrastructure.adapters` | Implementación de ports |
| `infrastructure.repositories` | Spring Data JPA |

## Reglas de dependencia (congeladas con ArchUnit)

1. **`domain`** no importa `application` ni `infrastructure`.
2. **`domain`** no importa Spring Web / SpringDoc.
3. **`application`** no importa `infrastructure` (solo ports del domain).
4. **`infrastructure`** no importa `application` (solo domain + JPA).

Tests: `src/test/java/com/ithink/corebank/architecture/ArchitectureRulesTest.java`.

## Trade-off: JPA en `domain.entities` (Opción A)

**Decisión del demo:** las entidades de dominio **son** el modelo de persistencia
(`@Entity`, `@Table`, etc.).

### Por qué

- Reduce boilerplate en un proyecto de muestra.
- Facilita seguir el flujo entidad → puerto → adapter → JPA.
- El objetivo pedagógico es el hexágono de **dependencias** y el facade BIAN,
  no un dominio 100 % libre de frameworks.

### Qué NO implica

- No es hexagonal estricto (Opción B: modelos de persistencia en infrastructure
  + mappers entity↔domain).
- Si el proyecto deja de ser demo, evaluar desacoplar JPA del domain.

### Qué sí se mantiene

- Domain **no** depende de controllers, DTOs HTTP ni adapters.
- Application **no** conoce Spring Data / JPA repositories.
- Controllers solo hablan con services; services solo con ports.

## Contratos HTTP fuera del domain

`ResponseModel` y `*Dto` viven en `application.dto`.

En domain solo quedan artefactos de negocio, p. ej. `LedgerPostingRequest`
(solicitud interna del ledger usada por `/api/mantenimiento/movimiento` y BIAN Deposit/Payment).

## API demo en español: Consultar / Mantenimiento

Prefijos claros, **un controller por entidad y categoría** (no dos clases gigantes ni CRUD legado):

| Prefijo | Paquete | Controllers |
|---------|---------|-------------|
| `/api/consultar/{entidad}` | `controllers.consultar` | `*ConsultaController` (GET) |
| `/api/mantenimiento/{entidad}` | `controllers.mantenimiento` | `*MantenimientoController` (POST: registrar / ops) |

Ejemplos: `GET /api/consultar/banco`, `POST /api/mantenimiento/persona/registrar`,
`POST /api/mantenimiento/cuenta/depositar`. BIAN sigue en grupo Swagger aparte.

## Persona detalle (`TPERSONADETALLE`)

Tabla 1:1 con `TPERSONA` para datos demográficos/laborales:

- estado civil, nacionalidad, ocupación, nivel educativo, empleador, ingresos

API: `GET /api/consultar/persona-detalle/**`, `POST /api/mantenimiento/persona-detalle/registrar`
(y atajos en persona: `GET .../persona/{id}/detalle`, `POST .../persona/completar`).
Pensada como base para un futuro BQ BIAN Demographics.

## Facade BIAN

Cada tipo tiene controlador + servicio propios:

| Tipo | Controller | Service |
|------|------------|---------|
| Party | `PartyReferenceDataDirectoryController` | `PartyReferenceDataDirectoryService` |
| Facility | `CurrentAccountFacilityController` | `CurrentAccountFacilityService` |
| DebitandCredit | `DebitandCreditController` | `DebitandCreditService` |
| Deposit | `DepositController` | `DepositService` |
| Withdrawal | `WithdrawalController` | `WithdrawalService` |
| Payment | `PaymentController` | `PaymentService` |
| AmountBlock | `AmountBlockController` | `AmountBlockBianService` |

OpenAPI Generator usa `useTags=true` para generar una interfaz API por tag.
Documentación en español: YAML bajo `src/main/resources/bian/` + `OpenApiConfig`.

## Ledger compartido

`AccountLedgerService` es el motor de movimientos (initiate / execute / postImmediate).
Lo usan:

- API interna `POST /api/mantenimiento/movimiento/registrar`
- BIAN Deposit / Withdrawal / Payment

`AmountBlockService` (application.services) gestiona bloqueos de saldo;
`AmountBlockBianService` lo adapta al contrato BIAN.
