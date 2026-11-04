package com.figure.figure.exception;

import com.figure.figure.dto.CharacterSubscriptionRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;


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
                "CHARACTER_NOT_FOUND",
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

        if (e.getBindingResult().getTarget() instanceof CharacterSubscriptionRequest) {
            List<String> order = List.of("announcementEnabled", "preorderEnabled", "deadlineEnabled",
                    "rereleaseEnabled", "prizeEnabled");
            message = e.getBindingResult().getFieldErrors().stream()
                    .sorted(Comparator.comparingInt(error -> order.indexOf(error.getField())))
                    .map(error -> error.getDefaultMessage()).collect(Collectors.joining(" "));
        }

        String code =
                e.getBindingResult().getTarget()
                        instanceof CharacterSubscriptionRequest
                        ? "INVALID_SUBSCRIPTION_SETTINGS"
                        : "REQUEST_FAILED";

        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                code,
                message
        );
    }

    @ExceptionHandler(CharacterSubscriptionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse subscriptionNotFound(CharacterSubscriptionNotFoundException e) {
        return new ErrorResponse(404, "SUBSCRIPTION_NOT_FOUND", e.getMessage());
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse invalidParameter(HandlerMethodValidationException e) {
        for (var result : e.getBeanResults()) {
            if (result.getArgument() instanceof CharacterSubscriptionRequest) {
                List<String> order = List.of("announcementEnabled", "preorderEnabled", "deadlineEnabled",
                        "rereleaseEnabled", "prizeEnabled");
                String message = result.getFieldErrors().stream()
                        .sorted(Comparator.comparingInt(error -> order.indexOf(error.getField())))
                        .map(error -> error.getDefaultMessage()).collect(Collectors.joining(" "));
                return new ErrorResponse(400, "INVALID_SUBSCRIPTION_SETTINGS", message);
            }
        }
        return new ErrorResponse(400, "INVALID_REQUEST_PARAMETER", "요청 경로와 파라미터를 확인해 주세요.");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse invalidJson(HttpMessageNotReadableException e) {
        return new ErrorResponse(400, "INVALID_REQUEST_BODY", "요청 JSON의 형식과 자료형을 확인해 주세요.");
    }

    @ExceptionHandler(NotificationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notificationNotFound(NotificationNotFoundException e) {
        return new ErrorResponse(404, "NOTIFICATION_NOT_FOUND", e.getMessage());
    }

    @ExceptionHandler(NewsNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse newsNotFound(NewsNotFoundException e) {
        return new ErrorResponse(404, "NEWS_NOT_FOUND", e.getMessage());
    }

    @ExceptionHandler(NewsConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse newsConflict(NewsConflictException e) {
        return new ErrorResponse(409, "NEWS_CONFLICT", e.getMessage());
    }
}
