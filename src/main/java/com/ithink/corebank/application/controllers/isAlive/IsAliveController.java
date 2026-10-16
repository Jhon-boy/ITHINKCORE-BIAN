package com.ithink.corebank.application.controllers.isAlive;

import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Util;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/")
public class IsAliveController {

    private static final Loggers logger = new Loggers();

    public  IsAliveController(){
        logger.setLogger(IsAliveController.class);
    }

    @GetMapping("isAlive")
    public ResponseModel isAlive(HttpServletRequest request){
        logger.info(Util.getClientIP(request));
        ResponseModel response = Util.generateOKResponse("Api Running");
        return response;
    }
}
