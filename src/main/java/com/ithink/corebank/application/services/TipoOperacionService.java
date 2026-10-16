package com.ithink.corebank.application.services;

import com.ithink.corebank.domain.entities.TipoOperacion;
import com.ithink.corebank.application.dto.TipoOperacionDto;
import com.ithink.corebank.domain.ports.TipoOperacionRepositoryPort;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio que orquesta los casos de uso de {@code TipoOperacion}.
 *
 * @author Jhon
 * @version 1.0
 */
@Service
public class TipoOperacionService {

    private static final Loggers logs = new Loggers();

    private final TipoOperacionRepositoryPort tipoOperacionRepositoryPort;

    public TipoOperacionService(TipoOperacionRepositoryPort tipoOperacionRepositoryPort) {
        logs.setLogger(TipoOperacionService.class);
        this.tipoOperacionRepositoryPort = tipoOperacionRepositoryPort;
    }

    /**
     * Consulta un tipo de operacion por su identificador.
     *
     * @param id identificador del tipo de operacion
     * @return DTO del tipo de operacion encontrado
     */
    @Transactional(readOnly = true)
    public TipoOperacionDto findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE TIPO DE OPERACION POR ID " + id);
        TipoOperacion tipoOperacion = tipoOperacionRepositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("TIPO DE OPERACION NO ENCONTRADO: " + id));
        TipoOperacionDto dto = TipoOperacionDto.from(tipoOperacion);
        logs.info(Const.ML_FIN + Util.toJson(dto));
        return dto;
    }

    /**
     * Consulta todos los tipos de operacion registrados.
     *
     * @return lista de DTOs de tipos de operacion
     */
    @Transactional(readOnly = true)
    public List<TipoOperacionDto> findAll() {
        logs.info(Const.ML_INI + "CONSULTA DE TODOS LOS TIPOS DE OPERACION");
        List<TipoOperacionDto> tipos = tipoOperacionRepositoryPort.findAll().stream()
                .map(TipoOperacionDto::from)
                .toList();
        logs.info(Const.ML_FIN + Util.toJson(tipos));
        return tipos;
    }

    /**
     * Registra un nuevo tipo de operacion.
     *
     * @param dto datos del tipo de operacion a registrar
     * @return DTO del tipo de operacion persistido
     */
    @Transactional
    public TipoOperacionDto save(TipoOperacionDto dto) {
        logs.info(Const.ML_INI + Util.toJson(dto));
        TipoOperacion tipoOperacion = mapToEntity(dto);
        TipoOperacion tipoGuardado = tipoOperacionRepositoryPort.save(tipoOperacion);
        TipoOperacionDto dtoGuardado = TipoOperacionDto.from(tipoGuardado);
        logs.info(Const.ML_FIN + Util.toJson(dtoGuardado));
        return dtoGuardado;
    }

    /**
     * Convierte un DTO en una entidad.
     *
     * @param dto DTO de origen
     * @return entidad lista para persistir
     */
    private TipoOperacion mapToEntity(TipoOperacionDto dto) {
        TipoOperacion tipoOperacion = new TipoOperacion();
        tipoOperacion.setCodigo(dto.getCodigo());
        tipoOperacion.setNombre(dto.getNombre());
        tipoOperacion.setDescripcion(dto.getDescripcion());
        return tipoOperacion;
    }
}
