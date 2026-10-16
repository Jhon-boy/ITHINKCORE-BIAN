package com.ithink.corebank.application.bian.services;

import com.ithink.bian.currentaccount.model.Withdrawal;
import com.ithink.corebank.application.bian.mapper.WithdrawalBianMapper;
import com.ithink.corebank.application.bian.support.BianAccountSupport;
import com.ithink.corebank.application.services.AccountLedgerService;
import com.ithink.corebank.domain.entities.Movimiento;
import com.ithink.corebank.domain.models.LedgerPostingRequest;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Casos de uso BIAN del Behavior Qualifier Withdrawal.
 *
 * @author Jhon
 * @version 1.0
 */
@Service
public class WithdrawalService {

    private static final Loggers logs = new Loggers();

    private final BianAccountSupport bianAccountSupport;
    private final AccountLedgerService accountLedgerService;
    private final WithdrawalBianMapper withdrawalBianMapper;

    /**
     * Crea el servicio BIAN Withdrawal.
     *
     * @param bianAccountSupport soporte compartido
     * @param accountLedgerService motor de ledger
     * @param withdrawalBianMapper mapper BIAN
     */
    public WithdrawalService(
            BianAccountSupport bianAccountSupport,
            AccountLedgerService accountLedgerService,
            WithdrawalBianMapper withdrawalBianMapper) {
        logs.setLogger(WithdrawalService.class);
        this.bianAccountSupport = bianAccountSupport;
        this.accountLedgerService = accountLedgerService;
        this.withdrawalBianMapper = withdrawalBianMapper;
    }

    /**
     * Inicia un retiro creando un débito en estado PENDIENTE.
     *
     * @param currentaccountid identificador de cuenta
     * @param withdrawal payload BIAN
     * @return retiro iniciado
     */
    @Transactional
    public Withdrawal initiate(String currentaccountid, Withdrawal withdrawal) {
        logs.info(Const.ML_INI + "BIAN Withdrawal Initiate account=" + currentaccountid);
        try {
            Integer idCuenta = bianAccountSupport.parseId(currentaccountid, "currentaccountid");
            bianAccountSupport.requireCuenta(idCuenta, currentaccountid);
            LedgerPostingRequest request = withdrawalBianMapper.toLedgerRequest(idCuenta, withdrawal);
            Movimiento pending = accountLedgerService.initiate(request);
            Withdrawal response = withdrawalBianMapper.toWithdrawal(pending);
            logs.info(Const.ML_FIN + "BIAN Withdrawal Initiate id=" + pending.getIdMovimiento());
            return response;
        } catch (IllegalArgumentException ex) {
            throw bianAccountSupport.mapLedgerException(ex);
        }
    }

    /**
     * Ejecuta (postea) un retiro pendiente afectando saldo e histórico.
     *
     * @param currentaccountid identificador de cuenta
     * @param withdrawalid identificador del retiro
     * @return retiro posteado
     */
    @Transactional
    public Withdrawal execute(String currentaccountid, String withdrawalid) {
        logs.info(Const.ML_INI + "BIAN Withdrawal Execute account=" + currentaccountid + " id=" + withdrawalid);
        try {
            Movimiento pending = bianAccountSupport.requireMovimientoForAccount(currentaccountid, withdrawalid, "Withdrawal");
            bianAccountSupport.assertDebit(pending, "Withdrawal");
            Movimiento posted = accountLedgerService.execute(pending.getIdMovimiento());
            Withdrawal response = withdrawalBianMapper.toWithdrawal(posted);
            logs.info(Const.ML_FIN + "BIAN Withdrawal Execute id=" + posted.getIdMovimiento());
            return response;
        } catch (IllegalArgumentException ex) {
            throw bianAccountSupport.mapLedgerException(ex);
        }
    }

    /**
     * Consulta un retiro de débito.
     *
     * @param currentaccountid identificador de cuenta
     * @param withdrawalid identificador del retiro
     * @return retiro BIAN
     */
    @Transactional(readOnly = true)
    public Withdrawal retrieve(String currentaccountid, String withdrawalid) {
        Movimiento movimiento = bianAccountSupport.requireMovimientoForAccount(currentaccountid, withdrawalid, "Withdrawal");
        bianAccountSupport.assertDebit(movimiento, "Withdrawal");
        return withdrawalBianMapper.toWithdrawal(movimiento);
    }
}
