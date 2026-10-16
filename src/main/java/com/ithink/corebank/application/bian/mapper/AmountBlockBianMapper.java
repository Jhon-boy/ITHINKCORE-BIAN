package com.ithink.corebank.application.bian.mapper;

import com.ithink.bian.currentaccount.model.Accountentrystatustypevalues;
import com.ithink.bian.currentaccount.model.AmountBlock;
import com.ithink.bian.currentaccount.model.Identifier;
import com.ithink.corebank.domain.entities.Saldo;
import com.ithink.corebank.util.MoneyUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Mapea el Behavior Qualifier AmountBlock de BIAN hacia {@link Saldo}.
 *
 * @author Jhon
 * @version 1.0
 */
@Component
public class AmountBlockBianMapper {

    /**
     * Convierte el saldo interno a respuesta AmountBlock BIAN.
     *
     * @param saldo saldo de la cuenta
     * @param reason motivo del bloqueo
     * @param type tipo de bloqueo
     * @return AmountBlock BIAN
     */
    public AmountBlock toAmountBlock(Saldo saldo, String reason, String type) {
        AmountBlock block = new AmountBlock();
        Identifier reference = CurrentAccountMappingSupport.toIdentifier(String.valueOf(saldo.getIdSaldo()));
        block.setAmountBlockReference(reference);
        block.setAmountBlockAmount(CurrentAccountMappingSupport.toAmount(saldo.getMontoBloqueado()));
        block.setAvailableBalance(CurrentAccountMappingSupport.toAmount(saldo.getMontoDisponible()));
        block.setLedgerBalance(CurrentAccountMappingSupport.toAmount(saldo.getMontoContable()));
        block.setAmountBlockType(type != null ? type : "Hold");
        block.setAmountBlockReason(reason != null ? reason : saldo.getComentario());
        block.setAmountBlockDate(CurrentAccountMappingSupport.toDatetime(
                saldo.getFModificacion() != null ? saldo.getFModificacion() : saldo.getFCreacion(),
                "ValueDate"));
        block.setStatus(Accountentrystatustypevalues.BOOKED);
        block.setMessage("Blocked=" + MoneyUtil.format(saldo.getMontoBloqueado())
                + "; Available=" + MoneyUtil.format(saldo.getMontoDisponible()));
        return block;
    }

    /**
     * Extrae el monto de bloqueo del payload BIAN.
     *
     * @param amountBlock payload BIAN
     * @return monto a bloquear/actualizar
     */
    public BigDecimal parseBlockAmount(AmountBlock amountBlock) {
        BigDecimal value = CurrentAccountMappingSupport.parseAmount(
                amountBlock == null ? null : amountBlock.getAmountBlockAmount(),
                "AmountBlockAmount");
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("AmountBlockAmount must be >= 0");
        }
        return value;
    }

    /**
     * Monto a liberar. Vacío o cero significa liberar todo el bloqueo.
     *
     * @param amountBlock payload BIAN, puede ser nulo
     * @return monto o {@code null} si se debe liberar todo
     */
    public BigDecimal parseOptionalReleaseAmount(AmountBlock amountBlock) {
        if (amountBlock == null
                || amountBlock.getAmountBlockAmount() == null
                || amountBlock.getAmountBlockAmount().getAmountValue() == null
                || amountBlock.getAmountBlockAmount().getAmountValue().isBlank()) {
            return null;
        }
        return parseBlockAmount(amountBlock);
    }

    /**
     * Extrae el motivo del bloqueo.
     *
     * @param amountBlock payload BIAN
     * @return motivo o mensaje
     */
    public String extractReason(AmountBlock amountBlock) {
        if (amountBlock == null) {
            return null;
        }
        if (amountBlock.getAmountBlockReason() != null && !amountBlock.getAmountBlockReason().isBlank()) {
            return amountBlock.getAmountBlockReason();
        }
        return amountBlock.getMessage();
    }

    /**
     * Extrae el tipo de bloqueo.
     *
     * @param amountBlock payload BIAN
     * @return tipo; por defecto {@code Hold}
     */
    public String extractType(AmountBlock amountBlock) {
        if (amountBlock == null || amountBlock.getAmountBlockType() == null
                || amountBlock.getAmountBlockType().isBlank()) {
            return "Hold";
        }
        return amountBlock.getAmountBlockType();
    }
}
