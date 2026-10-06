package com.figure.figure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import com.figure.figure.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ManufacturerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleManufacturerNotFound(ManufacturerNotFoundException e) {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage()
        );
    }

    @ExceptionHandler(CharacterNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleCharacterNotFound(CharacterNotFoundException e) {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage()
        );
    }

    @ExceptionHandler(ReleaseNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleReleaseNotFound(ReleaseNotFoundException e) {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage()
        );
    }

    @ExceptionHandler(FigureNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFigureNotFound(FigureNotFoundException e) {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage()
        );
    }

    @ExceptionHandler(DuplicateEmailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicateEmail(DuplicateEmailException e) {
        return new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                e.getMessage()
        );
    }

    @ExceptionHandler(InvalidLoginException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleInvalidLogin(InvalidLoginException e) {
        return new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                e.getMessage()
        );
    }

    @ExceptionHandler(ManufacturerConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleManufacturerConflict(
            ManufacturerConflictException e
    ) {
        return new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                e.getMessage()
        );
    }

    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleMemberNotFound(MemberNotFoundException e) {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage()
        );
    }

    @ExceptionHandler(CharacterSubscriptionAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleCharacterSubscriptionAlreadyExists(
            CharacterSubscriptionAlreadyExistsException e
    ) {
        return new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                e.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(
            MethodArgumentNotValidException e
    ) {
        String message = e.getBindingResult()
                .getAllErrors()
                .stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("입력값을 확인해 주세요.");

        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                message
        );
    }
}