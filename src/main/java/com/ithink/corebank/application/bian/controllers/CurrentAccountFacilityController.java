package com.ithink.corebank.application.bian.controllers;

import com.ithink.bian.currentaccount.api.CurrentAccountFacilityApi;
import com.ithink.bian.currentaccount.model.CurrentAccountFacility;
import com.ithink.corebank.application.bian.services.CurrentAccountFacilityService;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador BIAN del Control Record CurrentAccountFacility.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
public class CurrentAccountFacilityController implements CurrentAccountFacilityApi {

    private static final Loggers logs = new Loggers();

    private final CurrentAccountFacilityService currentAccountFacilityService;

    /**
     * Crea el controlador BIAN CurrentAccountFacility.
     *
     * @param currentAccountFacilityService servicio de casos de uso
     */
    public CurrentAccountFacilityController(CurrentAccountFacilityService currentAccountFacilityService) {
        logs.setLogger(CurrentAccountFacilityController.class);
        this.currentAccountFacilityService = currentAccountFacilityService;
    }

    /**
     * Consulta la facilidad de cuenta corriente con saldos.
     *
     * @param currentaccountid identificador de cuenta
     * @return facility BIAN
     */
    @Override
    public ResponseEntity<CurrentAccountFacility> retrieve(String currentaccountid) {
        logs.info(Const.ML_INI + "GET /CurrentAccount/" + currentaccountid + "/Retrieve");
        CurrentAccountFacility response = currentAccountFacilityService.retrieve(currentaccountid);
        logs.info(Const.ML_FIN + "GET /CurrentAccount/" + currentaccountid + "/Retrieve");
        return ResponseEntity.ok(response);
    }

    /**
     * Abre una cuenta corriente.
     *
     * @param currentAccountFacility datos de apertura
     * @return facility creada
     */
    @Override
    public ResponseEntity<CurrentAccountFacility> initiate(CurrentAccountFacility currentAccountFacility) {
        logs.info(Const.ML_INI + "POST /CurrentAccount/Initiate");
        CurrentAccountFacility response = currentAccountFacilityService.initiate(currentAccountFacility);
        logs.info(Const.ML_FIN + "POST /CurrentAccount/Initiate");
        return ResponseEntity.ok(response);
    }

    /**
     * Actualiza el estado de la cuenta.
     *
     * @param currentaccountid identificador de cuenta
     * @param currentAccountFacility payload con el estado
     * @return facility actualizada
     */
    @Override
    public ResponseEntity<CurrentAccountFacility> update(
            String currentaccountid, CurrentAccountFacility currentAccountFacility) {
        logs.info(Const.ML_INI + "PUT /CurrentAccount/" + currentaccountid + "/Update");
        CurrentAccountFacility response = currentAccountFacilityService.update(currentaccountid, currentAccountFacility);
        logs.info(Const.ML_FIN + "PUT /CurrentAccount/" + currentaccountid + "/Update");
        return ResponseEntity.ok(response);
    }
}
