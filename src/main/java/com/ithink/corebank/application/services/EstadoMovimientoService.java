package com.ithink.corebank.application.services;

import com.ithink.corebank.domain.entities.EstadoMovimiento;
import com.ithink.corebank.application.dto.EstadoMovimientoDto;
import com.ithink.corebank.domain.ports.EstadoMovimientoRepositoryPort;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio que orquesta los casos de uso de {@code EstadoMovimiento}.
 *
 * @author Jhon
 * @version 1.0
 */
@Service
public class EstadoMovimientoService {

    private static final Loggers logs = new Loggers();

    private final EstadoMovimientoRepositoryPort estadoMovimientoRepositoryPort;

    public EstadoMovimientoService(EstadoMovimientoRepositoryPort estadoMovimientoRepositoryPort) {
        logs.setLogger(EstadoMovimientoService.class);
        this.estadoMovimientoRepositoryPort = estadoMovimientoRepositoryPort;
    }

    /**
     * Consulta un estado de movimiento por su identificador.
     *
     * @param id identificador del estado de movimiento
     * @return DTO del estado de movimiento encontrado
     */
    @Transactional(readOnly = true)
    public EstadoMovimientoDto findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE ESTADO DE MOVIMIENTO POR ID " + id);
        EstadoMovimiento estadoMovimiento = estadoMovimientoRepositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ESTADO DE MOVIMIENTO NO ENCONTRADO: " + id));
        EstadoMovimientoDto dto = EstadoMovimientoDto.from(estadoMovimiento);
        logs.info(Const.ML_FIN + Util.toJson(dto));
        return dto;
    }

    /**
     * Consulta todos los estados de movimiento registrados.
     *
     * @return lista de DTOs de estados de movimiento
     */
    @Transactional(readOnly = true)
    public List<EstadoMovimientoDto> findAll() {
        logs.info(Const.ML_INI + "CONSULTA DE TODOS LOS ESTADOS DE MOVIMIENTO");
        List<EstadoMovimientoDto> estados = estadoMovimientoRepositoryPort.findAll().stream()
                .map(EstadoMovimientoDto::from)
                .toList();
        logs.info(Const.ML_FIN + Util.toJson(estados));
        return estados;
    }

    /**
     * Registra un nuevo estado de movimiento.
     *
     * @param dto datos del estado de movimiento a registrar
     * @return DTO del estado de movimiento persistido
     */
    @Transactional
    public EstadoMovimientoDto save(EstadoMovimientoDto dto) {
        logs.info(Const.ML_INI + Util.toJson(dto));
        EstadoMovimiento estadoMovimiento = mapToEntity(dto);
        EstadoMovimiento estadoGuardado = estadoMovimientoRepositoryPort.save(estadoMovimiento);
        EstadoMovimientoDto dtoGuardado = EstadoMovimientoDto.from(estadoGuardado);
        logs.info(Const.ML_FIN + Util.toJson(dtoGuardado));
        return dtoGuardado;
    }

    /**
     * Convierte un DTO en una entidad.
     *
     * @param dto DTO de origen
     * @return entidad lista para persistir
     */
    private EstadoMovimiento mapToEntity(EstadoMovimientoDto dto) {
        EstadoMovimiento estadoMovimiento = new EstadoMovimiento();
        estadoMovimiento.setCodigo(dto.getCodigo());
        estadoMovimiento.setNombre(dto.getNombre());
        estadoMovimiento.setDescripcion(dto.getDescripcion());
        return estadoMovimiento;
    }
}
