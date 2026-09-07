package com.figure.figure.service;

import com.figure.figure.model.Character;
import com.figure.figure.repository.CharacterRepository;
import org.springframework.stereotype.Service;

@Service // 비즈니스 로직을 처리하는 Service 객체로 Spring이 관리
public class CharacterService {

    private final CharacterRepository characterRepository;

    public CharacterService(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    public Character createCharacter(Character character) {
        return characterRepository.save(character);
    }
}