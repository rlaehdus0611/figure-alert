package com.figure.figure.repository;

import com.figure.figure.model.Figure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FigureRepository extends JpaRepository<Figure, Long> {

    Figure findByName(String name);
    List<Figure> findByNameContainingOrCharacterNameContaining(
            String figureKeyword,
            String characterKeyword
    );
}