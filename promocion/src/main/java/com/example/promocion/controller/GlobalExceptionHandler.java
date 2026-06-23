package com.example.promocion.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ============================================================
    //  MANEJAR ERRORES DE VALIDACIÓN (@Valid)
    // ============================================================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {

        log.error("Error de validación: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("error", "Error de validación");
        errorResponse.put("message", "Datos inválidos");
        errorResponse.put("errors", errors);
        errorResponse.put("path", request.getDescription(false).replace("uri=", ""));

        // 🔗 Agregar enlace a la colección
        errorResponse.put("_links", Map.of(
                "todas", Map.of("href", linkTo(PromoController.class)
                        .slash("/api/v1/promociones")
                        .withSelfRel()
                        .getHref())
        ));

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // ============================================================
    //  MANEJAR RUNTIME EXCEPTION
    // ============================================================
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(
            RuntimeException ex, WebRequest request) {

        log.error("Error en la aplicación: {}", ex.getMessage());

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", HttpStatus.NOT_FOUND.value());
        errorResponse.put("error", "Recurso no encontrado");
        errorResponse.put("message", ex.getMessage());
        errorResponse.put("path", request.getDescription(false).replace("uri=", ""));

        errorResponse.put("_links", Map.of(
                "todas", Map.of("href", linkTo(PromoController.class)
                        .slash("/api/v1/promociones")
                        .withSelfRel()
                        .getHref())
        ));

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}