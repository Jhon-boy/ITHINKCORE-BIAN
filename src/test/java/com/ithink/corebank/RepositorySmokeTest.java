package com.ithink.corebank;

import com.ithink.corebank.domain.entities.Banco;
import com.ithink.corebank.domain.entities.Movimiento;
import com.ithink.corebank.domain.ports.BancoRepositoryPort;
import com.ithink.corebank.domain.ports.MovimientoRepositoryPort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class RepositorySmokeTest {

    @Autowired
    private BancoRepositoryPort bancoRepositoryPort;

    @Autowired
    private MovimientoRepositoryPort movimientoRepositoryPort;

    @Test
    void bancoFindAllShouldReturnSeedData() {
        List<Banco> bancos = bancoRepositoryPort.findAll();
        assertEquals(3, bancos.size());
    }

    @Test
    void movimientoFindAllBetweenDatesShouldReturnSeedData() {
        LocalDateTime inicio = LocalDateTime.of(2026, 1, 1, 0, 0);
        LocalDateTime fin = LocalDateTime.of(2026, 12, 31, 23, 59);
        List<Movimiento> movimientos = movimientoRepositoryPort.findAll(inicio, fin);
        assertEquals(7, movimientos.size());
    }

    @Test
    void movimientoFindByIdShouldReturnSeedData() {
        Optional<Movimiento> movimiento = movimientoRepositoryPort.findById(1);
        assertTrue(movimiento.isPresent());
        assertEquals("C", movimiento.get().getTipo());
    }

    @Test
    void movimientoFindByIdMissingShouldBeEmpty() {
        Optional<Movimiento> movimiento = movimientoRepositoryPort.findById(999999);
        assertFalse(movimiento.isPresent());
    }
}
