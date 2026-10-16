package com.ithink.corebank.application.bian.mapper;

import com.ithink.bian.currentaccount.model.Accountentrystatustypevalues;
import com.ithink.bian.currentaccount.model.Accountidentification;
import com.ithink.bian.currentaccount.model.Accountidentificationtypevalues;
import com.ithink.bian.currentaccount.model.Amount;
import com.ithink.bian.currentaccount.model.Currencycode;
import com.ithink.bian.currentaccount.model.Datetime;
import com.ithink.bian.currentaccount.model.Identifier;
import com.ithink.corebank.domain.entities.EstadoMovimiento;
import com.ithink.corebank.util.MoneyUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Soporte compartido de mapeo para el Service Domain Current Account (BIAN).
 * Centraliza conversiones de monto, fecha e identificadores para evitar duplicación.
 *
 * @author Jhon
 * @version 1.0
 */
public final class CurrentAccountMappingSupport {

    public static final String DEFAULT_CURRENCY = "USD";
    public static final String DEFAULT_TIME_ZONE = "America/Guayaquil";
    public static final String USUARIO_BIAN = "BIAN";

    /** Identificadores de catálogo seed usados por Deposit. */
    public static final int METODO_DEPOSITO = 1;
    public static final int TRANSACCION_DEPOSITO = 1;
    public static final int CANAL_APP = 1;
    public static final int SUCURSAL_MATRIZ = 1;

    /** Identificadores de catálogo seed usados por Payment/retiro. */
    public static final int METODO_RETIRO = 2;
    public static final int TRANSACCION_RETIRO = 2;
    public static final int CANAL_VENTANILLA = 3;
    public static final int METODO_TRANSFERENCIA = 3;
    public static final int TRANSACCION_TRANSFERENCIA = 3;
    public static final int CANAL_WEB = 2;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private CurrentAccountMappingSupport() {
        // utility class
    }

    /**
     * Convierte un monto BIAN a {@link BigDecimal}.
     *
     * @param amount monto BIAN
     * @param fieldName nombre del campo para mensajes de error
     * @return valor numérico
     */
    public static BigDecimal parseAmount(Amount amount, String fieldName) {
        if (amount == null || amount.getAmountValue() == null || amount.getAmountValue().isBlank()) {
            throw new IllegalArgumentException(fieldName + ".AmountValue is required");
        }
        try {
            return new BigDecimal(amount.getAmountValue().trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(fieldName + ".AmountValue is invalid: " + amount.getAmountValue(), ex);
        }
    }

    /**
     * Convierte un {@link BigDecimal} a monto BIAN.
     *
     * @param value valor monetario
     * @return monto BIAN
     */
    public static Amount toAmount(BigDecimal value) {
        Amount amount = new Amount();
        amount.setAmountValue(MoneyUtil.format(value));
        Currencycode currencycode = new Currencycode();
        currencycode.setCurrencycode(DEFAULT_CURRENCY);
        amount.setAmountCurrency(currencycode);
        return amount;
    }

    /**
     * Convierte una fecha/hora local a estructura BIAN {@link Datetime}.
     *
     * @param value fecha/hora
     * @param dateTimeType tipo semántico BIAN
     * @return datetime BIAN o {@code null}
     */
    public static Datetime toDatetime(LocalDateTime value, String dateTimeType) {
        if (value == null) {
            return null;
        }
        Datetime datetime = new Datetime();
        datetime.setDateTimeContent(value.format(DATE_TIME_FORMATTER));
        datetime.setTimeZoneCode(DEFAULT_TIME_ZONE);
        datetime.setDateTimeType(dateTimeType);
        return datetime;
    }

    /**
     * Crea un {@link Identifier} BIAN.
     *
     * @param value valor del identificador
     * @return identifier o {@code null} si el valor es nulo
     */
    public static Identifier toIdentifier(String value) {
        if (value == null) {
            return null;
        }
        Identifier identifier = new Identifier();
        identifier.setIdentifierValue(value);
        return identifier;
    }

    /**
     * Crea una identificación de cuenta BIAN por número de cuenta.
     *
     * @param numeroCuenta número de cuenta
     * @return identificación BIAN o {@code null}
     */
    public static Accountidentification toAccountIdentification(String numeroCuenta) {
        if (numeroCuenta == null || numeroCuenta.isBlank()) {
            return null;
        }
        Accountidentification identification = new Accountidentification();
        identification.setAccountIdentificationType(Accountidentificationtypevalues.ACCOUNT_NUMBER);
        identification.setAccountIdentification(toIdentifier(numeroCuenta));
        return identification;
    }

    /**
     * Mapea el estado de movimiento interno a estado de entrada BIAN.
     *
     * @param estado estado del movimiento
     * @return estado BIAN
     */
    public static Accountentrystatustypevalues toEntryStatus(EstadoMovimiento estado) {
        if (estado == null || estado.getCodigo() == null) {
            return Accountentrystatustypevalues.PENDING;
        }
        return switch (estado.getCodigo().trim().toUpperCase()) {
            case "BOOKED" -> Accountentrystatustypevalues.BOOKED;
            case "FUT", "FUTURE" -> Accountentrystatustypevalues.FUTURE;
            case "INFO", "INFORMATION", "RECHAZADO" -> Accountentrystatustypevalues.INFORMATION;
            case "PEND", "PENDING", "PENDIENTE" -> Accountentrystatustypevalues.PENDING;
            default -> Accountentrystatustypevalues.PENDING;
        };
    }

    /**
     * Resuelve la referencia de un movimiento para respuestas BIAN.
     *
     * @param referencia referencia de negocio
     * @param idMovimiento id interno
     * @return valor de referencia
     */
    public static String resolveMovementReference(String referencia, Integer idMovimiento) {
        if (referencia != null && !referencia.isBlank()) {
            return referencia;
        }
        return idMovimiento == null ? null : String.valueOf(idMovimiento);
    }
}
