package com.figure.figure.exception;

public class ManufacturerNotFoundException extends RuntimeException {

    public ManufacturerNotFoundException() {
        super("제조사를 찾을 수 없습니다.");
    }
}