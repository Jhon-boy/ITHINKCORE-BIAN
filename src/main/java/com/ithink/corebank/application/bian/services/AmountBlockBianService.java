package com.ithink.corebank.application.bian.services;

import com.ithink.bian.currentaccount.model.AmountBlock;
import com.ithink.corebank.application.bian.mapper.AmountBlockBianMapper;
import com.ithink.corebank.application.bian.support.BianAccountSupport;
import com.ithink.corebank.application.services.AmountBlockService;
import com.ithink.corebank.domain.entities.Saldo;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Casos de uso BIAN del Behavior Qualifier AmountBlock.
 * Orquesta el dominio {@link AmountBlockService} hacia el contrato BIAN.
 *
 * @author Jhon
 * @version 1.0
 */
@Service
public class AmountBlockBianService {

    private static final Loggers logs = new Loggers();

    private final BianAccountSupport bianAccountSupport;
    private final AmountBlockService amountBlockService;
    private final AmountBlockBianMapper amountBlockBianMapper;

    /**
     * Crea el servicio BIAN AmountBlock.
     *
     * @param bianAccountSupport soporte compartido
     * @param amountBlockService servicio de dominio de bloqueos
     * @param amountBlockBianMapper mapper BIAN
     */
    public AmountBlockBianService(BianAccountSupport bianAccountSupport, AmountBlockService amountBlockService, AmountBlockBianMapper amountBlockBianMapper) {
        logs.setLogger(AmountBlockBianService.class);
        this.bianAccountSupport = bianAccountSupport;
        this.amountBlockService = amountBlockService;
        this.amountBlockBianMapper = amountBlockBianMapper;
    }

    /**
     * Inicia un bloqueo de monto sobre el saldo disponible.
     *
     * @param currentaccountid identificador de cuenta
     * @param amountBlock payload BIAN
     * @return bloqueo iniciado
     */
    @Transactional
    public AmountBlock initiate(String currentaccountid, AmountBlock amountBlock) {
        logs.info(Const.ML_INI + "BIAN AmountBlock Initiate account=" + currentaccountid);
        try {
            Integer idCuenta = bianAccountSupport.parseId(currentaccountid, "currentaccountid");
            bianAccountSupport.requireCuenta(idCuenta, currentaccountid);
            BigDecimal amount = amountBlockBianMapper.parseBlockAmount(amountBlock);
            String reason = amountBlockBianMapper.extractReason(amountBlock);
            String type = amountBlockBianMapper.extractType(amountBlock);
            Saldo saldo = amountBlockService.initiate(idCuenta, amount, reason);
            AmountBlock response = amountBlockBianMapper.toAmountBlock(saldo, reason, type);
            logs.info(Const.ML_FIN + "BIAN AmountBlock Initiate idSaldo=" + saldo.getIdSaldo());
            return response;
        } catch (IllegalArgumentException ex) {
            throw bianAccountSupport.mapLedgerException(ex);
        }
    }

    /**
     * Actualiza el monto bloqueado absoluto de un saldo.
     *
     * @param currentaccountid identificador de cuenta
     * @param amountblockid identificador del saldo (IDSALDO)
     * @param amountBlock payload BIAN
     * @return bloqueo actualizado
     */
    @Transactional
    public AmountBlock update(String currentaccountid, String amountblockid, AmountBlock amountBlock) {
        logs.info(Const.ML_INI + "BIAN AmountBlock Update account=" + currentaccountid + " id=" + amountblockid);
        try {
            Integer idCuenta = bianAccountSupport.parseId(currentaccountid, "currentaccountid");
            Integer idSaldo = bianAccountSupport.parseId(amountblockid, "amountblockid");
            bianAccountSupport.requireCuenta(idCuenta, currentaccountid);
            BigDecimal newBlocked = amountBlockBianMapper.parseBlockAmount(amountBlock);
            String reason = amountBlockBianMapper.extractReason(amountBlock);
            String type = amountBlockBianMapper.extractType(amountBlock);
            Saldo saldo = amountBlockService.update(idCuenta, idSaldo, newBlocked, reason);
            AmountBlock response = amountBlockBianMapper.toAmountBlock(saldo, reason, type);
            logs.info(Const.ML_FIN + "BIAN AmountBlock Update idSaldo=" + saldo.getIdSaldo());
            return response;
        } catch (IllegalArgumentException ex) {
            throw bianAccountSupport.mapLedgerException(ex);
        }
    }

    /**
     * Consulta el estado de un bloqueo de monto.
     *
     * @param currentaccountid identificador de cuenta
     * @param amountblockid identificador del saldo (IDSALDO)
     * @return bloqueo BIAN
     */
    @Transactional(readOnly = true)
    public AmountBlock retrieve(String currentaccountid, String amountblockid) {
        logs.info(Const.ML_INI + "BIAN AmountBlock Retrieve account=" + currentaccountid + " id=" + amountblockid);
        try {
            Integer idCuenta = bianAccountSupport.parseId(currentaccountid, "currentaccountid");
            Integer idSaldo = bianAccountSupport.parseId(amountblockid, "amountblockid");
            bianAccountSupport.requireCuenta(idCuenta, currentaccountid);
            Saldo saldo = amountBlockService.retrieve(idCuenta, idSaldo);
            AmountBlock response = amountBlockBianMapper.toAmountBlock(saldo, saldo.getComentario(), "Hold");
            logs.info(Const.ML_FIN + "BIAN AmountBlock Retrieve idSaldo=" + saldo.getIdSaldo());
            return response;
        } catch (IllegalArgumentException ex) {
            throw bianAccountSupport.mapLedgerException(ex);
        }
    }

    /**
     * Libera total o parcialmente el monto bloqueado.
     *
     * @param currentaccountid identificador de cuenta
     * @param amountblockid identificador del saldo
     * @param amountBlock payload BIAN; monto vacío libera todo
     * @return bloqueo resultante
     */
    @Transactional
    public AmountBlock release(String currentaccountid, String amountblockid, AmountBlock amountBlock) {
        logs.info(Const.ML_INI + "BIAN AmountBlock Release account=" + currentaccountid + " id=" + amountblockid);
        try {
            Integer idCuenta = bianAccountSupport.parseId(currentaccountid, "currentaccountid");
            Integer idSaldo = bianAccountSupport.parseId(amountblockid, "amountblockid");
            bianAccountSupport.requireCuenta(idCuenta, currentaccountid);
            BigDecimal amount = amountBlockBianMapper.parseOptionalReleaseAmount(amountBlock);
            String reason = amountBlockBianMapper.extractReason(amountBlock);
            String type = amountBlockBianMapper.extractType(amountBlock);
            Saldo saldo = amountBlockService.release(idCuenta, idSaldo, amount, reason);
            AmountBlock response = amountBlockBianMapper.toAmountBlock(saldo, reason, type);
            logs.info(Const.ML_FIN + "BIAN AmountBlock Release idSaldo=" + saldo.getIdSaldo());
            return response;
        } catch (IllegalArgumentException ex) {
            throw bianAccountSupport.mapLedgerException(ex);
        }
    }
}
