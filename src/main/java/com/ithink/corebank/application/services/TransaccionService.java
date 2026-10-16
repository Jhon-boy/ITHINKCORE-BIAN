package com.ithink.corebank.application.services;

import com.ithink.corebank.domain.entities.Canal;
import com.ithink.corebank.domain.entities.Modulo;
import com.ithink.corebank.domain.entities.Transaccion;
import com.ithink.corebank.application.dto.TransaccionDto;
import com.ithink.corebank.domain.ports.CanalRepositoryPort;
import com.ithink.corebank.domain.ports.ModuloRepositoryPort;
import com.ithink.corebank.domain.ports.TransaccionRepositoryPort;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio que orquesta los casos de uso de {@code Transaccion}.
 *
 * @author Jhon
 * @version 1.0
 */
@Service
public class TransaccionService {

    private static final Loggers logs = new Loggers();

    private final TransaccionRepositoryPort transaccionRepositoryPort;

    private final ModuloRepositoryPort moduloRepositoryPort;

    private final CanalRepositoryPort canalRepositoryPort;

    public TransaccionService(TransaccionRepositoryPort transaccionRepositoryPort,
                              ModuloRepositoryPort moduloRepositoryPort,
                              CanalRepositoryPort canalRepositoryPort) {
        logs.setLogger(TransaccionService.class);
        this.transaccionRepositoryPort = transaccionRepositoryPort;
        this.moduloRepositoryPort = moduloRepositoryPort;
        this.canalRepositoryPort = canalRepositoryPort;
    }

    /**
     * Consulta una transaccion por su identificador.
     *
     * @param id identificador de la transaccion
     * @return DTO de la transaccion encontrada
     */
    @Transactional(readOnly = true)
    public TransaccionDto findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE TRANSACCION POR ID " + id);
        Transaccion transaccion = transaccionRepositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("TRANSACCION NO ENCONTRADA: " + id));
        TransaccionDto dto = TransaccionDto.from(transaccion);
        logs.info(Const.ML_FIN + Util.toJson(dto));
        return dto;
    }

    /**
     * Consulta las transacciones dentro de un rango de fechas.
     *
     * @param fechaInicio fecha de inicio del rango
     * @param fechaFin    fecha de fin del rango
     * @return lista de DTOs de transacciones
     */
    @Transactional(readOnly = true)
    public List<TransaccionDto> findAll(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        logs.info(Const.ML_INI + "CONSULTA DE TRANSACCIONES ENTRE " + fechaInicio + " Y " + fechaFin);
        List<TransaccionDto> transacciones = transaccionRepositoryPort.findAll(fechaInicio, fechaFin).stream()
                .map(TransaccionDto::from)
                .toList();
        logs.info(Const.ML_FIN + Util.toJson(transacciones));
        return transacciones;
    }

    /**
     * Registra una nueva transaccion.
     *
     * @param dto datos de la transaccion a registrar
     * @return DTO de la transaccion persistida
     */
    @Transactional
    public TransaccionDto save(TransaccionDto dto) {
        logs.info(Const.ML_INI + Util.toJson(dto));
        Transaccion transaccion = mapToEntity(dto);
        Transaccion transaccionGuardada = transaccionRepositoryPort.save(transaccion);
        TransaccionDto dtoGuardado = TransaccionDto.from(transaccionGuardada);
        logs.info(Const.ML_FIN + Util.toJson(dtoGuardado));
        return dtoGuardado;
    }

    /**
     * Convierte un DTO en una entidad, resolviendo las relaciones con
     * {@link Modulo} y {@link Canal}.
     *
     * @param dto DTO de origen
     * @return entidad lista para persistir
     */
    private Transaccion mapToEntity(TransaccionDto dto) {
        Transaccion transaccion = new Transaccion();
        transaccion.setCodigo(dto.getCodigo());
        transaccion.setNombre(dto.getNombre());
        transaccion.setDescripcion(dto.getDescripcion());
        transaccion.setEstado(dto.getEstado());
        if (dto.getIdModulo() != null) {
            Modulo modulo = moduloRepositoryPort.findById(dto.getIdModulo())
                    .orElseThrow(() -> new IllegalArgumentException("MODULO NO ENCONTRADO: " + dto.getIdModulo()));
            transaccion.setModulo(modulo);
            transaccion.setIdModulo(dto.getIdModulo());
        }
        if (dto.getIdCanal() != null) {
            Canal canal = canalRepositoryPort.findById(dto.getIdCanal())
                    .orElseThrow(() -> new IllegalArgumentException("CANAL NO ENCONTRADO: " + dto.getIdCanal()));
            transaccion.setCanal(canal);
            transaccion.setIdCanal(dto.getIdCanal());
        }
        return transaccion;
    }
}
