package com.ithink.corebank.application.bian.controllers;

import com.ithink.bian.currentaccount.api.DebitandCreditApi;
import com.ithink.bian.currentaccount.model.DebitandCredit;
import com.ithink.corebank.application.bian.services.DebitandCreditService;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador BIAN del Behavior Qualifier DebitandCredit.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
public class DebitandCreditController implements DebitandCreditApi {

    private static final Loggers logs = new Loggers();

    private final DebitandCreditService debitandCreditService;

    /**
     * Crea el controlador BIAN DebitandCredit.
     *
     * @param debitandCreditService servicio de casos de uso
     */
    public DebitandCreditController(DebitandCreditService debitandCreditService) {
        logs.setLogger(DebitandCreditController.class);
        this.debitandCreditService = debitandCreditService;
    }

    /**
     * Consulta un movimiento débito/crédito.
     *
     * @param currentaccountid identificador de cuenta
     * @param debitandcreditid identificador del movimiento
     * @return DebitandCredit BIAN
     */
    @Override
    public ResponseEntity<DebitandCredit> retrieveDebitandCredit(String currentaccountid, String debitandcreditid) {
        logs.info(Const.ML_INI + "GET /CurrentAccount/" + currentaccountid + "/DebitandCredit/" + debitandcreditid + "/Retrieve");
        DebitandCredit response = debitandCreditService.retrieve(currentaccountid, debitandcreditid);
        logs.info(Const.ML_FIN + "GET /CurrentAccount/" + currentaccountid + "/DebitandCredit/" + debitandcreditid + "/Retrieve");
        return ResponseEntity.ok(response);
    }

    /**
     * Estado de cuenta: débitos y créditos en un rango de fechas.
     *
     * @param currentaccountid identificador de cuenta
     * @param fromDate inicio del rango
     * @param toDate fin del rango
     * @return movimientos BIAN
     */
    @Override
    public ResponseEntity<List<DebitandCredit>> retrieveDebitandCreditReport(
            String currentaccountid, String fromDate, String toDate) {
        logs.info(Const.ML_INI + "GET /CurrentAccount/" + currentaccountid + "/DebitandCredit");
        List<DebitandCredit> response = debitandCreditService.retrieveReport(currentaccountid, fromDate, toDate);
        logs.info(Const.ML_FIN + "GET /CurrentAccount/" + currentaccountid + "/DebitandCredit count=" + response.size());
        return ResponseEntity.ok(response);
    }
}
