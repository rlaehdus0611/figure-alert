package com.figure.figure.controller;

import com.figure.figure.model.Character;
import com.figure.figure.service.CharacterService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController // HTTP 요청을 처리하고 결과를 JSON으로 응답
@RequestMapping("/characters") // 이 Controller의 공통 URL
public class CharacterController {

    private final CharacterService characterService;

    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @PostMapping // POST /characters 요청을 이 메서드와 연결
    @ResponseStatus(HttpStatus.CREATED) // 정상 등록 시 HTTP 201 Created 응답
    public Character createCharacter(
            @Valid // Character에 설정한 검증 조건을 검사
            @RequestBody // 요청 JSON을 Character 객체로 변환
            Character character
    ) {
        return characterService.createCharacter(character);
    }
}