package com.figure.figure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.figure.figure.dto.ErrorResponse;

@RestControllerAdvice // 모든 Controller에서 발생한 예외를 여기서 처리
public class GlobalExceptionHandler {
    @ExceptionHandler(ManufacturerNotFoundException.class) // 제조사를 찾을 수 없을 떄
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleManufacturerNotFound(ManufacturerNotFoundException e) {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage()
        );
    }

    @ExceptionHandler(CharacterNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // 캐릭터를 찾을 수 없을때
    public ErrorResponse handleCharacterNotFound(CharacterNotFoundException e) {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage()
        );
    }

    @ExceptionHandler(ReleaseNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // 출시 정보를 찾을 수 없을 때
    public ErrorResponse handleReleaseNotFound(ReleaseNotFoundException e) {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage()
        );
    }

    @ExceptionHandler(FigureNotFoundException.class) // 피규어를 찾을 수 없을 때
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFigureNotFound(FigureNotFoundException e) {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage()
        );
    }
}