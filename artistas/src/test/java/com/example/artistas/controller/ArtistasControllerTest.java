package com.example.artistas.controller;

import com.example.artistas.assembler.ArtistasModelAssembler;
import com.example.artistas.model.Artistas;
import com.example.artistas.security.jwt.JwtService;
import com.example.artistas.service.ArtistasService;
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

@WebMvcTest(ArtistasController.class)
@AutoConfigureMockMvc(addFilters = false)
class ArtistasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ArtistasService service;

    @MockitoBean
    private ArtistasModelAssembler assembler;

    @MockitoBean
    private JwtService jwtService;

    private Artistas crearArtistaConLinks(Long id, String nombre, String genero, String pais) {
        Artistas artista = new Artistas();
        artista.setId(id);
        artista.setNombre(nombre);
        artista.setGenero(genero);
        artista.setPais(pais);

        artista.add(org.springframework.hateoas.Link.of(
                "http://localhost/api/v1/artistas/" + id, "self"));
        artista.add(org.springframework.hateoas.Link.of(
                "http://localhost/api/v1/artistas", "todos"));
        artista.add(org.springframework.hateoas.Link.of(
                "http://localhost/api/v1/artistas/" + id, "actualizar"));
        artista.add(org.springframework.hateoas.Link.of(
                "http://localhost/api/v1/artistas/" + id, "eliminar"));

        return artista;
    }

    @Test
    void deberiaListarArtistas() throws Exception {

        Artistas artista = crearArtistaConLinks(1L, "Coldplay", "Pop Rock", "Reino Unido");

        when(service.listar()).thenReturn(List.of(artista));
        when(assembler.toModel(any(Artistas.class))).thenReturn(artista);

        mockMvc.perform(get("/api/v1/artistas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Coldplay"))
                .andExpect(jsonPath("$[0].links[0].rel").value("self"));

        verify(service).listar();
    }

    @Test
    void deberiaRetornarArtistaPorId() throws Exception {

        Artistas artista = crearArtistaConLinks(1L, "Coldplay", "Pop Rock", "Reino Unido");

        when(service.obtener(1L)).thenReturn(Optional.of(artista));
        when(assembler.toModel(any(Artistas.class))).thenReturn(artista);

        mockMvc.perform(get("/api/v1/artistas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Coldplay"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/v1/artistas/1"))
                .andExpect(jsonPath("$._links.todos.href").value("http://localhost/api/v1/artistas"))
                .andExpect(jsonPath("$._links.actualizar.href").value("http://localhost/api/v1/artistas/1"))
                .andExpect(jsonPath("$._links.eliminar.href").value("http://localhost/api/v1/artistas/1"));

        verify(service).obtener(1L);
    }

    @Test
    void deberiaRetornar404CuandoArtistaNoExiste() throws Exception {

        when(service.obtener(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/artistas/99"))
                .andExpect(status().isNotFound());

        verify(service).obtener(99L);
    }

    @Test
    void deberiaCrearArtista() throws Exception {

        Artistas artista = crearArtistaConLinks(1L, "Coldplay", "Pop Rock", "Reino Unido");

        when(service.guardar(any(Artistas.class))).thenReturn(artista);
        when(assembler.toModel(any(Artistas.class))).thenReturn(artista);

        String json = """
                {
                    "nombre": "Coldplay",
                    "genero": "Pop Rock",
                    "pais": "Reino Unido"
                }
                """;

        mockMvc.perform(post("/api/v1/artistas")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Coldplay"))
                .andExpect(jsonPath("$._links.self.href").exists());

        verify(service).guardar(any(Artistas.class));
    }

    @Test
    void deberiaRetornar400CuandoFaltanCamposObligatorios() throws Exception {

        String json = """
                {
                    "nombre": "Coldplay"
                }
                """;

        mockMvc.perform(post("/api/v1/artistas")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deberiaActualizarArtista() throws Exception {

        Artistas artista = crearArtistaConLinks(1L, "Coldplay Updated", "Alternative Rock", "UK");

        when(service.actualizar(anyLong(), any(Artistas.class))).thenReturn(Optional.of(artista));
        when(assembler.toModel(any(Artistas.class))).thenReturn(artista);

        String json = """
                {
                    "nombre": "Coldplay Updated",
                    "genero": "Alternative Rock",
                    "pais": "UK"
                }
                """;

        mockMvc.perform(put("/api/v1/artistas/1")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Coldplay Updated"));

        verify(service).actualizar(anyLong(), any(Artistas.class));
    }

    @Test
    void deberiaRetornar404AlActualizarArtistaInexistente() throws Exception {

        when(service.actualizar(anyLong(), any(Artistas.class))).thenReturn(Optional.empty());

        String json = """
                {
                    "nombre": "No existe",
                    "genero": "Rock",
                    "pais": "Chile"
                }
                """;

        mockMvc.perform(put("/api/v1/artistas/99")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void deberiaEliminarArtista() throws Exception {

        when(service.eliminar(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/artistas/1"))
                .andExpect(status().isNoContent());

        verify(service).eliminar(1L);
    }

    @Test
    void deberiaRetornar404AlEliminarArtistaInexistente() throws Exception {

        when(service.eliminar(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/artistas/99"))
                .andExpect(status().isNotFound());

        verify(service).eliminar(99L);
    }

    @Test
    void deberiaBuscarPorNombre() throws Exception {

        Artistas artista = crearArtistaConLinks(1L, "Coldplay", "Pop Rock", "Reino Unido");

        when(service.buscarPorNombre("cold")).thenReturn(List.of(artista));
        when(assembler.toModel(any(Artistas.class))).thenReturn(artista);

        mockMvc.perform(get("/api/v1/artistas/buscar").param("nombre", "cold"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Coldplay"));

        verify(service).buscarPorNombre("cold");
    }
}