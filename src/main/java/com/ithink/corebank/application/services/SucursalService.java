package com.ithink.corebank.application.services;

import com.ithink.corebank.domain.entities.Sucursal;
import com.ithink.corebank.application.dto.SucursalDto;
import com.ithink.corebank.domain.ports.SucursalRepositoryPort;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio que orquesta los casos de uso de {@code Sucursal}.
 *
 * @author Jhon
 * @version 1.0
 */
@Service
public class SucursalService {

    private static final Loggers logs = new Loggers();

    private final SucursalRepositoryPort sucursalRepositoryPort;

    public SucursalService(SucursalRepositoryPort sucursalRepositoryPort) {
        logs.setLogger(SucursalService.class);
        this.sucursalRepositoryPort = sucursalRepositoryPort;
    }

    /**
     * Consulta una sucursal por su identificador.
     *
     * @param id identificador de la sucursal
     * @return DTO de la sucursal encontrada
     */
    @Transactional(readOnly = true)
    public SucursalDto findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE SUCURSAL POR ID " + id);
        Sucursal sucursal = sucursalRepositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("SUCURSAL NO ENCONTRADA: " + id));
        SucursalDto dto = SucursalDto.from(sucursal);
        logs.info(Const.ML_FIN + Util.toJson(dto));
        return dto;
    }

    /**
     * Consulta todas las sucursales registradas.
     *
     * @return lista de DTOs de sucursales
     */
    @Transactional(readOnly = true)
    public List<SucursalDto> findAll() {
        logs.info(Const.ML_INI + "CONSULTA DE TODAS LAS SUCURSALES");
        List<SucursalDto> sucursales = sucursalRepositoryPort.findAll().stream()
                .map(SucursalDto::from)
                .toList();
        logs.info(Const.ML_FIN + Util.toJson(sucursales));
        return sucursales;
    }

    /**
     * Registra una nueva sucursal.
     *
     * @param dto datos de la sucursal a registrar
     * @return DTO de la sucursal persistida
     */
    @Transactional
    public SucursalDto save(SucursalDto dto) {
        logs.info(Const.ML_INI + Util.toJson(dto));
        Sucursal sucursal = mapToEntity(dto);
        Sucursal sucursalGuardada = sucursalRepositoryPort.save(sucursal);
        SucursalDto dtoGuardado = SucursalDto.from(sucursalGuardada);
        logs.info(Const.ML_FIN + Util.toJson(dtoGuardado));
        return dtoGuardado;
    }

    /**
     * Convierte un DTO en una entidad.
     *
     * @param dto DTO de origen
     * @return entidad lista para persistir
     */
    private Sucursal mapToEntity(SucursalDto dto) {
        Sucursal sucursal = new Sucursal();
        sucursal.setNombre(dto.getNombre());
        sucursal.setDireccion(dto.getDireccion());
        sucursal.setEstado(dto.getEstado());
        return sucursal;
    }
}
