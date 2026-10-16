package com.ithink.corebank.application.services;

import com.ithink.corebank.domain.entities.Persona;
import com.ithink.corebank.domain.entities.Sucursal;
import com.ithink.corebank.domain.entities.Usuario;
import com.ithink.corebank.application.dto.UsuarioDto;
import com.ithink.corebank.domain.ports.PersonaRepositoryPort;
import com.ithink.corebank.domain.ports.SucursalRepositoryPort;
import com.ithink.corebank.domain.ports.UsuarioRepositoryPort;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio que orquesta los casos de uso de {@code Usuario}.
 *
 * @author Jhon
 * @version 1.0
 */
@Service
public class UsuarioService {

    private static final Loggers logs = new Loggers();

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    private final SucursalRepositoryPort sucursalRepositoryPort;

    private final PersonaRepositoryPort personaRepositoryPort;

    public UsuarioService(UsuarioRepositoryPort usuarioRepositoryPort,
                          SucursalRepositoryPort sucursalRepositoryPort,
                          PersonaRepositoryPort personaRepositoryPort) {
        logs.setLogger(UsuarioService.class);
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.sucursalRepositoryPort = sucursalRepositoryPort;
        this.personaRepositoryPort = personaRepositoryPort;
    }

    /**
     * Consulta un usuario por su identificador.
     *
     * @param id identificador del usuario
     * @return DTO del usuario encontrado
     */
    @Transactional(readOnly = true)
    public UsuarioDto findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE USUARIO POR ID " + id);
        Usuario usuario = usuarioRepositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("USUARIO NO ENCONTRADO: " + id));
        UsuarioDto dto = UsuarioDto.from(usuario);
        logs.info(Const.ML_FIN + Util.toJson(dto));
        return dto;
    }

    /**
     * Consulta todos los usuarios registrados.
     *
     * @return lista de DTOs de usuarios
     */
    @Transactional(readOnly = true)
    public List<UsuarioDto> findAll() {
        logs.info(Const.ML_INI + "CONSULTA DE TODOS LOS USUARIOS");
        List<UsuarioDto> usuarios = usuarioRepositoryPort.findAll().stream()
                .map(UsuarioDto::from)
                .toList();
        logs.info(Const.ML_FIN + Util.toJson(usuarios));
        return usuarios;
    }

    /**
     * Registra un nuevo usuario.
     *
     * @param dto datos del usuario a registrar
     * @return DTO del usuario persistido
     */
    @Transactional
    public UsuarioDto save(UsuarioDto dto) {
        logs.info(Const.ML_INI + Util.toJson(dto));
        Usuario usuario = mapToEntity(dto);
        Usuario usuarioGuardado = usuarioRepositoryPort.save(usuario);
        UsuarioDto dtoGuardado = UsuarioDto.from(usuarioGuardado);
        logs.info(Const.ML_FIN + Util.toJson(dtoGuardado));
        return dtoGuardado;
    }

    /**
     * Convierte un DTO en una entidad, resolviendo las relaciones con
     * {@link Sucursal} y {@link Persona}.
     *
     * @param dto DTO de origen
     * @return entidad lista para persistir
     */
    private Usuario mapToEntity(UsuarioDto dto) {
        Usuario usuario = new Usuario();
        usuario.setUsuario(dto.getUsuario());
        usuario.setTemporal(dto.getTemporal());
        if (dto.getIdSucursal() != null) {
            Sucursal sucursal = sucursalRepositoryPort.findById(dto.getIdSucursal())
                    .orElseThrow(() -> new IllegalArgumentException("SUCURSAL NO ENCONTRADA: " + dto.getIdSucursal()));
            usuario.setSucursal(sucursal);
            usuario.setIdSucursal(dto.getIdSucursal());
        }
        if (dto.getIdPersona() != null) {
            Persona persona = personaRepositoryPort.findById(dto.getIdPersona())
                    .orElseThrow(() -> new IllegalArgumentException("PERSONA NO ENCONTRADA: " + dto.getIdPersona()));
            usuario.setPersona(persona);
            usuario.setIdPersona(dto.getIdPersona());
        }
        return usuario;
    }
}
