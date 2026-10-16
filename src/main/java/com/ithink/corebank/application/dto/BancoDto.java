package com.ithink.corebank.application.dto;

import com.ithink.corebank.domain.entities.Banco;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO de la entidad {@link Banco}.
 *
 * @author Jhon
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BancoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idBanco;

    private String codigoBanco;

    private String nombreBanco;

    private String codigoSwift;

    private String estado;

    private LocalDateTime fCreacion;

    private LocalDateTime fModificacion;

    private String usuarioCreacion;

    private String usuarioModificacion;

    /**
     * Convierte una entidad {@link Banco} a su DTO.
     *
     * @param banco entidad origen
     * @return DTO o {@code null} si la entidad es nula
     */
    public static BancoDto from(Banco banco) {
        if (banco == null) {
            return null;
        }
        BancoDto dto = new BancoDto();
        dto.setIdBanco(banco.getIdBanco());
        dto.setCodigoBanco(banco.getCodigoBanco());
        dto.setNombreBanco(banco.getNombreBanco());
        dto.setCodigoSwift(banco.getCodigoSwift());
        dto.setEstado(banco.getEstado());
        dto.setFCreacion(banco.getFCreacion());
        dto.setFModificacion(banco.getFModificacion());
        dto.setUsuarioCreacion(banco.getUsuarioCreacion());
        dto.setUsuarioModificacion(banco.getUsuarioModificacion());
        return dto;
    }
}
