package com.figure.figure.controller;

import com.figure.figure.dto.CharacterCreateRequest;
import com.figure.figure.dto.CharacterResponse;
import com.figure.figure.dto.FigureResponse;
import com.figure.figure.service.CharacterService;
import com.figure.figure.service.FigureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/characters")
@RequiredArgsConstructor
public class CharacterController {

    private final CharacterService characterService;
    private final FigureService figureService;

    // 캐릭터 생성
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CharacterResponse createCharacter(
            @Valid
            @RequestBody CharacterCreateRequest request
    ) {
        return characterService.createCharacter(request);
    }

    // id로 캐릭터 조회
    @GetMapping("/{id}")
    public CharacterResponse getCharacter(@PathVariable Long id) {
        return characterService.findCharacter(id);
    }

    // 전체 캐릭터 조회
    @GetMapping
    public List<CharacterResponse> getCharacters() {
        return characterService.findAllCharacters();
    }

    // 이름으로 캐릭터 검색
    @GetMapping("/search")
    public List<CharacterResponse> searchCharacters(
            @RequestParam String keyword
    ) {
        return characterService.searchCharacters(keyword);
    }

    // 특정 캐릭터의 피규어 목록 조회
    @GetMapping("/{id}/figures")
    public List<FigureResponse> getCharacterFigures(@PathVariable Long id) {
        return figureService.findFiguresByCharacter(id);
    }
}