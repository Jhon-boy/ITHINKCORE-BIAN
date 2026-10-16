package com.ithink.corebank.application.dto;

import com.ithink.corebank.domain.entities.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO de la entidad {@link Usuario} con sus atributos escalares.
 * <p>
 * Se excluye el campo {@code password} por seguridad, evitando su registro
 * en los logs de la aplicacion.
 * </p>
 *
 * @author Jhon
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idUsuario;

    private Integer idSucursal;

    private Integer idPersona;

    private String usuario;

    private Boolean temporal;

    private LocalDateTime fCreacion;

    private LocalDateTime fModificacion;

    private String usuarioCreacion;

    private String usuarioModificacion;

    /**
     * Convierte una entidad {@link Usuario} a su DTO.
     *
     * @param usuario entidad origen
     * @return DTO con los datos escalares (sin password) o {@code null} si la entidad es nula
     */
    public static UsuarioDto from(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        UsuarioDto dto = new UsuarioDto();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setIdSucursal(usuario.getIdSucursal());
        dto.setIdPersona(usuario.getIdPersona());
        dto.setUsuario(usuario.getUsuario());
        dto.setTemporal(usuario.getTemporal());
        dto.setFCreacion(usuario.getFCreacion());
        dto.setFModificacion(usuario.getFModificacion());
        dto.setUsuarioCreacion(usuario.getUsuarioCreacion());
        dto.setUsuarioModificacion(usuario.getUsuarioModificacion());
        return dto;
    }
}
