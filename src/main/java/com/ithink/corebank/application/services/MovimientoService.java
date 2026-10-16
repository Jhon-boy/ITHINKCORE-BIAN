package com.ithink.corebank.application.services;

import com.ithink.corebank.domain.entities.Movimiento;
import com.ithink.corebank.domain.models.LedgerPostingRequest;
import com.ithink.corebank.application.dto.MovimientoDto;
import com.ithink.corebank.domain.ports.MovimientoRepositoryPort;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio que orquesta los casos de uso de {@code Movimiento}.
 * El alta pasa por el ledger (afecta saldo e histórico).
 *
 * @author Jhon
 * @version 1.0
 */
@Service
public class MovimientoService {

    private static final Loggers logs = new Loggers();

    private final MovimientoRepositoryPort movimientoRepositoryPort;
    private final AccountLedgerService accountLedgerService;

    public MovimientoService(MovimientoRepositoryPort movimientoRepositoryPort,
                             AccountLedgerService accountLedgerService) {
        logs.setLogger(MovimientoService.class);
        this.movimientoRepositoryPort = movimientoRepositoryPort;
        this.accountLedgerService = accountLedgerService;
    }

    /**
     * Consulta un movimiento por su identificador.
     *
     * @param id identificador del movimiento
     * @return DTO del movimiento encontrado
     */
    @Transactional(readOnly = true)
    public MovimientoDto findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE MOVIMIENTO POR ID " + id);
        Movimiento movimiento = movimientoRepositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("MOVIMIENTO NO ENCONTRADO: " + id));
        MovimientoDto dto = MovimientoDto.from(movimiento);
        logs.info(Const.ML_FIN + Util.toJson(dto));
        return dto;
    }

    /**
     * Consulta los movimientos dentro de un rango de fechas.
     *
     * @param fechaInicio fecha de inicio del rango
     * @param fechaFin    fecha de fin del rango
     * @return lista de DTOs de movimientos
     */
    @Transactional(readOnly = true)
    public List<MovimientoDto> findAll(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        logs.info(Const.ML_INI + "CONSULTA DE MOVIMIENTOS ENTRE " + fechaInicio + " Y " + fechaFin);
        List<MovimientoDto> movimientos = movimientoRepositoryPort.findAll(fechaInicio, fechaFin).stream()
                .map(MovimientoDto::from)
                .toList();
        logs.info(Const.ML_FIN + Util.toJson(movimientos));
        return movimientos;
    }

    /**
     * Registra y postea un movimiento (afecta saldo + histórico).
     *
     * @param dto datos del movimiento a registrar
     * @return DTO del movimiento persistido
     */
    @Transactional
    public MovimientoDto save(MovimientoDto dto) {
        logs.info(Const.ML_INI + Util.toJson(dto));
        LedgerPostingRequest request = LedgerPostingRequest.builder()
                .idCuenta(dto.getIdCuenta())
                .tipo(dto.getTipo())
                .monto(dto.getMonto())
                .idCuentaContraparte(dto.getIdCuentaContraparte())
                .numeroCuentaContraparte(dto.getNumeroCuentaContraparte())
                .nombreContraparte(dto.getNombreContraparte())
                .codigoBancoContraparte(dto.getCodigoBancoContraparte())
                .nombreBancoContraparte(dto.getNombreBancoContraparte())
                .idMetodoOperacion(dto.getIdMetodoOperacion())
                .idTransaccion(dto.getIdTransaccion())
                .idCanal(dto.getIdCanal())
                .idSucursal(dto.getIdSucursal())
                .idMedio(dto.getIdMedio())
                .mensaje(dto.getMensaje())
                .comentario(dto.getComentario())
                .referencia(dto.getReferencia())
                .uuid(dto.getUuid())
                .codigoRastreo(dto.getCodigoRastreo())
                .usuario(dto.getUsuarioCreacion() != null ? dto.getUsuarioCreacion() : "API")
                .build();

        Movimiento movimientoGuardado = accountLedgerService.postImmediate(request);
        MovimientoDto dtoGuardado = MovimientoDto.from(movimientoGuardado);
        logs.info(Const.ML_FIN + Util.toJson(dtoGuardado));
        return dtoGuardado;
    }
}
