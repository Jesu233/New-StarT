package com.example.localidades.controller;

import com.example.localidades.assembler.LocalidadesModelAssembler;
import com.example.localidades.model.Estadisticas;
import com.example.localidades.model.Localidades;
import com.example.localidades.security.jwt.JwtService;
import com.example.localidades.service.LocalidadesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
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

@WebMvcTest(LocalidadesController.class)
@AutoConfigureMockMvc(addFilters = false)
class LocalidadesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LocalidadesService service;

    @MockitoBean
    private LocalidadesModelAssembler assembler;

    @MockitoBean
    private JwtService jwtService;

    private Localidades crearLocalidadConLinks(Long id, String nombre, Long campoDelantero, Long campoTrasero,
                                               Long plateaBaja, Long plateaAlta, Long recintoId) {
        Localidades localidad = new Localidades();
        localidad.setId(id);
        localidad.setNombre(nombre);
        localidad.setCampoDelantero(campoDelantero);
        localidad.setCampoTrasero(campoTrasero);
        localidad.setPlateaBaja(plateaBaja);
        localidad.setPlateaAlta(plateaAlta);
        localidad.setRecintoId(recintoId);

        localidad.add(org.springframework.hateoas.Link.of(
                "http://localhost/api/v1/localidades/" + id, "self"));
        localidad.add(org.springframework.hateoas.Link.of(
                "http://localhost/api/v1/localidades", "todos"));
        localidad.add(org.springframework.hateoas.Link.of(
                "http://localhost/api/v1/localidades/" + id, "actualizar"));
        localidad.add(org.springframework.hateoas.Link.of(
                "http://localhost/api/v1/localidades/" + id, "eliminar"));
        localidad.add(org.springframework.hateoas.Link.of(
                "http://localhost/api/v1/localidades/" + id + "/total", "capacidad-total"));
        localidad.add(org.springframework.hateoas.Link.of(
                "http://localhost/api/v1/localidades/recintos/" + recintoId, "por-recinto"));

        return localidad;
    }

    @Test
    void deberiaListarLocalidades() throws Exception {

        Localidades localidad = crearLocalidadConLinks(1L, "Platea Norte", 1500L, 1500L, 800L, 600L, 3L);

        when(service.listar()).thenReturn(List.of(localidad));
        when(assembler.toModel(any(Localidades.class))).thenReturn(localidad);

        mockMvc.perform(get("/api/v1/localidades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Platea Norte"))
                .andExpect(jsonPath("$[0].links[0].rel").value("self"));

        verify(service).listar();
    }

    @Test
    void deberiaRetornarLocalidadPorId() throws Exception {

        Localidades localidad = crearLocalidadConLinks(1L, "Platea Norte", 1500L, 1500L, 800L, 600L, 3L);

        when(service.obtener(1L)).thenReturn(Optional.of(localidad));
        when(assembler.toModel(any(Localidades.class))).thenReturn(localidad);

        mockMvc.perform(get("/api/v1/localidades/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Platea Norte"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/v1/localidades/1"))
                .andExpect(jsonPath("$._links.todos.href").value("http://localhost/api/v1/localidades"))
                .andExpect(jsonPath("$._links.actualizar.href").value("http://localhost/api/v1/localidades/1"))
                .andExpect(jsonPath("$._links.eliminar.href").value("http://localhost/api/v1/localidades/1"));

        verify(service).obtener(1L);
    }

    @Test
    void deberiaRetornar404CuandoLocalidadNoExiste() throws Exception {

        when(service.obtener(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/localidades/99"))
                .andExpect(status().isNotFound());

        verify(service).obtener(99L);
    }

    @Test
    void deberiaRetornarLocalidadPorRecintoId() throws Exception {

        Localidades localidad = crearLocalidadConLinks(1L, "Platea Norte", 1500L, 1500L, 800L, 600L, 3L);

        when(service.obtenerPorRecintoId(3L)).thenReturn(Optional.of(localidad));
        when(assembler.toModel(any(Localidades.class))).thenReturn(localidad);

        mockMvc.perform(get("/api/v1/localidades/recintos/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Platea Norte"))
                .andExpect(jsonPath("$.recintoId").value(3));

        verify(service).obtenerPorRecintoId(3L);
    }

    @Test
    void deberiaRetornar404CuandoRecintoNoExiste() throws Exception {

        when(service.obtenerPorRecintoId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/localidades/recintos/99"))
                .andExpect(status().isNotFound());

        verify(service).obtenerPorRecintoId(99L);
    }

    @Test
    void deberiaBuscarPorNombre() throws Exception {

        Localidades localidad = crearLocalidadConLinks(1L, "Platea Norte", 1500L, 1500L, 800L, 600L, 3L);

        when(service.buscarPorNombre("platea")).thenReturn(List.of(localidad));
        when(assembler.toModel(any(Localidades.class))).thenReturn(localidad);

        mockMvc.perform(get("/api/v1/localidades/buscar").param("nombre", "platea"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Platea Norte"));

        verify(service).buscarPorNombre("platea");
    }

    @Test
    void deberiaCalcularCapacidadTotal() throws Exception {

        when(service.calcularCapacidadTotal(1L)).thenReturn(Optional.of(4400L));

        mockMvc.perform(get("/api/v1/localidades/1/total"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.capacidadTotal").value(4400));

        verify(service).calcularCapacidadTotal(1L);
    }

    @Test
    void deberiaRetornar404AlCalcularCapacidadTotalDeLocalidadInexistente() throws Exception {

        when(service.calcularCapacidadTotal(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/localidades/99/total"))
                .andExpect(status().isNotFound());

        verify(service).calcularCapacidadTotal(99L);
    }

    @Test
    void deberiaObtenerEstadisticas() throws Exception {

        Estadisticas estadisticas = new Estadisticas(2L, 3650L, 4400L, 2900L);

        when(service.obtenerEstadisticas()).thenReturn(estadisticas);

        mockMvc.perform(get("/api/v1/localidades/estadisticas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalLocalidades").value(2))
                .andExpect(jsonPath("$.capacidadPromedio").value(3650))
                .andExpect(jsonPath("$.mayorCapacidad").value(4400))
                .andExpect(jsonPath("$.menorCapacidad").value(2900));

        verify(service).obtenerEstadisticas();
    }

    @Test
    void deberiaCrearLocalidad() throws Exception {

        Localidades localidad = crearLocalidadConLinks(1L, "Platea Norte", 1500L, 1500L, 800L, 600L, 3L);

        when(service.guardar(any(Localidades.class))).thenReturn(localidad);
        when(assembler.toModel(any(Localidades.class))).thenReturn(localidad);

        String json = """
                {
                    "nombre": "Platea Norte",
                    "campoDelantero": 1500,
                    "campoTrasero": 1500,
                    "plateaBaja": 800,
                    "plateaAlta": 600,
                    "recintoId": 3
                }
                """;

        mockMvc.perform(post("/api/v1/localidades")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Platea Norte"))
                .andExpect(jsonPath("$._links.self.href").exists());

        verify(service).guardar(any(Localidades.class));
    }

    @Test
    void deberiaRetornar400CuandoFaltanCamposObligatorios() throws Exception {

        String json = """
                {
                    "nombre": "Platea Norte"
                }
                """;

        mockMvc.perform(post("/api/v1/localidades")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deberiaRetornar400CuandoCapacidadEsMenorOIgualACero() throws Exception {

        String json = """
                {
                    "nombre": "Platea Norte",
                    "campoDelantero": 0,
                    "campoTrasero": 1500,
                    "plateaBaja": 800,
                    "plateaAlta": 600,
                    "recintoId": 3
                }
                """;

        mockMvc.perform(post("/api/v1/localidades")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deberiaActualizarLocalidad() throws Exception {

        Localidades localidad = crearLocalidadConLinks(1L, "Platea Norte Renovada", 1600L, 1600L, 850L, 650L, 3L);

        when(service.actualizar(anyLong(), any(Localidades.class))).thenReturn(Optional.of(localidad));
        when(assembler.toModel(any(Localidades.class))).thenReturn(localidad);

        String json = """
                {
                    "nombre": "Platea Norte Renovada",
                    "campoDelantero": 1600,
                    "campoTrasero": 1600,
                    "plateaBaja": 850,
                    "plateaAlta": 650,
                    "recintoId": 3
                }
                """;

        mockMvc.perform(put("/api/v1/localidades/1")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Platea Norte Renovada"));

        verify(service).actualizar(anyLong(), any(Localidades.class));
    }

    @Test
    void deberiaRetornar404AlActualizarLocalidadInexistente() throws Exception {

        when(service.actualizar(anyLong(), any(Localidades.class))).thenReturn(Optional.empty());

        String json = """
                {
                    "nombre": "No existe",
                    "campoDelantero": 1000,
                    "campoTrasero": 1000,
                    "plateaBaja": 500,
                    "plateaAlta": 400,
                    "recintoId": 3
                }
                """;

        mockMvc.perform(put("/api/v1/localidades/99")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void deberiaEliminarLocalidad() throws Exception {

        when(service.eliminar(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/localidades/1"))
                .andExpect(status().isNoContent());

        verify(service).eliminar(1L);
    }

    @Test
    void deberiaRetornar404AlEliminarLocalidadInexistente() throws Exception {

        when(service.eliminar(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/localidades/99"))
                .andExpect(status().isNotFound());

        verify(service).eliminar(99L);
    }
}