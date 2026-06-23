package com.example.Tickets.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@Schema(
        description = "Estructura estandarizada para respuestas de error de la API",
        title = "Error Response"
)
public class ErrorResponse {

    @Schema(
            description = "Código de estado HTTP",
            example = "400",
            allowableValues = {"400", "404", "500"}
    )
    private int status;

    @Schema(
            description = "Marca de tiempo del error",
            example = "2026-06-23T10:00:00"
    )
    private LocalDateTime timestamp;

    @Schema(
            description = "Mensaje de error general",
            example = "Error de validación"
    )
    private String error;

    @Schema(
            description = "Mensaje detallado del error",
            example = "Verifique los campos e intente nuevamente."
    )
    private String message;

    @Schema(
            description = "Mapa de errores por campo (para validaciones)",
            example = "{\"nombre\": \"El nombre es obligatorio\"}"
    )
    private HashMap<String, String> errors;

    @Schema(
            description = "Enlaces HATEOAS para navegación",
            example = "{\"todos\": {\"href\": \"/api/v1/tickets\"}}"
    )
    private Map<String, Object> _links;

    public ErrorResponse(int status, String error, String message, HashMap<String, String> errors) {
        this.status = status;
        this.timestamp = LocalDateTime.now();
        this.error = error;
        this.message = message;
        this.errors = errors;
        this._links = new HashMap<>();
    }

    // ✅ Metodo para agregar enlaces HATEOAS
    public void addLink(String rel, String href) {
        Map<String, String> link = new HashMap<>();
        link.put("href", href);
        this._links.put(rel, link);
    }
}
