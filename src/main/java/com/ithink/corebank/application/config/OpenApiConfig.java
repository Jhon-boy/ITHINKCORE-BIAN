package com.ithink.corebank.application.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

/**
 * Configuración OpenAPI / Swagger UI para la demo Corebank.
 * Grupos: Consultar, Mantenimiento y BIAN.
 *
 * @author Jhon
 * @version 1.0
 */
@Configuration
public class OpenApiConfig {

    private static final String LINK_CONSULTAR =
            "/swagger-ui/index.html?urls.primaryName=Consultar%20(/api/consultar)";

    private static final String LINK_MANTENIMIENTO =
            "/swagger-ui/index.html?urls.primaryName=Mantenimiento%20(/api/mantenimiento)";

    private static final String LINK_BIAN =
            "/swagger-ui/index.html?urls.primaryName=BIAN%20%E2%80%94%20Core%20bancario";

    private static final String API_DESCRIPTION = """
            ## Corebank — Demo

            Elige la definición aquí:

            | Grupo | Prefijo | Uso |
            |---|---|---|
            | **[Consultar](%s)** | `/api/consultar/{entidad}` | Solo lectura — un controller por entidad |
            | **[Mantenimiento](%s)** | `/api/mantenimiento/{entidad}` | Altas y operaciones — un controller por entidad |
            | **[BIAN — Core bancario](%s)** | `/Party...`, `/CurrentAccount...` | Core expuesto con el estándar BIAN |

            Convención:
            - Lectura → `controllers.consultar.*ConsultaController`
            - Escritura → `controllers.mantenimiento.*MantenimientoController`
            - No hay CRUD legado bajo `/api/{entidad}`
            """.formatted(LINK_CONSULTAR, LINK_MANTENIMIENTO, LINK_BIAN);

