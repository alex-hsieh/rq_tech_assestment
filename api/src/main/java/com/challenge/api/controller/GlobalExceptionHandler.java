package com.challenge.api.controller;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

/**
 * Centralizes exception handling for all REST controllers, ensuring consistent
 * error response structure across the API.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation failures triggered by {@code @Valid} on request bodies.
     * Collects all field-level errors into a list so callers receive the full
     * picture in a single response rather than one error at a time.
     *
     * @param ex the validation exception raised by Spring MVC
     * @return 400 Bad Request with a structured error body
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList();

        return ResponseEntity.badRequest().body(buildBody(HttpStatus.BAD_REQUEST, "Validation failed", errors));
    }

    /**
     * Handles {@link ResponseStatusException} thrown explicitly by controllers,
     * such as the 404 raised when an employee UUID is not found.
     *
     * @param ex the status exception raised by a controller
     * @return response matching the exception's HTTP status with a structured error body
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        return ResponseEntity.status(status).body(buildBody(status, ex.getReason(), List.of()));
    }

    /**
     * Catch-all for any unexpected exceptions, preventing raw stack traces from
     * leaking to API consumers.
     *
     * @param ex the unhandled exception
     * @return 500 Internal Server Error with a generic error body
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return ResponseEntity.internalServerError()
                .body(buildBody(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", List.of()));
    }

    private Map<String, Object> buildBody(HttpStatus status, String message, List<String> errors) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        if (!errors.isEmpty()) {
            body.put("errors", errors);
        }
        return body;
    }
}
