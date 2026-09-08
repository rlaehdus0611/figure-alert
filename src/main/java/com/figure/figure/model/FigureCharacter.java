package com.figure.figure.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(
        name = "figure_character",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"figure_id", "character_id"})
        }
)
public class FigureCharacter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "figure_id", nullable = false)
    private Figure figure;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", nullable = false)
    private Character character;

    public FigureCharacter(Figure figure, Character character) {
        this.figure = figure;
        this.character = character;
    }
}