    /**
     * Metadatos globales visibles en la cabecera de Swagger UI.
     *
     * @return definición OpenAPI base
     */
    @Bean
    public OpenAPI corebankOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Corebank Demo — Consultar / Mantenimiento / BIAN")
                        .description(API_DESCRIPTION)
                        .version("1.0.0-demo")
                        .contact(new Contact()
                                .name("Corebank Demo")
                                .url("https://bian.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Estándar BIAN")
                        .url("https://bian.org"));
    }

    /**
     * Grupo de consultas (solo lectura).
     *
     * @return grupo OpenAPI consultar
     */
    @Bean
    public GroupedOpenApi consultarApi() {
        return GroupedOpenApi.builder()
                .group("01-Consultar")
                .displayName("Consultar (/api/consultar)")
                .pathsToMatch("/api/consultar/**")
                .addOpenApiCustomizer(openApi -> presentarPortada(openApi, API_DESCRIPTION, "1.0.0-demo"))
                .build();
    }

    /**
     * Grupo de mantenimiento / operaciones de escritura.
     *
     * @return grupo OpenAPI mantenimiento
     */
    @Bean
    public GroupedOpenApi mantenimientoApi() {
        return GroupedOpenApi.builder()
                .group("02-Mantenimiento")
                .displayName("Mantenimiento (/api/mantenimiento)")
                .pathsToMatch("/api/mantenimiento/**")
                .addOpenApiCustomizer(openApi -> presentarPortada(openApi, API_DESCRIPTION, "1.0.0-demo"))
                .build();
    }

    /**
     * Grupo Swagger del core bancario expuesto con el estándar BIAN.
     *
     * @return grupo OpenAPI BIAN
     */
    @Bean
    public GroupedOpenApi bianApi() {
        return GroupedOpenApi.builder()
                .group("03-BIAN")
                .displayName("BIAN — Core bancario")
                .pathsToMatch(
                        "/PartyReferenceDataDirectory/**",
                        "/CurrentAccount/**")
                .addOpenApiCustomizer(this::presentarCoreBian)
                .build();
    }

    /**
     * Portada común: tabla de grupos, convención y enlace Corebank Demo.
     *
     * @param openApi especificación del grupo
     * @param description texto de la cabecera
     * @param version versión visible en Swagger
     */
    private void presentarPortada(OpenAPI openApi, String description, String version) {
        openApi.info(new Info()
                .title("Corebank Demo — Consultar / Mantenimiento / BIAN")
                .description(description)
                .version(version)
                .contact(new Contact()
                        .name("Corebank Demo")
                        .url("https://bian.org")));
        openApi.externalDocs(new ExternalDocumentation()
                .description("Estándar BIAN")
                .url("https://bian.org"));
    }

    /**
     * Portada del grupo BIAN: este core y el vocabulario del estándar.
     *
     * @param openApi especificación del grupo
     */
    private void presentarCoreBian(OpenAPI openApi) {
        presentarPortada(openApi, API_DESCRIPTION + "\n" + BIAN_DESCRIPTION, "14.0.0-demo");
        openApi.getInfo().setTitle("Core bancario — Estándar BIAN");
        openApi.externalDocs(new ExternalDocumentation()
                .description("Banking Industry Architecture Network (BIAN)")
                .url("https://bian.org"));
        describirTag(openApi, "CurrentAccountFacility",
                "Control Record de la cuenta corriente. Initiate abre la cuenta, Retrieve consulta saldos "
                        + "y Update cambia el estado (TCUENTA, TSALDO).");
        describirTag(openApi, "DebitandCredit",
                "Behavior Qualifier de débito y crédito. Consulta un movimiento o el estado de cuenta por fechas.");
        describirTag(openApi, "Deposit",
                "Behavior Qualifier de depósito. Initiate deja el crédito pendiente; "
                        + "Execute lo contabiliza y mueve el saldo.");
        describirTag(openApi, "Withdrawal",
                "Behavior Qualifier de retiro. Débito sin contraparte (ventanilla o cajero). "
                        + "Initiate queda pendiente; Execute contabiliza.");
        describirTag(openApi, "Payment",
                "Behavior Qualifier de pago. Débito con destino (pago o transferencia). "
                        + "Initiate queda pendiente; Execute contabiliza.");
        describirTag(openApi, "AmountBlock",
                "Behavior Qualifier de bloqueo de monto. Initiate retiene, Update ajusta y Release "
                        + "devuelve fondos al disponible. El saldo contable no cambia.");
        describirTag(openApi, "CR - PartyReferenceDataDirectoryEntry",
                "Control Record del cliente. Register, Retrieve, Update, Control de vigencia "
                        + "y Demographics sobre TPERSONA y TPERSONADETALLE.");
    }

    /**
     * Sustituye la descripción de un tag BIAN ya declarado por las operaciones.
     *
     * @param openApi especificación
     * @param name nombre del tag
     * @param description texto visible bajo el tag
     */
    private void describirTag(OpenAPI openApi, String name, String description) {
        if (openApi.getTags() == null) {
            openApi.setTags(new ArrayList<>());
        }
        Tag tag = openApi.getTags().stream()
                .filter(item -> name.equals(item.getName()))
                .findFirst()
                .orElseGet(() -> {
                    Tag created = new Tag().name(name);
                    openApi.addTagsItem(created);
                    return created;
                });
        tag.setDescription(description);
    }

    private static final String BIAN_DESCRIPTION = """
            ## Core bancario

            Esta definición es el **core bancario** de la demo, publicado con el
            **estándar BIAN** (Banking Industry Architecture Network).

            BIAN le pone un nombre común a las capacidades del banco. Un canal
            (app, cajero, ventanilla u otro banco) no inventa su propio contrato:
            llama Service Domains, Control Records y Behavior Qualifiers.

            ## Dos dominios de este core

            | Service Domain | En el banco | En este core |
            |---|---|---|
            | **Party Reference Data Directory** | Quién es el cliente | `TPERSONA` |
            | **Current Account** | La cuenta corriente y su dinero | `TCUENTA`, `TSALDO`, `TMOVIMIENTO`, `TSALDOHISTORICO` |

            ## Cómo leer cada operación

            | Pieza BIAN | Significado |
            |---|---|
            | **Control Record** | El registro principal: la persona o la cuenta |
            | **Behavior Qualifier** | Un comportamiento sobre ese registro: depósito, retiro, pago, bloqueo, débito/crédito |
            | **Initiate** | Deja el movimiento pendiente. El saldo no cambia |
            | **Execute** | Contabiliza. Cambian el saldo y el histórico |
            | **Retrieve** | Consulta |
            | **Update** | Ajusta un dato que ya existe (el monto bloqueado) |

            ## Recorrido

            1. Registrar al cliente — `PartyReferenceDataDirectory/Register`
            2. Consultar la cuenta y sus saldos — `CurrentAccount/{id}/Retrieve`
            3. Depositar — `Deposit/Initiate` y después `Execute`
            4. Retirar — `Withdrawal/Initiate` y `Execute`
            5. Pagar o transferir — `Payment/Initiate` y `Execute`
            6. Ver el movimiento — `DebitandCredit/{id}/Retrieve`
            7. Bloquear un monto — `AmountBlock`

            Las mismas capacidades del core también están en español, en los grupos
            **Consultar** y **Mantenimiento**. Aquí el contrato es el del estándar.
            """;
}
