package com.example.promocion.controller;

import com.example.promocion.dto.CalculoPromoRequest;
import com.example.promocion.model.Promo;
import com.example.promocion.model.TipoDescuento;
import com.example.promocion.security.jwt.JwtService;
import com.example.promocion.service.PromoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PromoController.class)
@AutoConfigureMockMvc(addFilters = false)
class PromoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PromoService promoService;

    @MockitoBean
    private JwtService jwtService;

    // ============================================================
    //  TEST 1: OBTENER TODAS LAS PROMOCIONES
    // ============================================================
    @Test
    void deberiaObtenerTodasLasPromociones() throws Exception {
        // Preparar
        Promo promo1 = Promo.builder()
                .idRegla("B_STDER")
                .idEvento(1001L)
                .descripcion("20% descuento Banco Estado")
                .tipo(TipoDescuento.PORCENTAJE)
                .valorDescuento(20.0)
                .fechaInicio(LocalDateTime.now())
                .fechaFin(LocalDateTime.now().plusDays(30))
                .stockMaximo(100)
                .usosActuales(0)
                .limiteEntradasCliente(5)
                .montoMinimoRequerido(15000.0)
                .build();

        Promo promo2 = Promo.builder()
                .idRegla("T_ENTEL")
                .idEvento(1002L)
                .descripcion("15% descuento clientes Entel")
                .tipo(TipoDescuento.PORCENTAJE)
                .valorDescuento(15.0)
                .fechaInicio(LocalDateTime.now())
                .fechaFin(LocalDateTime.now().plusDays(15))
                .stockMaximo(50)
                .usosActuales(10)
                .limiteEntradasCliente(3)
                .montoMinimoRequerido(10000.0)
                .build();

        List<Promo> promos = Arrays.asList(promo1, promo2);

        when(promoService.obtenerTodas()).thenReturn(promos);

        // Ejecutar y verificar
        mockMvc.perform(get("/api/v1/promociones")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.promoList[0].idRegla").value("B_STDER"))
                .andExpect(jsonPath("$._embedded.promoList[0].descripcion").value("20% descuento Banco Estado"))
                .andExpect(jsonPath("$._embedded.promoList[0]._links.self.href").exists())
                .andExpect(jsonPath("$._embedded.promoList[0]._links.calcularDescuento.href").exists())
                .andExpect(jsonPath("$._embedded.promoList[0]._links.actualizar.href").exists())
                .andExpect(jsonPath("$._embedded.promoList[0]._links.eliminar.href").exists())
                .andExpect(jsonPath("$._embedded.promoList[1].idRegla").value("T_ENTEL"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.crear.href").exists())
                .andExpect(jsonPath("$._links.calcularDescuento.href").exists());

        verify(promoService).obtenerTodas();
    }

    // ============================================================
    //  TEST 2: OBTENER PROMOCIÓN POR ID EXITOSO
    // ============================================================
    @Test
    void deberiaObtenerPromocionPorId() throws Exception {
        // Preparar
        Promo promo = Promo.builder()
                .idRegla("B_STDER")
                .idEvento(1001L)
                .descripcion("20% descuento Banco Estado")
                .tipo(TipoDescuento.PORCENTAJE)
                .valorDescuento(20.0)
                .fechaInicio(LocalDateTime.now())
                .fechaFin(LocalDateTime.now().plusDays(30))
                .stockMaximo(100)
                .usosActuales(0)
                .limiteEntradasCliente(5)
                .montoMinimoRequerido(15000.0)
                .build();

        when(promoService.obtenerPorId("B_STDER")).thenReturn(promo);

        // Ejecutar y verificar
        mockMvc.perform(get("/api/v1/promociones/B_STDER")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idRegla").value("B_STDER"))
                .andExpect(jsonPath("$.descripcion").value("20% descuento Banco Estado"))
                .andExpect(jsonPath("$.tipo").value("PORCENTAJE"))
                .andExpect(jsonPath("$.valorDescuento").value(20.0))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.todas.href").exists())
                .andExpect(jsonPath("$._links.calcularDescuento.href").exists())
                .andExpect(jsonPath("$._links.actualizar.href").exists())
                .andExpect(jsonPath("$._links.eliminar.href").exists())
                .andExpect(jsonPath("$._links.crear.href").exists());

        verify(promoService).obtenerPorId("B_STDER");
    }

    // ============================================================
    //  TEST 3: OBTENER PROMOCIÓN POR ID - NO ENCONTRADA
    // ============================================================
    @Test
    void deberiaRetornar404CuandoPromocionNoExiste() throws Exception {
        // Preparar
        when(promoService.obtenerPorId("INEXISTENTE"))
                .thenThrow(new RuntimeException("Regla no encontrada con ID: INEXISTENTE"));

        // Ejecutar y verificar
        mockMvc.perform(get("/api/v1/promociones/INEXISTENTE")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Regla no encontrada con ID: INEXISTENTE"))
                .andExpect(jsonPath("$._links.todas.href").exists());

        verify(promoService).obtenerPorId("INEXISTENTE");
    }

    // ============================================================
    //  TEST 4: CREAR PROMOCIÓN EXITOSA
    // ============================================================
    @Test
    void deberiaCrearPromocion() throws Exception {
        // Preparar
        Promo promoRequest = Promo.builder()
                .idRegla("B_STDER")
                .idEvento(1001L)
                .descripcion("20% descuento Banco Estado")
                .tipo(TipoDescuento.PORCENTAJE)
                .valorDescuento(20.0)
                .fechaInicio(LocalDateTime.now())
                .fechaFin(LocalDateTime.now().plusDays(30))
                .stockMaximo(100)
                .limiteEntradasCliente(5)
                .montoMinimoRequerido(15000.0)
                .build();

        Promo promoGuardado = Promo.builder()
                .idRegla("B_STDER")
                .idEvento(1001L)
                .descripcion("20% descuento Banco Estado")
                .tipo(TipoDescuento.PORCENTAJE)
                .valorDescuento(20.0)
                .fechaInicio(LocalDateTime.now())
                .fechaFin(LocalDateTime.now().plusDays(30))
                .stockMaximo(100)
                .usosActuales(0)
                .limiteEntradasCliente(5)
                .montoMinimoRequerido(15000.0)
                .build();

        when(promoService.guardarNuevaPromo(any(Promo.class))).thenReturn(promoGuardado);

        String json = objectMapper.writeValueAsString(promoRequest);

        // Ejecutar y verificar
        mockMvc.perform(post("/api/v1/promociones")
                        .contentType("application/json")
                        .accept(MediaTypes.HAL_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idRegla").value("B_STDER"))
                .andExpect(jsonPath("$.descripcion").value("20% descuento Banco Estado"))
                .andExpect(jsonPath("$.tipo").value("PORCENTAJE"))
                .andExpect(jsonPath("$.valorDescuento").value(20.0))
                .andExpect(jsonPath("$.usosActuales").value(0))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.todas.href").exists())
                .andExpect(jsonPath("$._links.calcularDescuento.href").exists())
                .andExpect(jsonPath("$._links.actualizar.href").exists())
                .andExpect(jsonPath("$._links.eliminar.href").exists());

        verify(promoService).guardarNuevaPromo(any(Promo.class));
    }

    // ============================================================
    //  TEST 5: CREAR PROMOCIÓN CON ID DUPLICADO
    // ============================================================
    @Test
    void deberiaRetornar404CuandoIdReglaYaExiste() throws Exception {
        // Preparar
        Promo promoRequest = Promo.builder()
                .idRegla("B_STDER")
                .idEvento(1001L)
                .descripcion("20% descuento Banco Estado")
                .tipo(TipoDescuento.PORCENTAJE)
                .valorDescuento(20.0)
                .fechaInicio(LocalDateTime.now())
                .fechaFin(LocalDateTime.now().plusDays(30))
                .stockMaximo(100)
                .limiteEntradasCliente(5)
                .montoMinimoRequerido(15000.0)
                .build();

        when(promoService.guardarNuevaPromo(any(Promo.class)))
                .thenThrow(new RuntimeException("Ya existe una promoción con ID: B_STDER"));

        String json = objectMapper.writeValueAsString(promoRequest);

        // Ejecutar y verificar
        mockMvc.perform(post("/api/v1/promociones")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Ya existe una promoción con ID: B_STDER"));

        verify(promoService).guardarNuevaPromo(any(Promo.class));
    }

    // ============================================================
    //  TEST 6: CREAR PROMOCIÓN CON CAMPOS OBLIGATORIOS FALTANTES
    // ============================================================
    @Test
    void deberiaRetornar400CuandoFaltanCamposObligatorios() throws Exception {
        // Preparar (JSON incompleto - falta idRegla, descripcion, etc)
        String json = """
                {
                    "idEvento": 1001,
                    "tipo": "PORCENTAJE"
                }
                """;

        // Ejecutar y verificar
        mockMvc.perform(post("/api/v1/promociones")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    // ============================================================
    //  TEST 7: ACTUALIZAR PROMOCIÓN EXITOSA
    // ============================================================
    @Test
    void deberiaActualizarPromocion() throws Exception {
        // Preparar
        Promo promoRequest = Promo.builder()
                .idRegla("B_STDER")
                .idEvento(1001L)
                .descripcion("25% descuento Banco Estado (actualizado)")
                .tipo(TipoDescuento.PORCENTAJE)
                .valorDescuento(25.0)
                .fechaInicio(LocalDateTime.now())
                .fechaFin(LocalDateTime.now().plusDays(45))
                .stockMaximo(150)
                .limiteEntradasCliente(6)
                .montoMinimoRequerido(12000.0)
                .build();

        Promo promoActualizado = Promo.builder()
                .idRegla("B_STDER")
                .idEvento(1001L)
                .descripcion("25% descuento Banco Estado (actualizado)")
                .tipo(TipoDescuento.PORCENTAJE)
                .valorDescuento(25.0)
                .fechaInicio(LocalDateTime.now())
                .fechaFin(LocalDateTime.now().plusDays(45))
                .stockMaximo(150)
                .usosActuales(0)
                .limiteEntradasCliente(6)
                .montoMinimoRequerido(12000.0)
                .build();

        when(promoService.actualizarPromo(eq("B_STDER"), any(Promo.class)))
                .thenReturn(promoActualizado);

        String json = objectMapper.writeValueAsString(promoRequest);

        // Ejecutar y verificar
        mockMvc.perform(put("/api/v1/promociones/B_STDER")
                        .contentType("application/json")
                        .accept(MediaTypes.HAL_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idRegla").value("B_STDER"))
                .andExpect(jsonPath("$.descripcion").value("25% descuento Banco Estado (actualizado)"))
                .andExpect(jsonPath("$.valorDescuento").value(25.0))
                .andExpect(jsonPath("$.stockMaximo").value(150))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.todas.href").exists())
                .andExpect(jsonPath("$._links.calcularDescuento.href").exists())
                .andExpect(jsonPath("$._links.eliminar.href").exists());

        verify(promoService).actualizarPromo(eq("B_STDER"), any(Promo.class));
    }

    // ============================================================
    //  TEST 8: ELIMINAR PROMOCIÓN EXITOSA
    // ============================================================
    @Test
    void deberiaEliminarPromocion() throws Exception {
        // Preparar
        doNothing().when(promoService).eliminarPromo("B_STDER");

        // Ejecutar y verificar
        mockMvc.perform(delete("/api/v1/promociones/B_STDER"))
                .andExpect(status().isNoContent());

        verify(promoService).eliminarPromo("B_STDER");
    }

    // ============================================================
    //  TEST 9: CALCULAR DESCUENTO EXITOSO
    // ============================================================
    @Test
    void deberiaCalcularDescuento() throws Exception {
        // Preparar
        CalculoPromoRequest request = new CalculoPromoRequest();
        request.setIdRegla("B_STDER");
        request.setIdEvento(1001L);
        request.setUserRun("12345678-9");
        request.setCantidadTickets(4);
        request.setMontoTotal(80000.0);

        Double montoFinal = 64000.0; // 20% de descuento

        when(promoService.calcularDescuentoFinal(any(CalculoPromoRequest.class)))
                .thenReturn(montoFinal);

        String json = objectMapper.writeValueAsString(request);

        // Ejecutar y verificar
        mockMvc.perform(post("/api/v1/promociones/calcular")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("64000.0"));

        verify(promoService).calcularDescuentoFinal(any(CalculoPromoRequest.class));
    }

    // ============================================================
    //  TEST 10: CALCULAR DESCUENTO CON REGLA NO ENCONTRADA
    // ============================================================
    @Test
    void deberiaRetornar404AlCalcularDescuentoConReglaInexistente() throws Exception {
        // Preparar
        CalculoPromoRequest request = new CalculoPromoRequest();
        request.setIdRegla("INEXISTENTE");
        request.setIdEvento(1001L);
        request.setUserRun("12345678-9");
        request.setCantidadTickets(4);
        request.setMontoTotal(80000.0);

        when(promoService.calcularDescuentoFinal(any(CalculoPromoRequest.class)))
                .thenThrow(new RuntimeException("Regla no encontrada con ID: INEXISTENTE"));

        String json = objectMapper.writeValueAsString(request);

        // Ejecutar y verificar
        mockMvc.perform(post("/api/v1/promociones/calcular")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Regla no encontrada con ID: INEXISTENTE"))
                .andExpect(jsonPath("$._links.todas.href").exists());

        verify(promoService).calcularDescuentoFinal(any(CalculoPromoRequest.class));
    }
}