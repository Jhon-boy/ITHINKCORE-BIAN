package com.ithink.corebank.util;

import java.math.BigDecimal;

/**
 * Utilidades monetarias compartidas por el ledger y mapeos BIAN.
 *
 * @author Jhon
 * @version 1.0
 */
public final class MoneyUtil {

    private MoneyUtil() {
        // utility class
    }

    /**
     * Convierte un {@link BigDecimal} nulo a cero.
     *
     * @param value valor de entrada
     * @return el mismo valor o {@link BigDecimal#ZERO}
     */
    public static BigDecimal nullToZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    /**
     * Formatea un monto a texto plano.
     *
     * @param value valor de entrada
     * @return representación textual; {@code "0.00"} si es nulo
     */
    public static String format(BigDecimal value) {
        return value == null ? "0.00" : value.toPlainString();
    }
}
