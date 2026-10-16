package com.ithink.corebank.application.services;

import com.ithink.corebank.domain.entities.Modulo;
import com.ithink.corebank.application.dto.ModuloDto;
import com.ithink.corebank.domain.ports.ModuloRepositoryPort;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio que orquesta los casos de uso de {@code Modulo}.
 *
 * @author Jhon
 * @version 1.0
 */
@Service
public class ModuloService {

    private static final Loggers logs = new Loggers();

    private final ModuloRepositoryPort moduloRepositoryPort;

    public ModuloService(ModuloRepositoryPort moduloRepositoryPort) {
        logs.setLogger(ModuloService.class);
        this.moduloRepositoryPort = moduloRepositoryPort;
    }

    /**
     * Consulta un modulo por su identificador.
     *
     * @param id identificador del modulo
     * @return DTO del modulo encontrado
     */
    @Transactional(readOnly = true)
    public ModuloDto findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE MODULO POR ID " + id);
        Modulo modulo = moduloRepositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("MODULO NO ENCONTRADO: " + id));
        ModuloDto dto = ModuloDto.from(modulo);
        logs.info(Const.ML_FIN + Util.toJson(dto));
        return dto;
    }

    /**
     * Consulta todos los modulos registrados.
     *
     * @return lista de DTOs de modulos
     */
    @Transactional(readOnly = true)
    public List<ModuloDto> findAll() {
        logs.info(Const.ML_INI + "CONSULTA DE TODOS LOS MODULOS");
        List<ModuloDto> modulos = moduloRepositoryPort.findAll().stream()
                .map(ModuloDto::from)
                .toList();
        logs.info(Const.ML_FIN + Util.toJson(modulos));
        return modulos;
    }

    /**
     * Registra un nuevo modulo.
     *
     * @param dto datos del modulo a registrar
     * @return DTO del modulo persistido
     */
    @Transactional
    public ModuloDto save(ModuloDto dto) {
        logs.info(Const.ML_INI + Util.toJson(dto));
        Modulo modulo = mapToEntity(dto);
        Modulo moduloGuardado = moduloRepositoryPort.save(modulo);
        ModuloDto dtoGuardado = ModuloDto.from(moduloGuardado);
        logs.info(Const.ML_FIN + Util.toJson(dtoGuardado));
        return dtoGuardado;
    }

    /**
     * Convierte un DTO en una entidad.
     *
     * @param dto DTO de origen
     * @return entidad lista para persistir
     */
    private Modulo mapToEntity(ModuloDto dto) {
        Modulo modulo = new Modulo();
        modulo.setCodigo(dto.getCodigo());
        modulo.setNombre(dto.getNombre());
        modulo.setDescripcion(dto.getDescripcion());
        modulo.setEstado(dto.getEstado());
        return modulo;
    }
}
