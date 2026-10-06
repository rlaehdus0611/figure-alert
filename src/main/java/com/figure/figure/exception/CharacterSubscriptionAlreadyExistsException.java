package com.figure.figure.exception;

public class CharacterSubscriptionAlreadyExistsException
        extends RuntimeException {

    public CharacterSubscriptionAlreadyExistsException() {
        super("이미 구독한 캐릭터입니다.");
    }
}