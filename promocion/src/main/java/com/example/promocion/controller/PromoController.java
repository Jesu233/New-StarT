package com.example.promocion.controller;

import com.example.promocion.dto.CalculoPromoRequest;
import com.example.promocion.model.Promo;
import com.example.promocion.service.PromoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;  // ✅ IMPORTAR ESTO
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Slf4j
@RestController
@RequestMapping("api/v1/promociones")
@RequiredArgsConstructor
@Tag(name = "Gestión de Promociones", description = "API para gestionar promociones y calcular descuentos")
@SecurityRequirement(name = "bearerAuth")
public class PromoController {

    private final PromoService promoService;

    // ============================================================
    //  GET ALL
    // ============================================================
    @GetMapping
    @Operation(summary = "Obtener todas las promociones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Promociones obtenidas exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos")
    })
    public ResponseEntity<CollectionModel<Promo>> getAllPromos() {
        log.info("GET /promociones - Obteniendo todas las promociones");
        List<Promo> promos = promoService.obtenerTodas();

        for (Promo promo : promos) {
            promo.add(linkTo(methodOn(PromoController.class)
                    .getPromoById(promo.getIdRegla()))
                    .withSelfRel());
            promo.add(linkTo(methodOn(PromoController.class)
                    .calcularDescuento(new CalculoPromoRequest()))
                    .withRel("calcularDescuento"));
            promo.add(linkTo(methodOn(PromoController.class)
                    .actualizarPromo(promo.getIdRegla(), null))
                    .withRel("actualizar"));
            promo.add(linkTo(methodOn(PromoController.class)
                    .eliminarPromo(promo.getIdRegla()))
                    .withRel("eliminar"));
        }

        CollectionModel<Promo> model = CollectionModel.of(promos);
        model.add(linkTo(methodOn(PromoController.class).getAllPromos()).withSelfRel());
        model.add(linkTo(methodOn(PromoController.class).guardarPromo(null)).withRel("crear"));
        model.add(linkTo(methodOn(PromoController.class).calcularDescuento(new CalculoPromoRequest())).withRel("calcularDescuento"));

        return ResponseEntity.ok(model);
    }

    // ============================================================
    //  GET BY ID
    // ============================================================
    @GetMapping("/{idRegla}")
    @Operation(summary = "Obtener una promoción por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Promoción encontrada"),
            @ApiResponse(responseCode = "404", description = "Promoción no encontrada"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos")
    })
    public ResponseEntity<Promo> getPromoById(@PathVariable String idRegla) {
        log.info("GET /promociones/{} - Buscando promoción", idRegla);
        Promo promo = promoService.obtenerPorId(idRegla);

        promo.add(linkTo(methodOn(PromoController.class).getPromoById(idRegla)).withSelfRel());
        promo.add(linkTo(methodOn(PromoController.class).getAllPromos()).withRel("todas"));
        promo.add(linkTo(methodOn(PromoController.class).calcularDescuento(new CalculoPromoRequest())).withRel("calcularDescuento"));
        promo.add(linkTo(methodOn(PromoController.class).actualizarPromo(idRegla, null)).withRel("actualizar"));
        promo.add(linkTo(methodOn(PromoController.class).eliminarPromo(idRegla)).withRel("eliminar"));
        promo.add(linkTo(methodOn(PromoController.class).guardarPromo(null)).withRel("crear"));

        return ResponseEntity.ok(promo);
    }

    // ============================================================
    //  POST - CREAR ✅ CON @Valid
    // ============================================================
    @PostMapping
    @Operation(summary = "Crear nueva promoción")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Promoción creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o ID ya existe"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos")
    })
    public ResponseEntity<Promo> guardarPromo(@Valid @RequestBody Promo promo) {  // ✅ AGREGAR @Valid
        log.info("POST /promociones - Creando nueva promoción con ID: {}", promo.getIdRegla());
        Promo nuevaPromo = promoService.guardarNuevaPromo(promo);

        nuevaPromo.add(linkTo(methodOn(PromoController.class).getPromoById(nuevaPromo.getIdRegla())).withSelfRel());
        nuevaPromo.add(linkTo(methodOn(PromoController.class).getAllPromos()).withRel("todas"));
        nuevaPromo.add(linkTo(methodOn(PromoController.class).calcularDescuento(new CalculoPromoRequest())).withRel("calcularDescuento"));
        nuevaPromo.add(linkTo(methodOn(PromoController.class).actualizarPromo(nuevaPromo.getIdRegla(), null)).withRel("actualizar"));
        nuevaPromo.add(linkTo(methodOn(PromoController.class).eliminarPromo(nuevaPromo.getIdRegla())).withRel("eliminar"));

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaPromo);
    }

    // ============================================================
    //  PUT - ACTUALIZAR
    // ============================================================
    @PutMapping("/{idRegla}")
    @Operation(summary = "Actualizar una promoción existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Promoción actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Promoción no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos")
    })
    public ResponseEntity<Promo> actualizarPromo(
            @PathVariable String idRegla,
            @Valid @RequestBody Promo promo) {  // ✅ AGREGAR @Valid
        log.info("PUT /promociones/{} - Actualizando promoción", idRegla);
        Promo promoActualizada = promoService.actualizarPromo(idRegla, promo);

        promoActualizada.add(linkTo(methodOn(PromoController.class).getPromoById(idRegla)).withSelfRel());
        promoActualizada.add(linkTo(methodOn(PromoController.class).getAllPromos()).withRel("todas"));
        promoActualizada.add(linkTo(methodOn(PromoController.class).calcularDescuento(new CalculoPromoRequest())).withRel("calcularDescuento"));
        promoActualizada.add(linkTo(methodOn(PromoController.class).eliminarPromo(idRegla)).withRel("eliminar"));

        return ResponseEntity.ok(promoActualizada);
    }

    // ============================================================
    //  DELETE
    // ============================================================
    @DeleteMapping("/{idRegla}")
    @Operation(summary = "Eliminar una promoción por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Promoción eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Promoción no encontrada"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos")
    })
    public ResponseEntity<Void> eliminarPromo(@PathVariable String idRegla) {
        log.info("DELETE /promociones/{} - Eliminando promoción", idRegla);
        promoService.eliminarPromo(idRegla);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    //  POST - CALCULAR DESCUENTO
    // ============================================================
    @PostMapping("/calcular")
    @Operation(summary = "Calcular descuento aplicable")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Descuento calculado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "404", description = "Regla de promoción no encontrada")
    })
    public ResponseEntity<Double> calcularDescuento(@Valid @RequestBody CalculoPromoRequest request) {  // ✅ AGREGAR @Valid
        log.info("POST /promociones/calcular - Calculando descuento para usuario: {}, evento: {}",
                request.getUserRun(), request.getIdEvento());
        Double montoFinal = promoService.calcularDescuentoFinal(request);
        return ResponseEntity.ok(montoFinal);
    }
}