package com.ithink.corebank.application.services;

import com.ithink.corebank.domain.entities.MetodoOperacion;
import com.ithink.corebank.domain.entities.TipoOperacion;
import com.ithink.corebank.application.dto.MetodoOperacionDto;
import com.ithink.corebank.domain.ports.MetodoOperacionRepositoryPort;
import com.ithink.corebank.domain.ports.TipoOperacionRepositoryPort;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio que orquesta los casos de uso de {@code MetodoOperacion}.
 *
 * @author Jhon
 * @version 1.0
 */
@Service
public class MetodoOperacionService {

    private static final Loggers logs = new Loggers();

    private final MetodoOperacionRepositoryPort metodoOperacionRepositoryPort;

    private final TipoOperacionRepositoryPort tipoOperacionRepositoryPort;

    public MetodoOperacionService(MetodoOperacionRepositoryPort metodoOperacionRepositoryPort,
                                  TipoOperacionRepositoryPort tipoOperacionRepositoryPort) {
        logs.setLogger(MetodoOperacionService.class);
        this.metodoOperacionRepositoryPort = metodoOperacionRepositoryPort;
        this.tipoOperacionRepositoryPort = tipoOperacionRepositoryPort;
    }

    /**
     * Consulta un metodo de operacion por su identificador.
     *
     * @param id identificador del metodo de operacion
     * @return DTO del metodo de operacion encontrado
     */
    @Transactional(readOnly = true)
    public MetodoOperacionDto findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE METODO DE OPERACION POR ID " + id);
        MetodoOperacion metodoOperacion = metodoOperacionRepositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("METODO DE OPERACION NO ENCONTRADO: " + id));
        MetodoOperacionDto dto = MetodoOperacionDto.from(metodoOperacion);
        logs.info(Const.ML_FIN + Util.toJson(dto));
        return dto;
    }

    /**
     * Consulta todos los metodos de operacion registrados.
     *
     * @return lista de DTOs de metodos de operacion
     */
    @Transactional(readOnly = true)
    public List<MetodoOperacionDto> findAll() {
        logs.info(Const.ML_INI + "CONSULTA DE TODOS LOS METODOS DE OPERACION");
        List<MetodoOperacionDto> metodos = metodoOperacionRepositoryPort.findAll().stream()
                .map(MetodoOperacionDto::from)
                .toList();
        logs.info(Const.ML_FIN + Util.toJson(metodos));
        return metodos;
    }

    /**
     * Registra un nuevo metodo de operacion.
     *
     * @param dto datos del metodo de operacion a registrar
     * @return DTO del metodo de operacion persistido
     */
    @Transactional
    public MetodoOperacionDto save(MetodoOperacionDto dto) {
        logs.info(Const.ML_INI + Util.toJson(dto));
        MetodoOperacion metodoOperacion = mapToEntity(dto);
        MetodoOperacion metodoGuardado = metodoOperacionRepositoryPort.save(metodoOperacion);
        MetodoOperacionDto dtoGuardado = MetodoOperacionDto.from(metodoGuardado);
        logs.info(Const.ML_FIN + Util.toJson(dtoGuardado));
        return dtoGuardado;
    }

    /**
     * Convierte un DTO en una entidad, resolviendo la relacion con
     * {@link TipoOperacion}.
     *
     * @param dto DTO de origen
     * @return entidad lista para persistir
     */
    private MetodoOperacion mapToEntity(MetodoOperacionDto dto) {
        MetodoOperacion metodoOperacion = new MetodoOperacion();
        metodoOperacion.setCodigo(dto.getCodigo());
        metodoOperacion.setNombre(dto.getNombre());
        metodoOperacion.setDescripcion(dto.getDescripcion());
        metodoOperacion.setMontoMinimo(dto.getMontoMinimo());
        metodoOperacion.setMontoMaximo(dto.getMontoMaximo());
        if (dto.getIdTipoOperacion() != null) {
            TipoOperacion tipoOperacion = tipoOperacionRepositoryPort.findById(dto.getIdTipoOperacion())
                    .orElseThrow(() -> new IllegalArgumentException("TIPO DE OPERACION NO ENCONTRADO: " + dto.getIdTipoOperacion()));
            metodoOperacion.setTipoOperacion(tipoOperacion);
            metodoOperacion.setIdTipoOperacion(dto.getIdTipoOperacion());
        }
        return metodoOperacion;
    }
}
