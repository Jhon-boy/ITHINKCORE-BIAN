package com.ithink.corebank.security.loggers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Clase que maneja los Logs de la aplicacion
 * @author Jhon
 * @version 1.0
 * */
public class Loggers implements LoggerPort{

    private Logger logger;
    public void setLogger(Class<?> clazz) {
        this.logger = LogManager.getLogger(clazz);
    }

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void error(String message, Throwable error) {
        logger.error(message, error.getMessage());
    }
}
