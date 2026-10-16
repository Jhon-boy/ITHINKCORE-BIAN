package com.ithink.corebank.application.bian.mapper;

import com.ithink.bian.model.PartyDemographics;
import com.ithink.corebank.application.bian.exception.BianBadRequestException;
import com.ithink.corebank.domain.entities.PersonaDetalle;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Mapea el Behavior Qualifier PartyDemographics de BIAN hacia {@link PersonaDetalle}.
 *
 * @author Jhon
 * @version 1.0
 */
@Component
public class DemographicsBianMapper {

    /**
     * Convierte el detalle interno a la respuesta BIAN.
     *
     * @param detalle detalle persistido
     * @return demografía BIAN
     */
    public PartyDemographics toPartyDemographics(PersonaDetalle detalle) {
        PartyDemographics demographics = new PartyDemographics();
        demographics.setMaritalStatus(detalle.getEstadoCivil());
        demographics.setNationality(detalle.getNacionalidad());
        demographics.setOccupation(detalle.getOcupacion());
        demographics.setEducationLevel(detalle.getNivelEducativo());
        demographics.setEmployer(detalle.getEmpleador());
        demographics.setIncome(detalle.getIngresos() == null ? null : detalle.getIngresos().toPlainString());
        return demographics;
    }

    /**
     * Copia los campos demográficos sobre el detalle.
     *
     * @param demographics payload BIAN
     * @param detalle detalle destino
     */
    public void apply(PartyDemographics demographics, PersonaDetalle detalle) {
        if (demographics == null) {
            throw new BianBadRequestException("PartyDemographics payload is required");
        }
        detalle.setEstadoCivil(demographics.getMaritalStatus());
        detalle.setNacionalidad(demographics.getNationality());
        detalle.setOcupacion(demographics.getOccupation());
        detalle.setNivelEducativo(demographics.getEducationLevel());
        detalle.setEmpleador(demographics.getEmployer());
        detalle.setIngresos(parseIncome(demographics.getIncome()));
    }

    private BigDecimal parseIncome(String income) {
        if (income == null || income.isBlank()) {
            return null;
        }
        try {
            return new BigDecimal(income.trim());
        } catch (NumberFormatException ex) {
            throw new BianBadRequestException("Income is invalid: " + income);
        }
    }
}
