package com.figure.figure.exception;

public class NewsNotFoundException extends RuntimeException {
    public NewsNotFoundException() { super("공식 소식을 찾을 수 없습니다."); }
}
