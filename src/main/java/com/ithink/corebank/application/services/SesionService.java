package com.ithink.corebank.application.services;

import com.ithink.corebank.domain.entities.Sesion;
import com.ithink.corebank.domain.entities.Usuario;
import com.ithink.corebank.application.dto.SesionDto;
import com.ithink.corebank.domain.ports.SesionRepositoryPort;
import com.ithink.corebank.domain.ports.UsuarioRepositoryPort;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Servicio que orquesta los casos de uso de {@code Sesion}.
 *
 * @author Jhon
 * @version 1.0
 */
@Service
public class SesionService {

    private static final Loggers logs = new Loggers();

    private final SesionRepositoryPort sesionRepositoryPort;

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public SesionService(SesionRepositoryPort sesionRepositoryPort,
                         UsuarioRepositoryPort usuarioRepositoryPort) {
        logs.setLogger(SesionService.class);
        this.sesionRepositoryPort = sesionRepositoryPort;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    /**
     * Consulta una sesion por su identificador.
     *
     * @param id identificador de la sesion
     * @return DTO de la sesion encontrada
     */
    @Transactional(readOnly = true)
    public SesionDto findById(UUID id) {
        logs.info(Const.ML_INI + "CONSULTA DE SESION POR ID " + id);
        Sesion sesion = sesionRepositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("SESION NO ENCONTRADA: " + id));
        SesionDto dto = SesionDto.from(sesion);
        logs.info(Const.ML_FIN + Util.toJson(dto));
        return dto;
    }

    /**
     * Consulta las sesiones dentro de un rango de fechas.
     *
     * @param fechaInicio fecha de inicio del rango
     * @param fechaFin    fecha de fin del rango
     * @return lista de DTOs de sesiones
     */
    @Transactional(readOnly = true)
    public List<SesionDto> findAll(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        logs.info(Const.ML_INI + "CONSULTA DE SESIONES ENTRE " + fechaInicio + " Y " + fechaFin);
        List<SesionDto> sesiones = sesionRepositoryPort.findAll(fechaInicio, fechaFin).stream()
                .map(SesionDto::from)
                .toList();
        logs.info(Const.ML_FIN + Util.toJson(sesiones));
        return sesiones;
    }

    /**
     * Registra una nueva sesion.
     *
     * @param dto datos de la sesion a registrar
     * @return DTO de la sesion persistida
     */
    @Transactional
    public SesionDto save(SesionDto dto) {
        logs.info(Const.ML_INI + Util.toJson(dto));
        Sesion sesion = mapToEntity(dto);
        Sesion sesionGuardada = sesionRepositoryPort.save(sesion);
        SesionDto dtoGuardado = SesionDto.from(sesionGuardada);
        logs.info(Const.ML_FIN + Util.toJson(dtoGuardado));
        return dtoGuardado;
    }

    /**
     * Convierte un DTO en una entidad, resolviendo la relacion con {@link Usuario}.
     *
     * @param dto DTO de origen
     * @return entidad lista para persistir
     */
    private Sesion mapToEntity(SesionDto dto) {
        Sesion sesion = new Sesion();
        sesion.setToken(dto.getToken());
        sesion.setFExpiracion(dto.getFExpiracion());
        sesion.setActivo(dto.getActivo());
        if (dto.getIdUsuario() != null) {
            Usuario usuario = usuarioRepositoryPort.findById(dto.getIdUsuario())
                    .orElseThrow(() -> new IllegalArgumentException("USUARIO NO ENCONTRADO: " + dto.getIdUsuario()));
            sesion.setUsuario(usuario);
            sesion.setIdUsuario(dto.getIdUsuario());
        }
        return sesion;
    }
}
