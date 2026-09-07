package com.figure.figure.exception;

public class FigureNotFoundException extends RuntimeException {

    public FigureNotFoundException() {
        super("피규어를 찾을 수 없습니다.");
    }
}