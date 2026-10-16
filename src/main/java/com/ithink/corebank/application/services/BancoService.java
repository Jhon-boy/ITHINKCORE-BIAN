package com.ithink.corebank.application.services;

import com.ithink.corebank.domain.entities.Banco;
import com.ithink.corebank.application.dto.BancoDto;
import com.ithink.corebank.domain.ports.BancoRepositoryPort;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio que orquesta los casos de uso de {@code Banco}.
 *
 * @author Jhon
 * @version 1.0
 */
@Service
public class BancoService {

    private static final Loggers logs = new Loggers();

    private final BancoRepositoryPort bancoRepositoryPort;

    public BancoService(BancoRepositoryPort bancoRepositoryPort) {
        logs.setLogger(BancoService.class);
        this.bancoRepositoryPort = bancoRepositoryPort;
    }

    /**
     * Consulta un banco por su identificador.
     *
     * @param id identificador del banco
     * @return DTO del banco encontrado
     */
    @Transactional(readOnly = true)
    public BancoDto findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE BANCO POR ID " + id);
        Banco banco = bancoRepositoryPort.findById(id).orElseThrow(() -> new IllegalArgumentException("BANCO NO ENCONTRADO: " + id));
        BancoDto dto = BancoDto.from(banco);
        logs.info(Const.ML_FIN + Util.toJson(dto));
        return dto;
    }

    /**
     * Consulta todos los bancos registrados.
     *
     * @return lista de DTOs de bancos
     */
    @Transactional(readOnly = true)
    public List<BancoDto> findAll() {
        logs.info(Const.ML_INI + "CONSULTA DE TODOS LOS BANCOS");
        List<BancoDto> bancos = bancoRepositoryPort.findAll().stream().map(BancoDto::from)
                .toList();
        logs.info(Const.ML_FIN + Util.toJson(bancos));
        return bancos;
    }

    /**
     * Registra un nuevo banco.
     *
     * @param dto datos del banco a registrar
     * @return DTO del banco persistido
     */
    @Transactional
    public BancoDto save(BancoDto dto) {
        logs.info(Const.ML_INI + Util.toJson(dto));
        Banco banco = mapToEntity(dto);
        Banco bancoGuardado = bancoRepositoryPort.save(banco);
        BancoDto dtoGuardado = BancoDto.from(bancoGuardado);
        logs.info(Const.ML_FIN + Util.toJson(dtoGuardado));
        return dtoGuardado;
    }

    /**
     * Convierte un DTO en una entidad.
     *
     * @param dto DTO de origen
     * @return entidad lista para persistir
     */
    private Banco mapToEntity(BancoDto dto) {
        Banco banco = new Banco();
        banco.setCodigoBanco(dto.getCodigoBanco());
        banco.setNombreBanco(dto.getNombreBanco());
        banco.setCodigoSwift(dto.getCodigoSwift());
        banco.setEstado(dto.getEstado());
        return banco;
    }
}
