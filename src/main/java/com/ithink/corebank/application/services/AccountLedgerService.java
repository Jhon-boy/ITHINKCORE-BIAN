package com.ithink.corebank.application.services;

import com.ithink.corebank.domain.entities.Canal;
import com.ithink.corebank.domain.entities.Cuenta;
import com.ithink.corebank.domain.entities.EstadoMovimiento;
import com.ithink.corebank.domain.entities.Medio;
import com.ithink.corebank.domain.entities.MetodoOperacion;
import com.ithink.corebank.domain.entities.Movimiento;
import com.ithink.corebank.domain.entities.Saldo;
import com.ithink.corebank.domain.entities.SaldoHistorico;
import com.ithink.corebank.domain.entities.Sucursal;
import com.ithink.corebank.domain.entities.Transaccion;
import com.ithink.corebank.domain.models.LedgerPostingRequest;
import com.ithink.corebank.domain.ports.CanalRepositoryPort;
import com.ithink.corebank.domain.ports.CuentaRepositoryPort;
import com.ithink.corebank.domain.ports.EstadoMovimientoRepositoryPort;
import com.ithink.corebank.domain.ports.MedioRepositoryPort;
import com.ithink.corebank.domain.ports.MetodoOperacionRepositoryPort;
import com.ithink.corebank.domain.ports.MovimientoRepositoryPort;
import com.ithink.corebank.domain.ports.SaldoHistoricoRepositoryPort;
import com.ithink.corebank.domain.ports.SaldoRepositoryPort;
import com.ithink.corebank.domain.ports.SucursalRepositoryPort;
import com.ithink.corebank.domain.ports.TransaccionRepositoryPort;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.MoneyUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Motor de dominio para iniciar y postear movimientos con afectación de saldo e histórico.
 * Es reutilizado por la API interna de movimiento y por los endpoints BIAN.
 *
 * @author Jhon
 * @version 1.0
 */
@Service
public class AccountLedgerService {

    public static final String TIPO_CREDITO = "C";
    public static final String TIPO_DEBITO = "D";
    public static final String ESTADO_PENDIENTE = "PENDIENTE";
    public static final String ESTADO_BOOKED = "BOOKED";

    private static final Loggers logs = new Loggers();

    private final MovimientoRepositoryPort movimientoRepositoryPort;
    private final CuentaRepositoryPort cuentaRepositoryPort;
    private final SaldoRepositoryPort saldoRepositoryPort;
    private final SaldoHistoricoRepositoryPort saldoHistoricoRepositoryPort;
    private final EstadoMovimientoRepositoryPort estadoMovimientoRepositoryPort;
    private final MetodoOperacionRepositoryPort metodoOperacionRepositoryPort;
    private final TransaccionRepositoryPort transaccionRepositoryPort;
    private final CanalRepositoryPort canalRepositoryPort;
    private final SucursalRepositoryPort sucursalRepositoryPort;
    private final MedioRepositoryPort medioRepositoryPort;

    /**
     * Crea el motor de ledger con sus puertos de persistencia.
     *
     * @param movimientoRepositoryPort puerto de movimientos
     * @param cuentaRepositoryPort puerto de cuentas
     * @param saldoRepositoryPort puerto de saldos
     * @param saldoHistoricoRepositoryPort puerto de históricos
     * @param estadoMovimientoRepositoryPort puerto de estados
     * @param metodoOperacionRepositoryPort puerto de métodos
     * @param transaccionRepositoryPort puerto de transacciones
     * @param canalRepositoryPort puerto de canales
     * @param sucursalRepositoryPort puerto de sucursales
     * @param medioRepositoryPort puerto de medios
     */
    public AccountLedgerService(
            MovimientoRepositoryPort movimientoRepositoryPort,
            CuentaRepositoryPort cuentaRepositoryPort,
            SaldoRepositoryPort saldoRepositoryPort,
            SaldoHistoricoRepositoryPort saldoHistoricoRepositoryPort,
            EstadoMovimientoRepositoryPort estadoMovimientoRepositoryPort,
            MetodoOperacionRepositoryPort metodoOperacionRepositoryPort,
            TransaccionRepositoryPort transaccionRepositoryPort,
            CanalRepositoryPort canalRepositoryPort,
            SucursalRepositoryPort sucursalRepositoryPort,
            MedioRepositoryPort medioRepositoryPort) {
        logs.setLogger(AccountLedgerService.class);
        this.movimientoRepositoryPort = movimientoRepositoryPort;
        this.cuentaRepositoryPort = cuentaRepositoryPort;
        this.saldoRepositoryPort = saldoRepositoryPort;
        this.saldoHistoricoRepositoryPort = saldoHistoricoRepositoryPort;
        this.estadoMovimientoRepositoryPort = estadoMovimientoRepositoryPort;
        this.metodoOperacionRepositoryPort = metodoOperacionRepositoryPort;
        this.transaccionRepositoryPort = transaccionRepositoryPort;
        this.canalRepositoryPort = canalRepositoryPort;
        this.sucursalRepositoryPort = sucursalRepositoryPort;
        this.medioRepositoryPort = medioRepositoryPort;
    }

