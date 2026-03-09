package com.nilsson.latentnexus.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Global exception handler for the Latent Nexus application.
 * <p>
 * This class, annotated with {@code @ControllerAdvice}, provides centralized exception
 * handling across all {@code @RestController} classes. It intercepts exceptions
 * thrown during request processing and returns standardized, client-friendly
 * error responses.
 * </p>
 * <p>
 * It includes specific handlers for custom exceptions like {@link AssetNotFoundException},
 * mapping them to appropriate HTTP status codes (e.g., 404 Not Found), and a generic
 * handler for all other unhandled exceptions, ensuring a consistent 500 Internal Server Error
 * response. Each error response includes a timestamp, HTTP status, error message, and the
 * request path for better debugging and client understanding.
 * </p>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AssetNotFoundException.class)
    public ResponseEntity<Object> handleAssetNotFoundException(
            AssetNotFoundException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(
            Exception ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", "An unexpected error occurred");
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
