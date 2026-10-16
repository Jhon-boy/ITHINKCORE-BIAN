package com.ithink.corebank.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Congela las reglas de dependencia del hexágono documentadas en {@code ARCHITECTURE.md}.
 *
 * @author Jhon
 * @version 1.0
 */
@AnalyzeClasses(
        packages = "com.ithink.corebank",
        importOptions = ImportOption.DoNotIncludeTests.class)
public class ArchitectureRulesTest {

    /**
     * El domain no puede depender de application (DTOs, controllers, services de app).
     */
    @ArchTest
    static final ArchRule domain_must_not_depend_on_application =
            noClasses()
                    .that().resideInAPackage("..domain..")
                    .should().dependOnClassesThat().resideInAPackage("..application..")
                    .because("El domain es el núcleo: no conoce la capa de aplicación ni contratos HTTP");

    /**
     * El domain no puede depender de infrastructure (adapters / JPA repos).
     */
    @ArchTest
    static final ArchRule domain_must_not_depend_on_infrastructure =
            noClasses()
                    .that().resideInAPackage("..domain..")
                    .should().dependOnClassesThat().resideInAPackage("..infrastructure..")
                    .because("El domain define ports; infrastructure los implementa");

    /**
     * El domain no usa Spring Web ni SpringDoc.
     */
    @ArchTest
    static final ArchRule domain_must_not_depend_on_web_frameworks =
            noClasses()
                    .that().resideInAPackage("..domain..")
                    .should().dependOnClassesThat().resideInAnyPackage(
                            "org.springframework.web..",
                            "org.springframework.http..",
                            "org.springdoc..",
                            "io.swagger..")
                    .because("El domain permanece libre de concerns HTTP");

    /**
     * Application no importa infrastructure (solo ports del domain).
     */
    @ArchTest
    static final ArchRule application_must_not_depend_on_infrastructure =
            noClasses()
                    .that().resideInAPackage("..application..")
                    .should().dependOnClassesThat().resideInAPackage("..infrastructure..")
                    .because("Application orquesta a través de domain.ports, no de adapters JPA");

    /**
     * Infrastructure no importa application (DTOs / services de app).
     */
    @ArchTest
    static final ArchRule infrastructure_must_not_depend_on_application =
            noClasses()
                    .that().resideInAPackage("..infrastructure..")
                    .should().dependOnClassesThat().resideInAPackage("..application..")
                    .because("Los adapters solo conocen domain (entities/ports) y JPA");
}
