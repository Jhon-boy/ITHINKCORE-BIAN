package com.ithink.corebank.application.bian.services;

import com.ithink.bian.currentaccount.model.Payment;
import com.ithink.corebank.application.bian.mapper.PaymentBianMapper;
import com.ithink.corebank.application.bian.support.BianAccountSupport;
import com.ithink.corebank.application.services.AccountLedgerService;
import com.ithink.corebank.domain.entities.Movimiento;
import com.ithink.corebank.domain.models.LedgerPostingRequest;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Casos de uso BIAN del Behavior Qualifier Payment.
 *
 * @author Jhon
 * @version 1.0
 */
@Service
public class PaymentService {

    private static final Loggers logs = new Loggers();

    private final BianAccountSupport bianAccountSupport;
    private final AccountLedgerService accountLedgerService;
    private final PaymentBianMapper paymentBianMapper;

    /**
     * Crea el servicio BIAN Payment.
     *
     * @param bianAccountSupport soporte compartido
     * @param accountLedgerService motor de ledger
     * @param paymentBianMapper mapper BIAN
     */
    public PaymentService(
            BianAccountSupport bianAccountSupport,
            AccountLedgerService accountLedgerService,
            PaymentBianMapper paymentBianMapper) {
        logs.setLogger(PaymentService.class);
        this.bianAccountSupport = bianAccountSupport;
        this.accountLedgerService = accountLedgerService;
        this.paymentBianMapper = paymentBianMapper;
    }

    /**
     * Inicia un pago/retiro creando un débito en estado PENDIENTE.
     *
     * @param currentaccountid identificador de cuenta
     * @param payment payload BIAN
     * @return pago iniciado
     */
    @Transactional
    public Payment initiate(String currentaccountid, Payment payment) {
        logs.info(Const.ML_INI + "BIAN Payment Initiate account=" + currentaccountid);
        try {
            Integer idCuenta = bianAccountSupport.parseId(currentaccountid, "currentaccountid");
            bianAccountSupport.requireCuenta(idCuenta, currentaccountid);
            LedgerPostingRequest request = paymentBianMapper.toLedgerRequest(idCuenta, payment);
            Movimiento pending = accountLedgerService.initiate(request);
            Payment response = paymentBianMapper.toPayment(pending);
            logs.info(Const.ML_FIN + "BIAN Payment Initiate id=" + pending.getIdMovimiento());
            return response;
        } catch (IllegalArgumentException ex) {
            throw bianAccountSupport.mapLedgerException(ex);
        }
    }

    /**
     * Ejecuta (postea) un pago pendiente afectando saldo e histórico.
     *
     * @param currentaccountid identificador de cuenta
     * @param paymentid identificador del pago
     * @return pago posteado
     */
    @Transactional
    public Payment execute(String currentaccountid, String paymentid) {
        logs.info(Const.ML_INI + "BIAN Payment Execute account=" + currentaccountid + " id=" + paymentid);
        try {
            Movimiento pending = bianAccountSupport.requireMovimientoForAccount(currentaccountid, paymentid, "Payment");
            bianAccountSupport.assertDebit(pending, "Payment");
            Movimiento posted = accountLedgerService.execute(pending.getIdMovimiento());
            Payment response = paymentBianMapper.toPayment(posted);
            logs.info(Const.ML_FIN + "BIAN Payment Execute id=" + posted.getIdMovimiento());
            return response;
        } catch (IllegalArgumentException ex) {
            throw bianAccountSupport.mapLedgerException(ex);
        }
    }

    /**
     * Consulta un pago de débito.
     *
     * @param currentaccountid identificador de cuenta
     * @param paymentid identificador del pago
     * @return pago BIAN
     */
    @Transactional(readOnly = true)
    public Payment retrieve(String currentaccountid, String paymentid) {
        Movimiento movimiento = bianAccountSupport.requireMovimientoForAccount(currentaccountid, paymentid, "Payment");
        bianAccountSupport.assertDebit(movimiento, "Payment");
        return paymentBianMapper.toPayment(movimiento);
    }
}
