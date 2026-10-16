package com.ithink.corebank.security.loggers;

import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransactionUuidFilterTest {

    @Test
    void eachRequestGetsItsOwnUuidAndTheThreadIsClearedAfterwards() throws Exception {
        TransactionUuidFilter filter = new TransactionUuidFilter();
        AtomicReference<String> first = new AtomicReference<>();
        AtomicReference<String> second = new AtomicReference<>();

        filter.doFilter(new MockHttpServletRequest(), new MockHttpServletResponse(),
                (request, response) -> first.set(MDC.get(TransactionUuidFilter.MDC_KEY)));
        filter.doFilter(new MockHttpServletRequest(), new MockHttpServletResponse(),
                (request, response) -> second.set(MDC.get(TransactionUuidFilter.MDC_KEY)));

        assertNotNull(first.get());
        assertTrue(first.get().matches(
                "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"));
        assertNotEquals(first.get(), second.get());
        assertNull(MDC.get(TransactionUuidFilter.MDC_KEY));
    }
}
