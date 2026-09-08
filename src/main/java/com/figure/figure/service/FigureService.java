package com.figure.figure.service;

import com.figure.figure.dto.FigureCreateRequest;
import com.figure.figure.dto.FigureResponse;
import com.figure.figure.exception.CharacterNotFoundException;
import com.figure.figure.exception.FigureNotFoundException;
import com.figure.figure.exception.ManufacturerNotFoundException;
import com.figure.figure.model.Character;
import com.figure.figure.model.Figure;
import com.figure.figure.model.FigureCharacter;
import com.figure.figure.model.Manufacturer;
import com.figure.figure.repository.CharacterRepository;
import com.figure.figure.repository.FigureCharacterRepository;
import com.figure.figure.repository.FigureRepository;
import com.figure.figure.repository.ManufacturerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FigureService {

    private final FigureRepository figureRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final CharacterRepository characterRepository;
    private final FigureCharacterRepository figureCharacterRepository;

    // id로 피규어 조회
    public FigureResponse findFigure(Long id) {
        Figure figure = figureRepository.findById(id)
                .orElseThrow(FigureNotFoundException::new);

        return toResponse(figure);
    }

    // 모든 피규어 조회
    public List<FigureResponse> findAllFigures() {
        return figureRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // 특정 캐릭터의 피규어 목록 조회
    public List<FigureResponse> findFiguresByCharacter(Long characterId) {

        if (!characterRepository.existsById(characterId)) {
            throw new CharacterNotFoundException();
        }

        return figureCharacterRepository.findByCharacterId(characterId)
                .stream()
                .map(FigureCharacter::getFigure)
                .map(this::toResponse)
                .toList();
    }

    // 이름 또는 캐릭터 이름으로 검색
    public List<FigureResponse> searchFiguresByName(String keyword) {

        // 피규어 이름으로 검색
        List<Figure> figures = figureRepository.findByNameContaining(keyword);

        // 캐릭터 이름으로 검색
        List<Character> characters = characterRepository.findByNameContaining(keyword);

        // 검색된 캐릭터와 연결된 피규어 추가
        for (Character character : characters) {
            List<Figure> characterFigures = figureCharacterRepository
                    .findByCharacterId(character.getId())
                    .stream()
                    .map(FigureCharacter::getFigure)
                    .toList();

            figures.addAll(characterFigures);
        }

        return figures.stream()
                .distinct()
                .map(this::toResponse)
                .toList();
    }

    // 새로운 피규어 생성
    @Transactional
    public FigureResponse createFigure(FigureCreateRequest request) {

        Manufacturer manufacturer = manufacturerRepository
                .findById(request.getManufacturerId())
                .orElseThrow(ManufacturerNotFoundException::new);

        List<Character> characters = characterRepository
                .findAllById(request.getCharacterIds());

        if (characters.size() != request.getCharacterIds().size()) {
            throw new CharacterNotFoundException();
        }

        Figure figure = new Figure(
                request.getName(),
                manufacturer
        );

        Figure savedFigure = figureRepository.save(figure);

        List<FigureCharacter> figureCharacters = characters.stream()
                .map(character -> new FigureCharacter(savedFigure, character))
                .toList();

        figureCharacterRepository.saveAll(figureCharacters);

        return toResponse(savedFigure);
    }

    // 피규어 삭제
    @Transactional
    public void deleteFigure(Long id) {
        if (!figureRepository.existsById(id)) {
            throw new FigureNotFoundException();
        }

        figureCharacterRepository.deleteByFigureId(id);
        figureRepository.deleteById(id);
    }

    // Figure Entity -> FigureResponse DTO 변환
    private FigureResponse toResponse(Figure figure) {

        List<String> characterNames = figureCharacterRepository
                .findByFigureId(figure.getId())
                .stream()
                .map(figureCharacter -> figureCharacter.getCharacter().getName())
                .toList();

        return new FigureResponse(
                figure.getId(),
                figure.getName(),
                figure.getManufacturer().getName(),
                characterNames
        );
    }
}