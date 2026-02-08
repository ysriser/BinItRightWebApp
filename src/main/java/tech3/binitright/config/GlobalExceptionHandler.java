package tech3.binitright.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleInternalServerError(final Exception ex) {
        final Map<String, String> errorResponse = new HashMap<>();
        // Internal logging for developers, generic message for the user/ZAP
        errorResponse.put("message", "An unexpected error occurred. Please try again later.");
        errorResponse.put("status", "500");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}