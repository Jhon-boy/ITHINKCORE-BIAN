package com.ithink.corebank.application.bian.mapper;

import com.ithink.bian.currentaccount.model.Account;
import com.ithink.bian.currentaccount.model.Accountbalance;
import com.ithink.bian.currentaccount.model.Accountcurrency;
import com.ithink.bian.currentaccount.model.Accountstatustypevalues;
import com.ithink.bian.currentaccount.model.Balancetypevalues;
import com.ithink.bian.currentaccount.model.Currencycode;
import com.ithink.bian.currentaccount.model.Currencytypevalues;
import com.ithink.bian.currentaccount.model.CurrentAccountFacility;
import com.ithink.bian.currentaccount.model.Currentaccounttypevalues;
import com.ithink.bian.currentaccount.model.Name;
import com.ithink.corebank.domain.entities.Cuenta;
import com.ithink.corebank.domain.entities.Saldo;
import com.ithink.corebank.domain.entities.TipoCuenta;
import com.ithink.corebank.util.MoneyUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Mapea {@link Cuenta} + {@link Saldo} al Control Record CurrentAccountFacility de BIAN.
 *
 * @author Jhon
 * @version 1.0
 */
@Component
public class CurrentAccountBianMapper {

    /**
     * Construye el facility BIAN a partir de cuenta y saldo.
     *
     * @param cuenta cuenta origen
     * @param saldo saldo asociado (puede ser nulo)
     * @return CurrentAccountFacility BIAN
     */
    public CurrentAccountFacility toFacility(Cuenta cuenta, Saldo saldo) {
        CurrentAccountFacility facility = new CurrentAccountFacility();
        facility.setCurrentAccountNumber(
                CurrentAccountMappingSupport.toAccountIdentification(cuenta.getNumeroCuenta()));
        facility.setCustomerReference(
                cuenta.getIdPersona() == null ? null : String.valueOf(cuenta.getIdPersona()));
        facility.setAccountType(toAccountType(cuenta.getTipoCuenta()));
        facility.setAccountCurrency(toAccountCurrency());
        facility.setAccountStatus(toAccountStatus(cuenta.getEstado()));
        facility.setAccountDate(CurrentAccountMappingSupport.toDatetime(cuenta.getFCreacion(), "OpeningDate"));
        facility.setAccountDetails(toAccountDetails(cuenta, saldo));

        if (saldo != null) {
            facility.setAccountBalance(toBalance(
                    saldo.getMontoDisponible(),
                    Balancetypevalues.AVAILABLE_BALANCE,
                    saldo.getFModificacion()));
            facility.setLimitValue(MoneyUtil.format(saldo.getMontoBloqueado()));
        }

        return facility;
    }

    private Account toAccountDetails(Cuenta cuenta, Saldo saldo) {
        Account account = new Account();
        account.setAccountIdentification(
                CurrentAccountMappingSupport.toAccountIdentification(cuenta.getNumeroCuenta()));
        account.setAccountCurrency(toAccountCurrency());
        account.setAccountDescription(buildDescription(cuenta, saldo));
        account.setAccountPurpose(cuenta.getComentario());

        Name name = new Name();
        name.setName(cuenta.getNumeroCuenta());
        account.setAccountName(name);

        if (cuenta.getTipoCuenta() != null) {
            account.setAccountType(cuenta.getTipoCuenta().getNombre());
        }

        if (saldo != null) {
            account.setAccountBalance(toBalance(
                    saldo.getMontoContable(),
                    Balancetypevalues.LEDGER_BALANCE,
                    saldo.getFModificacion()));
        }

        return account;
    }

    private Accountbalance toBalance(BigDecimal amount, Balancetypevalues type, LocalDateTime valueDate) {
        Accountbalance balance = new Accountbalance();
        balance.setBalanceType(type);
        balance.setBalanceAmount(CurrentAccountMappingSupport.toAmount(amount));
        balance.setBalanceValueDate(CurrentAccountMappingSupport.toDatetime(valueDate, "ValueDate"));
        balance.setBalanceCalculationDate(
                CurrentAccountMappingSupport.toDatetime(LocalDateTime.now(), "ValueDate"));
        return balance;
    }

    private Accountcurrency toAccountCurrency() {
        Accountcurrency accountCurrency = new Accountcurrency();
        accountCurrency.setAccountCurrencyType(Currencytypevalues.BASE_CURRENCY);
        Currencycode currencycode = new Currencycode();
        currencycode.setCurrencycode(CurrentAccountMappingSupport.DEFAULT_CURRENCY);
        accountCurrency.setAccountCurrency(currencycode);
        return accountCurrency;
    }

    private Currentaccounttypevalues toAccountType(TipoCuenta tipoCuenta) {
        if (tipoCuenta == null || tipoCuenta.getCodigo() == null) {
            return Currentaccounttypevalues.STANDARD_CURRENT_ACCOUNT;
        }
        return switch (tipoCuenta.getCodigo().trim().toUpperCase()) {
            case "COR" -> Currentaccounttypevalues.PERSONAL_CURRENT_ACCOUNT;
            case "AHO" -> Currentaccounttypevalues.STANDARD_CURRENT_ACCOUNT;
            default -> Currentaccounttypevalues.STANDARD_CURRENT_ACCOUNT;
        };
    }

    private Accountstatustypevalues toAccountStatus(String estado) {
        if (estado == null) {
            return Accountstatustypevalues.PENDING;
        }
        return switch (estado.trim().toUpperCase()) {
            case "ACTIVA", "ACTIVO", "ENABLED" -> Accountstatustypevalues.ENABLED;
            case "INACTIVA", "INACTIVO", "BLOQUEADA", "DISABLED" -> Accountstatustypevalues.DISABLED;
            case "CERRADA", "DELETED" -> Accountstatustypevalues.DELETED;
            default -> Accountstatustypevalues.PENDING;
        };
    }

    private String buildDescription(Cuenta cuenta, Saldo saldo) {
        StringBuilder sb = new StringBuilder();
        appendPart(sb, "Banco", cuenta.getNombreBanco());
        appendPart(sb, "CodigoBanco", cuenta.getCodigoBanco());
        if (saldo != null && saldo.getMontoPorEfectivizar() != null) {
            appendPart(sb, "MontoPorEfectivizar", MoneyUtil.format(saldo.getMontoPorEfectivizar()));
        }
        if (cuenta.getComentario() != null) {
            if (!sb.isEmpty()) {
                sb.append("; ");
            }
            sb.append(cuenta.getComentario());
        }
        return sb.isEmpty() ? null : sb.toString();
    }

    private void appendPart(StringBuilder sb, String key, String value) {
        if (value == null) {
            return;
        }
        if (!sb.isEmpty()) {
            sb.append("; ");
        }
        sb.append(key).append('=').append(value);
    }
}
