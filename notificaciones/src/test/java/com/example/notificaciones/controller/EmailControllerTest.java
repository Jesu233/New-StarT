package com.example.notificaciones.controller;

import com.example.notificaciones.dto.EmailBienvenidaDTO;
import com.example.notificaciones.dto.EmailCompraDTO;
import com.example.notificaciones.dto.EmailLogoutDTO;
import com.example.notificaciones.dto.EmailRecuperacionDTO;
import com.example.notificaciones.model.CorreoEnviado;
import com.example.notificaciones.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmailController.class)
@AutoConfigureMockMvc(addFilters = false)
class EmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmailService emailService;

    @Test
    void deberiaEnviarCorreoDeCompra() throws Exception {
        CorreoEnviado correo = new CorreoEnviado();
        correo.setId(1L);
        correo.setDestinatario("juan@email.com");
        correo.setTipo("COMPRA");
        correo.setEstado("ENVIADO");
        correo.setFechaEnvio(LocalDateTime.now());

        when(emailService.enviarConfirmacionCompra(any(EmailCompraDTO.class)))
                .thenReturn(correo);

        EmailCompraDTO dto = new EmailCompraDTO();
        dto.setDestinatario("juan@email.com");
        dto.setNombreUsuario("Juan");
        dto.setNombreEvento("Concierto Rock");
        dto.setTokens(java.util.List.of("ABC123", "DEF456"));

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/email/compra")
                        .contentType("application/json")
                        .accept(MediaTypes.HAL_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensaje").value("Correo de compra enviado exitosamente"))
                .andExpect(jsonPath("$.idCorreo").value(1))
                .andExpect(jsonPath("$.destinatario").value("juan@email.com"))
                .andExpect(jsonPath("$.estado").value("ENVIADO"))
                .andExpect(jsonPath("$.tipo").value("COMPRA"))
                .andExpect(jsonPath("$._links.self.href").exists());

        verify(emailService).enviarConfirmacionCompra(any(EmailCompraDTO.class));
    }

    @Test
    void deberiaEnviarCorreoDeBienvenida() throws Exception {
        CorreoEnviado correo = new CorreoEnviado();
        correo.setId(2L);
        correo.setDestinatario("maria@email.com");
        correo.setTipo("BIENVENIDA");
        correo.setEstado("ENVIADO");
        correo.setFechaEnvio(LocalDateTime.now());

        when(emailService.enviarBienvenida(any(EmailBienvenidaDTO.class)))
                .thenReturn(correo);

        EmailBienvenidaDTO dto = new EmailBienvenidaDTO();
        dto.setDestinatario("maria@email.com");
        dto.setNombreUsuario("Maria");

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/email/bienvenida")
                        .contentType("application/json")
                        .accept(MediaTypes.HAL_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensaje").value("Correo de bienvenida enviado exitosamente"))
                .andExpect(jsonPath("$.idCorreo").value(2))
                .andExpect(jsonPath("$.destinatario").value("maria@email.com"))
                .andExpect(jsonPath("$.estado").value("ENVIADO"))
                .andExpect(jsonPath("$.tipo").value("BIENVENIDA"))
                .andExpect(jsonPath("$._links.self.href").exists());

        verify(emailService).enviarBienvenida(any(EmailBienvenidaDTO.class));
    }

    @Test
    void deberiaEnviarCorreoDeLogout() throws Exception {
        CorreoEnviado correo = new CorreoEnviado();
        correo.setId(3L);
        correo.setDestinatario("pedro@email.com");
        correo.setTipo("LOGOUT");
        correo.setEstado("ENVIADO");
        correo.setFechaEnvio(LocalDateTime.now());

        when(emailService.enviarAvisoLogout(any(EmailLogoutDTO.class)))
                .thenReturn(correo);

        EmailLogoutDTO dto = new EmailLogoutDTO();
        dto.setDestinatario("pedro@email.com");
        dto.setNombreUsuario("Pedro");

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/email/logout")
                        .contentType("application/json")
                        .accept(MediaTypes.HAL_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensaje").value("Correo de logout enviado exitosamente"))
                .andExpect(jsonPath("$.idCorreo").value(3))
                .andExpect(jsonPath("$.destinatario").value("pedro@email.com"))
                .andExpect(jsonPath("$.estado").value("ENVIADO"))
                .andExpect(jsonPath("$.tipo").value("LOGOUT"))
                .andExpect(jsonPath("$._links.self.href").exists());

        verify(emailService).enviarAvisoLogout(any(EmailLogoutDTO.class));
    }

    @Test
    void deberiaEnviarCorreoDeRecuperacion() throws Exception {
        CorreoEnviado correo = new CorreoEnviado();
        correo.setId(4L);
        correo.setDestinatario("laura@email.com");
        correo.setTipo("RECUPERACION");
        correo.setEstado("ENVIADO");
        correo.setFechaEnvio(LocalDateTime.now());

        when(emailService.enviarRecuperacion(any(EmailRecuperacionDTO.class)))
                .thenReturn(correo);

        EmailRecuperacionDTO dto = new EmailRecuperacionDTO();
        dto.setDestinatario("laura@email.com");
        dto.setNombreUsuario("Laura");
        dto.setLinkRecuperacion("http://localhost:8080/recuperar?token=abc123");

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/email/recuperacion")
                        .contentType("application/json")
                        .accept(MediaTypes.HAL_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensaje").value("Correo de recuperación enviado exitosamente"))
                .andExpect(jsonPath("$.idCorreo").value(4))
                .andExpect(jsonPath("$.destinatario").value("laura@email.com"))
                .andExpect(jsonPath("$.estado").value("ENVIADO"))
                .andExpect(jsonPath("$.tipo").value("RECUPERACION"))
                .andExpect(jsonPath("$._links.self.href").exists());

        verify(emailService).enviarRecuperacion(any(EmailRecuperacionDTO.class));
    }
}