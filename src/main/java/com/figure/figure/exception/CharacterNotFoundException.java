package com.figure.figure.exception;

public class CharacterNotFoundException extends RuntimeException {

    public CharacterNotFoundException() {
        super("캐릭터를 찾을 수 없습니다.");
    }
}