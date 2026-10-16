package com.ithink.corebank.application.dto;

import com.ithink.corebank.domain.entities.Sesion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO de la entidad {@link Sesion} con sus atributos escalares.
 *
 * @author Jhon
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SesionDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID idSesion;

    private Integer idUsuario;

    private UUID token;

    private LocalDateTime fCreacion;

    private LocalDateTime fExpiracion;

    private Boolean activo;

    /**
     * Convierte una entidad {@link Sesion} a su DTO.
     *
     * @param sesion entidad origen
     * @return DTO con los datos escalares o {@code null} si la entidad es nula
     */
    public static SesionDto from(Sesion sesion) {
        if (sesion == null) {
            return null;
        }
        SesionDto dto = new SesionDto();
        dto.setIdSesion(sesion.getIdSesion());
        dto.setIdUsuario(sesion.getIdUsuario());
        dto.setToken(sesion.getToken());
        dto.setFCreacion(sesion.getFCreacion());
        dto.setFExpiracion(sesion.getFExpiracion());
        dto.setActivo(sesion.getActivo());
        return dto;
    }
}
