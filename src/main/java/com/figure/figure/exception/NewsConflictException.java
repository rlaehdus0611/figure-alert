package com.figure.figure.exception;

public class NewsConflictException extends RuntimeException {
    public NewsConflictException() { super("같은 발행 키로 다른 내용을 발행할 수 없습니다."); }
}
