package ru.practicum.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.controllers.adminApi.AdminCategoriesController;
import ru.practicum.controllers.adminApi.AdminCompilationsController;
import ru.practicum.controllers.adminApi.AdminEventsController;
import ru.practicum.controllers.adminApi.AdminUsersController;
import ru.practicum.controllers.privateApi.PrivateEventsController;
import ru.practicum.controllers.privateApi.PrivateParticipationRequestsController;
import ru.practicum.controllers.publicApi.PublicCategoriesController;
import ru.practicum.controllers.publicApi.PublicCompilationsController;
import ru.practicum.controllers.publicApi.PublicEventsController;
import ru.practicum.exceptions.*;
import ru.practicum.exceptions.exception404.*;
import ru.practicum.models.ErrorResponse;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
@RestControllerAdvice(assignableTypes = {AdminUsersController.class, AdminCategoriesController.class, AdminCompilationsController.class,
        AdminEventsController.class, PrivateEventsController.class, PrivateParticipationRequestsController.class, PublicCategoriesController.class,
        PublicCompilationsController.class, PublicEventsController.class})
public class ErrorHandler {

    @ExceptionHandler(value = {RepeatedRequestException.class, SameEmailException.class,
            SameNameException.class, DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> handleInternalServerErrorException(final RuntimeException e) {
        log.error("409 {}", e.getMessage(), e);
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String sStackTrace = sw.toString();
        ErrorResponse error = new ErrorResponse(sStackTrace, e.getMessage(), HttpStatus.CONFLICT);

        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(value = {CategoryNotFoundException.class, UserNotFoundException.class, EventNotFoundException.class, RequestNotFoundException.class,
            CompilationNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleNotFoundException(final RuntimeException e) {
        log.error("404 {}", e.getMessage(), e);
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String sStackTrace = sw.toString();
        ErrorResponse error = new ErrorResponse(sStackTrace, e.getMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(value = {InitiatorSendRequestException.class,
            UserNotAccessException.class, LikeAlreadySetException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorResponse> handleForbiddenException(final RuntimeException e) {
        log.error("403 {}", e.getMessage(), e);
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String sStackTrace = sw.toString();
        ErrorResponse error = new ErrorResponse(sStackTrace, e.getMessage(), HttpStatus.FORBIDDEN);

        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler({ValidationException.class,
            MissingServletRequestParameterException.class,
            HttpMessageNotReadableException.class,
            ConstraintViolationException.class,
            InappropriateStateForAction.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationException(Exception e) {
        log.error("400 {}", e.getMessage(), e);
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String sStackTrace = sw.toString();
        ErrorResponse error = new ErrorResponse(sStackTrace, e.getMessage(), HttpStatus.BAD_REQUEST);

        return ResponseEntity.status(error.getStatus()).body(error);
    }


}