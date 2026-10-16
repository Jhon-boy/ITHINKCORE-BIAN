package com.ithink.corebank.security.loggers;

/*
* Interfaz logger
* */
public interface LoggerPort {
    void info(String message);

    void error(String message, Throwable error);
}
