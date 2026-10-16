package com.ithink.corebank.application.services;

import com.ithink.corebank.domain.entities.Cuenta;
import com.ithink.corebank.domain.entities.Saldo;
import com.ithink.corebank.domain.entities.SaldoHistorico;
import com.ithink.corebank.domain.ports.CuentaRepositoryPort;
import com.ithink.corebank.domain.ports.SaldoHistoricoRepositoryPort;
import com.ithink.corebank.domain.ports.SaldoRepositoryPort;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.MoneyUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Gestiona bloqueos de monto sobre {@link Saldo}
 * moviendo fondos entre disponible y bloqueado.
 *
 * @author Jhon
 * @version 1.0
 */
@Service
public class AmountBlockService {

    private static final Loggers logs = new Loggers();

    private final CuentaRepositoryPort cuentaRepositoryPort;
    private final SaldoRepositoryPort saldoRepositoryPort;
    private final SaldoHistoricoRepositoryPort saldoHistoricoRepositoryPort;

    /**
     * Crea el servicio de bloqueos de monto.
     *
     * @param cuentaRepositoryPort puerto de cuentas
     * @param saldoRepositoryPort puerto de saldos
     * @param saldoHistoricoRepositoryPort puerto de históricos
     */
    public AmountBlockService(
            CuentaRepositoryPort cuentaRepositoryPort,
            SaldoRepositoryPort saldoRepositoryPort,
            SaldoHistoricoRepositoryPort saldoHistoricoRepositoryPort) {
        logs.setLogger(AmountBlockService.class);
        this.cuentaRepositoryPort = cuentaRepositoryPort;
        this.saldoRepositoryPort = saldoRepositoryPort;
        this.saldoHistoricoRepositoryPort = saldoHistoricoRepositoryPort;
    }

    /**
     * Consulta el saldo/bloqueo asociado a una cuenta.
     *
     * @param idCuenta identificador de cuenta
     * @param amountBlockId identificador del saldo ({@code IDSALDO})
     * @return saldo encontrado
     */
    @Transactional(readOnly = true)
    public Saldo retrieve(Integer idCuenta, Integer amountBlockId) {
        Saldo saldo = requireSaldoForAccount(idCuenta, amountBlockId);
        logs.info(Const.ML_FIN + "AMOUNT BLOCK RETRIEVE idSaldo=" + saldo.getIdSaldo());
        return saldo;
    }

    /**
     * Incrementa el monto bloqueado restando del disponible.
     *
     * @param idCuenta identificador de cuenta
     * @param amountToBlock monto a bloquear
     * @param reason motivo del bloqueo
     * @return saldo actualizado
     */
    @Transactional
    public Saldo initiate(Integer idCuenta, BigDecimal amountToBlock, String reason) {
        logs.info(Const.ML_INI + "AMOUNT BLOCK INITIATE cuenta=" + idCuenta + " monto=" + amountToBlock);
        if (amountToBlock == null || amountToBlock.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("MONTO A BLOQUEAR DEBE SER MAYOR A CERO");
        }

        requireCuenta(idCuenta);
        Saldo saldo = requireSaldoByCuenta(idCuenta);

        BigDecimal disponible = MoneyUtil.nullToZero(saldo.getMontoDisponible());
        if (disponible.compareTo(amountToBlock) < 0) {
            throw new IllegalArgumentException(
                    "FONDOS DISPONIBLES INSUFICIENTES PARA BLOQUEO. Disponible="
                            + disponible + ", solicitado=" + amountToBlock);
        }

        saldo.setMontoDisponible(disponible.subtract(amountToBlock));
        saldo.setMontoBloqueado(MoneyUtil.nullToZero(saldo.getMontoBloqueado()).add(amountToBlock));
        applyAudit(saldo, reason, "BLOQUEO +" + amountToBlock.toPlainString());
        Saldo saved = saldoRepositoryPort.save(saldo);
        saveHistorico(saved, "AMOUNT BLOCK INITIATE +" + amountToBlock.toPlainString());
        logs.info(Const.ML_FIN + "AMOUNT BLOCK INITIATE idSaldo=" + saved.getIdSaldo());
        return saved;
    }

    /**
     * Define el monto bloqueado absoluto y ajusta el disponible.
     *
     * @param idCuenta identificador de cuenta
     * @param amountBlockId identificador del saldo
     * @param newBlockedAmount nuevo monto bloqueado absoluto
     * @param reason motivo de la actualización
     * @return saldo actualizado
     */
    @Transactional
    public Saldo update(Integer idCuenta, Integer amountBlockId, BigDecimal newBlockedAmount, String reason) {
        logs.info(Const.ML_INI + "AMOUNT BLOCK UPDATE cuenta=" + idCuenta
                + " id=" + amountBlockId + " nuevoBloqueado=" + newBlockedAmount);
        if (newBlockedAmount == null || newBlockedAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("MONTO BLOQUEADO DEBE SER >= 0");
        }

        Saldo saldo = requireSaldoForAccount(idCuenta, amountBlockId);
        BigDecimal currentBlocked = MoneyUtil.nullToZero(saldo.getMontoBloqueado());
        BigDecimal currentAvailable = MoneyUtil.nullToZero(saldo.getMontoDisponible());
        BigDecimal delta = newBlockedAmount.subtract(currentBlocked);

        if (delta.compareTo(BigDecimal.ZERO) > 0) {
            if (currentAvailable.compareTo(delta) < 0) {
                throw new IllegalArgumentException(
                        "FONDOS DISPONIBLES INSUFICIENTES PARA AUMENTAR BLOQUEO. Disponible="
                                + currentAvailable + ", incremento=" + delta);
            }
            saldo.setMontoDisponible(currentAvailable.subtract(delta));
        } else if (delta.compareTo(BigDecimal.ZERO) < 0) {
            saldo.setMontoDisponible(currentAvailable.add(delta.abs()));
        }

        saldo.setMontoBloqueado(newBlockedAmount);
        applyAudit(saldo, reason, "BLOQUEO ABSOLUTO=" + newBlockedAmount.toPlainString());
        Saldo saved = saldoRepositoryPort.save(saldo);
        saveHistorico(saved, "AMOUNT BLOCK UPDATE = " + newBlockedAmount.toPlainString());
        logs.info(Const.ML_FIN + "AMOUNT BLOCK UPDATE idSaldo=" + saved.getIdSaldo());
        return saved;
    }

