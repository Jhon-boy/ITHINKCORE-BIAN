package com.ithink.corebank.application.services;

import com.ithink.corebank.domain.entities.Medio;
import com.ithink.corebank.application.dto.MedioDto;
import com.ithink.corebank.domain.ports.MedioRepositoryPort;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio que orquesta los casos de uso de {@code Medio}.
 *
 * @author Jhon
 * @version 1.0
 */
@Service
public class MedioService {

    private static final Loggers logs = new Loggers();

    private final MedioRepositoryPort medioRepositoryPort;

    public MedioService(MedioRepositoryPort medioRepositoryPort) {
        logs.setLogger(MedioService.class);
        this.medioRepositoryPort = medioRepositoryPort;
    }

    /**
     * Consulta un medio por su identificador.
     *
     * @param id identificador del medio
     * @return DTO del medio encontrado
     */
    @Transactional(readOnly = true)
    public MedioDto findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE MEDIO POR ID " + id);
        Medio medio = medioRepositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("MEDIO NO ENCONTRADO: " + id));
        MedioDto dto = MedioDto.from(medio);
        logs.info(Const.ML_FIN + Util.toJson(dto));
        return dto;
    }

    /**
     * Consulta todos los medios registrados.
     *
     * @return lista de DTOs de medios
     */
    @Transactional(readOnly = true)
    public List<MedioDto> findAll() {
        logs.info(Const.ML_INI + "CONSULTA DE TODOS LOS MEDIOS");
        List<MedioDto> medios = medioRepositoryPort.findAll().stream()
                .map(MedioDto::from)
                .toList();
        logs.info(Const.ML_FIN + Util.toJson(medios));
        return medios;
    }

    /**
     * Registra un nuevo medio.
     *
     * @param dto datos del medio a registrar
     * @return DTO del medio persistido
     */
    @Transactional
    public MedioDto save(MedioDto dto) {
        logs.info(Const.ML_INI + Util.toJson(dto));
        Medio medio = mapToEntity(dto);
        Medio medioGuardado = medioRepositoryPort.save(medio);
        MedioDto dtoGuardado = MedioDto.from(medioGuardado);
        logs.info(Const.ML_FIN + Util.toJson(dtoGuardado));
        return dtoGuardado;
    }

    /**
     * Convierte un DTO en una entidad.
     *
     * @param dto DTO de origen
     * @return entidad lista para persistir
     */
    private Medio mapToEntity(MedioDto dto) {
        Medio medio = new Medio();
        medio.setCodigo(dto.getCodigo());
        medio.setNombre(dto.getNombre());
        medio.setDescripcion(dto.getDescripcion());
        return medio;
    }
}
