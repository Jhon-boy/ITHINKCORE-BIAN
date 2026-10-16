package com.ithink.corebank.application.services;

import com.ithink.corebank.domain.entities.Canal;
import com.ithink.corebank.application.dto.CanalDto;
import com.ithink.corebank.domain.ports.CanalRepositoryPort;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio que orquesta los casos de uso de {@code Canal}.
 *
 * @author Jhon
 * @version 1.0
 */
@Service
public class CanalService {

    private static final Loggers logs = new Loggers();

    private final CanalRepositoryPort canalRepositoryPort;

    public CanalService(CanalRepositoryPort canalRepositoryPort) {
        logs.setLogger(CanalService.class);
        this.canalRepositoryPort = canalRepositoryPort;
    }

    /**
     * Consulta un canal por su identificador.
     *
     * @param id identificador del canal
     * @return DTO del canal encontrado
     */
    @Transactional(readOnly = true)
    public CanalDto findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE CANAL POR ID " + id);
        Canal canal = canalRepositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("CANAL NO ENCONTRADO: " + id));
        CanalDto dto = CanalDto.from(canal);
        logs.info(Const.ML_FIN + Util.toJson(dto));
        return dto;
    }

    /**
     * Consulta todos los canales registrados.
     *
     * @return lista de DTOs de canales
     */
    @Transactional(readOnly = true)
    public List<CanalDto> findAll() {
        logs.info(Const.ML_INI + "CONSULTA DE TODOS LOS CANALES");
        List<CanalDto> canales = canalRepositoryPort.findAll().stream()
                .map(CanalDto::from)
                .toList();
        logs.info(Const.ML_FIN + Util.toJson(canales));
        return canales;
    }

    /**
     * Registra un nuevo canal.
     *
     * @param dto datos del canal a registrar
     * @return DTO del canal persistido
     */
    @Transactional
    public CanalDto save(CanalDto dto) {
        logs.info(Const.ML_INI + Util.toJson(dto));
        Canal canal = mapToEntity(dto);
        Canal canalGuardado = canalRepositoryPort.save(canal);
        CanalDto dtoGuardado = CanalDto.from(canalGuardado);
        logs.info(Const.ML_FIN + Util.toJson(dtoGuardado));
        return dtoGuardado;
    }

    /**
     * Convierte un DTO en una entidad.
     *
     * @param dto DTO de origen
     * @return entidad lista para persistir
     */
    private Canal mapToEntity(CanalDto dto) {
        Canal canal = new Canal();
        canal.setCodigo(dto.getCodigo());
        canal.setNombre(dto.getNombre());
        canal.setDescripcion(dto.getDescripcion());
        canal.setEstado(dto.getEstado());
        return canal;
    }
}
