package com.ithink.corebank.application.services;

import com.ithink.corebank.domain.entities.Dispositivo;
import com.ithink.corebank.domain.entities.Usuario;
import com.ithink.corebank.application.dto.DispositivoDto;
import com.ithink.corebank.domain.ports.DispositivoRepositoryPort;
import com.ithink.corebank.domain.ports.UsuarioRepositoryPort;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio que orquesta los casos de uso de {@code Dispositivo}.
 * <p>
 * Depende de los puertos de repositorio, realiza el mapeo entre entidades y
 * DTOs y registra la traza de cada operacion.
 * </p>
 *
 * @author Jhon
 * @version 1.0
 */
@Service
public class DispositivoService {

    private static final Loggers logs = new Loggers();

    private final DispositivoRepositoryPort dispositivoRepositoryPort;

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public DispositivoService(DispositivoRepositoryPort dispositivoRepositoryPort,
                              UsuarioRepositoryPort usuarioRepositoryPort) {
        logs.setLogger(DispositivoService.class);
        this.dispositivoRepositoryPort = dispositivoRepositoryPort;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    /**
     * Consulta un dispositivo por su identificador.
     *
     * @param id identificador del dispositivo
     * @return DTO del dispositivo encontrado
     */
    @Transactional(readOnly = true)
    public DispositivoDto findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE DISPOSITIVO POR ID " + id);
        Dispositivo dispositivo = dispositivoRepositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("DISPOSITIVO NO ENCONTRADO: " + id));
        DispositivoDto dto = DispositivoDto.from(dispositivo);
        logs.info(Const.ML_FIN + Util.toJson(dto));
        return dto;
    }

    /**
     * Consulta todos los dispositivos registrados.
     *
     * @return lista de DTOs de dispositivos
     */
    @Transactional(readOnly = true)
    public List<DispositivoDto> findAll() {
        logs.info(Const.ML_INI + "CONSULTA DE TODOS LOS DISPOSITIVOS");
        List<DispositivoDto> dispositivos = dispositivoRepositoryPort.findAll().stream()
                .map(DispositivoDto::from)
                .toList();
        logs.info(Const.ML_FIN + Util.toJson(dispositivos));
        return dispositivos;
    }

    /**
     * Registra un nuevo dispositivo.
     *
     * @param dto datos del dispositivo a registrar
     * @return DTO del dispositivo persistido con su identificador generado
     */
    @Transactional
    public DispositivoDto save(DispositivoDto dto) {
        logs.info(Const.ML_INI + Util.toJson(dto));
        Dispositivo dispositivo = mapToEntity(dto);
        Dispositivo dispositivoGuardado = dispositivoRepositoryPort.save(dispositivo);
        DispositivoDto dtoGuardado = DispositivoDto.from(dispositivoGuardado);
        logs.info(Const.ML_FIN + Util.toJson(dtoGuardado));
        return dtoGuardado;
    }

    /**
     * Convierte un DTO en una entidad, resolviendo la relacion con {@link Usuario}
     * a partir del identificador contenido en el DTO.
     *
     * @param dto DTO de origen
     * @return entidad lista para persistir
     */
    private Dispositivo mapToEntity(DispositivoDto dto) {
        Dispositivo dispositivo = new Dispositivo();
        dispositivo.setImei(dto.getImei());
        dispositivo.setMarca(dto.getMarca());
        dispositivo.setModelo(dto.getModelo());
        dispositivo.setUltimoAcceso(dto.getUltimoAcceso());
        dispositivo.setSistemaOperativo(dto.getSistemaOperativo());
        if (dto.getIdUsuario() != null) {
            Usuario usuario = usuarioRepositoryPort.findById(dto.getIdUsuario())
                    .orElseThrow(() -> new IllegalArgumentException("USUARIO NO ENCONTRADO: " + dto.getIdUsuario()));
            dispositivo.setUsuario(usuario);
            dispositivo.setIdUsuario(dto.getIdUsuario());
        }
        return dispositivo;
    }
}
