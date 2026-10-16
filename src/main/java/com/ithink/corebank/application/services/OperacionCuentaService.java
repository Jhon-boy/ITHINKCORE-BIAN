package com.ithink.corebank.application.services;

import com.ithink.corebank.application.dto.DepositarRequest;
import com.ithink.corebank.application.dto.MovimientoDto;
import com.ithink.corebank.application.dto.RetirarRequest;
import com.ithink.corebank.domain.entities.Movimiento;
import com.ithink.corebank.domain.models.LedgerPostingRequest;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Casos de uso de mantenimiento sobre cuenta: depositar y retirar.
 * Delega en {@link AccountLedgerService}.
 *
 * @author Jhon
 * @version 1.0
 */
@Service
public class OperacionCuentaService {

    private static final int METODO_DEPOSITO = 1;
    private static final int TRANSACCION_DEPOSITO = 1;
    private static final int CANAL_APP = 1;
    private static final int SUCURSAL_MATRIZ = 1;
    private static final int METODO_RETIRO = 2;
    private static final int TRANSACCION_RETIRO = 2;
    private static final int CANAL_VENTANILLA = 3;

    private static final Loggers logs = new Loggers();

    private final AccountLedgerService accountLedgerService;

    /**
     * Crea el servicio de operaciones de cuenta.
     *
     * @param accountLedgerService motor de ledger
     */
    public OperacionCuentaService(AccountLedgerService accountLedgerService) {
        logs.setLogger(OperacionCuentaService.class);
        this.accountLedgerService = accountLedgerService;
    }

    /**
     * Deposita fondos (crédito inmediato BOOKED).
     *
     * @param request datos del depósito
     * @return movimiento generado
     */
    @Transactional
    public MovimientoDto depositar(DepositarRequest request) {
        logs.info(Const.ML_INI + "DEPOSITAR " + Util.toJson(request));
        validateMoneyRequest(request.getIdCuenta(), request.getMonto());
        LedgerPostingRequest ledgerRequest = LedgerPostingRequest.builder()
                .idCuenta(request.getIdCuenta())
                .tipo(AccountLedgerService.TIPO_CREDITO)
                .monto(request.getMonto())
                .idMetodoOperacion(METODO_DEPOSITO)
                .idTransaccion(TRANSACCION_DEPOSITO)
                .idCanal(CANAL_APP)
                .idSucursal(SUCURSAL_MATRIZ)
                .mensaje(defaultText(request.getMensaje(), "DEPOSITO API"))
                .comentario(request.getComentario())
                .referencia(request.getReferencia())
                .usuario(defaultText(request.getUsuario(), "API"))
                .build();
        Movimiento posted = accountLedgerService.postImmediate(ledgerRequest);
        MovimientoDto dto = MovimientoDto.from(posted);
        logs.info(Const.ML_FIN + Util.toJson(dto));
        return dto;
    }

    /**
     * Retira fondos (débito inmediato BOOKED).
     *
     * @param request datos del retiro
     * @return movimiento generado
     */
    @Transactional
    public MovimientoDto retirar(RetirarRequest request) {
        logs.info(Const.ML_INI + "RETIRAR " + Util.toJson(request));
        validateMoneyRequest(request.getIdCuenta(), request.getMonto());
        LedgerPostingRequest ledgerRequest = LedgerPostingRequest.builder()
                .idCuenta(request.getIdCuenta())
                .tipo(AccountLedgerService.TIPO_DEBITO)
                .monto(request.getMonto())
                .idMetodoOperacion(METODO_RETIRO)
                .idTransaccion(TRANSACCION_RETIRO)
                .idCanal(CANAL_VENTANILLA)
                .idSucursal(SUCURSAL_MATRIZ)
                .mensaje(defaultText(request.getMensaje(), "RETIRO API"))
                .comentario(request.getComentario())
                .referencia(request.getReferencia())
                .usuario(defaultText(request.getUsuario(), "API"))
                .build();
        Movimiento posted = accountLedgerService.postImmediate(ledgerRequest);
        MovimientoDto dto = MovimientoDto.from(posted);
        logs.info(Const.ML_FIN + Util.toJson(dto));
        return dto;
    }

    private void validateMoneyRequest(Integer idCuenta, BigDecimal monto) {
        if (idCuenta == null) {
            throw new IllegalArgumentException("IDCUENTA ES REQUERIDO");
        }
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("MONTO DEBE SER MAYOR A CERO");
        }
    }

    private String defaultText(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }
}
