package com.ithink.corebank.application.bian.mapper;

import com.ithink.bian.currentaccount.model.DebitandCredit;
import com.ithink.bian.currentaccount.model.Debitcreditindicatorvalues;
import com.ithink.corebank.domain.entities.Canal;
import com.ithink.corebank.domain.entities.MetodoOperacion;
import com.ithink.corebank.domain.entities.Movimiento;
import com.ithink.corebank.domain.entities.Sucursal;
import org.springframework.stereotype.Component;

/**
 * Mapea {@link Movimiento} al Behavior Qualifier DebitandCredit de BIAN.
 *
 * @author Jhon
 * @version 1.0
 */
@Component
public class DebitandCreditBianMapper {

    /**
     * Convierte un movimiento interno a DebitandCredit BIAN.
     *
     * @param movimiento movimiento persistido
     * @return DebitandCredit BIAN
     */
    public DebitandCredit toDebitandCredit(Movimiento movimiento) {
        DebitandCredit entry = new DebitandCredit();
        entry.setTransactionReference(CurrentAccountMappingSupport.toIdentifier(resolveReference(movimiento)));
        entry.setDebitCreditIndicator(toIndicator(movimiento.getTipo()));
        entry.setTransactionAmount(CurrentAccountMappingSupport.toAmount(movimiento.getMonto()));
        entry.setDebitAmount(CurrentAccountMappingSupport.toAmount(movimiento.getDebito()));
        entry.setTransactionDescription(resolveDescription(movimiento));
        entry.setTransactionDate(CurrentAccountMappingSupport.toDatetime(movimiento.getFechaReal(), "EntryDate"));
        entry.setValueDate(CurrentAccountMappingSupport.toDatetime(movimiento.getFechaContable(), "ValueDate"));
        entry.setBookingDate(CurrentAccountMappingSupport.toDatetime(movimiento.getFCreacion(), "PostingDate"));
        entry.setCounterpartyName(movimiento.getNombreContraparte());
        entry.setCounterpartyAccount(
                CurrentAccountMappingSupport.toAccountIdentification(movimiento.getNumeroCuentaContraparte()));
        entry.setCounterpartyBankCode(movimiento.getCodigoBancoContraparte());
        entry.setCounterpartyBankName(movimiento.getNombreBancoContraparte());
        entry.setChannelReference(resolveCanal(movimiento.getCanal()));
        entry.setBranchReference(resolveSucursal(movimiento.getSucursal()));
        entry.setMethodReference(resolveMetodo(movimiento.getMetodoOperacion()));
        entry.setStatus(CurrentAccountMappingSupport.toEntryStatus(movimiento.getEstadoMovimiento()));
        entry.setMessage(movimiento.getMensaje());
        entry.setTrackingCode(movimiento.getCodigoRastreo());
        entry.setRejectionReason(movimiento.getMotivoRechazo());
        return entry;
    }

    private Debitcreditindicatorvalues toIndicator(String tipo) {
        if (tipo == null) {
            return Debitcreditindicatorvalues.CREDIT;
        }
        return switch (tipo.trim().toUpperCase()) {
            case "D", "DEBIT", "DEBITO" -> Debitcreditindicatorvalues.DEBIT;
            default -> Debitcreditindicatorvalues.CREDIT;
        };
    }

    private String resolveReference(Movimiento movimiento) {
        if (movimiento.getReferencia() != null && !movimiento.getReferencia().isBlank()) {
            return movimiento.getReferencia();
        }
        if (movimiento.getUuid() != null && !movimiento.getUuid().isBlank()) {
            return movimiento.getUuid();
        }
        return CurrentAccountMappingSupport.resolveMovementReference(null, movimiento.getIdMovimiento());
    }

    private String resolveDescription(Movimiento movimiento) {
        if (movimiento.getComentario() != null && !movimiento.getComentario().isBlank()) {
            return movimiento.getComentario();
        }
        return movimiento.getMensaje();
    }

    private String resolveCanal(Canal canal) {
        if (canal == null) {
            return null;
        }
        return canal.getCodigo() != null ? canal.getCodigo() : canal.getNombre();
    }

    private String resolveSucursal(Sucursal sucursal) {
        return sucursal == null ? null : sucursal.getNombre();
    }

    private String resolveMetodo(MetodoOperacion metodoOperacion) {
        if (metodoOperacion == null) {
            return null;
        }
        return metodoOperacion.getCodigo() != null ? metodoOperacion.getCodigo() : metodoOperacion.getNombre();
    }
}
