package com.example.taskmng.exception.handler;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.example.taskmng.exception.custom.ResourceNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Gestion des ressources non trouvées (404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleNotFound(ResourceNotFoundException ex, WebRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Ressource non trouvée");
        problem.setProperty("timestamp", System.currentTimeMillis());
        problem.setProperty("path", request.getDescription(false).replace("uri=", ""));
        return problem;
    }

    // Gestion des erreurs de validation (@Valid) (400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex, WebRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Erreur de validation");
        problem.setDetail("Les données fournies ne sont pas valides");

        // Collecte des erreurs par champ
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(),
                        fieldError -> fieldError.getDefaultMessage(),
                        (existing, replacement) -> existing + "; " + replacement
                ));
        problem.setProperty("errors", errors);
        problem.setProperty("timestamp", System.currentTimeMillis());
        problem.setProperty("path", request.getDescription(false).replace("uri=", ""));
        return problem;
    }

    // Gestion des exceptions génériques non anticipées (500)
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex, WebRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problem.setTitle("Erreur interne du serveur");
        problem.setDetail("Une erreur inattendue s'est produite. Veuillez réessayer plus tard.");
        problem.setProperty("timestamp", System.currentTimeMillis());
        problem.setProperty("path", request.getDescription(false).replace("uri=", ""));
        // En développement, vous pouvez ajouter la stack trace : problem.setProperty("exception", ex.getMessage());
        return problem;
    }
}