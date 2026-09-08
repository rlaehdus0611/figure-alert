package com.figure.figure.repository;

import com.figure.figure.model.Character;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CharacterRepository
        extends JpaRepository<Character, Long> {

    List<Character> findByNameContaining(String keyword);
}