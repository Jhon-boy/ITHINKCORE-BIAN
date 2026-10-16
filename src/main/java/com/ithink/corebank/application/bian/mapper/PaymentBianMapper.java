package com.ithink.corebank.application.bian.mapper;

import com.ithink.bian.currentaccount.model.Identifier;
import com.ithink.bian.currentaccount.model.Payment;
import com.ithink.corebank.domain.entities.Movimiento;
import com.ithink.corebank.domain.models.LedgerPostingRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Mapea el Behavior Qualifier Payment de BIAN hacia el ledger interno.
 *
 * @author Jhon
 * @version 1.0
 */
@Component
public class PaymentBianMapper {

    /**
     * Construye una solicitud de ledger de débito/transferencia a partir de un Payment BIAN.
     *
     * @param idCuenta identificador de la cuenta origen
     * @param payment payload BIAN
     * @return solicitud de posting
     */
    public LedgerPostingRequest toLedgerRequest(Integer idCuenta, Payment payment) {
        BigDecimal monto = CurrentAccountMappingSupport.parseAmount(
                payment == null ? null : payment.getPaymentAmount(),
                "PaymentAmount");
        String referencia = payment.getPaymentTransactionReference() != null
                ? payment.getPaymentTransactionReference().getIdentifierValue()
                : null;
        String description = payment.getPaymentDescription();
        String numeroContraparte = extractCounterpartyAccount(payment);
        boolean hasCounterparty = numeroContraparte != null || payment.getCounterpartyName() != null;

        return LedgerPostingRequest.builder()
                .idCuenta(idCuenta)
                .tipo("D")
                .monto(monto)
                .numeroCuentaContraparte(numeroContraparte)
                .nombreContraparte(payment.getCounterpartyName())
                .codigoBancoContraparte(payment.getCounterpartyBankCode())
                .nombreBancoContraparte(payment.getCounterpartyBankName())
                .idMetodoOperacion(hasCounterparty
                        ? CurrentAccountMappingSupport.METODO_TRANSFERENCIA
                        : CurrentAccountMappingSupport.METODO_RETIRO)
                .idTransaccion(hasCounterparty
                        ? CurrentAccountMappingSupport.TRANSACCION_TRANSFERENCIA
                        : CurrentAccountMappingSupport.TRANSACCION_RETIRO)
                .idCanal(hasCounterparty
                        ? CurrentAccountMappingSupport.CANAL_WEB
                        : CurrentAccountMappingSupport.CANAL_VENTANILLA)
                .idSucursal(CurrentAccountMappingSupport.SUCURSAL_MATRIZ)
                .mensaje(description != null ? description : "PAGO BIAN")
                .comentario(description)
                .referencia(referencia)
                .usuario(CurrentAccountMappingSupport.USUARIO_BIAN)
                .build();
    }

    /**
     * Convierte un movimiento interno a respuesta Payment BIAN.
     *
     * @param movimiento movimiento persistido
     * @return Payment BIAN
     */
    public Payment toPayment(Movimiento movimiento) {
        Payment payment = new Payment();
        Identifier reference = CurrentAccountMappingSupport.toIdentifier(
                CurrentAccountMappingSupport.resolveMovementReference(
                        movimiento.getReferencia(), movimiento.getIdMovimiento()));
        payment.setPaymentTransactionReference(reference);
        payment.setPaymentAmount(CurrentAccountMappingSupport.toAmount(movimiento.getMonto()));
        payment.setPaymentDescription(movimiento.getComentario() != null
                ? movimiento.getComentario()
                : movimiento.getMensaje());
        payment.setPaymentDate(CurrentAccountMappingSupport.toDatetime(movimiento.getFechaReal(), "ValueDate"));
        payment.setCounterpartyName(movimiento.getNombreContraparte());
        payment.setCounterpartyAccount(
                CurrentAccountMappingSupport.toAccountIdentification(movimiento.getNumeroCuentaContraparte()));
        payment.setCounterpartyBankCode(movimiento.getCodigoBancoContraparte());
        payment.setCounterpartyBankName(movimiento.getNombreBancoContraparte());
        payment.setStatus(CurrentAccountMappingSupport.toEntryStatus(movimiento.getEstadoMovimiento()));
        payment.setMessage(movimiento.getMensaje());
        return payment;
    }

    private String extractCounterpartyAccount(Payment payment) {
        if (payment == null
                || payment.getCounterpartyAccount() == null
                || payment.getCounterpartyAccount().getAccountIdentification() == null) {
            return null;
        }
        return payment.getCounterpartyAccount().getAccountIdentification().getIdentifierValue();
    }
}
