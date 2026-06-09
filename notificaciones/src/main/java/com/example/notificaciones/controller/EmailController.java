package com.example.notificaciones.controller;

import com.example.notificaciones.dto.EmailBienvenidaDTO;
import com.example.notificaciones.dto.EmailCompraDTO;
import com.example.notificaciones.dto.EmailLogoutDTO;
import com.example.notificaciones.dto.EmailRecuperacionDTO;
import com.example.notificaciones.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/email")
@RequiredArgsConstructor
@Slf4j
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/compra")
    public ResponseEntity<?> confirmarCompra(@RequestBody EmailCompraDTO dto) {
        log.info("POST /email/compra - destinatario: {}", dto.getDestinatario());
        emailService.enviarConfirmacionCompra(dto);
        return ResponseEntity.ok(Map.of("mensaje", "Correo de compra enviado"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> avisarLogout(@RequestBody EmailLogoutDTO dto) {
        log.info("POST /email/logout - destinatario: {}", dto.getDestinatario());
        emailService.enviarAvisoLogout(dto);
        return ResponseEntity.ok(Map.of("mensaje", "Correo de logout enviado"));
    }

    @PostMapping("/bienvenida")
    public ResponseEntity<?> enviarBienvenida(@RequestBody EmailBienvenidaDTO dto) {
        log.info("POST /email/bienvenida - destinatario: {}", dto.getDestinatario());
        emailService.enviarBienvenida(dto);
        return ResponseEntity.ok(Map.of("mensaje", "Correo de bienvenida enviado"));
    }

    @PostMapping("/recuperacion")
    public ResponseEntity<?> recuperarPassword(@RequestBody EmailRecuperacionDTO dto) {
        log.info("POST /email/recuperacion - destinatario: {}", dto.getDestinatario());
        emailService.enviarRecuperacion(dto);
        return ResponseEntity.ok(Map.of("mensaje", "Correo de recuperación enviado"));
    }
}


