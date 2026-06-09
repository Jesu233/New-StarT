package com.example.Tickets.exception;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;

@Data
public class ErrorResponse {

    private int status;
    private LocalDateTime timestamp;
    private String error;
    private HashMap<String, String> errors;


    public ErrorResponse(int status, String error, HashMap<String, String> errors) {
        this.status = status;
        this.error = error;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }
}
