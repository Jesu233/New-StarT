package com.example.recintos.controller;

import com.example.recintos.assembler.RecintosModelAssembler;
import com.example.recintos.client.RecintosClient;
import com.example.recintos.model.Estadisticas;
import com.example.recintos.model.Localidades;
import com.example.recintos.model.Recintos;
import com.example.recintos.security.jwt.JwtService;
import com.example.recintos.service.RecintosService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecintosController.class)
@AutoConfigureMockMvc(addFilters = false)
class RecintosControllerTest {

    private static final MediaType HAL_JSON = MediaType.parseMediaType("application/hal+json");

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecintosService service;

    @MockitoBean
    private RecintosClient client;

    @MockitoBean
    private RecintosModelAssembler assembler;

    @MockitoBean
    private JwtService jwtService;

    private Recintos crearRecintoConLinks(Long id, String nombre, String ubicacion, Long capacidad) {
        Recintos recinto = new Recintos();
        recinto.setId(id);
        recinto.setNombre(nombre);
        recinto.setUbicacion(ubicacion);
        recinto.setCapacidad(capacidad);

        recinto.add(org.springframework.hateoas.Link.of(
                "http://localhost/api/v1/recintos/" + id, "self"));
        recinto.add(org.springframework.hateoas.Link.of(
                "http://localhost/api/v1/recintos", "todos"));
        recinto.add(org.springframework.hateoas.Link.of(
                "http://localhost/api/v1/recintos/" + id, "actualizar"));
        recinto.add(org.springframework.hateoas.Link.of(
                "http://localhost/api/v1/recintos/" + id, "eliminar"));

        return recinto;
    }

    @Test
    void deberiaListarRecintos() throws Exception {

        Recintos recinto = crearRecintoConLinks(1L, "Movistar Arena", "Santiago", 15500L);

        when(service.listar()).thenReturn(List.of(recinto));
        when(assembler.toModel(any(Recintos.class))).thenReturn(recinto);

        mockMvc.perform(get("/api/v1/recintos")
                        .accept(HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Movistar Arena"))
                .andExpect(jsonPath("$[0].links[0].rel").value("self"));

        verify(service).listar();
    }

    @Test
    void deberiaRetornarRecintoPorId() throws Exception {

        Recintos recinto = crearRecintoConLinks(1L, "Movistar Arena", "Santiago", 15500L);

        Localidades localidades = new Localidades();
        localidades.setCampoDelantero(3000L);
        localidades.setCampoTrasero(4000L);
        localidades.setPlateaBaja(4500L);
        localidades.setPlateaAlta(4000L);

        when(service.obtener(1L)).thenReturn(Optional.of(recinto));
        when(client.obtenerLocalidades(1L, "Bearer token123")).thenReturn(localidades);
        when(assembler.toModel(any(Recintos.class))).thenReturn(recinto);

        mockMvc.perform(get("/api/v1/recintos/1")
                        .header("Authorization", "Bearer token123")
                        .accept(HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Movistar Arena"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/v1/recintos/1"))
                .andExpect(jsonPath("$._links.todos.href").value("http://localhost/api/v1/recintos"))
                .andExpect(jsonPath("$._links.actualizar.href").value("http://localhost/api/v1/recintos/1"))
                .andExpect(jsonPath("$._links.eliminar.href").value("http://localhost/api/v1/recintos/1"));

        verify(service).obtener(1L);
        verify(client).obtenerLocalidades(1L, "Bearer token123");
    }

    @Test
    void deberiaRetornar404CuandoRecintoNoExiste() throws Exception {

        when(service.obtener(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/recintos/99")
                        .header("Authorization", "Bearer token123")
                        .accept(HAL_JSON))
                .andExpect(status().isNotFound());

        verify(service).obtener(99L);
    }

    @Test
    void deberiaCrearRecinto() throws Exception {

        Recintos recinto = crearRecintoConLinks(1L, "Movistar Arena", "Santiago", 15500L);

        when(service.guardar(any(Recintos.class))).thenReturn(recinto);
        when(assembler.toModel(any(Recintos.class))).thenReturn(recinto);

        String json = """
                {
                    "nombre": "Movistar Arena",
                    "ubicacion": "Santiago",
                    "capacidad": 15500
                }
                """;

        mockMvc.perform(post("/api/v1/recintos")
                        .contentType("application/json")
                        .accept(HAL_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Movistar Arena"))
                .andExpect(jsonPath("$._links.self.href").exists());

        verify(service).guardar(any(Recintos.class));
    }

    @Test
    void deberiaRetornar400CuandoFaltanCamposObligatorios() throws Exception {

        String json = """
                {
                    "nombre": "Movistar Arena"
                }
                """;

        mockMvc.perform(post("/api/v1/recintos")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deberiaActualizarRecinto() throws Exception {

        Recintos recinto = crearRecintoConLinks(1L, "Movistar Arena Updated", "Santiago Centro", 16000L);

        when(service.actualizar(anyLong(), any(Recintos.class))).thenReturn(Optional.of(recinto));
        when(assembler.toModel(any(Recintos.class))).thenReturn(recinto);

        String json = """
                {
                    "nombre": "Movistar Arena Updated",
                    "ubicacion": "Santiago Centro",
                    "capacidad": 16000
                }
                """;

        mockMvc.perform(put("/api/v1/recintos/1")
                        .contentType("application/json")
                        .accept(HAL_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Movistar Arena Updated"));

        verify(service).actualizar(anyLong(), any(Recintos.class));
    }

    @Test
    void deberiaRetornar404AlActualizarRecintoInexistente() throws Exception {

        when(service.actualizar(anyLong(), any(Recintos.class))).thenReturn(Optional.empty());

        String json = """
                {
                    "nombre": "No existe",
                    "ubicacion": "Santiago",
                    "capacidad": 1000
                }
                """;

        mockMvc.perform(put("/api/v1/recintos/99")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void deberiaEliminarRecinto() throws Exception {

        when(service.eliminar(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/recintos/1"))
                .andExpect(status().isNoContent());

        verify(service).eliminar(1L);
    }

    @Test
    void deberiaRetornar404AlEliminarRecintoInexistente() throws Exception {

        when(service.eliminar(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/recintos/99"))
                .andExpect(status().isNotFound());

        verify(service).eliminar(99L);
    }

    @Test
    void deberiaBuscarPorNombre() throws Exception {

        Recintos recinto = crearRecintoConLinks(1L, "Movistar Arena", "Santiago", 15500L);

        when(service.buscarPorNombre("movistar")).thenReturn(List.of(recinto));
        when(assembler.toModel(any(Recintos.class))).thenReturn(recinto);

        mockMvc.perform(get("/api/v1/recintos/buscar")
                        .param("nombre", "movistar")
                        .accept(HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Movistar Arena"));

        verify(service).buscarPorNombre("movistar");
    }

    @Test
    void deberiaRetornarEstadisticasLocalidades() throws Exception {

        Estadisticas estadisticas = new Estadisticas();
        estadisticas.setTotalLocalidades(5L);
        estadisticas.setCapacidadPromedio(20000L);
        estadisticas.setMayorCapacidad(55000L);
        estadisticas.setMenorCapacidad(12000L);

        when(client.obtenerEstadisticas("Bearer token123")).thenReturn(estadisticas);

        mockMvc.perform(get("/api/v1/recintos/localidades/estadisticas")
                        .header("Authorization", "Bearer token123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalLocalidades").value(5))
                .andExpect(jsonPath("$.capacidadPromedio").value(20000));

        verify(client).obtenerEstadisticas("Bearer token123");
    }
}