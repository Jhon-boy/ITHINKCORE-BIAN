package com.ithink.corebank.application.services;

import com.ithink.corebank.domain.entities.Reporte;
import com.ithink.corebank.application.dto.ReporteDto;
import com.ithink.corebank.domain.ports.ReporteRepositoryPort;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio que orquesta los casos de uso de {@code Reporte}.
 *
 * @author Jhon
 * @version 1.0
 */
@Service
public class ReporteService {

    private static final Loggers logs = new Loggers();

    private final ReporteRepositoryPort reporteRepositoryPort;

    public ReporteService(ReporteRepositoryPort reporteRepositoryPort) {
        logs.setLogger(ReporteService.class);
        this.reporteRepositoryPort = reporteRepositoryPort;
    }

    /**
     * Consulta un reporte por su identificador.
     *
     * @param id identificador del reporte
     * @return DTO del reporte encontrado
     */
    @Transactional(readOnly = true)
    public ReporteDto findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE REPORTE POR ID " + id);
        Reporte reporte = reporteRepositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("REPORTE NO ENCONTRADO: " + id));
        ReporteDto dto = ReporteDto.from(reporte);
        logs.info(Const.ML_FIN + Util.toJson(dto));
        return dto;
    }

    /**
     * Consulta los reportes dentro de un rango de fechas.
     *
     * @param fechaInicio fecha de inicio del rango
     * @param fechaFin    fecha de fin del rango
     * @return lista de DTOs de reportes
     */
    @Transactional(readOnly = true)
    public List<ReporteDto> findAll(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        logs.info(Const.ML_INI + "CONSULTA DE REPORTES ENTRE " + fechaInicio + " Y " + fechaFin);
        List<ReporteDto> reportes = reporteRepositoryPort.findAll(fechaInicio, fechaFin).stream()
                .map(ReporteDto::from)
                .toList();
        logs.info(Const.ML_FIN + Util.toJson(reportes));
        return reportes;
    }

    /**
     * Registra un nuevo reporte.
     *
     * @param dto datos del reporte a registrar
     * @return DTO del reporte persistido
     */
    @Transactional
    public ReporteDto save(ReporteDto dto) {
        logs.info(Const.ML_INI + Util.toJson(dto));
        Reporte reporte = mapToEntity(dto);
        Reporte reporteGuardado = reporteRepositoryPort.save(reporte);
        ReporteDto dtoGuardado = ReporteDto.from(reporteGuardado);
        logs.info(Const.ML_FIN + Util.toJson(dtoGuardado));
        return dtoGuardado;
    }

    /**
     * Convierte un DTO en una entidad.
     *
     * @param dto DTO de origen
     * @return entidad lista para persistir
     */
    private Reporte mapToEntity(ReporteDto dto) {
        Reporte reporte = new Reporte();
        reporte.setIdReporte(dto.getIdReporte());
        reporte.setFechaMovimiento(dto.getFechaMovimiento());
        reporte.setFechaContable(dto.getFechaContable());
        reporte.setTipo(dto.getTipo());
        reporte.setTransaccion(dto.getTransaccion());
        reporte.setMontoMovimiento(dto.getMontoMovimiento());
        reporte.setNombreOrdenante(dto.getNombreOrdenante());
        reporte.setBancoOrdenante(dto.getBancoOrdenante());
        reporte.setIdentificacionOrdenante(dto.getIdentificacionOrdenante());
        reporte.setNombreBeneficiario(dto.getNombreBeneficiario());
        reporte.setBancoBeneficiario(dto.getBancoBeneficiario());
        reporte.setIdentificacionBeneficiario(dto.getIdentificacionBeneficiario());
        reporte.setSaldoAnterior(dto.getSaldoAnterior());
        reporte.setSaldoActual(dto.getSaldoActual());
        reporte.setCanal(dto.getCanal());
        reporte.setReferencia(dto.getReferencia());
        reporte.setComentario(dto.getComentario());
        reporte.setUsuarioEjecucion(dto.getUsuarioEjecucion());
        reporte.setCuentaOrdenante(dto.getCuentaOrdenante());
        reporte.setCuentaBeneficiario(dto.getCuentaBeneficiario());
        reporte.setIdCuenta(dto.getIdCuenta());
        return reporte;
    }
}
