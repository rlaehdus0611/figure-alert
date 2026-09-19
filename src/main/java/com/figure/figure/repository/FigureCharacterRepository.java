package com.figure.figure.repository;

import com.figure.figure.model.FigureCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface FigureCharacterRepository
        extends JpaRepository<FigureCharacter, Long> {

    List<FigureCharacter> findByFigureId(Long figureId);

    List<FigureCharacter> findByCharacterId(Long characterId);

    void deleteByFigureId(Long figureId);

    @Query("""
        SELECT fc
        FROM FigureCharacter fc
        JOIN FETCH fc.character
        WHERE fc.figure.id IN :figureIds
        """)
    List<FigureCharacter> findAllByFigureIdsWithCharacter(
            @Param("figureIds") List<Long> figureIds
    );

}