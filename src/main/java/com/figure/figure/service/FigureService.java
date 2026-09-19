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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FigureService {

    private final FigureRepository figureRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final CharacterRepository characterRepository;
    private final FigureCharacterRepository figureCharacterRepository;

    public FigureResponse findFigure(Long id) {
        Figure figure = figureRepository.findById(id)
                .orElseThrow(FigureNotFoundException::new);

        return toResponse(figure);
    }

    public List<FigureResponse> findAllFigures() {
        return figureRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<FigureResponse> findFigures(String keyword, Long manufacturerId) {
        String searchKeyword = keyword == null ? "" : keyword.trim();

        List<Figure> figures = figureRepository.findByFilters(
                searchKeyword,
                manufacturerId
        );

        if (figures.isEmpty()) {
            return List.of();
        }

        List<Long> figureIds = figures.stream()
                .map(Figure::getId)
                .toList();

        List<FigureCharacter> relations =
                figureCharacterRepository.findAllByFigureIdsWithCharacter(figureIds);

        Map<Long, List<String>> characterNamesByFigureId = new HashMap<>();

        for (FigureCharacter relation : relations) {
            Long figureId = relation.getFigure().getId();

            characterNamesByFigureId
                    .computeIfAbsent(figureId, id -> new ArrayList<>())
                    .add(relation.getCharacter().getName());
        }

        return figures.stream()
                .map(figure -> toResponse(
                        figure,
                        characterNamesByFigureId.getOrDefault(figure.getId(), List.of())
                ))
                .toList();
    }

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

    public List<FigureResponse> searchFiguresByName(String keyword) {
        return findFigures(keyword, null);
    }

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

    @Transactional
    public void deleteFigure(Long id) {
        if (!figureRepository.existsById(id)) {
            throw new FigureNotFoundException();
        }

        figureCharacterRepository.deleteByFigureId(id);
        figureRepository.deleteById(id);
    }

    private FigureResponse toResponse(Figure figure) {
        List<String> characterNames = figureCharacterRepository
                .findByFigureId(figure.getId())
                .stream()
                .map(figureCharacter -> figureCharacter.getCharacter().getName())
                .toList();

        return toResponse(figure, characterNames);
    }

    private FigureResponse toResponse(Figure figure, List<String> characterNames) {
        return new FigureResponse(
                figure.getId(),
                figure.getName(),
                figure.getManufacturer().getName(),
                characterNames
        );
    }

}