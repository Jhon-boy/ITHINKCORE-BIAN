package com.ithink.corebank.application.bian.mapper;

import com.ithink.bian.currentaccount.model.Identifier;
import com.ithink.bian.currentaccount.model.Withdrawal;
import com.ithink.corebank.domain.entities.Movimiento;
import com.ithink.corebank.domain.models.LedgerPostingRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Mapea el Behavior Qualifier Withdrawal de BIAN hacia el ledger interno.
 *
 * @author Jhon
 * @version 1.0
 */
@Component
public class WithdrawalBianMapper {

    /**
     * Construye una solicitud de ledger de débito (retiro) a partir de un Withdrawal BIAN.
     *
     * @param idCuenta identificador de la cuenta
     * @param withdrawal payload BIAN
     * @return solicitud de posting
     */
    public LedgerPostingRequest toLedgerRequest(Integer idCuenta, Withdrawal withdrawal) {
        BigDecimal monto = CurrentAccountMappingSupport.parseAmount(
                withdrawal == null ? null : withdrawal.getWithdrawalAmount(),
                "WithdrawalAmount");
        String referencia = withdrawal != null && withdrawal.getWithdrawalTransactionReference() != null
                ? withdrawal.getWithdrawalTransactionReference().getIdentifierValue()
                : null;
        String description = withdrawal == null ? null : withdrawal.getWithdrawalDescription();

        return LedgerPostingRequest.builder()
                .idCuenta(idCuenta)
                .tipo("D")
                .monto(monto)
                .idMetodoOperacion(CurrentAccountMappingSupport.METODO_RETIRO)
                .idTransaccion(CurrentAccountMappingSupport.TRANSACCION_RETIRO)
                .idCanal(CurrentAccountMappingSupport.CANAL_VENTANILLA)
                .idSucursal(CurrentAccountMappingSupport.SUCURSAL_MATRIZ)
                .mensaje(description != null ? description : "RETIRO BIAN")
                .comentario(description)
                .referencia(referencia)
                .usuario(CurrentAccountMappingSupport.USUARIO_BIAN)
                .build();
    }

    /**
     * Convierte un movimiento interno a respuesta Withdrawal BIAN.
     *
     * @param movimiento movimiento persistido
     * @return Withdrawal BIAN
     */
    public Withdrawal toWithdrawal(Movimiento movimiento) {
        Withdrawal withdrawal = new Withdrawal();
        Identifier reference = CurrentAccountMappingSupport.toIdentifier(
                CurrentAccountMappingSupport.resolveMovementReference(
                        movimiento.getReferencia(), movimiento.getIdMovimiento()));
        withdrawal.setWithdrawalTransactionReference(reference);
        withdrawal.setWithdrawalAmount(CurrentAccountMappingSupport.toAmount(movimiento.getMonto()));
        withdrawal.setWithdrawalDescription(movimiento.getComentario() != null
                ? movimiento.getComentario()
                : movimiento.getMensaje());
        withdrawal.setWithdrawalDate(
                CurrentAccountMappingSupport.toDatetime(movimiento.getFechaReal(), "ValueDate"));
        withdrawal.setStatus(CurrentAccountMappingSupport.toEntryStatus(movimiento.getEstadoMovimiento()));
        withdrawal.setMessage(movimiento.getMensaje());
        return withdrawal;
    }
}
