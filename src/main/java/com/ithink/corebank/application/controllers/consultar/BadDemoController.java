package com.ithink.corebank.application.controllers.consultar;

import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.util.Util;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint de prueba a propósito mal nombrado para validar el gate CI.
 * NO usar en demos reales. Eliminar tras la prueba del agente.
 */
@Hidden
@RestController
@RequestMapping("api/consultar/bad-demo")
public class BadDemoController {

    @GetMapping("doIt")
    public ResponseModel doIt(@RequestParam(required = false) String str) {
        String tmp = str == null ? "x" : str;
        Object data = tmp;
        return Util.generateOKResponse(data);
    }
}
