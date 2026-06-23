package com.example.notificaciones.service;

import com.example.notificaciones.dto.EmailBienvenidaDTO;
import com.example.notificaciones.dto.EmailCompraDTO;
import com.example.notificaciones.dto.EmailLogoutDTO;
import com.example.notificaciones.dto.EmailRecuperacionDTO;
import com.example.notificaciones.model.CorreoEnviado;
import com.example.notificaciones.repository.CorreoEnviadoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final CorreoEnviadoRepository correoRepo;

    // 🔄 CAMBIAR DE void A CorreoEnviado
    public CorreoEnviado enviarConfirmacionCompra(EmailCompraDTO dto) {
        log.info("Enviando confirmación de compra a: {}", dto.getDestinatario());
        CorreoEnviado registro = new CorreoEnviado();
        registro.setDestinatario(dto.getDestinatario());
        registro.setTipo("COMPRA");
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setTo(dto.getDestinatario());
            mensaje.setSubject("Confirmación de compra - " + dto.getNombreEvento());

            StringBuilder cuerpo = new StringBuilder();
            cuerpo.append("Hola ").append(dto.getNombreUsuario()).append(",\n\n");
            cuerpo.append("Tu compra fue exitosa. Aquí están tus entradas para ")
                    .append(dto.getNombreEvento()).append("\n\n");
            for (String token : dto.getTokens()) {
                cuerpo.append("Token: ").append(token).append("\n\n");
            }
            cuerpo.append("\nPresenta estos códigos en el evento.");
            cuerpo.append("\n\nGracias por tu compra.");

            mensaje.setText(cuerpo.toString());
            mailSender.send(mensaje);
            registro.setEstado("ENVIADO");
            log.info("Confirmación de compra enviada correctamente a: {}", dto.getDestinatario());

        } catch (Exception e) {
            registro.setEstado("FALLIDO");
            log.error("Error al enviar confirmación de compra a {}: {}", dto.getDestinatario(), e.getMessage());
            throw new RuntimeException("Error al enviar el correo de compra");

        } finally {
            correoRepo.save(registro);
        }

        return registro; // 🔄 RETORNAR EL REGISTRO
    }

    // 🔄 CAMBIAR DE void A CorreoEnviado
    public CorreoEnviado enviarAvisoLogout(EmailLogoutDTO dto) {
        CorreoEnviado registro = new CorreoEnviado();
        registro.setDestinatario(dto.getDestinatario());
        registro.setTipo("LOGOUT");
        log.info("Enviando aviso de logout a: {}", dto.getDestinatario());
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setTo(dto.getDestinatario());
            mensaje.setSubject("Tu sesión fue cerrada por seguridad");

            String cuerpo = "Hola " + dto.getNombreUsuario() + ",\n\n"
                    + "Tu sesión ha sido cerrada por motivos de seguridad. \n"
                    + "Si fuiste tú, puedes ignorar este mensaje. \n"
                    + "Si no reconoces esta acción, cambia tu contraseña inmediatamente. \n\n"
                    + "Equipo de seguridad.";
            mensaje.setText(cuerpo);
            mailSender.send(mensaje);
            registro.setEstado("ENVIADO");
            log.info("Aviso de logout enviado correctamente a: {}", dto.getDestinatario());
        } catch (Exception e) {
            registro.setEstado("FALLIDO");
            log.error("Error al enviar aviso de logout a {} : {}", dto.getDestinatario(), e.getMessage());
            throw new RuntimeException("Error al enviar el correo de logout");

        } finally {
            correoRepo.save(registro);
        }

        return registro; // 🔄 RETORNAR EL REGISTRO
    }

    // 🔄 CAMBIAR DE void A CorreoEnviado
    public CorreoEnviado enviarBienvenida(EmailBienvenidaDTO dto) {
        CorreoEnviado registro = new CorreoEnviado();
        registro.setDestinatario(dto.getDestinatario());
        registro.setTipo("BIENVENIDA");
        log.info("Enviando correo de bienvenida a: {}", dto.getDestinatario());
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setTo(dto.getDestinatario());
            mensaje.setSubject("¡Bienvenido/a!");

            String cuerpo = "Hola " + dto.getNombreUsuario() + ",\n\n"
                    + "Tu cuenta ha sido creada exitosamente.\n"
                    + "Ya puedes iniciar sesión y comenzar a comprar entradas.\n\n"
                    + "¡Bienvenido/a!";

            mensaje.setText(cuerpo);
            mailSender.send(mensaje);
            registro.setEstado("ENVIADO");
            log.info("Correo de bienvenida enviado correctamente a: {}", dto.getDestinatario());
        } catch (Exception e) {
            registro.setEstado("FALLIDO");
            log.error("Error al enviar bienvenida a {}: {}", dto.getDestinatario(), e.getMessage());
            throw new RuntimeException("Error al enviar el correo de bienvenida");
        } finally {
            correoRepo.save(registro);
        }

        return registro; // 🔄 RETORNAR EL REGISTRO
    }

    // 🔄 CAMBIAR DE void A CorreoEnviado
    public CorreoEnviado enviarRecuperacion(EmailRecuperacionDTO dto) {
        CorreoEnviado registro = new CorreoEnviado();
        registro.setDestinatario(dto.getDestinatario());
        registro.setTipo("RECUPERACION");
        log.info("Enviando correo de recuperación a: {}", dto.getDestinatario());
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setTo(dto.getDestinatario());
            mensaje.setSubject("Recuperación de contraseña");

            StringBuilder cuerpo = new StringBuilder();
            cuerpo.append("Hola ").append(dto.getNombreUsuario()).append(",\n\n");
            cuerpo.append("Recibimos una solicitud para restablecer tu contraseña.\n");
            cuerpo.append("Haz clic en el siguiente enlace:\n\n");
            cuerpo.append(dto.getLinkRecuperacion()).append("\n\n");
            cuerpo.append("Si no solicitaste esto, ignora este mensaje.\n");
            cuerpo.append("El enlace expira en 30 minutos.");

            mensaje.setText(cuerpo.toString());
            mailSender.send(mensaje);
            registro.setEstado("ENVIADO");
            log.info("Correo de recuperación enviado correctamente a: {}", dto.getDestinatario());
        } catch (Exception e) {
            registro.setEstado("FALLIDO");
            log.error("Error al enviar recuperación a {}: {}", dto.getDestinatario(), e.getMessage());
            throw new RuntimeException("Error al enviar el correo de recuperación");
        } finally {
            correoRepo.save(registro);
        }

        return registro; // 🔄 RETORNAR EL REGISTRO
    }
}