package com.figure.figure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class ReleaseCreateRequest {

    private Long id;

    private Long figureId;

    private LocalDate releaseDate;

    @Positive
    private int price;

    @NotBlank
    private String type;

    private String note;

    public ReleaseCreateRequest() {
    }

    public Long getId() {
        return id;
    }

    public Long getFigureId() {
        return figureId;
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
