package com.ithink.corebank.security.loggers;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Asigna un UUID por petición HTTP para el patrón {@code %X{uuid}}.
 *
 * @author Jhon
 * @version 1.0
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TransactionUuidFilter extends OncePerRequestFilter {

    static final String MDC_KEY = "uuid";

    /**
     * Genera el UUID de la transacción, lo publica en el log y lo retira al salir.
     *
     * @param request petición
     * @param response respuesta
     * @param filterChain cadena de filtros
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String uuid = UUID.randomUUID().toString();
        MDC.put(MDC_KEY, uuid);
        ThreadContext.put(MDC_KEY, uuid);
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(MDC_KEY);
            ThreadContext.remove(MDC_KEY);
        }
    }
}
