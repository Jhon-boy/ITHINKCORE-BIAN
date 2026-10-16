package com.ithink.corebank.application.dto;

import com.ithink.corebank.domain.entities.Dispositivo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO de la entidad Dispositivo.
 * <p>
 * Expone unicamente los atributos escalares de la entidad para evitar la
 * serializacion de relaciones LAZY fuera de un contexto transaccional.
 * </p>
 *
 * @author Jhon
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DispositivoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idDispositivo;

    private Integer idUsuario;

    private String imei;

    private String marca;

    private String modelo;

    private LocalDateTime ultimoAcceso;

    private String sistemaOperativo;

    private LocalDateTime fCreacion;

    private LocalDateTime fModificacion;

    private String usuarioCreacion;

    private String usuarioModificacion;

    /**
     * Convierte una entidad Dispositivo a su DTO correspondiente.
     *
     * @param dispositivo entidad origen
     * @return DTO con los datos escalares o {@code null} si la entidad es nula
     */
    public static DispositivoDto from(Dispositivo dispositivo) {
        if (dispositivo == null) {
            return null;
        }
        DispositivoDto dto = new DispositivoDto();
        dto.setIdDispositivo(dispositivo.getIdDispositivo());
        dto.setIdUsuario(dispositivo.getIdUsuario());
        dto.setImei(dispositivo.getImei());
        dto.setMarca(dispositivo.getMarca());
        dto.setModelo(dispositivo.getModelo());
        dto.setUltimoAcceso(dispositivo.getUltimoAcceso());
        dto.setSistemaOperativo(dispositivo.getSistemaOperativo());
        dto.setFCreacion(dispositivo.getFCreacion());
        dto.setFModificacion(dispositivo.getFModificacion());
        dto.setUsuarioCreacion(dispositivo.getUsuarioCreacion());
        dto.setUsuarioModificacion(dispositivo.getUsuarioModificacion());
        return dto;
    }
}
