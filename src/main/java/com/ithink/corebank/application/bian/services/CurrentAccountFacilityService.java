package com.ithink.corebank.application.bian.services;

import com.ithink.bian.currentaccount.model.Accountstatustypevalues;
import com.ithink.bian.currentaccount.model.CurrentAccountFacility;
import com.ithink.corebank.application.bian.exception.BianBadRequestException;
import com.ithink.corebank.application.bian.exception.BianNotFoundException;
import com.ithink.corebank.application.bian.mapper.CurrentAccountBianMapper;
import com.ithink.corebank.application.bian.mapper.CurrentAccountMappingSupport;
import com.ithink.corebank.application.bian.support.BianAccountSupport;
import com.ithink.corebank.domain.entities.Cuenta;
import com.ithink.corebank.domain.entities.Persona;
import com.ithink.corebank.domain.entities.Saldo;
import com.ithink.corebank.domain.entities.TipoCuenta;
import com.ithink.corebank.domain.ports.CuentaRepositoryPort;
import com.ithink.corebank.domain.ports.PersonaRepositoryPort;
import com.ithink.corebank.domain.ports.SaldoRepositoryPort;
import com.ithink.corebank.domain.ports.TipoCuentaRepositoryPort;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Casos de uso BIAN del Control Record CurrentAccountFacility.
 *
 * @author Jhon
 * @version 1.0
 */
@Service
public class CurrentAccountFacilityService {

    private static final Loggers logs = new Loggers();

    private static final String TIPO_CORRIENTE = "COR";

    private final BianAccountSupport bianAccountSupport;
    private final SaldoRepositoryPort saldoRepositoryPort;
    private final CuentaRepositoryPort cuentaRepositoryPort;
    private final PersonaRepositoryPort personaRepositoryPort;
    private final TipoCuentaRepositoryPort tipoCuentaRepositoryPort;
    private final CurrentAccountBianMapper currentAccountBianMapper;

    /**
     * Crea el servicio BIAN de CurrentAccountFacility.
     *
     * @param bianAccountSupport soporte compartido
     * @param saldoRepositoryPort puerto de saldos
     * @param cuentaRepositoryPort puerto de cuentas
     * @param personaRepositoryPort puerto de personas
     * @param tipoCuentaRepositoryPort puerto de tipos de cuenta
     * @param currentAccountBianMapper mapper BIAN
     */
    public CurrentAccountFacilityService(
            BianAccountSupport bianAccountSupport,
            SaldoRepositoryPort saldoRepositoryPort,
            CuentaRepositoryPort cuentaRepositoryPort,
            PersonaRepositoryPort personaRepositoryPort,
            TipoCuentaRepositoryPort tipoCuentaRepositoryPort,
            CurrentAccountBianMapper currentAccountBianMapper) {
        logs.setLogger(CurrentAccountFacilityService.class);
        this.bianAccountSupport = bianAccountSupport;
        this.saldoRepositoryPort = saldoRepositoryPort;
        this.cuentaRepositoryPort = cuentaRepositoryPort;
        this.personaRepositoryPort = personaRepositoryPort;
        this.tipoCuentaRepositoryPort = tipoCuentaRepositoryPort;
        this.currentAccountBianMapper = currentAccountBianMapper;
    }

    /**
     * Consulta el Control Record CurrentAccountFacility con saldo.
     *
     * @param currentaccountid identificador de cuenta
     * @return facility BIAN
     */
    @Transactional(readOnly = true)
    public CurrentAccountFacility retrieve(String currentaccountid) {
        logs.info(Const.ML_INI + "BIAN CurrentAccountFacility Retrieve id=" + currentaccountid);
        Integer idCuenta = bianAccountSupport.parseId(currentaccountid, "currentaccountid");
        Cuenta cuenta = bianAccountSupport.requireCuenta(idCuenta, currentaccountid);
        Saldo saldo = saldoRepositoryPort.findByIdCuenta(idCuenta).orElse(null);
        CurrentAccountFacility facility = currentAccountBianMapper.toFacility(cuenta, saldo);
        logs.info(Const.ML_FIN + "BIAN CurrentAccountFacility Retrieve id=" + currentaccountid);
        return facility;
    }

