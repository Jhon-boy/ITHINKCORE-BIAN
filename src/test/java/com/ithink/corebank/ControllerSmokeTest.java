package com.ithink.corebank;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ControllerSmokeTest {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${local.server.port}")
    private int port;

    @Test
    void bancoFindAllShouldReturnOkResponseModel() {
        ResponseEntity<Map> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/consultar/banco", Map.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("1", response.getBody().get("code"));
        assertEquals("Transacción OK", response.getBody().get("message"));
        assertNotNull(response.getBody().get("data"));
    }

    @Test
    void bancoFindByIdMissingShouldReturnGenericError() {
        ResponseEntity<Map> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/consultar/banco/999999", Map.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("ERROR", response.getBody().get("code"));
        assertEquals("ERROR, INTENTE NUEVAMENTE MÁS TARDE", response.getBody().get("message"));
    }
}
