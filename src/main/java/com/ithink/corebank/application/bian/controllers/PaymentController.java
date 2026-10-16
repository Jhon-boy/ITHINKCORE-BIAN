package com.ithink.corebank.application.bian.controllers;

import com.ithink.bian.currentaccount.api.PaymentApi;
import com.ithink.bian.currentaccount.model.Payment;
import com.ithink.corebank.application.bian.services.PaymentService;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador BIAN del Behavior Qualifier Payment.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
public class PaymentController implements PaymentApi {

    private static final Loggers logs = new Loggers();

    private final PaymentService paymentService;

    /**
     * Crea el controlador BIAN Payment.
     *
     * @param paymentService servicio de casos de uso
     */
    public PaymentController(PaymentService paymentService) {
        logs.setLogger(PaymentController.class);
        this.paymentService = paymentService;
    }

    /**
     * Inicia un pago/retiro (débito pendiente).
     *
     * @param currentaccountid identificador de cuenta
     * @param payment payload BIAN
     * @return pago iniciado
     */
    @Override
    public ResponseEntity<Payment> initiatePayment(String currentaccountid, Payment payment) {
        logs.info(Const.ML_INI + "POST /CurrentAccount/" + currentaccountid + "/Payment/Initiate");
        Payment response = paymentService.initiate(currentaccountid, payment);
        logs.info(Const.ML_FIN + "POST /CurrentAccount/" + currentaccountid + "/Payment/Initiate");
        return ResponseEntity.ok(response);
    }

    /**
     * Ejecuta un pago pendiente.
     *
     * @param currentaccountid identificador de cuenta
     * @param paymentid identificador del pago
     * @param payment payload BIAN (no usado en execute)
     * @return pago posteado
     */
    @Override
    public ResponseEntity<Payment> executePayment(String currentaccountid, String paymentid, Payment payment) {
        logs.info(Const.ML_INI + "PUT /CurrentAccount/" + currentaccountid + "/Payment/" + paymentid + "/Execute");
        Payment response = paymentService.execute(currentaccountid, paymentid);
        logs.info(Const.ML_FIN + "PUT /CurrentAccount/" + currentaccountid + "/Payment/" + paymentid + "/Execute");
        return ResponseEntity.ok(response);
    }

    /**
     * Consulta un pago.
     *
     * @param currentaccountid identificador de cuenta
     * @param paymentid identificador del pago
     * @return pago BIAN
     */
    @Override
    public ResponseEntity<Payment> retrievePayment(String currentaccountid, String paymentid) {
        logs.info(Const.ML_INI + "GET /CurrentAccount/" + currentaccountid + "/Payment/" + paymentid + "/Retrieve");
        Payment response = paymentService.retrieve(currentaccountid, paymentid);
        logs.info(Const.ML_FIN + "GET /CurrentAccount/" + currentaccountid + "/Payment/" + paymentid + "/Retrieve");
        return ResponseEntity.ok(response);
    }
}