    /**
     * Abre una cuenta corriente y su saldo en cero.
     *
     * @param facility datos de apertura
     * @return facility creada
     */
    @Transactional
    public CurrentAccountFacility initiate(CurrentAccountFacility facility) {
        logs.info(Const.ML_INI + "BIAN CurrentAccountFacility Initiate");
        if (facility == null || facility.getCustomerReference() == null || facility.getCustomerReference().isBlank()) {
            throw new BianBadRequestException("CustomerReference (idPersona) is required");
        }
        Integer idPersona = bianAccountSupport.parseId(facility.getCustomerReference(), "CustomerReference");
        Persona persona = personaRepositoryPort.findById(idPersona)
                .orElseThrow(() -> new BianNotFoundException("Party not found: " + idPersona));

        String numero = accountNumber(facility);
        if (numero == null) {
            numero = "22" + String.format("%010d", System.currentTimeMillis() % 10_000_000_000L);
        }
        if (cuentaRepositoryPort.findByNumeroCuenta(numero).isPresent()) {
            throw new BianBadRequestException("Account number already exists: " + numero);
        }

        TipoCuenta tipo = tipoCuentaRepositoryPort.findAll().stream()
                .filter(item -> TIPO_CORRIENTE.equalsIgnoreCase(item.getCodigo()))
                .findFirst()
                .orElseThrow(() -> new BianBadRequestException("Account type COR is not configured"));

        LocalDateTime now = LocalDateTime.now();
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(numero);
        cuenta.setTipoCuenta(tipo);
        cuenta.setIdTipoCuenta(tipo.getIdTipoCuenta());
        cuenta.setPersona(persona);
        cuenta.setIdPersona(idPersona);
        cuenta.setInterna(Boolean.TRUE);
        cuenta.setCodigoBanco("0010");
        cuenta.setNombreBanco("PRODUBANCO");
        cuenta.setEstado(toInternalStatus(facility.getAccountStatus()));
        cuenta.setComentario(facility.getAccountDetails() == null ? "APERTURA BIAN" : facility.getAccountDetails().getAccountPurpose());
        cuenta.setVigencia("VIGENTE");
        cuenta.setFCreacion(now);
        cuenta.setFModificacion(now);
        cuenta.setUsuarioCreacion(CurrentAccountMappingSupport.USUARIO_BIAN);
        cuenta.setUsuarioModificacion(CurrentAccountMappingSupport.USUARIO_BIAN);
        Cuenta saved = cuentaRepositoryPort.save(cuenta);

        Saldo saldo = new Saldo();
        saldo.setCuenta(saved);
        saldo.setIdCuenta(saved.getIdCuenta());
        saldo.setMontoContable(BigDecimal.ZERO);
        saldo.setMontoDisponible(BigDecimal.ZERO);
        saldo.setMontoBloqueado(BigDecimal.ZERO);
        saldo.setMontoPorEfectivizar(BigDecimal.ZERO);
        saldo.setEstado("ACTIVO");
        saldo.setComentario("APERTURA BIAN");
        saldo.setFCreacion(now);
        saldo.setFModificacion(now);
        saldo.setUsuarioCreacion(CurrentAccountMappingSupport.USUARIO_BIAN);
        saldo.setUsuarioModificacion(CurrentAccountMappingSupport.USUARIO_BIAN);
        Saldo savedSaldo = saldoRepositoryPort.save(saldo);

        CurrentAccountFacility response = currentAccountBianMapper.toFacility(saved, savedSaldo);
        logs.info(Const.ML_FIN + "BIAN CurrentAccountFacility Initiate id=" + saved.getIdCuenta());
        return response;
    }

    /**
     * Cambia el estado de la cuenta.
     *
     * @param currentaccountid identificador de cuenta
     * @param facility payload con AccountStatus
     * @return facility actualizada
     */
    @Transactional
    public CurrentAccountFacility update(String currentaccountid, CurrentAccountFacility facility) {
        logs.info(Const.ML_INI + "BIAN CurrentAccountFacility Update id=" + currentaccountid);
        if (facility == null || facility.getAccountStatus() == null) {
            throw new BianBadRequestException("AccountStatus is required");
        }
        Integer idCuenta = bianAccountSupport.parseId(currentaccountid, "currentaccountid");
        Cuenta cuenta = bianAccountSupport.requireCuenta(idCuenta, currentaccountid);
        cuenta.setEstado(toInternalStatus(facility.getAccountStatus()));
        cuenta.setFModificacion(LocalDateTime.now());
        cuenta.setUsuarioModificacion(CurrentAccountMappingSupport.USUARIO_BIAN);
        Cuenta saved = cuentaRepositoryPort.save(cuenta);
        Saldo saldo = saldoRepositoryPort.findByIdCuenta(idCuenta).orElse(null);
        CurrentAccountFacility response = currentAccountBianMapper.toFacility(saved, saldo);
        logs.info(Const.ML_FIN + "BIAN CurrentAccountFacility Update estado=" + saved.getEstado());
        return response;
    }

    private String accountNumber(CurrentAccountFacility facility) {
        if (facility.getCurrentAccountNumber() == null
                || facility.getCurrentAccountNumber().getAccountIdentification() == null) {
            return null;
        }
        String value = facility.getCurrentAccountNumber().getAccountIdentification().getIdentifierValue();
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private String toInternalStatus(Accountstatustypevalues status) {
        if (status == null) {
            return "ACTIVA";
        }
        return switch (status) {
            case ENABLED -> "ACTIVA";
            case DISABLED -> "BLOQUEADA";
            case DELETED -> "CERRADA";
            case PENDING, PROFORMA -> "PENDIENTE";
        };
    }
}
