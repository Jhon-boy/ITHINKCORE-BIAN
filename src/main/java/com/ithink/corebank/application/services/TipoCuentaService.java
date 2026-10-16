package com.ithink.corebank.application.services;

import com.ithink.corebank.domain.entities.TipoCuenta;
import com.ithink.corebank.application.dto.TipoCuentaDto;
import com.ithink.corebank.domain.ports.TipoCuentaRepositoryPort;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio que orquesta los casos de uso de {@code TipoCuenta}.
 *
 * @author Jhon
 * @version 1.0
 */
@Service
public class TipoCuentaService {

    private static final Loggers logs = new Loggers();

    private final TipoCuentaRepositoryPort tipoCuentaRepositoryPort;

    public TipoCuentaService(TipoCuentaRepositoryPort tipoCuentaRepositoryPort) {
        logs.setLogger(TipoCuentaService.class);
        this.tipoCuentaRepositoryPort = tipoCuentaRepositoryPort;
    }

    /**
     * Consulta un tipo de cuenta por su identificador.
     *
     * @param id identificador del tipo de cuenta
     * @return DTO del tipo de cuenta encontrado
     */
    @Transactional(readOnly = true)
    public TipoCuentaDto findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE TIPO DE CUENTA POR ID " + id);
        TipoCuenta tipoCuenta = tipoCuentaRepositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("TIPO DE CUENTA NO ENCONTRADO: " + id));
        TipoCuentaDto dto = TipoCuentaDto.from(tipoCuenta);
        logs.info(Const.ML_FIN + Util.toJson(dto));
        return dto;
    }

    /**
     * Consulta todos los tipos de cuenta registrados.
     *
     * @return lista de DTOs de tipos de cuenta
     */
    @Transactional(readOnly = true)
    public List<TipoCuentaDto> findAll() {
        logs.info(Const.ML_INI + "CONSULTA DE TODOS LOS TIPOS DE CUENTA");
        List<TipoCuentaDto> tipos = tipoCuentaRepositoryPort.findAll().stream()
                .map(TipoCuentaDto::from)
                .toList();
        logs.info(Const.ML_FIN + Util.toJson(tipos));
        return tipos;
    }

    /**
     * Registra un nuevo tipo de cuenta.
     *
     * @param dto datos del tipo de cuenta a registrar
     * @return DTO del tipo de cuenta persistido
     */
    @Transactional
    public TipoCuentaDto save(TipoCuentaDto dto) {
        logs.info(Const.ML_INI + Util.toJson(dto));
        TipoCuenta tipoCuenta = mapToEntity(dto);
        TipoCuenta tipoGuardado = tipoCuentaRepositoryPort.save(tipoCuenta);
        TipoCuentaDto dtoGuardado = TipoCuentaDto.from(tipoGuardado);
        logs.info(Const.ML_FIN + Util.toJson(dtoGuardado));
        return dtoGuardado;
    }

    /**
     * Convierte un DTO en una entidad.
     *
     * @param dto DTO de origen
     * @return entidad lista para persistir
     */
    private TipoCuenta mapToEntity(TipoCuentaDto dto) {
        TipoCuenta tipoCuenta = new TipoCuenta();
        tipoCuenta.setCodigo(dto.getCodigo());
        tipoCuenta.setNombre(dto.getNombre());
        tipoCuenta.setDescripcion(dto.getDescripcion());
        tipoCuenta.setMontoMinimo(dto.getMontoMinimo());
        tipoCuenta.setMontoMaximo(dto.getMontoMaximo());
        return tipoCuenta;
    }
}
