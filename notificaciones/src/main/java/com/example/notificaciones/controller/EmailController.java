package com.example.notificaciones.controller;

import com.example.notificaciones.dto.EmailBienvenidaDTO;
import com.example.notificaciones.dto.EmailCompraDTO;
import com.example.notificaciones.dto.EmailLogoutDTO;
import com.example.notificaciones.dto.EmailRecuperacionDTO;
import com.example.notificaciones.model.CorreoEnviado;
import com.example.notificaciones.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v1/email")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Notificaciones por Email")
@SecurityRequirement(name = "bearerAuth")
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/compra")
    @Operation(summary = "Enviar confirmación de compra")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Correo de compra enviado correctamente."),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (email incorrecto, campos vacíos, etc.)"),
            @ApiResponse(responseCode = "401", description = "No autenticado - Se requiere token de acceso."),
            @ApiResponse(responseCode = "403", description = "Sin permisos - El usuario no tiene autorización."),
            @ApiResponse(responseCode = "500", description = "Error interno al enviar el correo.")
    })
    public ResponseEntity<EntityModel<Map<String, Object>>> confirmarCompra(@RequestBody EmailCompraDTO dto) {
        log.info("POST /email/compra - destinatario: {}", dto.getDestinatario());

        CorreoEnviado correo = emailService.enviarConfirmacionCompra(dto);

        EntityModel<Map<String, Object>> response = EntityModel.of(
                Map.of(
                        "mensaje", "Correo de compra enviado exitosamente",
                        "idCorreo", correo.getId(),
                        "destinatario", correo.getDestinatario(),
                        "estado", correo.getEstado(),
                        "fechaEnvio", correo.getFechaEnvio().toString(),
                        "tipo", correo.getTipo()
                )
        );

        response.add(linkTo(methodOn(CorreoEnviadoController.class)
                .findById(correo.getId())).withSelfRel());

        response.add(linkTo(methodOn(CorreoEnviadoController.class)
                .findAll()).withRel("historialCompleto"));

        response.add(linkTo(methodOn(CorreoEnviadoController.class)
                .findByDestinatario(dto.getDestinatario()))
                .withRel("historialPorUsuario"));

        response.add(linkTo(methodOn(EmailController.class)
                .enviarBienvenida(new EmailBienvenidaDTO()))
                .withRel("enviarBienvenida"));

        response.add(linkTo(methodOn(EmailController.class)
                .avisarLogout(new EmailLogoutDTO()))
                .withRel("enviarLogout"));

        response.add(linkTo(methodOn(EmailController.class)
                .recuperarPassword(new EmailRecuperacionDTO()))
                .withRel("enviarRecuperacion"));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "Notificar cierre de sesión")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Correo de logout enviado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "500", description = "Error interno al enviar el correo")
    })
    public ResponseEntity<EntityModel<Map<String, Object>>> avisarLogout(@RequestBody EmailLogoutDTO dto) {
        log.info("POST /email/logout - destinatario: {}", dto.getDestinatario());

        CorreoEnviado correo = emailService.enviarAvisoLogout(dto);

        EntityModel<Map<String, Object>> response = EntityModel.of(
                Map.of(
                        "mensaje", "Correo de logout enviado exitosamente",
                        "idCorreo", correo.getId(),
                        "destinatario", correo.getDestinatario(),
                        "estado", correo.getEstado(),
                        "fechaEnvio", correo.getFechaEnvio().toString(),
                        "tipo", correo.getTipo()
                )
        );

        response.add(linkTo(methodOn(CorreoEnviadoController.class)
                .findById(correo.getId())).withSelfRel());

        response.add(linkTo(methodOn(CorreoEnviadoController.class)
                .findByDestinatario(dto.getDestinatario()))
                .withRel("historialPorUsuario"));

        response.add(linkTo(methodOn(EmailController.class)
                .confirmarCompra(new EmailCompraDTO()))
                .withRel("enviarCompra"));

        response.add(linkTo(methodOn(EmailController.class)
                .enviarBienvenida(new EmailBienvenidaDTO()))
                .withRel("enviarBienvenida"));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/bienvenida")
    @Operation(summary = "Enviar correo de bienvenida")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Correo de bienvenida enviado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "500", description = "Error interno al enviar el correo")
    })
    public ResponseEntity<EntityModel<Map<String, Object>>> enviarBienvenida(@RequestBody EmailBienvenidaDTO dto) {
        log.info("POST /email/bienvenida - destinatario: {}", dto.getDestinatario());

        CorreoEnviado correo = emailService.enviarBienvenida(dto);

        EntityModel<Map<String, Object>> response = EntityModel.of(
                Map.of(
                        "mensaje", "Correo de bienvenida enviado exitosamente",
                        "idCorreo", correo.getId(),
                        "destinatario", correo.getDestinatario(),
                        "estado", correo.getEstado(),
                        "fechaEnvio", correo.getFechaEnvio().toString(),
                        "tipo", correo.getTipo()
                )
        );

        response.add(linkTo(methodOn(CorreoEnviadoController.class)
                .findById(correo.getId())).withSelfRel());

        response.add(linkTo(methodOn(CorreoEnviadoController.class)
                .findAll()).withRel("historialCompleto"));

        response.add(linkTo(methodOn(EmailController.class)
                .confirmarCompra(new EmailCompraDTO()))
                .withRel("enviarCompra"));

        response.add(linkTo(methodOn(EmailController.class)
                .avisarLogout(new EmailLogoutDTO()))
                .withRel("enviarLogout"));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/recuperacion")
    @Operation(summary = "Enviar correo de recuperación de contraseña")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Correo de recuperación enviado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "500", description = "Error interno al enviar el correo")
    })
    public ResponseEntity<EntityModel<Map<String, Object>>> recuperarPassword(@RequestBody EmailRecuperacionDTO dto) {
        log.info("POST /email/recuperacion - destinatario: {}", dto.getDestinatario());

        CorreoEnviado correo = emailService.enviarRecuperacion(dto);

        EntityModel<Map<String, Object>> response = EntityModel.of(
                Map.of(
                        "mensaje", "Correo de recuperación enviado exitosamente",
                        "idCorreo", correo.getId(),
                        "destinatario", correo.getDestinatario(),
                        "estado", correo.getEstado(),
                        "fechaEnvio", correo.getFechaEnvio().toString(),
                        "tipo", correo.getTipo()
                )
        );

        response.add(linkTo(methodOn(CorreoEnviadoController.class)
                .findById(correo.getId())).withSelfRel());

        response.add(linkTo(methodOn(CorreoEnviadoController.class)
                .findAll()).withRel("historialCompleto"));

        response.add(linkTo(methodOn(EmailController.class)
                .confirmarCompra(new EmailCompraDTO()))
                .withRel("enviarCompra"));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

