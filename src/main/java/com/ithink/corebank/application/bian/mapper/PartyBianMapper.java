package com.ithink.corebank.application.bian.mapper;

import com.ithink.bian.model.Datetime;
import com.ithink.bian.model.Datetimetypevalues;
import com.ithink.bian.model.Directoryentrydatetypevalues;
import com.ithink.bian.model.Identifier;
import com.ithink.bian.model.Name;
import com.ithink.bian.model.Party;
import com.ithink.bian.model.PartyReferenceDataDirectoryEntry;
import com.ithink.bian.model.Partyidentification;
import com.ithink.bian.model.Partyidentificationtypevalues;
import com.ithink.bian.model.Partytypevalues;
import com.ithink.corebank.domain.entities.Persona;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 * Mapea entre el contrato BIAN {@link PartyReferenceDataDirectoryEntry}
 * y la entidad de dominio {@link Persona}.
 *
 * @author Jhon
 * @version 1.0
 */
@Component
public class PartyBianMapper {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final Pattern WHITESPACE = Pattern.compile("\\s+");

    /**
     * Convierte una persona interna a entrada del directorio BIAN.
     *
     * @param persona entidad persistida
     * @return entrada BIAN
     */
    public PartyReferenceDataDirectoryEntry toBianEntry(Persona persona) {
        PartyReferenceDataDirectoryEntry entry = new PartyReferenceDataDirectoryEntry();
        entry.setPartyType(Partytypevalues.PERSON);
        entry.setDirectoryEntryDateType(Directoryentrydatetypevalues.OPEN_DATE);
        entry.setDirectoryEntryDate(toDatetime(persona.getFCreacion(), Datetimetypevalues.INITIATED_DATE));
        entry.setPartyReference(toParty(persona));
        return entry;
    }

    /**
     * Aplica los campos del payload BIAN sobre una entidad {@link Persona}.
     *
     * @param entry payload BIAN
     * @param persona entidad destino
     */
    public void applyToPersona(PartyReferenceDataDirectoryEntry entry, Persona persona) {
        if (entry == null || entry.getPartyReference() == null) {
            return;
        }

        Party party = entry.getPartyReference();

        if (party.getPartyName() != null && party.getPartyName().getName() != null) {
            applyFullName(party.getPartyName().getName(), persona);
        }

        if (party.getPartyIdentification() != null) {
            Partyidentification identification = party.getPartyIdentification();
            if (identification.getPartyIdentification() != null
                    && identification.getPartyIdentification().getIdentifierValue() != null) {
                persona.setIdentificacion(identification.getPartyIdentification().getIdentifierValue());
            }
            if (identification.getPartyIdentificationType() != null) {
                persona.setTipoIdentificacion(toLocalTipoIdentificacion(identification.getPartyIdentificationType()));
            }
        }

        if (party.getPartyDateTime() != null && party.getPartyDateTime().getDateTimeContent() != null) {
            LocalDateTime birthDate = parseDateTime(party.getPartyDateTime().getDateTimeContent());
            if (birthDate != null) {
                persona.setFNacimiento(birthDate);
            }
        }
    }

    private Party toParty(Persona persona) {
        Party party = new Party();
        party.setPartyType(Partytypevalues.PERSON);

        Name name = new Name();
        name.setName(buildFullName(persona.getNombres(), persona.getApellidos()));
        party.setPartyName(name);

        Partyidentification partyIdentification = new Partyidentification();
        partyIdentification.setPartyIdentificationType(toBianIdentificationType(persona.getTipoIdentificacion()));

        Identifier identifier = new Identifier();
        identifier.setIdentifierValue(persona.getIdentificacion());
        partyIdentification.setPartyIdentification(identifier);

        party.setPartyIdentification(partyIdentification);
        party.setPartyDateTime(toDatetime(persona.getFNacimiento(), Datetimetypevalues.INITIATED_DATE));
        return party;
    }

    private Datetime toDatetime(LocalDateTime value, Datetimetypevalues type) {
        if (value == null) {
            return null;
        }
        Datetime datetime = new Datetime();
        datetime.setDateTimeContent(value.format(DATE_TIME_FORMATTER));
        datetime.setDateTimeType(type);
        datetime.setTimeZoneCode("America/Guayaquil");
        return datetime;
    }

    private String buildFullName(String nombres, String apellidos) {
        String first = nombres == null ? "" : nombres.trim();
        String last = apellidos == null ? "" : apellidos.trim();
        return (first + " " + last).trim();
    }

    private void applyFullName(String fullName, Persona persona) {
        String normalized = WHITESPACE.matcher(fullName.trim()).replaceAll(" ");
        String[] parts = normalized.split(" ", 2);
        persona.setNombres(parts[0]);
        persona.setApellidos(parts.length > 1 ? parts[1] : "");
    }

    private Partyidentificationtypevalues toBianIdentificationType(String tipoIdentificacion) {
        if (tipoIdentificacion == null) {
            return Partyidentificationtypevalues.IDENTITYCARDNUMBER;
        }
        return switch (tipoIdentificacion.trim().toUpperCase()) {
            case "CEDULA", "CI", "IDENTITYCARDNUMBER" -> Partyidentificationtypevalues.IDENTITYCARDNUMBER;
            case "RUC", "TAXIDENTIFICATIONNUMBER" -> Partyidentificationtypevalues.TAXIDENTIFICATIONNUMBER;
            case "PASAPORTE", "PASSPORT", "PASSPORTNUMBER" -> Partyidentificationtypevalues.PASSPORTNUMBER;
            default -> Partyidentificationtypevalues.IDENTITYCARDNUMBER;
        };
    }

    private String toLocalTipoIdentificacion(Partyidentificationtypevalues type) {
        if (type == null) {
            return "CEDULA";
        }
        return switch (type) {
            case TAXIDENTIFICATIONNUMBER -> "RUC";
            case PASSPORTNUMBER -> "PASAPORTE";
            default -> "CEDULA";
        };
    }

    private LocalDateTime parseDateTime(String value) {
        try {
            return LocalDateTime.parse(value, DATE_TIME_FORMATTER);
        } catch (DateTimeParseException ignored) {
            // Intentar formato solo fecha
        }
        try {
            return java.time.LocalDate.parse(value, DATE_FORMATTER).atStartOfDay();
        } catch (DateTimeParseException ignored) {
            return null;
        }
    }
}
