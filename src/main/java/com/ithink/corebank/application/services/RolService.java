package com.ithink.corebank.application.services;

import com.ithink.corebank.domain.entities.Rol;
import com.ithink.corebank.application.dto.RolDto;
import com.ithink.corebank.domain.ports.RolRepositoryPort;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio que orquesta los casos de uso de {@code Rol}.
 *
 * @author Jhon
 * @version 1.0
 */
@Service
public class RolService {

    private static final Loggers logs = new Loggers();

    private final RolRepositoryPort rolRepositoryPort;

    public RolService(RolRepositoryPort rolRepositoryPort) {
        logs.setLogger(RolService.class);
        this.rolRepositoryPort = rolRepositoryPort;
    }

    /**
     * Consulta un rol por su identificador.
     *
     * @param id identificador del rol
     * @return DTO del rol encontrado
     */
    @Transactional(readOnly = true)
    public RolDto findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE ROL POR ID " + id);
        Rol rol = rolRepositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ROL NO ENCONTRADO: " + id));
        RolDto dto = RolDto.from(rol);
        logs.info(Const.ML_FIN + Util.toJson(dto));
        return dto;
    }

    /**
     * Consulta todos los roles registrados.
     *
     * @return lista de DTOs de roles
     */
    @Transactional(readOnly = true)
    public List<RolDto> findAll() {
        logs.info(Const.ML_INI + "CONSULTA DE TODOS LOS ROLES");
        List<RolDto> roles = rolRepositoryPort.findAll().stream()
                .map(RolDto::from)
                .toList();
        logs.info(Const.ML_FIN + Util.toJson(roles));
        return roles;
    }

    /**
     * Registra un nuevo rol.
     *
     * @param dto datos del rol a registrar
     * @return DTO del rol persistido
     */
    @Transactional
    public RolDto save(RolDto dto) {
        logs.info(Const.ML_INI + Util.toJson(dto));
        Rol rol = mapToEntity(dto);
        Rol rolGuardado = rolRepositoryPort.save(rol);
        RolDto dtoGuardado = RolDto.from(rolGuardado);
        logs.info(Const.ML_FIN + Util.toJson(dtoGuardado));
        return dtoGuardado;
    }

    /**
     * Convierte un DTO en una entidad.
     *
     * @param dto DTO de origen
     * @return entidad lista para persistir
     */
    private Rol mapToEntity(RolDto dto) {
        Rol rol = new Rol();
        rol.setNombre(dto.getNombre());
        rol.setObservacion(dto.getObservacion());
        rol.setEstado(dto.getEstado());
        return rol;
    }
}
