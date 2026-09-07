package com.figure.figure.exception;

public class ReleaseNotFoundException extends RuntimeException {

    public ReleaseNotFoundException() {
        super("출시 정보를 찾을 수 없습니다.");
    }
}
