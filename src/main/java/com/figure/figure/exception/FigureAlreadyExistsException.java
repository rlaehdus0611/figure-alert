package com.figure.figure.exception;

public class FigureAlreadyExistsException extends RuntimeException {

    public FigureAlreadyExistsException() {
        super("이미 존재하는 피규어 ID입니다.");
    }
}