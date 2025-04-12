package com.advocacia.leads.infrastructure.exceptions;

import io.jsonwebtoken.JwtException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Captura erros de validação dos DTOs
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    // Captura erros de integridade, como campos que excedem o tamanho permitido
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage();

        if (message.contains("value too long")) {
            message = "Um dos campos ultrapassou o tamanho máximo permitido.";
        } else {
            message = "Erro de integridade nos dados. Verifique os valores enviados.";
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    // Captura erros de token JWT inválido ou expirado
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<?> handleJwtException(JwtException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("mensagem", "Token inválido ou expirado."));
    }

    // Captura quaisquer outras exceções e retorna um erro genérico
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralErrors(Exception ex) {
        // Log completo do erro é registrado internamente para análise (não exposto ao cliente)
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Ocorreu um erro interno. Por favor, tente novamente mais tarde.");
    }
}
