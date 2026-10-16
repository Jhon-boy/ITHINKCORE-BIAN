package com.ithink.corebank.application.services;

import com.ithink.corebank.domain.entities.Cuenta;
import com.ithink.corebank.domain.entities.SaldoHistorico;
import com.ithink.corebank.application.dto.SaldoHistoricoDto;
import com.ithink.corebank.domain.ports.CuentaRepositoryPort;
import com.ithink.corebank.domain.ports.SaldoHistoricoRepositoryPort;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio que orquesta los casos de uso de {@code SaldoHistorico}.
 *
 * @author Jhon
 * @version 1.0
 */
@Service
public class SaldoHistoricoService {

    private static final Loggers logs = new Loggers();

    private final SaldoHistoricoRepositoryPort saldoHistoricoRepositoryPort;

    private final CuentaRepositoryPort cuentaRepositoryPort;

    public SaldoHistoricoService(SaldoHistoricoRepositoryPort saldoHistoricoRepositoryPort,
                                 CuentaRepositoryPort cuentaRepositoryPort) {
        logs.setLogger(SaldoHistoricoService.class);
        this.saldoHistoricoRepositoryPort = saldoHistoricoRepositoryPort;
        this.cuentaRepositoryPort = cuentaRepositoryPort;
    }

    /**
     * Consulta un saldo historico por su identificador.
     *
     * @param id identificador del saldo historico
     * @return DTO del saldo historico encontrado
     */
    @Transactional(readOnly = true)
    public SaldoHistoricoDto findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE SALDO HISTORICO POR ID " + id);
        SaldoHistorico saldoHistorico = saldoHistoricoRepositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("SALDO HISTORICO NO ENCONTRADO: " + id));
        SaldoHistoricoDto dto = SaldoHistoricoDto.from(saldoHistorico);
        logs.info(Const.ML_FIN + Util.toJson(dto));
        return dto;
    }

    /**
     * Consulta los saldos historicos dentro de un rango de fechas.
     *
     * @param fechaInicio fecha de inicio del rango
     * @param fechaFin    fecha de fin del rango
     * @return lista de DTOs de saldos historicos
     */
    @Transactional(readOnly = true)
    public List<SaldoHistoricoDto> findAll(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        logs.info(Const.ML_INI + "CONSULTA DE SALDOS HISTORICOS ENTRE " + fechaInicio + " Y " + fechaFin);
        List<SaldoHistoricoDto> saldos = saldoHistoricoRepositoryPort.findAll(fechaInicio, fechaFin).stream()
                .map(SaldoHistoricoDto::from)
                .toList();
        logs.info(Const.ML_FIN + Util.toJson(saldos));
        return saldos;
    }

    /**
     * Registra un nuevo saldo historico.
     *
     * @param dto datos del saldo historico a registrar
     * @return DTO del saldo historico persistido
     */
    @Transactional
    public SaldoHistoricoDto save(SaldoHistoricoDto dto) {
        logs.info(Const.ML_INI + Util.toJson(dto));
        SaldoHistorico saldoHistorico = mapToEntity(dto);
        SaldoHistorico saldoGuardado = saldoHistoricoRepositoryPort.save(saldoHistorico);
        SaldoHistoricoDto dtoGuardado = SaldoHistoricoDto.from(saldoGuardado);
        logs.info(Const.ML_FIN + Util.toJson(dtoGuardado));
        return dtoGuardado;
    }

    /**
     * Convierte un DTO en una entidad, resolviendo la relacion con {@link Cuenta}.
     *
     * @param dto DTO de origen
     * @return entidad lista para persistir
     */
    private SaldoHistorico mapToEntity(SaldoHistoricoDto dto) {
        SaldoHistorico saldoHistorico = new SaldoHistorico();
        saldoHistorico.setMontoContable(dto.getMontoContable());
        saldoHistorico.setMontoDisponible(dto.getMontoDisponible());
        saldoHistorico.setMontoBloqueado(dto.getMontoBloqueado());
        saldoHistorico.setMontoPorEfectivizar(dto.getMontoPorEfectivizar());
        saldoHistorico.setEstado(dto.getEstado());
        saldoHistorico.setComentario(dto.getComentario());
        if (dto.getIdCuenta() != null) {
            Cuenta cuenta = cuentaRepositoryPort.findById(dto.getIdCuenta())
                    .orElseThrow(() -> new IllegalArgumentException("CUENTA NO ENCONTRADA: " + dto.getIdCuenta()));
            saldoHistorico.setCuenta(cuenta);
            saldoHistorico.setIdCuenta(dto.getIdCuenta());
        }
        return saldoHistorico;
    }
}
