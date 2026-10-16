# Corebank — Demo BIAN

Proyecto de muestra de un core bancario con **arquitectura hexagonal** y
facade **BIAN** (Party + Current Account).

## Arranque rápido

```bash
mvn spring-boot:run
```

- API / Swagger: http://localhost:8088/swagger-ui.html
- H2 console: http://localhost:8088/h2-console
- Puerto: `8088`

Swagger abre en **BIAN — Core bancario**: el core publicado con el estándar
(Party y Current Account). Consultar y Mantenimiento siguen en el selector.

## Capas

| Capa | Paquete | Responsabilidad |
|------|---------|-----------------|
| Application | `application.*` | Controllers, services, DTOs HTTP, facade BIAN |
| Domain | `domain.*` | Entidades, ports, requests de dominio |
| Infrastructure | `infrastructure.*` | Adapters JPA que implementan ports |

Detalle completo: [ARCHITECTURE.md](ARCHITECTURE.md).

## APIs

- **Consultar**: `/api/consultar/{entidad}` — un `*ConsultaController` por entidad
- **Mantenimiento**: `/api/mantenimiento/{entidad}` — un `*MantenimientoController` por entidad
- **BIAN**: `/PartyReferenceDataDirectory/**`, `/CurrentAccount/**`

## Tests

```bash
mvn test
```

Incluye tests de arquitectura (ArchUnit) que congelan las reglas de dependencia.
