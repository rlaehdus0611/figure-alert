package com.figure.figure.exception;

public class CharacterSubscriptionNotFoundException extends RuntimeException {
    public CharacterSubscriptionNotFoundException() {
        super("해당 캐릭터의 구독이 없습니다.");
    }
}
