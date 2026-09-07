package com.figure.figure.dto;

import jakarta.validation.constraints.NotBlank;

public class FigureCreateRequest { // 사용자가 서버에 보내는 데이터

    private Long id;

    @NotBlank
    private String name;

    private Long manufacturerId;

    private Long characterId;

    public FigureCreateRequest() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getManufacturerId() {
        return manufacturerId;
    }

    public Long getCharacterId() {
        return characterId;
    }
}