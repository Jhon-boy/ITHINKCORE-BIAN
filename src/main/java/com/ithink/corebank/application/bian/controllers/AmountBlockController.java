package com.ithink.corebank.application.bian.controllers;

import com.ithink.bian.currentaccount.api.AmountBlockApi;
import com.ithink.bian.currentaccount.model.AmountBlock;
import com.ithink.corebank.application.bian.services.AmountBlockBianService;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador BIAN del Behavior Qualifier AmountBlock.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
public class AmountBlockController implements AmountBlockApi {

    private static final Loggers logs = new Loggers();

    private final AmountBlockBianService amountBlockBianService;

    /**
     * Crea el controlador BIAN AmountBlock.
     *
     * @param amountBlockBianService servicio de casos de uso
     */
    public AmountBlockController(AmountBlockBianService amountBlockBianService) {
        logs.setLogger(AmountBlockController.class);
        this.amountBlockBianService = amountBlockBianService;
    }

    /**
     * Inicia un bloqueo de monto.
     *
     * @param currentaccountid identificador de cuenta
     * @param amountBlock payload BIAN
     * @return bloqueo iniciado
     */
    @Override
    public ResponseEntity<AmountBlock> initiateAmountBlock(String currentaccountid, AmountBlock amountBlock) {
        logs.info(Const.ML_INI + "POST /CurrentAccount/" + currentaccountid + "/AmountBlock/Initiate");
        AmountBlock response = amountBlockBianService.initiate(currentaccountid, amountBlock);
        logs.info(Const.ML_FIN + "POST /CurrentAccount/" + currentaccountid + "/AmountBlock/Initiate");
        return ResponseEntity.ok(response);
    }

    /**
     * Actualiza el monto bloqueado absoluto.
     *
     * @param currentaccountid identificador de cuenta
     * @param amountblockid identificador del saldo (IDSALDO)
     * @param amountBlock payload BIAN
     * @return bloqueo actualizado
     */
    @Override
    public ResponseEntity<AmountBlock> updateAmountBlock(String currentaccountid, String amountblockid, AmountBlock amountBlock) {
        logs.info(Const.ML_INI + "PUT /CurrentAccount/" + currentaccountid + "/AmountBlock/" + amountblockid + "/Update");
        AmountBlock response = amountBlockBianService.update(currentaccountid, amountblockid, amountBlock);
        logs.info(Const.ML_FIN + "PUT /CurrentAccount/" + currentaccountid + "/AmountBlock/" + amountblockid + "/Update");
        return ResponseEntity.ok(response);
    }

    /**
     * Consulta un bloqueo de monto.
     *
     * @param currentaccountid identificador de cuenta
     * @param amountblockid identificador del saldo (IDSALDO)
     * @return bloqueo BIAN
     */
    @Override
    public ResponseEntity<AmountBlock> retrieveAmountBlock(String currentaccountid, String amountblockid) {
        logs.info(Const.ML_INI + "GET /CurrentAccount/" + currentaccountid + "/AmountBlock/" + amountblockid + "/Retrieve");
        AmountBlock response = amountBlockBianService.retrieve(currentaccountid, amountblockid);
        logs.info(Const.ML_FIN + "GET /CurrentAccount/" + currentaccountid + "/AmountBlock/" + amountblockid + "/Retrieve");
        return ResponseEntity.ok(response);
    }

    /**
     * Libera el monto bloqueado hacia el disponible.
     *
     * @param currentaccountid identificador de cuenta
     * @param amountblockid identificador del saldo
     * @param amountBlock payload BIAN
     * @return bloqueo resultante
     */
    @Override
    public ResponseEntity<AmountBlock> releaseAmountBlock(
            String currentaccountid, String amountblockid, AmountBlock amountBlock) {
        logs.info(Const.ML_INI + "PUT /CurrentAccount/" + currentaccountid
                + "/AmountBlock/" + amountblockid + "/Release");
        AmountBlock response = amountBlockBianService.release(currentaccountid, amountblockid, amountBlock);
        logs.info(Const.ML_FIN + "PUT /CurrentAccount/" + currentaccountid
                + "/AmountBlock/" + amountblockid + "/Release");
        return ResponseEntity.ok(response);
    }
}
