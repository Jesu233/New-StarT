package com.example.Tickets.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Slf4j
@RestControllerAdvice
@Schema(description = "Manejador global de excepciones de la API")
public class GlobalHandlerException {

    private static final String BASE_URL = "http://localhost:8086/api/v1/tickets";

    // ============================================================
    //  ERROR DE VALIDACIÓN (@Valid)
    // ============================================================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleBadRequest(MethodArgumentNotValidException ex) {
        log.error("Error de validación: {}", ex.getMessage());

        HashMap<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        });

        // ✅ CONSTRUCTOR CORRECTO (4 parámetros)
        ErrorResponse errorResponse = new ErrorResponse(
                400,
                "Error de validación",
                "Verifique los campos e intente nuevamente.",
                errors
        );

        // ✅ AHORA addLink() EXISTE
        errorResponse.addLink("todos", BASE_URL);
        errorResponse.addLink("ayuda", "https://docs.starticket.cl/errores/validacion");

        return ResponseEntity.badRequest().body(errorResponse);
    }

    // ============================================================
    //  ERROR DE RECURSO NO ENCONTRADO
    // ============================================================
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex, WebRequest request) {
        log.error("Error en la aplicación: {}", ex.getMessage());

        HashMap<String, String> errors = new HashMap<>();
        errors.put("recurso", ex.getMessage());

        // ✅ CONSTRUCTOR CORRECTO (4 parámetros)
        ErrorResponse errorResponse = new ErrorResponse(
                404,
                "Recurso no encontrado",
                ex.getMessage(),
                errors
        );

        // ✅ AHORA addLink() EXISTE
        errorResponse.addLink("todos", BASE_URL);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}