    /**
     * Crea un movimiento en estado PENDIENTE sin afectar saldos.
     *
     * @param request solicitud de posting
     * @return movimiento pendiente persistido
     */
    @Transactional
    public Movimiento initiate(LedgerPostingRequest request) {
        logs.info(Const.ML_INI + "LEDGER INITIATE cuenta=" + request.getIdCuenta());
        validateRequest(request);

        Cuenta cuenta = cuentaRepositoryPort.findById(request.getIdCuenta())
                .orElseThrow(() -> new IllegalArgumentException("CUENTA NO ENCONTRADA: " + request.getIdCuenta()));

        Movimiento movimiento = buildPendingMovimiento(request, cuenta);
        Movimiento saved = movimientoRepositoryPort.save(movimiento);
        logs.info(Const.ML_FIN + "LEDGER INITIATE idMovimiento=" + saved.getIdMovimiento());
        return saved;
    }

    /**
     * Postea un movimiento pendiente: afecta saldo, crea histórico y marca BOOKED.
     * Si es débito con contraparte interna, genera el crédito espejo.
     *
     * @param idMovimiento identificador del movimiento pendiente
     * @return movimiento posteado
     */
    @Transactional
    public Movimiento execute(Integer idMovimiento) {
        logs.info(Const.ML_INI + "LEDGER EXECUTE idMovimiento=" + idMovimiento);
        Movimiento movimiento = movimientoRepositoryPort.findById(idMovimiento).orElseThrow(() -> new IllegalArgumentException("MOVIMIENTO NO ENCONTRADO: " + idMovimiento));
        assertPending(movimiento);
        Movimiento posted = postSingle(movimiento);

        if (isDebit(posted.getTipo()) && posted.getIdCuentaContraparte() != null) {
            createAndPostMirrorCredit(posted);
        }

        logs.info(Const.ML_FIN + "LEDGER EXECUTE idMovimiento=" + posted.getIdMovimiento());
        return posted;
    }

    /**
     * Initiate + Execute en un solo paso (API interna POST /api/mantenimiento/movimiento/registrar).
     *
     * @param request solicitud de posting
     * @return movimiento posteado
     */
    @Transactional
    public Movimiento postImmediate(LedgerPostingRequest request) {
        Movimiento pending = initiate(request);
        return execute(pending.getIdMovimiento());
    }

