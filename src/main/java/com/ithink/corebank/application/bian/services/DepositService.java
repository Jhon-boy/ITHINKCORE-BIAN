package com.ithink.corebank.application.bian.services;

import com.ithink.bian.currentaccount.model.Deposit;
import com.ithink.corebank.application.bian.mapper.DepositBianMapper;
import com.ithink.corebank.application.bian.support.BianAccountSupport;
import com.ithink.corebank.application.services.AccountLedgerService;
import com.ithink.corebank.domain.entities.Movimiento;
import com.ithink.corebank.domain.models.LedgerPostingRequest;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Casos de uso BIAN del Behavior Qualifier Deposit.
 *
 * @author Jhon
 * @version 1.0
 */
@Service
public class DepositService {

    private static final Loggers logs = new Loggers();

    private final BianAccountSupport bianAccountSupport;
    private final AccountLedgerService accountLedgerService;
    private final DepositBianMapper depositBianMapper;

    /**
     * Crea el servicio BIAN Deposit.
     *
     * @param bianAccountSupport soporte compartido
     * @param accountLedgerService motor de ledger
     * @param depositBianMapper mapper BIAN
     */
    public DepositService(BianAccountSupport bianAccountSupport, AccountLedgerService accountLedgerService, DepositBianMapper depositBianMapper) {
        logs.setLogger(DepositService.class);
        this.bianAccountSupport = bianAccountSupport;
        this.accountLedgerService = accountLedgerService;
        this.depositBianMapper = depositBianMapper;
    }

    /**
     * Inicia un depósito creando un crédito en estado PENDIENTE.
     *
     * @param currentaccountid identificador de cuenta
     * @param deposit payload BIAN
     * @return depósito iniciado
     */
    @Transactional
    public Deposit initiate(String currentaccountid, Deposit deposit) {
        logs.info(Const.ML_INI + "BIAN Deposit Initiate account=" + currentaccountid);
        try {
            Integer idCuenta = bianAccountSupport.parseId(currentaccountid, "currentaccountid");
            bianAccountSupport.requireCuenta(idCuenta, currentaccountid);
            LedgerPostingRequest request = depositBianMapper.toLedgerRequest(idCuenta, deposit);
            Movimiento pending = accountLedgerService.initiate(request);
            Deposit response = depositBianMapper.toDeposit(pending);
            logs.info(Const.ML_FIN + "BIAN Deposit Initiate id=" + pending.getIdMovimiento());
            return response;
        } catch (IllegalArgumentException ex) {
            throw bianAccountSupport.mapLedgerException(ex);
        }
    }

    /**
     * Ejecuta (postea) un depósito pendiente afectando saldo e histórico.
     *
     * @param currentaccountid identificador de cuenta
     * @param depositid identificador del depósito
     * @return depósito posteado
     */
    @Transactional
    public Deposit execute(String currentaccountid, String depositid) {
        logs.info(Const.ML_INI + "BIAN Deposit Execute account=" + currentaccountid + " id=" + depositid);
        try {
            Movimiento pending = bianAccountSupport.requireMovimientoForAccount(
                    currentaccountid, depositid, "Deposit");
            bianAccountSupport.assertCredit(pending, "Deposit");
            Movimiento posted = accountLedgerService.execute(pending.getIdMovimiento());
            Deposit response = depositBianMapper.toDeposit(posted);
            logs.info(Const.ML_FIN + "BIAN Deposit Execute id=" + posted.getIdMovimiento());
            return response;
        } catch (IllegalArgumentException ex) {
            throw bianAccountSupport.mapLedgerException(ex);
        }
    }

    /**
     * Consulta un depósito de crédito.
     *
     * @param currentaccountid identificador de cuenta
     * @param depositid identificador del depósito
     * @return depósito BIAN
     */
    @Transactional(readOnly = true)
    public Deposit retrieve(String currentaccountid, String depositid) {
        Movimiento movimiento = bianAccountSupport.requireMovimientoForAccount(currentaccountid, depositid, "Deposit");
        bianAccountSupport.assertCredit(movimiento, "Deposit");
        return depositBianMapper.toDeposit(movimiento);
    }
}
