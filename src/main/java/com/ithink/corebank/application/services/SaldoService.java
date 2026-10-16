package com.ithink.corebank.application.services;

import com.ithink.corebank.domain.entities.Cuenta;
import com.ithink.corebank.domain.entities.Saldo;
import com.ithink.corebank.application.dto.SaldoDto;
import com.ithink.corebank.domain.ports.CuentaRepositoryPort;
import com.ithink.corebank.domain.ports.SaldoRepositoryPort;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio que orquesta los casos de uso de {@code Saldo}.
 *
 * @author Jhon
 * @version 1.0
 */
@Service
public class SaldoService {

    private static final Loggers logs = new Loggers();

    private final SaldoRepositoryPort saldoRepositoryPort;

    private final CuentaRepositoryPort cuentaRepositoryPort;

    public SaldoService(SaldoRepositoryPort saldoRepositoryPort,
                        CuentaRepositoryPort cuentaRepositoryPort) {
        logs.setLogger(SaldoService.class);
        this.saldoRepositoryPort = saldoRepositoryPort;
        this.cuentaRepositoryPort = cuentaRepositoryPort;
    }

    /**
     * Consulta un saldo por su identificador.
     *
     * @param id identificador del saldo
     * @return DTO del saldo encontrado
     */
    @Transactional(readOnly = true)
    public SaldoDto findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE SALDO POR ID " + id);
        Saldo saldo = saldoRepositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("SALDO NO ENCONTRADO: " + id));
        SaldoDto dto = SaldoDto.from(saldo);
        logs.info(Const.ML_FIN + Util.toJson(dto));
        return dto;
    }

    /**
     * Consulta el saldo asociado a una cuenta.
     *
     * @param idCuenta identificador de la cuenta
     * @return DTO del saldo encontrado
     */
    @Transactional(readOnly = true)
    public SaldoDto findByIdCuenta(Integer idCuenta) {
        logs.info(Const.ML_INI + "CONSULTA DE SALDO POR IDCUENTA " + idCuenta);
        Saldo saldo = saldoRepositoryPort.findByIdCuenta(idCuenta)
                .orElseThrow(() -> new IllegalArgumentException(
                        "SALDO NO ENCONTRADO PARA CUENTA: " + idCuenta));
        SaldoDto dto = SaldoDto.from(saldo);
        logs.info(Const.ML_FIN + Util.toJson(dto));
        return dto;
    }

    /**
     * Consulta los saldos dentro de un rango de fechas.
     *
     * @param fechaInicio fecha de inicio del rango
     * @param fechaFin    fecha de fin del rango
     * @return lista de DTOs de saldos
     */
    @Transactional(readOnly = true)
    public List<SaldoDto> findAll(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        logs.info(Const.ML_INI + "CONSULTA DE SALDOS ENTRE " + fechaInicio + " Y " + fechaFin);
        List<SaldoDto> saldos = saldoRepositoryPort.findAll(fechaInicio, fechaFin).stream()
                .map(SaldoDto::from)
                .toList();
        logs.info(Const.ML_FIN + Util.toJson(saldos));
        return saldos;
    }

    /**
     * Registra un nuevo saldo.
     *
     * @param dto datos del saldo a registrar
     * @return DTO del saldo persistido
     */
    @Transactional
    public SaldoDto save(SaldoDto dto) {
        logs.info(Const.ML_INI + Util.toJson(dto));
        Saldo saldo = mapToEntity(dto);
        Saldo saldoGuardado = saldoRepositoryPort.save(saldo);
        SaldoDto dtoGuardado = SaldoDto.from(saldoGuardado);
        logs.info(Const.ML_FIN + Util.toJson(dtoGuardado));
        return dtoGuardado;
    }

    /**
     * Convierte un DTO en una entidad, resolviendo la relacion con {@link Cuenta}.
     *
     * @param dto DTO de origen
     * @return entidad lista para persistir
     */
    private Saldo mapToEntity(SaldoDto dto) {
        Saldo saldo = new Saldo();
        saldo.setMontoContable(dto.getMontoContable());
        saldo.setMontoDisponible(dto.getMontoDisponible());
        saldo.setMontoBloqueado(dto.getMontoBloqueado());
        saldo.setMontoPorEfectivizar(dto.getMontoPorEfectivizar());
        saldo.setEstado(dto.getEstado());
        saldo.setComentario(dto.getComentario());
        if (dto.getIdCuenta() != null) {
            Cuenta cuenta = cuentaRepositoryPort.findById(dto.getIdCuenta())
                    .orElseThrow(() -> new IllegalArgumentException("CUENTA NO ENCONTRADA: " + dto.getIdCuenta()));
            saldo.setCuenta(cuenta);
            saldo.setIdCuenta(dto.getIdCuenta());
        }
        return saldo;
    }
}
