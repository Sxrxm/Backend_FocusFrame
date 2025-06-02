package com.example.security.utils;

import com.example.security.dto.ErrorResponse;
import com.example.security.dto.ValidationError;
import com.example.security.exception.BadRequestException;
import com.example.security.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex, WebRequest request) {

        List<ValidationError> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ValidationError(
                        fieldError.getField(),
                        messageSource.getMessage(fieldError, LocaleContextHolder.getLocale()),
                        fieldError.getCode()
                ))
                .collect(Collectors.toList());

        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                "validation.error",
                messageSource.getMessage("validation.error", null, LocaleContextHolder.getLocale()),
                request.getDescription(false),
                errors
        );

        return ResponseEntity.status(response.getStatus()).body(response);
    }


    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(
            BadRequestException ex, WebRequest request) {

        Locale locale = LocaleContextHolder.getLocale();

        String localizedMessage = messageSource.getMessage(
                ex.getMessageKey(), ex.getArgs(), "Error desconocido", locale
        );

        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessageKey(),
                localizedMessage,
                request.getDescription(false)
        );
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(
            EntityNotFoundException ex, WebRequest request) {

        Locale locale = LocaleContextHolder.getLocale();

        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessageKey(),
                messageSource.getMessage(ex.getMessageKey(), ex.getArgs(), "Error desconocido", locale),
                request.getDescription(false)
        );

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex, WebRequest request) {

        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                "illegal.argument",
                ex.getMessage(),
                request.getDescription(false)
        );

        return ResponseEntity.status(response.getStatus()).body(response);
    }
}