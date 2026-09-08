package com.figure.figure.repository;

import com.figure.figure.model.FigureCharacter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FigureCharacterRepository
        extends JpaRepository<FigureCharacter, Long> {

    List<FigureCharacter> findByFigureId(Long figureId);

    List<FigureCharacter> findByCharacterId(Long characterId);

    void deleteByFigureId(Long figureId);

}