    /**
     * Devuelve fondos bloqueados al disponible. El saldo contable no cambia.
     *
     * @param idCuenta identificador de cuenta
     * @param amountBlockId identificador del saldo
     * @param amountToRelease monto a liberar; nulo o cero libera todo el bloqueo
     * @param reason motivo de la liberación
     * @return saldo actualizado
     */
    @Transactional
    public Saldo release(Integer idCuenta, Integer amountBlockId, BigDecimal amountToRelease, String reason) {
        logs.info(Const.ML_INI + "AMOUNT BLOCK RELEASE cuenta=" + idCuenta
                + " id=" + amountBlockId + " monto=" + amountToRelease);
        Saldo saldo = requireSaldoForAccount(idCuenta, amountBlockId);
        BigDecimal blocked = MoneyUtil.nullToZero(saldo.getMontoBloqueado());
        if (blocked.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("NO HAY MONTO BLOQUEADO PARA LIBERAR");
        }
        BigDecimal release = amountToRelease == null || amountToRelease.compareTo(BigDecimal.ZERO) == 0
                ? blocked
                : amountToRelease;
        if (release.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("MONTO A LIBERAR DEBE SER MAYOR A CERO");
        }
        if (release.compareTo(blocked) > 0) {
            throw new IllegalArgumentException(
                    "MONTO A LIBERAR SUPERA EL BLOQUEADO. Bloqueado=" + blocked + ", solicitado=" + release);
        }
        saldo.setMontoBloqueado(blocked.subtract(release));
        saldo.setMontoDisponible(MoneyUtil.nullToZero(saldo.getMontoDisponible()).add(release));
        applyAudit(saldo, reason, "LIBERACION +" + release.toPlainString());
        Saldo saved = saldoRepositoryPort.save(saldo);
        saveHistorico(saved, "AMOUNT BLOCK RELEASE +" + release.toPlainString());
        logs.info(Const.ML_FIN + "AMOUNT BLOCK RELEASE idSaldo=" + saved.getIdSaldo());
        return saved;
    }

    private Saldo requireSaldoForAccount(Integer idCuenta, Integer amountBlockId) {
        requireCuenta(idCuenta);
        Saldo saldo = saldoRepositoryPort.findById(amountBlockId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "AMOUNT BLOCK / SALDO NO ENCONTRADO: " + amountBlockId));
        if (saldo.getIdCuenta() == null || !saldo.getIdCuenta().equals(idCuenta)) {
            throw new IllegalArgumentException(
                    "AMOUNT BLOCK " + amountBlockId + " NO PERTENECE A CUENTA " + idCuenta);
        }
        return saldo;
    }

    private Saldo requireSaldoByCuenta(Integer idCuenta) {
        return saldoRepositoryPort.findByIdCuenta(idCuenta)
                .orElseThrow(() -> new IllegalArgumentException(
                        "SALDO NO ENCONTRADO PARA CUENTA: " + idCuenta));
    }

    private void requireCuenta(Integer idCuenta) {
        cuentaRepositoryPort.findById(idCuenta)
                .orElseThrow(() -> new IllegalArgumentException("CUENTA NO ENCONTRADA: " + idCuenta));
    }

    private void applyAudit(Saldo saldo, String reason, String defaultComment) {
        LocalDateTime now = LocalDateTime.now();
        saldo.setFModificacion(now);
        saldo.setUsuarioModificacion("BIAN");
        saldo.setComentario(reason != null && !reason.isBlank() ? reason : defaultComment);
    }

    private void saveHistorico(Saldo saldo, String comentario) {
        LocalDateTime now = LocalDateTime.now();
        SaldoHistorico historico = new SaldoHistorico();
        Cuenta cuenta = saldo.getCuenta();
        if (cuenta == null && saldo.getIdCuenta() != null) {
            cuenta = cuentaRepositoryPort.findById(saldo.getIdCuenta()).orElse(null);
        }
        historico.setCuenta(cuenta);
        historico.setIdCuenta(saldo.getIdCuenta());
        historico.setMontoContable(saldo.getMontoContable());
        historico.setMontoDisponible(saldo.getMontoDisponible());
        historico.setMontoBloqueado(saldo.getMontoBloqueado());
        historico.setMontoPorEfectivizar(saldo.getMontoPorEfectivizar());
        historico.setEstado(saldo.getEstado());
        historico.setComentario(comentario);
        historico.setFCreacion(now);
        historico.setUsuarioCreacion("BIAN");
        saldoHistoricoRepositoryPort.save(historico);
    }
}
