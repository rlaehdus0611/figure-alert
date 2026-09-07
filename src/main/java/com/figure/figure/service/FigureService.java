package com.figure.figure.service;

import com.figure.figure.dto.FigureCreateRequest;
import com.figure.figure.exception.FigureAlreadyExistsException;
import com.figure.figure.exception.FigureNotFoundException;
import com.figure.figure.model.Figure;
import com.figure.figure.model.Manufacturer;
import com.figure.figure.repository.FigureRepository;
import com.figure.figure.repository.ManufacturerRepository;
import org.springframework.stereotype.Service;
import com.figure.figure.model.Character;
import com.figure.figure.repository.CharacterRepository;
import com.figure.figure.exception.ManufacturerNotFoundException;
import com.figure.figure.exception.CharacterNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class FigureService {

    private final FigureRepository figureRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final CharacterRepository characterRepository;

    public FigureService(
            FigureRepository figureRepository,
            ManufacturerRepository manufacturerRepository,
            CharacterRepository characterRepository
    ) {
        this.figureRepository = figureRepository;
        this.manufacturerRepository = manufacturerRepository;
        this.characterRepository = characterRepository;
    }

    public Figure findFigure(Long id) { // id로 찾기
        Optional<Figure> figure = figureRepository.findById(id);

        if (figure.isEmpty()) {
            throw new FigureNotFoundException();
        }

        return figure.get();
    }

    public List<Figure> findAllFigures() { // 모든 피규어 찾기
        return figureRepository.findAll();
    }

    public Figure findFigureByName(String name) { // 이름으로 찾기
        return figureRepository.findByName(name);
    }

    public List<Figure> searchFiguresByName(String keyword) { // Name, Character 키워드로 피규어 정보 찾기
        return figureRepository.findByNameContainingOrCharacterNameContaining(keyword, keyword);
    }

    public Figure createFigure(FigureCreateRequest request) { // 새로운 피규어 정보 생성

        if (figureRepository.existsById(request.getId())) {
            throw new FigureAlreadyExistsException();
        }

        Optional<Manufacturer> manufacturer =
                manufacturerRepository.findById(request.getManufacturerId());

        if (manufacturer.isEmpty()) {
            throw new ManufacturerNotFoundException();
        }

        Optional<Character> character =
                characterRepository.findById(request.getCharacterId());

        if (character.isEmpty()) {
            throw new CharacterNotFoundException();
        }

        Figure figure = new Figure(
                request.getId(),
                request.getName(),
                manufacturer.get(),
                character.get()
        );

        return figureRepository.save(figure);
    }


    public void deleteFigure(Long id) {
        if (!figureRepository.existsById(id)) {
            throw new FigureNotFoundException();
        }

        figureRepository.deleteById(id);
    }

}