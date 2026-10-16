package com.ithink.corebank.domain.ports;

import com.ithink.corebank.domain.entities.Banco;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de acceso a datos para la entidad {@link Banco}.
 *
 * @author Jhon
 * @version 1.0
 */
public interface BancoRepositoryPort {

    Optional<Banco> findById(Integer id);

    List<Banco> findAll();

    Banco save(Banco banco);
}
