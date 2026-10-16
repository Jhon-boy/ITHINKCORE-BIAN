package com.ithink.corebank.domain.ports;

import com.ithink.corebank.domain.entities.Dispositivo;

import java.util.List;
import java.util.Optional;

/**
 * Puerto (contrato) de acceso a datos para la entidad {@link Dispositivo}.
 * <p>
 * Define las operaciones basicas de consulta y persistencia que la capa de
 * dominio requiere, desacoplandola de la tecnologia de persistencia.
 * </p>
 *
 * @author Jhon
 * @version 1.0
 */
public interface DispositivoRepositoryPort {

    /**
     * Consulta un dispositivo por su identificador.
     *
     * @param id identificador del dispositivo
     * @return dispositivo encontrado o {@link Optional#empty()} si no existe
     */
    Optional<Dispositivo> findById(Integer id);

    /**
     * Consulta todos los dispositivos registrados.
     *
     * @return lista de dispositivos
     */
    List<Dispositivo> findAll();

    /**
     * Inserta un nuevo dispositivo.
     *
     * @param dispositivo entidad a persistir
     * @return dispositivo persistido con su identificador generado
     */
    Dispositivo save(Dispositivo dispositivo);
}
