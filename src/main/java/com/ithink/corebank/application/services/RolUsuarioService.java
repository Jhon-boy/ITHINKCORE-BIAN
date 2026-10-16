package com.ithink.corebank.application.services;

import com.ithink.corebank.domain.entities.Rol;
import com.ithink.corebank.domain.entities.RolUsuario;
import com.ithink.corebank.domain.entities.Usuario;
import com.ithink.corebank.application.dto.RolUsuarioDto;
import com.ithink.corebank.domain.ports.RolRepositoryPort;
import com.ithink.corebank.domain.ports.RolUsuarioRepositoryPort;
import com.ithink.corebank.domain.ports.UsuarioRepositoryPort;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio que orquesta los casos de uso de {@code RolUsuario}.
 *
 * @author Jhon
 * @version 1.0
 */
@Service
public class RolUsuarioService {

    private static final Loggers logs = new Loggers();

    private final RolUsuarioRepositoryPort rolUsuarioRepositoryPort;

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    private final RolRepositoryPort rolRepositoryPort;

    public RolUsuarioService(RolUsuarioRepositoryPort rolUsuarioRepositoryPort,
                             UsuarioRepositoryPort usuarioRepositoryPort,
                             RolRepositoryPort rolRepositoryPort) {
        logs.setLogger(RolUsuarioService.class);
        this.rolUsuarioRepositoryPort = rolUsuarioRepositoryPort;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.rolRepositoryPort = rolRepositoryPort;
    }

    /**
     * Consulta un rol de usuario por su identificador.
     *
     * @param id identificador del rol de usuario
     * @return DTO del rol de usuario encontrado
     */
    @Transactional(readOnly = true)
    public RolUsuarioDto findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE ROL DE USUARIO POR ID " + id);
        RolUsuario rolUsuario = rolUsuarioRepositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ROL DE USUARIO NO ENCONTRADO: " + id));
        RolUsuarioDto dto = RolUsuarioDto.from(rolUsuario);
        logs.info(Const.ML_FIN + Util.toJson(dto));
        return dto;
    }

    /**
     * Consulta todos los roles de usuario registrados.
     *
     * @return lista de DTOs de roles de usuario
     */
    @Transactional(readOnly = true)
    public List<RolUsuarioDto> findAll() {
        logs.info(Const.ML_INI + "CONSULTA DE TODOS LOS ROLES DE USUARIO");
        List<RolUsuarioDto> roles = rolUsuarioRepositoryPort.findAll().stream()
                .map(RolUsuarioDto::from)
                .toList();
        logs.info(Const.ML_FIN + Util.toJson(roles));
        return roles;
    }

    /**
     * Registra un nuevo rol de usuario.
     *
     * @param dto datos del rol de usuario a registrar
     * @return DTO del rol de usuario persistido
     */
    @Transactional
    public RolUsuarioDto save(RolUsuarioDto dto) {
        logs.info(Const.ML_INI + Util.toJson(dto));
        RolUsuario rolUsuario = mapToEntity(dto);
        RolUsuario rolGuardado = rolUsuarioRepositoryPort.save(rolUsuario);
        RolUsuarioDto dtoGuardado = RolUsuarioDto.from(rolGuardado);
        logs.info(Const.ML_FIN + Util.toJson(dtoGuardado));
        return dtoGuardado;
    }

    /**
     * Convierte un DTO en una entidad, resolviendo las relaciones con
     * {@link Usuario} y {@link Rol}.
     *
     * @param dto DTO de origen
     * @return entidad lista para persistir
     */
    private RolUsuario mapToEntity(RolUsuarioDto dto) {
        RolUsuario rolUsuario = new RolUsuario();
        rolUsuario.setEstado(dto.getEstado());
        if (dto.getIdUsuario() != null) {
            Usuario usuario = usuarioRepositoryPort.findById(dto.getIdUsuario())
                    .orElseThrow(() -> new IllegalArgumentException("USUARIO NO ENCONTRADO: " + dto.getIdUsuario()));
            rolUsuario.setUsuario(usuario);
            rolUsuario.setIdUsuario(dto.getIdUsuario());
        }
        if (dto.getIdRol() != null) {
            Rol rol = rolRepositoryPort.findById(dto.getIdRol())
                    .orElseThrow(() -> new IllegalArgumentException("ROL NO ENCONTRADO: " + dto.getIdRol()));
            rolUsuario.setRol(rol);
            rolUsuario.setIdRol(dto.getIdRol());
        }
        return rolUsuario;
    }
}
