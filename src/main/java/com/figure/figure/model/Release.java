package com.figure.figure.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "releases")
public class Release {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "figure_id", nullable = false)
    private Figure figure;

    private LocalDate releaseDate;

    private int price;

    private String type;

    private String note;

    public Release(
            Figure figure,
            LocalDate releaseDate,
            int price,
            String type,
            String note
    ) {
        this.figure = figure;
        this.releaseDate = releaseDate;
        this.price = price;
        this.type = type;
        this.note = note;
    }
}