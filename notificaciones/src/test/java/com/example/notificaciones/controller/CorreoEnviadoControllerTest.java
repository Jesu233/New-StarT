package com.example.notificaciones.controller;

import com.example.notificaciones.model.CorreoEnviado;
import com.example.notificaciones.repository.CorreoEnviadoRepository;
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
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CorreoEnviadoController.class)
@AutoConfigureMockMvc(addFilters = false)
class CorreoEnviadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CorreoEnviadoRepository correoRepo;

    @Test
    void deberiaObtenerTodosLosCorreos() throws Exception {
        CorreoEnviado correo1 = new CorreoEnviado();
        correo1.setId(1L);
        correo1.setDestinatario("juan@email.com");
        correo1.setTipo("COMPRA");
        correo1.setEstado("ENVIADO");
        correo1.setFechaEnvio(LocalDateTime.now());

        CorreoEnviado correo2 = new CorreoEnviado();
        correo2.setId(2L);
        correo2.setDestinatario("maria@email.com");
        correo2.setTipo("BIENVENIDA");
        correo2.setEstado("ENVIADO");
        correo2.setFechaEnvio(LocalDateTime.now());

        List<CorreoEnviado> correos = Arrays.asList(correo1, correo2);

        when(correoRepo.findAll()).thenReturn(correos);

        // ✅ CORREGIDO: Usar "$._embedded.correoEnviadoList" en lugar de "$[0]"
        mockMvc.perform(get("/api/v1/correos")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.correoEnviadoList[0].id").value(1))
                .andExpect(jsonPath("$._embedded.correoEnviadoList[0].destinatario").value("juan@email.com"))
                .andExpect(jsonPath("$._embedded.correoEnviadoList[0].tipo").value("COMPRA"))
                .andExpect(jsonPath("$._embedded.correoEnviadoList[0]._links.self.href").exists())
                .andExpect(jsonPath("$._embedded.correoEnviadoList[1].id").value(2))
                .andExpect(jsonPath("$._embedded.correoEnviadoList[1].destinatario").value("maria@email.com"))
                .andExpect(jsonPath("$._embedded.correoEnviadoList[1].tipo").value("BIENVENIDA"))
                .andExpect(jsonPath("$._embedded.correoEnviadoList[1]._links.self.href").exists());

        verify(correoRepo).findAll();
    }

    @Test
    void deberiaObtenerCorreoPorId() throws Exception {
        CorreoEnviado correo = new CorreoEnviado();
        correo.setId(1L);
        correo.setDestinatario("juan@email.com");
        correo.setTipo("COMPRA");
        correo.setEstado("ENVIADO");
        correo.setFechaEnvio(LocalDateTime.now());

        when(correoRepo.findById(1L)).thenReturn(Optional.of(correo));

        // ✅ ESTO NO CAMBIA - findById devuelve un objeto, no una lista
        mockMvc.perform(get("/api/v1/correos/1")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.destinatario").value("juan@email.com"))
                .andExpect(jsonPath("$.tipo").value("COMPRA"))
                .andExpect(jsonPath("$.estado").value("ENVIADO"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.todos.href").exists());

        verify(correoRepo).findById(1L);
    }

    @Test
    void deberiaRetornar404CuandoCorreoNoExiste() throws Exception {
        when(correoRepo.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/correos/999")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isNotFound());

        verify(correoRepo).findById(999L);
    }

    @Test
    void deberiaBuscarCorreosPorDestinatario() throws Exception {
        CorreoEnviado correo1 = new CorreoEnviado();
        correo1.setId(1L);
        correo1.setDestinatario("juan@email.com");
        correo1.setTipo("COMPRA");
        correo1.setEstado("ENVIADO");
        correo1.setFechaEnvio(LocalDateTime.now());

        CorreoEnviado correo2 = new CorreoEnviado();
        correo2.setId(2L);
        correo2.setDestinatario("juan@email.com");
        correo2.setTipo("LOGOUT");
        correo2.setEstado("ENVIADO");
        correo2.setFechaEnvio(LocalDateTime.now());

        List<CorreoEnviado> correos = Arrays.asList(correo1, correo2);

        when(correoRepo.findByDestinatario("juan@email.com")).thenReturn(correos);

        // ✅ CORREGIDO: Usar "$._embedded.correoEnviadoList"
        mockMvc.perform(get("/api/v1/correos/destinatario/juan@email.com")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.correoEnviadoList[0].id").value(1))
                .andExpect(jsonPath("$._embedded.correoEnviadoList[0].destinatario").value("juan@email.com"))
                .andExpect(jsonPath("$._embedded.correoEnviadoList[0].tipo").value("COMPRA"))
                .andExpect(jsonPath("$._embedded.correoEnviadoList[0]._links.self.href").exists())
                .andExpect(jsonPath("$._embedded.correoEnviadoList[1].id").value(2))
                .andExpect(jsonPath("$._embedded.correoEnviadoList[1].destinatario").value("juan@email.com"))
                .andExpect(jsonPath("$._embedded.correoEnviadoList[1].tipo").value("LOGOUT"))
                .andExpect(jsonPath("$._embedded.correoEnviadoList[1]._links.self.href").exists());

        verify(correoRepo).findByDestinatario("juan@email.com");
    }

    @Test
    void deberiaBuscarCorreosPorTipo() throws Exception {
        CorreoEnviado correo1 = new CorreoEnviado();
        correo1.setId(1L);
        correo1.setDestinatario("juan@email.com");
        correo1.setTipo("COMPRA");
        correo1.setEstado("ENVIADO");
        correo1.setFechaEnvio(LocalDateTime.now());

        CorreoEnviado correo2 = new CorreoEnviado();
        correo2.setId(2L);
        correo2.setDestinatario("maria@email.com");
        correo2.setTipo("COMPRA");
        correo2.setEstado("ENVIADO");
        correo2.setFechaEnvio(LocalDateTime.now());

        List<CorreoEnviado> correos = Arrays.asList(correo1, correo2);

        when(correoRepo.findByTipo("COMPRA")).thenReturn(correos);

        // ✅ CORREGIDO: Usar "$._embedded.correoEnviadoList"
        mockMvc.perform(get("/api/v1/correos/tipo/COMPRA")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.correoEnviadoList[0].id").value(1))
                .andExpect(jsonPath("$._embedded.correoEnviadoList[0].tipo").value("COMPRA"))
                .andExpect(jsonPath("$._embedded.correoEnviadoList[0]._links.self.href").exists())
                .andExpect(jsonPath("$._embedded.correoEnviadoList[1].id").value(2))
                .andExpect(jsonPath("$._embedded.correoEnviadoList[1].tipo").value("COMPRA"))
                .andExpect(jsonPath("$._embedded.correoEnviadoList[1]._links.self.href").exists());

        verify(correoRepo).findByTipo("COMPRA");
    }

    @Test
    void deberiaBuscarCorreosPorEstado() throws Exception {
        CorreoEnviado correo1 = new CorreoEnviado();
        correo1.setId(1L);
        correo1.setDestinatario("juan@email.com");
        correo1.setTipo("COMPRA");
        correo1.setEstado("ENVIADO");
        correo1.setFechaEnvio(LocalDateTime.now());

        CorreoEnviado correo2 = new CorreoEnviado();
        correo2.setId(2L);
        correo2.setDestinatario("maria@email.com");
        correo2.setTipo("BIENVENIDA");
        correo2.setEstado("ENVIADO");
        correo2.setFechaEnvio(LocalDateTime.now());

        List<CorreoEnviado> correos = Arrays.asList(correo1, correo2);

        when(correoRepo.findByEstado("ENVIADO")).thenReturn(correos);

        // ✅ CORREGIDO: Usar "$._embedded.correoEnviadoList"
        mockMvc.perform(get("/api/v1/correos/estado/ENVIADO")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.correoEnviadoList[0].id").value(1))
                .andExpect(jsonPath("$._embedded.correoEnviadoList[0].estado").value("ENVIADO"))
                .andExpect(jsonPath("$._embedded.correoEnviadoList[0]._links.self.href").exists())
                .andExpect(jsonPath("$._embedded.correoEnviadoList[1].id").value(2))
                .andExpect(jsonPath("$._embedded.correoEnviadoList[1].estado").value("ENVIADO"))
                .andExpect(jsonPath("$._embedded.correoEnviadoList[1]._links.self.href").exists());

        verify(correoRepo).findByEstado("ENVIADO");
    }
}