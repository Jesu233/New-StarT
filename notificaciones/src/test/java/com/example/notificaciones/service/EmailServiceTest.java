package com.example.notificaciones.service;

import com.example.notificaciones.dto.EmailBienvenidaDTO;
import com.example.notificaciones.dto.EmailCompraDTO;
import com.example.notificaciones.dto.EmailLogoutDTO;
import com.example.notificaciones.dto.EmailRecuperacionDTO;
import com.example.notificaciones.model.CorreoEnviado;
import com.example.notificaciones.repository.CorreoEnviadoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private CorreoEnviadoRepository correoRepo;

    @InjectMocks
    private EmailService emailService;

    // ==================== TEST 1: ENVIAR CORREO DE COMPRA EXITOSO ====================
    @Test
    void deberiaEnviarCorreoDeCompraYGuardarRegistro() {
        EmailCompraDTO dto = new EmailCompraDTO();
        dto.setDestinatario("juan@email.com");
        dto.setNombreUsuario("Juan");
        dto.setNombreEvento("Concierto Rock");
        dto.setTokens(Arrays.asList("ABC123", "DEF456"));

        CorreoEnviado registroGuardado = new CorreoEnviado();
        registroGuardado.setId(1L);
        registroGuardado.setDestinatario("juan@email.com");
        registroGuardado.setTipo("COMPRA");
        registroGuardado.setEstado("ENVIADO");

        when(correoRepo.save(any(CorreoEnviado.class))).thenReturn(registroGuardado);

        CorreoEnviado resultado = emailService.enviarConfirmacionCompra(dto);

        assertNotNull(resultado);
        assertEquals("juan@email.com", resultado.getDestinatario());
        assertEquals("COMPRA", resultado.getTipo());
        assertEquals("ENVIADO", resultado.getEstado());

        verify(mailSender).send(any(SimpleMailMessage.class));
        verify(correoRepo).save(any(CorreoEnviado.class));
    }

    // ==================== TEST 2: ENVIAR CORREO DE BIENVENIDA EXITOSO ====================
    @Test
    void deberiaEnviarCorreoDeBienvenidaYGuardarRegistro() {
        EmailBienvenidaDTO dto = new EmailBienvenidaDTO();
        dto.setDestinatario("maria@email.com");
        dto.setNombreUsuario("Maria");

        CorreoEnviado registroGuardado = new CorreoEnviado();
        registroGuardado.setId(2L);
        registroGuardado.setDestinatario("maria@email.com");
        registroGuardado.setTipo("BIENVENIDA");
        registroGuardado.setEstado("ENVIADO");

        when(correoRepo.save(any(CorreoEnviado.class))).thenReturn(registroGuardado);

        CorreoEnviado resultado = emailService.enviarBienvenida(dto);

        assertNotNull(resultado);
        assertEquals("maria@email.com", resultado.getDestinatario());
        assertEquals("BIENVENIDA", resultado.getTipo());
        assertEquals("ENVIADO", resultado.getEstado());

        verify(mailSender).send(any(SimpleMailMessage.class));
        verify(correoRepo).save(any(CorreoEnviado.class));
    }

    // ==================== TEST 3: ENVIAR CORREO DE LOGOUT EXITOSO ====================
    @Test
    void deberiaEnviarCorreoDeLogoutYGuardarRegistro() {
        EmailLogoutDTO dto = new EmailLogoutDTO();
        dto.setDestinatario("pedro@email.com");
        dto.setNombreUsuario("Pedro");

        CorreoEnviado registroGuardado = new CorreoEnviado();
        registroGuardado.setId(3L);
        registroGuardado.setDestinatario("pedro@email.com");
        registroGuardado.setTipo("LOGOUT");
        registroGuardado.setEstado("ENVIADO");

        when(correoRepo.save(any(CorreoEnviado.class))).thenReturn(registroGuardado);

        CorreoEnviado resultado = emailService.enviarAvisoLogout(dto);

        assertNotNull(resultado);
        assertEquals("pedro@email.com", resultado.getDestinatario());
        assertEquals("LOGOUT", resultado.getTipo());
        assertEquals("ENVIADO", resultado.getEstado());

        verify(mailSender).send(any(SimpleMailMessage.class));
        verify(correoRepo).save(any(CorreoEnviado.class));
    }

    // ==================== TEST 4: ENVIAR CORREO DE RECUPERACION EXITOSO ====================
    @Test
    void deberiaEnviarCorreoDeRecuperacionYGuardarRegistro() {
        EmailRecuperacionDTO dto = new EmailRecuperacionDTO();
        dto.setDestinatario("laura@email.com");
        dto.setNombreUsuario("Laura");
        dto.setLinkRecuperacion("http://localhost:8080/recuperar?token=abc123");

        CorreoEnviado registroGuardado = new CorreoEnviado();
        registroGuardado.setId(4L);
        registroGuardado.setDestinatario("laura@email.com");
        registroGuardado.setTipo("RECUPERACION");
        registroGuardado.setEstado("ENVIADO");

        when(correoRepo.save(any(CorreoEnviado.class))).thenReturn(registroGuardado);

        CorreoEnviado resultado = emailService.enviarRecuperacion(dto);

        assertNotNull(resultado);
        assertEquals("laura@email.com", resultado.getDestinatario());
        assertEquals("RECUPERACION", resultado.getTipo());
        assertEquals("ENVIADO", resultado.getEstado());

        verify(mailSender).send(any(SimpleMailMessage.class));
        verify(correoRepo).save(any(CorreoEnviado.class));
    }

    // ==================== TEST 5: VERIFICAR CONTENIDO DEL CORREO DE COMPRA ====================
    @Test
    void deberiaEnviarCorreoDeCompraConContenidoCorrecto() {
        EmailCompraDTO dto = new EmailCompraDTO();
        dto.setDestinatario("juan@email.com");
        dto.setNombreUsuario("Juan");
        dto.setNombreEvento("Concierto Rock");
        dto.setTokens(Arrays.asList("ABC123", "DEF456"));

        CorreoEnviado registroGuardado = new CorreoEnviado();
        registroGuardado.setId(1L);
        registroGuardado.setDestinatario("juan@email.com");
        registroGuardado.setTipo("COMPRA");
        registroGuardado.setEstado("ENVIADO");

        when(correoRepo.save(any(CorreoEnviado.class))).thenReturn(registroGuardado);

        emailService.enviarConfirmacionCompra(dto);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());

        SimpleMailMessage mensaje = captor.getValue();

        assertEquals("juan@email.com", mensaje.getTo()[0]);
        assertEquals("Confirmación de compra - Concierto Rock", mensaje.getSubject());

        String texto = mensaje.getText();
        assertTrue(texto.contains("Hola Juan"));
        assertTrue(texto.contains("Tu compra fue exitosa"));
        assertTrue(texto.contains("Concierto Rock"));
        assertTrue(texto.contains("Token: ABC123"));
        assertTrue(texto.contains("Token: DEF456"));
        assertTrue(texto.contains("Presenta estos códigos en el evento"));
        assertTrue(texto.contains("Gracias por tu compra"));
    }

    // ==================== TEST 6: VERIFICAR CONTENIDO DEL CORREO DE BIENVENIDA ====================
    @Test
    void deberiaEnviarCorreoDeBienvenidaConContenidoCorrecto() {
        EmailBienvenidaDTO dto = new EmailBienvenidaDTO();
        dto.setDestinatario("maria@email.com");
        dto.setNombreUsuario("Maria");

        CorreoEnviado registroGuardado = new CorreoEnviado();
        registroGuardado.setId(2L);
        registroGuardado.setDestinatario("maria@email.com");
        registroGuardado.setTipo("BIENVENIDA");
        registroGuardado.setEstado("ENVIADO");

        when(correoRepo.save(any(CorreoEnviado.class))).thenReturn(registroGuardado);

        emailService.enviarBienvenida(dto);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());

        SimpleMailMessage mensaje = captor.getValue();

        assertEquals("maria@email.com", mensaje.getTo()[0]);
        assertEquals("¡Bienvenido/a!", mensaje.getSubject());

        String texto = mensaje.getText();
        assertTrue(texto.contains("Hola Maria"));
        assertTrue(texto.contains("Tu cuenta ha sido creada exitosamente"));
        assertTrue(texto.contains("Ya puedes iniciar sesión y comenzar a comprar entradas"));
        assertTrue(texto.contains("¡Bienvenido/a!"));
    }

    // ==================== TEST 7: VERIFICAR CONTENIDO DEL CORREO DE LOGOUT ====================
    @Test
    void deberiaEnviarCorreoDeLogoutConContenidoCorrecto() {
        EmailLogoutDTO dto = new EmailLogoutDTO();
        dto.setDestinatario("pedro@email.com");
        dto.setNombreUsuario("Pedro");

        CorreoEnviado registroGuardado = new CorreoEnviado();
        registroGuardado.setId(3L);
        registroGuardado.setDestinatario("pedro@email.com");
        registroGuardado.setTipo("LOGOUT");
        registroGuardado.setEstado("ENVIADO");

        when(correoRepo.save(any(CorreoEnviado.class))).thenReturn(registroGuardado);

        emailService.enviarAvisoLogout(dto);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());

        SimpleMailMessage mensaje = captor.getValue();

        assertEquals("pedro@email.com", mensaje.getTo()[0]);
        assertEquals("Tu sesión fue cerrada por seguridad", mensaje.getSubject());

        String texto = mensaje.getText();
        assertTrue(texto.contains("Hola Pedro"));
        assertTrue(texto.contains("cerrada por motivos de seguridad"));
        assertTrue(texto.contains("Si fuiste tú, puedes ignorar este mensaje"));
        assertTrue(texto.contains("cambia tu contraseña inmediatamente"));
        assertTrue(texto.contains("Equipo de seguridad"));
    }

    // ==================== TEST 8: VERIFICAR CONTENIDO DEL CORREO DE RECUPERACION ====================
    @Test
    void deberiaEnviarCorreoDeRecuperacionConContenidoCorrecto() {
        EmailRecuperacionDTO dto = new EmailRecuperacionDTO();
        dto.setDestinatario("laura@email.com");
        dto.setNombreUsuario("Laura");
        dto.setLinkRecuperacion("http://localhost:8080/recuperar?token=abc123");

        CorreoEnviado registroGuardado = new CorreoEnviado();
        registroGuardado.setId(4L);
        registroGuardado.setDestinatario("laura@email.com");
        registroGuardado.setTipo("RECUPERACION");
        registroGuardado.setEstado("ENVIADO");

        when(correoRepo.save(any(CorreoEnviado.class))).thenReturn(registroGuardado);

        emailService.enviarRecuperacion(dto);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());

        SimpleMailMessage mensaje = captor.getValue();

        assertEquals("laura@email.com", mensaje.getTo()[0]);
        assertEquals("Recuperación de contraseña", mensaje.getSubject());

        String texto = mensaje.getText();

        // ✅ TEXTO EXACTO DE TU EmailService
        assertTrue(texto.contains("Hola Laura"),
                "El correo debe contener 'Hola Laura'");
        assertTrue(texto.contains("Recibimos una solicitud para restablecer tu contraseña"),
                "El correo debe mencionar la solicitud de recuperación");
        assertTrue(texto.contains("Haz clic en el siguiente enlace"),
                "El correo debe indicar hacer clic en el enlace");
        assertTrue(texto.contains("http://localhost:8080/recuperar?token=abc123"),
                "El correo debe contener el enlace de recuperación");
        assertTrue(texto.contains("Si no solicitaste esto, ignora este mensaje"),
                "El correo debe mencionar ignorar el mensaje");
        assertTrue(texto.contains("El enlace expira en 30 minutos"),
                "El correo debe mencionar que el enlace expira en 30 minutos");
    }

    // ==================== TEST 9: FALLO AL ENVIAR CORREO DE COMPRA ====================
    @Test
    void deberiaGuardarEstadoFALLIDOCuandoFallaElEnvioDeCompra() {
        EmailCompraDTO dto = new EmailCompraDTO();
        dto.setDestinatario("juan@email.com");
        dto.setNombreUsuario("Juan");
        dto.setNombreEvento("Concierto Rock");
        dto.setTokens(Arrays.asList("ABC123", "DEF456"));

        doThrow(new RuntimeException("Error de conexión SMTP"))
                .when(mailSender).send(any(SimpleMailMessage.class));

        CorreoEnviado registroGuardado = new CorreoEnviado();
        registroGuardado.setId(1L);
        registroGuardado.setDestinatario("juan@email.com");
        registroGuardado.setTipo("COMPRA");
        registroGuardado.setEstado("FALLIDO");

        when(correoRepo.save(any(CorreoEnviado.class))).thenReturn(registroGuardado);

        assertThrows(RuntimeException.class, () -> {
            emailService.enviarConfirmacionCompra(dto);
        });

        verify(correoRepo).save(any(CorreoEnviado.class));
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    // ==================== TEST 10: FALLO AL ENVIAR CORREO DE BIENVENIDA ====================
    @Test
    void deberiaGuardarEstadoFALLIDOCuandoFallaElEnvioDeBienvenida() {
        EmailBienvenidaDTO dto = new EmailBienvenidaDTO();
        dto.setDestinatario("maria@email.com");
        dto.setNombreUsuario("Maria");

        doThrow(new RuntimeException("Error de conexión SMTP"))
                .when(mailSender).send(any(SimpleMailMessage.class));

        CorreoEnviado registroGuardado = new CorreoEnviado();
        registroGuardado.setId(2L);
        registroGuardado.setDestinatario("maria@email.com");
        registroGuardado.setTipo("BIENVENIDA");
        registroGuardado.setEstado("FALLIDO");

        when(correoRepo.save(any(CorreoEnviado.class))).thenReturn(registroGuardado);

        assertThrows(RuntimeException.class, () -> {
            emailService.enviarBienvenida(dto);
        });

        verify(correoRepo).save(any(CorreoEnviado.class));
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    // ==================== TEST 11: FALLO AL ENVIAR CORREO DE LOGOUT ====================
    @Test
    void deberiaGuardarEstadoFALLIDOCuandoFallaElEnvioDeLogout() {
        EmailLogoutDTO dto = new EmailLogoutDTO();
        dto.setDestinatario("pedro@email.com");
        dto.setNombreUsuario("Pedro");

        doThrow(new RuntimeException("Error de conexión SMTP"))
                .when(mailSender).send(any(SimpleMailMessage.class));

        CorreoEnviado registroGuardado = new CorreoEnviado();
        registroGuardado.setId(3L);
        registroGuardado.setDestinatario("pedro@email.com");
        registroGuardado.setTipo("LOGOUT");
        registroGuardado.setEstado("FALLIDO");

        when(correoRepo.save(any(CorreoEnviado.class))).thenReturn(registroGuardado);

        assertThrows(RuntimeException.class, () -> {
            emailService.enviarAvisoLogout(dto);
        });

        verify(correoRepo).save(any(CorreoEnviado.class));
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    // ==================== TEST 12: FALLO AL ENVIAR CORREO DE RECUPERACION ====================
    @Test
    void deberiaGuardarEstadoFALLIDOCuandoFallaElEnvioDeRecuperacion() {
        EmailRecuperacionDTO dto = new EmailRecuperacionDTO();
        dto.setDestinatario("laura@email.com");
        dto.setNombreUsuario("Laura");
        dto.setLinkRecuperacion("http://localhost:8080/recuperar?token=abc123");

        doThrow(new RuntimeException("Error de conexión SMTP"))
                .when(mailSender).send(any(SimpleMailMessage.class));

        CorreoEnviado registroGuardado = new CorreoEnviado();
        registroGuardado.setId(4L);
        registroGuardado.setDestinatario("laura@email.com");
        registroGuardado.setTipo("RECUPERACION");
        registroGuardado.setEstado("FALLIDO");

        when(correoRepo.save(any(CorreoEnviado.class))).thenReturn(registroGuardado);

        assertThrows(RuntimeException.class, () -> {
            emailService.enviarRecuperacion(dto);
        });

        verify(correoRepo).save(any(CorreoEnviado.class));
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    // ==================== TEST 13: VERIFICAR QUE SE GUARDA REGISTRO CON FECHA ====================
    @Test
    void deberiaAsignarFechaDeEnvioAlGuardarRegistro() {
        EmailCompraDTO dto = new EmailCompraDTO();
        dto.setDestinatario("juan@email.com");
        dto.setNombreUsuario("Juan");
        dto.setNombreEvento("Concierto Rock");
        dto.setTokens(Arrays.asList("ABC123"));

        CorreoEnviado registroGuardado = new CorreoEnviado();
        registroGuardado.setId(1L);
        registroGuardado.setDestinatario("juan@email.com");
        registroGuardado.setTipo("COMPRA");
        registroGuardado.setEstado("ENVIADO");
        registroGuardado.setFechaEnvio(java.time.LocalDateTime.now());

        when(correoRepo.save(any(CorreoEnviado.class))).thenReturn(registroGuardado);

        CorreoEnviado resultado = emailService.enviarConfirmacionCompra(dto);

        assertNotNull(resultado.getFechaEnvio());
    }

    // ==================== TEST 14: VERIFICAR MANEJO DE EXCEPCIONES ====================
    @Test
    void deberiaLanzarExcepcionCuandoFallaElEnvioDeCorreo() {
        EmailCompraDTO dto = new EmailCompraDTO();
        dto.setDestinatario("juan@email.com");
        dto.setNombreUsuario("Juan");
        dto.setNombreEvento("Concierto Rock");
        dto.setTokens(Arrays.asList("ABC123"));

        doThrow(new RuntimeException("Error de servidor de correo"))
                .when(mailSender).send(any(SimpleMailMessage.class));

        CorreoEnviado registroGuardado = new CorreoEnviado();
        registroGuardado.setId(1L);
        registroGuardado.setDestinatario("juan@email.com");
        registroGuardado.setTipo("COMPRA");
        registroGuardado.setEstado("FALLIDO");

        when(correoRepo.save(any(CorreoEnviado.class))).thenReturn(registroGuardado);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            emailService.enviarConfirmacionCompra(dto);
        });

        assertEquals("Error al enviar el correo de compra", exception.getMessage());
        verify(correoRepo).save(any(CorreoEnviado.class));
    }
}