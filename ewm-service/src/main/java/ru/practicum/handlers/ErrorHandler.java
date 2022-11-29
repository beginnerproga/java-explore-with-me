package ru.practicum.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.controllers.adminApi.AdminUsersController;
import ru.practicum.exceptions.SameEmailException;

import java.util.Map;

@Slf4j
@RestControllerAdvice(assignableTypes = {AdminUsersController.class})
public class ErrorHandler {

    @ExceptionHandler(value = {SameEmailException.class})
    public ResponseEntity<Map<String, String>> handleInternalServerErrorException(final RuntimeException e) {
        log.error("Server returned HttpCode 500. {}", e.getMessage(), e);
        return new ResponseEntity<>(
                Map.of("error", e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}