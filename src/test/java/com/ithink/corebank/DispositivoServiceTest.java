package com.ithink.corebank;

import com.ithink.corebank.application.services.DispositivoService;
import com.ithink.corebank.application.dto.DispositivoDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class DispositivoServiceTest {

    @Autowired
    private DispositivoService dispositivoService;

    @Test
    void findByIdShouldReturnSeedDevice() {
        DispositivoDto dto = dispositivoService.findById(1);
        assertNotNull(dto);
        assertEquals("356938035643809", dto.getImei());
        assertEquals(1, dto.getIdUsuario());
    }

    @Test
    void findAllShouldReturnSeedDevices() {
        List<DispositivoDto> dispositivos = dispositivoService.findAll();
        assertFalse(dispositivos.isEmpty());
    }

    @Test
    void saveShouldPersistNewDevice() {
        DispositivoDto dto = new DispositivoDto();
        dto.setIdUsuario(1);
        dto.setImei("111111111111111");
        dto.setMarca("TEST");
        dto.setModelo("MODEL-TEST");

        DispositivoDto guardado = dispositivoService.save(dto);

        assertNotNull(guardado.getIdDispositivo());
        assertEquals(1, guardado.getIdUsuario());
        assertEquals("111111111111111", guardado.getImei());
    }
}
