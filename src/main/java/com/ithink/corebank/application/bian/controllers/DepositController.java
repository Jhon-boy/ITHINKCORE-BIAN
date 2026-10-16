package com.ithink.corebank.application.bian.controllers;

import com.ithink.bian.currentaccount.api.DepositApi;
import com.ithink.bian.currentaccount.model.Deposit;
import com.ithink.corebank.application.bian.services.DepositService;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador BIAN del Behavior Qualifier Deposit.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
public class DepositController implements DepositApi {

    private static final Loggers logs = new Loggers();

    private final DepositService depositService;

    /**
     * Crea el controlador BIAN Deposit.
     *
     * @param depositService servicio de casos de uso
     */
    public DepositController(DepositService depositService) {
        logs.setLogger(DepositController.class);
        this.depositService = depositService;
    }

    /**
     * Inicia un depósito (crédito pendiente).
     *
     * @param currentaccountid identificador de cuenta
     * @param deposit payload BIAN
     * @return depósito iniciado
     */
    @Override
    public ResponseEntity<Deposit> initiateDeposit(String currentaccountid, Deposit deposit) {
        logs.info(Const.ML_INI + "POST /CurrentAccount/" + currentaccountid + "/Deposit/Initiate");
        Deposit response = depositService.initiate(currentaccountid, deposit);
        logs.info(Const.ML_FIN + "POST /CurrentAccount/" + currentaccountid + "/Deposit/Initiate");
        return ResponseEntity.ok(response);
    }

    /**
     * Ejecuta un depósito pendiente.
     *
     * @param currentaccountid identificador de cuenta
     * @param depositid identificador del depósito
     * @param deposit payload BIAN (no usado en execute)
     * @return depósito posteado
     */
    @Override
    public ResponseEntity<Deposit> executeDeposit(String currentaccountid, String depositid, Deposit deposit) {
        logs.info(Const.ML_INI + "PUT /CurrentAccount/" + currentaccountid + "/Deposit/" + depositid + "/Execute");
        Deposit response = depositService.execute(currentaccountid, depositid);
        logs.info(Const.ML_FIN + "PUT /CurrentAccount/" + currentaccountid + "/Deposit/" + depositid + "/Execute");
        return ResponseEntity.ok(response);
    }

    /**
     * Consulta un depósito.
     *
     * @param currentaccountid identificador de cuenta
     * @param depositid identificador del depósito
     * @return depósito BIAN
     */
    @Override
    public ResponseEntity<Deposit> retrieveDeposit(String currentaccountid, String depositid) {
        logs.info(Const.ML_INI + "GET /CurrentAccount/" + currentaccountid + "/Deposit/" + depositid + "/Retrieve");
        Deposit response = depositService.retrieve(currentaccountid, depositid);
        logs.info(Const.ML_FIN + "GET /CurrentAccount/" + currentaccountid + "/Deposit/" + depositid + "/Retrieve");
        return ResponseEntity.ok(response);
    }
}
