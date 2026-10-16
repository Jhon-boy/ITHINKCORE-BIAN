package com.ithink.corebank.application.bian.mapper;

import com.ithink.bian.currentaccount.model.Deposit;
import com.ithink.bian.currentaccount.model.Identifier;
import com.ithink.corebank.domain.entities.Movimiento;
import com.ithink.corebank.domain.models.LedgerPostingRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Mapea el Behavior Qualifier Deposit de BIAN hacia el ledger interno.
 *
 * @author Jhon
 * @version 1.0
 */
@Component
public class DepositBianMapper {

    /**
     * Construye una solicitud de ledger de crédito a partir de un Deposit BIAN.
     *
     * @param idCuenta identificador de la cuenta
     * @param deposit payload BIAN
     * @return solicitud de posting
     */
    public LedgerPostingRequest toLedgerRequest(Integer idCuenta, Deposit deposit) {
        BigDecimal monto = CurrentAccountMappingSupport.parseAmount(
                deposit == null ? null : deposit.getDepositAmount(),
                "DepositAmount");
        String referencia = deposit.getDepositTransactionReference() != null
                ? deposit.getDepositTransactionReference().getIdentifierValue()
                : null;
        String description = deposit.getDepositDescription();

        return LedgerPostingRequest.builder()
                .idCuenta(idCuenta)
                .tipo("C")
                .monto(monto)
                .idMetodoOperacion(CurrentAccountMappingSupport.METODO_DEPOSITO)
                .idTransaccion(CurrentAccountMappingSupport.TRANSACCION_DEPOSITO)
                .idCanal(CurrentAccountMappingSupport.CANAL_APP)
                .idSucursal(CurrentAccountMappingSupport.SUCURSAL_MATRIZ)
                .mensaje(description != null ? description : "DEPOSITO BIAN")
                .comentario(description)
                .referencia(referencia)
                .usuario(CurrentAccountMappingSupport.USUARIO_BIAN)
                .build();
    }

    /**
     * Convierte un movimiento interno a respuesta Deposit BIAN.
     *
     * @param movimiento movimiento persistido
     * @return Deposit BIAN
     */
    public Deposit toDeposit(Movimiento movimiento) {
        Deposit deposit = new Deposit();
        Identifier reference = CurrentAccountMappingSupport.toIdentifier(
                CurrentAccountMappingSupport.resolveMovementReference(
                        movimiento.getReferencia(), movimiento.getIdMovimiento()));
        deposit.setDepositTransactionReference(reference);
        deposit.setDepositAmount(CurrentAccountMappingSupport.toAmount(movimiento.getMonto()));
        deposit.setDepositDescription(movimiento.getComentario() != null
                ? movimiento.getComentario()
                : movimiento.getMensaje());
        deposit.setDepositDate(CurrentAccountMappingSupport.toDatetime(movimiento.getFechaReal(), "ValueDate"));
        deposit.setStatus(CurrentAccountMappingSupport.toEntryStatus(movimiento.getEstadoMovimiento()));
        deposit.setMessage(movimiento.getMensaje());
        return deposit;
    }
}
