package com.ithink.corebank.application.bian.support;

import com.ithink.corebank.application.bian.exception.BianBadRequestException;
import com.ithink.corebank.application.bian.exception.BianNotFoundException;
import com.ithink.corebank.application.services.AccountLedgerService;
import com.ithink.corebank.domain.entities.Cuenta;
import com.ithink.corebank.domain.entities.Movimiento;
import com.ithink.corebank.domain.ports.CuentaRepositoryPort;
import com.ithink.corebank.domain.ports.MovimientoRepositoryPort;
import org.springframework.stereotype.Component;

/**
 * Utilidades compartidas por los casos de uso BIAN de Current Account.
 * Centraliza parseo de ids, validación de pertenencia y mapeo de errores del ledger.
 *
 * @author Jhon
 * @version 1.0
 */
@Component
public class BianAccountSupport {

    private final CuentaRepositoryPort cuentaRepositoryPort;
    private final MovimientoRepositoryPort movimientoRepositoryPort;

    /**
     * Crea el soporte compartido BIAN.
     *
     * @param cuentaRepositoryPort puerto de cuentas
     * @param movimientoRepositoryPort puerto de movimientos
     */
    public BianAccountSupport(
            CuentaRepositoryPort cuentaRepositoryPort,
            MovimientoRepositoryPort movimientoRepositoryPort) {
        this.cuentaRepositoryPort = cuentaRepositoryPort;
        this.movimientoRepositoryPort = movimientoRepositoryPort;
    }

    /**
     * Parsea un identificador numérico de path BIAN.
     *
     * @param value valor del path
     * @param fieldName nombre del campo para mensajes
     * @return id numérico
     */
    public Integer parseId(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new BianBadRequestException(fieldName + " is required");
        }
        try {
            return Integer.valueOf(value.trim());
        } catch (NumberFormatException ex) {
            throw new BianBadRequestException(fieldName + " must be a numeric id: " + value);
        }
    }

    /**
     * Obtiene la cuenta o lanza {@link BianNotFoundException}.
     *
     * @param idCuenta id interno
     * @param currentaccountid id expuesto en BIAN
     * @return cuenta encontrada
     */
    public Cuenta requireCuenta(Integer idCuenta, String currentaccountid) {
        return cuentaRepositoryPort.findById(idCuenta)
                .orElseThrow(() -> new BianNotFoundException(
                        "CurrentAccountFacility not found: " + currentaccountid));
    }

    /**
     * Obtiene un movimiento que pertenece a la cuenta indicada.
     *
     * @param currentaccountid id de cuenta BIAN
     * @param movementId id del movimiento
     * @param resourceName nombre del recurso BIAN (Deposit, Payment, etc.)
     * @return movimiento validado
     */
    public Movimiento requireMovimientoForAccount(String currentaccountid, String movementId, String resourceName) {
        Integer idCuenta = parseId(currentaccountid, "currentaccountid");
        Integer idMovimiento = parseId(movementId, resourceName.toLowerCase() + "id");
        requireCuenta(idCuenta, currentaccountid);

        Movimiento movimiento = movimientoRepositoryPort.findById(idMovimiento).orElseThrow(() -> new BianNotFoundException(resourceName + " not found: " + movementId));

        if (movimiento.getIdCuenta() == null || !movimiento.getIdCuenta().equals(idCuenta)) {
            throw new BianNotFoundException(resourceName + " " + movementId + " does not belong to CurrentAccount " + currentaccountid);
        }
        return movimiento;
    }

    /**
     * Valida que el movimiento sea un crédito.
     *
     * @param movimiento movimiento a validar
     * @param resourceName nombre del recurso
     */
    public void assertCredit(Movimiento movimiento, String resourceName) {
        if (!AccountLedgerService.TIPO_CREDITO.equalsIgnoreCase(movimiento.getTipo())) {
            throw new BianBadRequestException(resourceName + " must be a credit movement");
        }
    }

    /**
     * Valida que el movimiento sea un débito.
     *
     * @param movimiento movimiento a validar
     * @param resourceName nombre del recurso
     */
    public void assertDebit(Movimiento movimiento, String resourceName) {
        if (!AccountLedgerService.TIPO_DEBITO.equalsIgnoreCase(movimiento.getTipo())) {
            throw new BianBadRequestException(resourceName + " must be a debit movement");
        }
    }

    /**
     * Traduce excepciones del ledger a excepciones BIAN.
     *
     * @param ex excepción de dominio
     * @return excepción BIAN equivalente
     */
    public RuntimeException mapLedgerException(IllegalArgumentException ex) {
        String message = ex.getMessage() == null ? "Bad request" : ex.getMessage();
        if (message.contains("NO ENCONTRAD")) {
            return new BianNotFoundException(message);
        }
        return new BianBadRequestException(message);
    }
}
