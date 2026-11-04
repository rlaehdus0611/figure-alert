package com.figure.figure.exception;

public class SubscriptionSaveException extends RuntimeException {
    public SubscriptionSaveException(RuntimeException cause) {
        super("구독 설정을 저장하지 못했습니다.", cause);
    }
}
