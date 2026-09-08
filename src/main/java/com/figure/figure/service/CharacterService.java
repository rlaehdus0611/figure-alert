package com.figure.figure.service;

import com.figure.figure.dto.CharacterCreateRequest;
import com.figure.figure.dto.CharacterResponse;
import com.figure.figure.exception.CharacterNotFoundException;
import com.figure.figure.model.Character;
import com.figure.figure.repository.CharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CharacterService {

    private final CharacterRepository characterRepository;

    // 캐릭터 생성
    @Transactional
    public CharacterResponse createCharacter(CharacterCreateRequest request) {

        Character character = new Character(
                request.getName(),
                request.getSeries()
        );

        Character savedCharacter = characterRepository.save(character);

        return toResponse(savedCharacter);
    }

    // id로 캐릭터 조회
    public CharacterResponse findCharacter(Long id) {

        Character character = characterRepository.findById(id)
                .orElseThrow(CharacterNotFoundException::new);

        return toResponse(character);
    }

    //  DTO 변환
    private CharacterResponse toResponse(Character character) {
        return new CharacterResponse(
                character.getId(),
                character.getName(),
                character.getSeries()
        );
    }

    // 전체 캐릭터 조회
    public List<CharacterResponse> findAllCharacters() {
        return characterRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // 이름으로 캐릭터 검색
    public List<CharacterResponse> searchCharacters(String keyword) {
        return characterRepository.findByNameContaining(keyword)
                .stream()
                .map(this::toResponse)
                .toList();
    }
}