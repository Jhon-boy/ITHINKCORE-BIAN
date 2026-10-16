package com.ithink.corebank.application.bian.controllers;

import com.ithink.bian.currentaccount.api.WithdrawalApi;
import com.ithink.bian.currentaccount.model.Withdrawal;
import com.ithink.corebank.application.bian.services.WithdrawalService;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador BIAN del Behavior Qualifier Withdrawal.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
public class WithdrawalController implements WithdrawalApi {

    private static final Loggers logs = new Loggers();

    private final WithdrawalService withdrawalService;

    /**
     * Crea el controlador BIAN Withdrawal.
     *
     * @param withdrawalService servicio de casos de uso
     */
    public WithdrawalController(WithdrawalService withdrawalService) {
        logs.setLogger(WithdrawalController.class);
        this.withdrawalService = withdrawalService;
    }

    /**
     * Inicia un retiro (débito pendiente).
     *
     * @param currentaccountid identificador de cuenta
     * @param withdrawal payload BIAN
     * @return retiro iniciado
     */
    @Override
    public ResponseEntity<Withdrawal> initiateWithdrawal(String currentaccountid, Withdrawal withdrawal) {
        logs.info(Const.ML_INI + "POST /CurrentAccount/" + currentaccountid + "/Withdrawal/Initiate");
        Withdrawal response = withdrawalService.initiate(currentaccountid, withdrawal);
        logs.info(Const.ML_FIN + "POST /CurrentAccount/" + currentaccountid + "/Withdrawal/Initiate");
        return ResponseEntity.ok(response);
    }

    /**
     * Ejecuta un retiro pendiente.
     *
     * @param currentaccountid identificador de cuenta
     * @param withdrawalid identificador del retiro
     * @param withdrawal payload BIAN (no usado en execute)
     * @return retiro posteado
     */
    @Override
    public ResponseEntity<Withdrawal> executeWithdrawal(String currentaccountid, String withdrawalid, Withdrawal withdrawal) {
        logs.info(Const.ML_INI + "PUT /CurrentAccount/" + currentaccountid + "/Withdrawal/" + withdrawalid + "/Execute");
        Withdrawal response = withdrawalService.execute(currentaccountid, withdrawalid);
        logs.info(Const.ML_FIN + "PUT /CurrentAccount/" + currentaccountid + "/Withdrawal/" + withdrawalid + "/Execute");
        return ResponseEntity.ok(response);
    }

    /**
     * Consulta un retiro.
     *
     * @param currentaccountid identificador de cuenta
     * @param withdrawalid identificador del retiro
     * @return retiro BIAN
     */
    @Override
    public ResponseEntity<Withdrawal> retrieveWithdrawal(String currentaccountid, String withdrawalid) {
        logs.info(Const.ML_INI + "GET /CurrentAccount/" + currentaccountid + "/Withdrawal/" + withdrawalid + "/Retrieve");
        Withdrawal response = withdrawalService.retrieve(currentaccountid, withdrawalid);
        logs.info(Const.ML_FIN + "GET /CurrentAccount/" + currentaccountid + "/Withdrawal/" + withdrawalid + "/Retrieve");
        return ResponseEntity.ok(response);
    }
}
