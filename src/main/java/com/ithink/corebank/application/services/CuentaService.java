package com.ithink.corebank.application.services;

import com.ithink.corebank.domain.entities.Cuenta;
import com.ithink.corebank.domain.entities.Persona;
import com.ithink.corebank.domain.entities.TipoCuenta;
import com.ithink.corebank.application.dto.CuentaDto;
import com.ithink.corebank.domain.ports.CuentaRepositoryPort;
import com.ithink.corebank.domain.ports.PersonaRepositoryPort;
import com.ithink.corebank.domain.ports.TipoCuentaRepositoryPort;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio que orquesta los casos de uso de {@code Cuenta}.
 *
 * @author Jhon
 * @version 1.0
 */
@Service
public class CuentaService {

    private static final Loggers logs = new Loggers();

    private final CuentaRepositoryPort cuentaRepositoryPort;

    private final TipoCuentaRepositoryPort tipoCuentaRepositoryPort;

    private final PersonaRepositoryPort personaRepositoryPort;

    public CuentaService(CuentaRepositoryPort cuentaRepositoryPort,
                         TipoCuentaRepositoryPort tipoCuentaRepositoryPort,
                         PersonaRepositoryPort personaRepositoryPort) {
        logs.setLogger(CuentaService.class);
        this.cuentaRepositoryPort = cuentaRepositoryPort;
        this.tipoCuentaRepositoryPort = tipoCuentaRepositoryPort;
        this.personaRepositoryPort = personaRepositoryPort;
    }

    /**
     * Consulta una cuenta por su identificador.
     *
     * @param id identificador de la cuenta
     * @return DTO de la cuenta encontrada
     */
    @Transactional(readOnly = true)
    public CuentaDto findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE CUENTA POR ID " + id);
        Cuenta cuenta = cuentaRepositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("CUENTA NO ENCONTRADA: " + id));
        CuentaDto dto = CuentaDto.from(cuenta);
        logs.info(Const.ML_FIN + Util.toJson(dto));
        return dto;
    }

    /**
     * Consulta todas las cuentas registradas.
     *
     * @return lista de DTOs de cuentas
     */
    @Transactional(readOnly = true)
    public List<CuentaDto> findAll() {
        logs.info(Const.ML_INI + "CONSULTA DE TODAS LAS CUENTAS");
        List<CuentaDto> cuentas = cuentaRepositoryPort.findAll().stream()
                .map(CuentaDto::from)
                .toList();
        logs.info(Const.ML_FIN + Util.toJson(cuentas));
        return cuentas;
    }

    /**
     * Registra una nueva cuenta.
     *
     * @param dto datos de la cuenta a registrar
     * @return DTO de la cuenta persistida
     */
    @Transactional
    public CuentaDto save(CuentaDto dto) {
        logs.info(Const.ML_INI + Util.toJson(dto));
        Cuenta cuenta = mapToEntity(dto);
        Cuenta cuentaGuardada = cuentaRepositoryPort.save(cuenta);
        CuentaDto dtoGuardado = CuentaDto.from(cuentaGuardada);
        logs.info(Const.ML_FIN + Util.toJson(dtoGuardado));
        return dtoGuardado;
    }

    /**
     * Convierte un DTO en una entidad, resolviendo las relaciones con
     * {@link TipoCuenta} y {@link Persona}.
     *
     * @param dto DTO de origen
     * @return entidad lista para persistir
     */
    private Cuenta mapToEntity(CuentaDto dto) {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(dto.getNumeroCuenta());
        cuenta.setInterna(dto.getInterna());
        cuenta.setCodigoBanco(dto.getCodigoBanco());
        cuenta.setNombreBanco(dto.getNombreBanco());
        cuenta.setEstado(dto.getEstado());
        cuenta.setComentario(dto.getComentario());
        cuenta.setVigencia(dto.getVigencia());
        cuenta.setCaducidad(dto.getCaducidad());
        if (dto.getIdTipoCuenta() != null) {
            TipoCuenta tipoCuenta = tipoCuentaRepositoryPort.findById(dto.getIdTipoCuenta())
                    .orElseThrow(() -> new IllegalArgumentException("TIPO DE CUENTA NO ENCONTRADO: " + dto.getIdTipoCuenta()));
            cuenta.setTipoCuenta(tipoCuenta);
            cuenta.setIdTipoCuenta(dto.getIdTipoCuenta());
        }
        if (dto.getIdPersona() != null) {
            Persona persona = personaRepositoryPort.findById(dto.getIdPersona())
                    .orElseThrow(() -> new IllegalArgumentException("PERSONA NO ENCONTRADA: " + dto.getIdPersona()));
            cuenta.setPersona(persona);
            cuenta.setIdPersona(dto.getIdPersona());
        }
        return cuenta;
    }
}
