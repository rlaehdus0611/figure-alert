package com.figure.figure.repository;

import com.figure.figure.model.Character;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterRepository
        extends JpaRepository<Character, Long> {
}