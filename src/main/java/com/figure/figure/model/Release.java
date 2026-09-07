package com.figure.figure.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "releases")
public class Release {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "figure_id")
    private Figure figure;

    private LocalDate releaseDate;

    private int price;

    private String type;

    private String note;

    public Release() {
    }

    public Release(
            Long id,
            Figure figure,
            LocalDate releaseDate,
            int price,
            String type,
            String note
    ) {
        this.id = id;
        this.figure = figure;
        this.releaseDate = releaseDate;
        this.price = price;
        this.type = type;
        this.note = note;
    }

    public Long getId() {
        return id;
    }

    public Figure getFigure() {
        return figure;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public int getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public String getNote() {
        return note;
    }
}