package com.ithink.corebank.application.bian.services;

import com.ithink.bian.currentaccount.model.DebitandCredit;
import com.ithink.corebank.application.bian.exception.BianBadRequestException;
import com.ithink.corebank.application.bian.mapper.DebitandCreditBianMapper;
import com.ithink.corebank.application.bian.support.BianAccountSupport;
import com.ithink.corebank.domain.entities.Movimiento;
import com.ithink.corebank.domain.ports.MovimientoRepositoryPort;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Casos de uso BIAN del Behavior Qualifier DebitandCredit.
 *
 * @author Jhon
 * @version 1.0
 */
@Service
public class DebitandCreditService {

    private static final Loggers logs = new Loggers();

    private final BianAccountSupport bianAccountSupport;
    private final DebitandCreditBianMapper debitandCreditBianMapper;
    private final MovimientoRepositoryPort movimientoRepositoryPort;

    /**
     * Crea el servicio BIAN DebitandCredit.
     *
     * @param bianAccountSupport soporte compartido
     * @param debitandCreditBianMapper mapper BIAN
     * @param movimientoRepositoryPort puerto de movimientos
     */
    public DebitandCreditService(
            BianAccountSupport bianAccountSupport,
            DebitandCreditBianMapper debitandCreditBianMapper,
            MovimientoRepositoryPort movimientoRepositoryPort) {
        logs.setLogger(DebitandCreditService.class);
        this.bianAccountSupport = bianAccountSupport;
        this.debitandCreditBianMapper = debitandCreditBianMapper;
        this.movimientoRepositoryPort = movimientoRepositoryPort;
    }

    /**
     * Consulta un movimiento DebitandCredit asociado a la cuenta.
     *
     * @param currentaccountid identificador de cuenta
     * @param debitandcreditid identificador del movimiento
     * @return DebitandCredit BIAN
     */
    @Transactional(readOnly = true)
    public DebitandCredit retrieve(String currentaccountid, String debitandcreditid) {
        logs.info(Const.ML_INI + "BIAN DebitandCredit Retrieve account=" + currentaccountid + " id=" + debitandcreditid);
        Movimiento movimiento = bianAccountSupport.requireMovimientoForAccount(currentaccountid, debitandcreditid, "DebitandCredit");
        DebitandCredit response = debitandCreditBianMapper.toDebitandCredit(movimiento);
        logs.info(Const.ML_FIN + "BIAN DebitandCredit Retrieve id=" + debitandcreditid);
        return response;
    }

    /**
     * Lista los movimientos de la cuenta en un rango de fechas.
     *
     * @param currentaccountid identificador de cuenta
     * @param fromDate inicio ISO-8601
     * @param toDate fin ISO-8601
     * @return movimientos BIAN
     */
    @Transactional(readOnly = true)
    public List<DebitandCredit> retrieveReport(String currentaccountid, String fromDate, String toDate) {
        logs.info(Const.ML_INI + "BIAN DebitandCredit Report account=" + currentaccountid);
        Integer idCuenta = bianAccountSupport.parseId(currentaccountid, "currentaccountid");
        bianAccountSupport.requireCuenta(idCuenta, currentaccountid);
        LocalDateTime inicio = parseDate(fromDate, "fromDate", false);
        LocalDateTime fin = parseDate(toDate, "toDate", true);
        if (inicio.isAfter(fin)) {
            throw new BianBadRequestException("fromDate must be before toDate");
        }
        List<DebitandCredit> response = movimientoRepositoryPort.findByCuentaBetween(idCuenta, inicio, fin).stream()
                .map(debitandCreditBianMapper::toDebitandCredit)
                .toList();
        logs.info(Const.ML_FIN + "BIAN DebitandCredit Report count=" + response.size());
        return response;
    }

    private LocalDateTime parseDate(String value, String fieldName, boolean endOfDay) {
        if (value == null || value.isBlank()) {
            throw new BianBadRequestException(fieldName + " is required");
        }
        String trimmed = value.trim();
        try {
            return LocalDateTime.parse(trimmed);
        } catch (DateTimeParseException ignored) {
            try {
                LocalDate date = LocalDate.parse(trimmed);
                return endOfDay ? date.atTime(23, 59, 59) : date.atStartOfDay();
            } catch (DateTimeParseException ex) {
                throw new BianBadRequestException(fieldName + " must be ISO-8601: " + value);
            }
        }
    }
}
