package com.example.promocion.controller;

import com.example.promocion.dto.CalculoPromoRequest;
import com.example.promocion.model.Promo;
import com.example.promocion.service.PromoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/v1/promociones")
@RequiredArgsConstructor
public class PromoController {

    private final PromoService promoService;

    @PostMapping("/calcular")
    public ResponseEntity<Double> calcularDescuento(@RequestBody CalculoPromoRequest request) {
        log.info("Solicitud de cálculo de descuento recibida para usuario: {}, evento: {}",request.getUserRun(), request.getIdEvento());
        Double montoFinal = promoService.calcularDescuentoFinal(request);
        return ResponseEntity.ok(montoFinal);
    }
    @PostMapping("/guardar")
    public ResponseEntity<Promo> guardarPromo(@RequestBody Promo promo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(promoService.guardarNuevaPromo(promo));
    }
}
