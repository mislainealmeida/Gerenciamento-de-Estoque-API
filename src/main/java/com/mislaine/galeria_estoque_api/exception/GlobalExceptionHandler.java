package com.mislaine.galeria_estoque_api.exception;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Hidden
@RestControllerAdvice // Esta anotação diz ao Spring que esta classe vigia todos os Controllers
public class GlobalExceptionHandler {

    // 1. Captura os erros de validação. O @Valid do Controller manda erro para cá
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    // 2. Captura erros de negócios (Ex: "Produto não encontrado"). Toda vez que der um throw new RuntimeException, ele cai aqui
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDetails> handleRuntimeException(RuntimeException ex) {
        ErrorDetails details = new ErrorDetails(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(details);
    }

    // 3. Captura erros de duplicidade ou erro de vínculo
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDetails> handleDataIntegrity(DataIntegrityViolationException ex) {
        String message = "Erro de integridade: Este registro já existe ou está vinculado a outro dado.";

        if (ex.getMessage().contains("fk")) {
            message = "Não é possível excluir: este item possui outros dados vinculados a ele.";
        }

        ErrorDetails details = new ErrorDetails(LocalDateTime.now(), HttpStatus.CONFLICT.value(), message);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(details);
    }

    // 4. Captura de Json inválido (Ex: Texto onde deveria ser número)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDetails> handleInvalidJson(HttpMessageNotReadableException ex) {
        ErrorDetails details = new ErrorDetails(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "O corpo da requisição possui erros de sintaxe ou tipos de dados inválidos."
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(details);
    }

    // 5. Captura qualquer erro desconhecido
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGenericException(Exception ex) {
        ErrorDetails details = new ErrorDetails(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ocorreu um erro interno inesperado. Por favor, contate o suporte técnico."
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(details);
    }


    // Classe auxiliar para formatar a resposta do erro de negócio
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ErrorDetails {
        private LocalDateTime timestamp;
        private int status;
        private String message;
    }
}