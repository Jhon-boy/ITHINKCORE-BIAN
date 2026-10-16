package com.ithink.corebank.domain.ports;

import com.ithink.corebank.domain.entities.Canal;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de acceso a datos para la entidad {@link Canal}.
 *
 * @author Jhon
 * @version 1.0
 */
public interface CanalRepositoryPort {

    Optional<Canal> findById(Integer id);

    List<Canal> findAll();

    Canal save(Canal canal);
}
