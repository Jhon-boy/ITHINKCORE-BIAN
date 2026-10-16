package com.ithink.corebank.domain.ports;

import com.ithink.corebank.domain.entities.Medio;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de acceso a datos para la entidad {@link Medio}.
 *
 * @author Jhon
 * @version 1.0
 */
public interface MedioRepositoryPort {

    Optional<Medio> findById(Integer id);

    List<Medio> findAll();

    Medio save(Medio medio);
}