    private Movimiento postSingle(Movimiento movimiento) {
        BigDecimal monto = movimiento.getMonto();
        Integer idCuenta = movimiento.getIdCuenta();

        Saldo saldo = saldoRepositoryPort.findByIdCuenta(idCuenta)
                .orElseThrow(() -> new IllegalArgumentException("SALDO NO ENCONTRADO PARA CUENTA: " + idCuenta));

        if (isCredit(movimiento.getTipo())) {
            applyCredit(saldo, monto);
            movimiento.setDebito(BigDecimal.ZERO);
        } else if (isDebit(movimiento.getTipo())) {
            applyDebit(saldo, monto);
            movimiento.setDebito(monto);
        } else {
            throw new IllegalArgumentException("TIPO DE MOVIMIENTO NO SOPORTADO: " + movimiento.getTipo());
        }

        LocalDateTime now = LocalDateTime.now();
        saldo.setFModificacion(now);
        saldo.setUsuarioModificacion(defaultUser(movimiento.getUsuarioCreacion()));
        saldo.setComentario("ACTUALIZADO POR MOVIMIENTO " + movimiento.getIdMovimiento());
        Saldo saldoGuardado = saldoRepositoryPort.save(saldo);

        SaldoHistorico historico = buildHistorico(saldoGuardado, movimiento, now);
        SaldoHistorico historicoGuardado = saldoHistoricoRepositoryPort.save(historico);

        EstadoMovimiento booked = requireEstado(ESTADO_BOOKED);
        movimiento.setSaldoHistorico(historicoGuardado);
        movimiento.setIdSaldoHistorico(historicoGuardado.getIdSaldoHistorico());
        movimiento.setEstadoMovimiento(booked);
        movimiento.setIdEstadoMovimiento(booked.getIdEstadoMovimiento());
        movimiento.setFModificacion(now);
        movimiento.setUsuarioModificacion(defaultUser(movimiento.getUsuarioCreacion()));
        if (movimiento.getFechaContable() == null) {
            movimiento.setFechaContable(now);
        }
        if (movimiento.getFechaReal() == null) {
            movimiento.setFechaReal(now);
        }

        return movimientoRepositoryPort.save(movimiento);
    }

    private void createAndPostMirrorCredit(Movimiento debitPosted) {
        Cuenta destino = cuentaRepositoryPort.findById(debitPosted.getIdCuentaContraparte())
                .orElseThrow(() -> new IllegalArgumentException(
                        "CUENTA CONTRAPARTE NO ENCONTRADA: " + debitPosted.getIdCuentaContraparte()));

        LedgerPostingRequest mirrorRequest = LedgerPostingRequest.builder()
                .idCuenta(destino.getIdCuenta())
                .tipo(TIPO_CREDITO)
                .monto(debitPosted.getMonto())
                .idCuentaContraparte(debitPosted.getIdCuenta())
                .numeroCuentaContraparte(debitPosted.getCuenta() != null
                        ? debitPosted.getCuenta().getNumeroCuenta()
                        : null)
                .nombreContraparte(debitPosted.getNombreContraparte())
                .codigoBancoContraparte(debitPosted.getCodigoBancoContraparte())
                .nombreBancoContraparte(debitPosted.getNombreBancoContraparte())
                .idMetodoOperacion(debitPosted.getIdMetodoOperacion())
                .idTransaccion(debitPosted.getIdTransaccion())
                .idCanal(debitPosted.getIdCanal())
                .idSucursal(debitPosted.getIdSucursal())
                .idMedio(debitPosted.getIdMedio())
                .mensaje("CREDITO ESPEJO TRANSFERENCIA")
                .comentario("ESPEJO DE MOVIMIENTO " + debitPosted.getIdMovimiento())
                .referencia(debitPosted.getReferencia())
                .uuid(UUID.randomUUID().toString())
                .codigoRastreo(debitPosted.getCodigoRastreo())
                .usuario(defaultUser(debitPosted.getUsuarioCreacion()))
                .build();

        Movimiento mirrorPending = initiate(mirrorRequest);
        postSingle(mirrorPending);
    }

    private Movimiento buildPendingMovimiento(LedgerPostingRequest request, Cuenta cuenta) {
        LocalDateTime now = LocalDateTime.now();
        String tipo = normalizeTipo(request.getTipo());
        BigDecimal monto = request.getMonto();

        Movimiento movimiento = new Movimiento();
        movimiento.setCuenta(cuenta);
        movimiento.setIdCuenta(cuenta.getIdCuenta());
        movimiento.setTipo(tipo);
        movimiento.setMonto(monto);
        movimiento.setDebito(isDebit(tipo) ? monto : BigDecimal.ZERO);
        movimiento.setFechaContable(now);
        movimiento.setFechaReal(now);
        movimiento.setMensaje(request.getMensaje());
        movimiento.setComentario(request.getComentario());
        movimiento.setReferencia(request.getReferencia());
        movimiento.setUuid(request.getUuid() == null || request.getUuid().isBlank()
                ? UUID.randomUUID().toString()
                : request.getUuid());
        movimiento.setCodigoRastreo(request.getCodigoRastreo());
        movimiento.setFCreacion(now);
        movimiento.setFModificacion(now);
        movimiento.setUsuarioCreacion(defaultUser(request.getUsuario()));
        movimiento.setUsuarioModificacion(defaultUser(request.getUsuario()));

        resolveCounterparty(request, movimiento);
        resolveRelations(request, movimiento);

        EstadoMovimiento pendiente = requireEstado(ESTADO_PENDIENTE);
        movimiento.setEstadoMovimiento(pendiente);
        movimiento.setIdEstadoMovimiento(pendiente.getIdEstadoMovimiento());
        return movimiento;
    }

