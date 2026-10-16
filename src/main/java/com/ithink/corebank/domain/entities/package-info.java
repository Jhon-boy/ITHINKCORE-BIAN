/**
 * Entidades del núcleo de dominio.
 *
 * <p><b>Trade-off deliberado del proyecto de muestra (Opción A):</b>
 * estas clases llevan anotaciones JPA ({@code @Entity}, {@code @Table}, etc.)
 * y actúan a la vez como modelo de dominio y de persistencia.
 * Ver {@code ARCHITECTURE.md} en la raíz del repositorio.</p>
 *
 * <p>Las reglas ArchUnit permiten {@code jakarta.persistence} aquí, pero
 * prohíben que este paquete dependa de {@code application} o {@code infrastructure}.</p>
 */
package com.ithink.corebank.domain.entities;
