package com.ithink.corebank.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.security.loggers.Loggers;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * Clase que alimenta de funcionalidades a toda la aplicacion
 * @author Jhon
 * @version 1.0
 * */
@Component
public class Util {

    private static final Loggers log = new Loggers();

    Util(){
        log.setLogger(Util.class);
    }
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    /**
     * Genera una respuesta a para cualquier solicitud HTTP
     * @Param cod: Status de respues
     * @Param message: Mensaje personalizado
     * @Param obj: Cualquier dato adicional
     *
     * @Return Response
     * */
    public static ResponseModel generateResponse(String cod, String message, Object obj){
        return new ResponseModel(cod, message, obj);
    }

    /**
     * Genera una respuesta exitosa a para cualquier solicitud HTTP
     * @Param obj: Dato adicional
     * @Return Response
     * */
    public static ResponseModel generateOKResponse(Object obj){
        return new ResponseModel(Const.COD_OK, Const.MENSAJE_OK, obj);
    }


    /**
     * Genera una respuesta Erronea a para cualquier solicitud HTTP
     * @Param obj: Cualquier dato adicional
     * @Return Response
     * */
    public static ResponseModel generateBadResponseDefault(){
        return new ResponseModel(Const.COD_ERROR, Const.MENSAJE_ERROR, null);
    }

    /**
     * Metodo que captura la direccion Ip de una peticion HTTP
     * */
    public static String getClientIP(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader != null && !xForwardedForHeader.isEmpty()) {
            return xForwardedForHeader.split(",")[0];
        }
        return request.getRemoteAddr();
    }

    /**
     * Metodo que convirte a un JSON cualquier OBjecto
     * */
    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.info("Error al convertir objeto a JSON" + e);
            return "Error al convertir a JSON: " + e.getMessage();
        }
    }
    /**
     * Metodo que genera la lista de Errores de una solicitud HTTP en los
     * controladores
     * */
    public static <T> ResponseModel getAllErros(BindingResult bindingResult, Class<T> clazz){
        List<String> errors = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).toList();
        log.info(Const.CLASS_ERROR + clazz.getSimpleName() +  Const.ML_ERROR + toJson(errors));
        return Util.generateResponse(Const.COD_ERROR, "Verifique que los campos esten completos", errors);
    }

    /**
     * Metodo que controla el ingreso de NULOS o caracteres Invalidos
     * */
    public static String safeString(String x) {
        if (x == null || x.isEmpty()) {
            return "";
        }
        String normalized = Normalizer.normalize(x, Normalizer.Form.NFD);
        String clean = normalized.replaceAll("[^\\p{ASCII}]", "");
        clean = clean.replaceAll("[\\+\\-\\.]", "");
        return clean.replaceAll("[^\\p{L}\\p{N}\\p{Z}\\p{P}]", "").trim();
    }

    /**
     * Fecha que devuelve la hora instantenea
     * */
    public static Date getDateNow(){
        LocalDateTime fechaNow = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatedDate = fechaNow.format(formatter);
        LocalDateTime parsedTime = LocalDateTime.parse(formatedDate, formatter);
        return Date.from(parsedTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    /**
     * Parsea la Fecha cuando se recibe en formato String
     * */
    public  static  Date getDateNow(String fecha){
        SimpleDateFormat fechaFormato = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            return  fechaFormato.parse(fecha);
        }catch (Exception ex){
            throw  new IllegalArgumentException("FECHA EN FORMATO INCORRECTO");
        }
    }
}
