package com.example.recintos.client;

import com.example.recintos.model.Estadisticas;
import com.example.recintos.model.Localidades;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecintosClient {

    private final RestClient restClient;

    public Localidades obtenerLocalidades(Long id, String token) {
        try {
            log.info("Consultando localidades para recinto ID {}", id);
            return restClient.get()
                    .uri("/recintos/{id}", id)
                    .header("Authorization", token)
                    .retrieve()
                    .body(Localidades.class);
        } catch (Exception e) {
            log.error("Error consultando localidades para recinto ID {}", id, e);
            return null;
        }
    }

    public Estadisticas obtenerEstadisticas(String token) {
        try {
            log.info("Consultando estadisticas de localidades");
            return restClient.get()
                    .uri("/estadisticas")
                    .header("Authorization", token)
                    .retrieve()
                    .body(Estadisticas.class);
        } catch (Exception e) {
            log.error("Error consultando estadisticas de localidades", e);
            return null;
        }
    }
}