    private void resolveCounterparty(LedgerPostingRequest request, Movimiento movimiento) {
        Integer resolvedIdContraparte = request.getIdCuentaContraparte();
        if (resolvedIdContraparte == null
                && request.getNumeroCuentaContraparte() != null
                && !request.getNumeroCuentaContraparte().isBlank()) {
            resolvedIdContraparte = cuentaRepositoryPort.findByNumeroCuenta(request.getNumeroCuentaContraparte())
                    .map(Cuenta::getIdCuenta)
                    .orElse(null);
        }

        final Integer idContraparte = resolvedIdContraparte;
        if (idContraparte != null) {
            Cuenta contraparte = cuentaRepositoryPort.findById(idContraparte)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "CUENTA CONTRAPARTE NO ENCONTRADA: " + idContraparte));
            movimiento.setCuentaContraparte(contraparte);
            movimiento.setIdCuentaContraparte(contraparte.getIdCuenta());
            if (request.getNumeroCuentaContraparte() == null) {
                movimiento.setNumeroCuentaContraparte(contraparte.getNumeroCuenta());
            }
            if (request.getNombreBancoContraparte() == null) {
                movimiento.setNombreBancoContraparte(contraparte.getNombreBanco());
            }
            if (request.getCodigoBancoContraparte() == null) {
                movimiento.setCodigoBancoContraparte(contraparte.getCodigoBanco());
            }
        }

        movimiento.setNumeroCuentaContraparte(request.getNumeroCuentaContraparte() != null
                ? request.getNumeroCuentaContraparte()
                : movimiento.getNumeroCuentaContraparte());
        movimiento.setNombreContraparte(request.getNombreContraparte());
        if (request.getCodigoBancoContraparte() != null) {
            movimiento.setCodigoBancoContraparte(request.getCodigoBancoContraparte());
        }
        if (request.getNombreBancoContraparte() != null) {
            movimiento.setNombreBancoContraparte(request.getNombreBancoContraparte());
        }
    }

    private void resolveRelations(LedgerPostingRequest request, Movimiento movimiento) {
        if (request.getIdMetodoOperacion() != null) {
            MetodoOperacion metodo = metodoOperacionRepositoryPort.findById(request.getIdMetodoOperacion())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "METODO DE OPERACION NO ENCONTRADO: " + request.getIdMetodoOperacion()));
            movimiento.setMetodoOperacion(metodo);
            movimiento.setIdMetodoOperacion(metodo.getIdMetodoOperacion());
        }
        if (request.getIdTransaccion() != null) {
            Transaccion transaccion = transaccionRepositoryPort.findById(request.getIdTransaccion())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "TRANSACCION NO ENCONTRADA: " + request.getIdTransaccion()));
            movimiento.setTransaccion(transaccion);
            movimiento.setIdTransaccion(transaccion.getIdTransaccion());
        }
        if (request.getIdCanal() != null) {
            Canal canal = canalRepositoryPort.findById(request.getIdCanal())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "CANAL NO ENCONTRADO: " + request.getIdCanal()));
            movimiento.setCanal(canal);
            movimiento.setIdCanal(canal.getIdCanal());
        }
        if (request.getIdSucursal() != null) {
            Sucursal sucursal = sucursalRepositoryPort.findById(request.getIdSucursal())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "SUCURSAL NO ENCONTRADA: " + request.getIdSucursal()));
            movimiento.setSucursal(sucursal);
            movimiento.setIdSucursal(sucursal.getIdSucursal());
        }
        if (request.getIdMedio() != null) {
            Medio medio = medioRepositoryPort.findById(request.getIdMedio())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "MEDIO NO ENCONTRADO: " + request.getIdMedio()));
            movimiento.setMedio(medio);
            movimiento.setIdMedio(medio.getIdMedio());
        }
    }

    private SaldoHistorico buildHistorico(Saldo saldo, Movimiento movimiento, LocalDateTime now) {
        SaldoHistorico historico = new SaldoHistorico();
        historico.setCuenta(saldo.getCuenta());
        historico.setIdCuenta(saldo.getIdCuenta());
        historico.setMontoContable(saldo.getMontoContable());
        historico.setMontoDisponible(saldo.getMontoDisponible());
        historico.setMontoBloqueado(saldo.getMontoBloqueado());
        historico.setMontoPorEfectivizar(saldo.getMontoPorEfectivizar());
        historico.setEstado(saldo.getEstado());
        historico.setComentario("POSTING MOVIMIENTO " + movimiento.getIdMovimiento()
                + " TIPO=" + movimiento.getTipo()
                + " MONTO=" + movimiento.getMonto());
        historico.setFCreacion(now);
        historico.setUsuarioCreacion(defaultUser(movimiento.getUsuarioCreacion()));
        return historico;
    }

    private void applyCredit(Saldo saldo, BigDecimal monto) {
        saldo.setMontoContable(MoneyUtil.nullToZero(saldo.getMontoContable()).add(monto));
        saldo.setMontoDisponible(MoneyUtil.nullToZero(saldo.getMontoDisponible()).add(monto));
    }

    private void applyDebit(Saldo saldo, BigDecimal monto) {
        BigDecimal disponible = MoneyUtil.nullToZero(saldo.getMontoDisponible());
        if (disponible.compareTo(monto) < 0) {
            throw new IllegalArgumentException(
                    "FONDOS INSUFICIENTES. Disponible=" + disponible + ", solicitado=" + monto);
        }
        saldo.setMontoContable(MoneyUtil.nullToZero(saldo.getMontoContable()).subtract(monto));
        saldo.setMontoDisponible(disponible.subtract(monto));
    }

    private void assertPending(Movimiento movimiento) {
        if (movimiento.getEstadoMovimiento() == null
                || movimiento.getEstadoMovimiento().getCodigo() == null
                || !ESTADO_PENDIENTE.equalsIgnoreCase(movimiento.getEstadoMovimiento().getCodigo())) {
            throw new IllegalArgumentException(
                    "SOLO SE PUEDEN EJECUTAR MOVIMIENTOS PENDIENTES. id=" + movimiento.getIdMovimiento());
        }
    }

    private void validateRequest(LedgerPostingRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("REQUEST DE LEDGER REQUERIDO");
        }
        if (request.getIdCuenta() == null) {
            throw new IllegalArgumentException("IDCUENTA ES REQUERIDO");
        }
        if (request.getMonto() == null || request.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("MONTO DEBE SER MAYOR A CERO");
        }
        if (request.getTipo() == null || request.getTipo().isBlank()) {
            throw new IllegalArgumentException("TIPO DE MOVIMIENTO ES REQUERIDO (C/D)");
        }
        normalizeTipo(request.getTipo());
    }

    private EstadoMovimiento requireEstado(String codigo) {
        return estadoMovimientoRepositoryPort.findByCodigo(codigo)
                .orElseThrow(() -> new IllegalArgumentException("ESTADO DE MOVIMIENTO NO ENCONTRADO: " + codigo));
    }

    private String normalizeTipo(String tipo) {
        String normalized = tipo.trim().toUpperCase();
        return switch (normalized) {
            case "C", "CREDIT", "CREDITO" -> TIPO_CREDITO;
            case "D", "DEBIT", "DEBITO" -> TIPO_DEBITO;
            default -> throw new IllegalArgumentException("TIPO DE MOVIMIENTO NO SOPORTADO: " + tipo);
        };
    }

    private boolean isCredit(String tipo) {
        return TIPO_CREDITO.equalsIgnoreCase(tipo);
    }

    private boolean isDebit(String tipo) {
        return TIPO_DEBITO.equalsIgnoreCase(tipo);
    }

    private String defaultUser(String usuario) {
        return usuario == null || usuario.isBlank() ? "LEDGER" : usuario;
    }
}
