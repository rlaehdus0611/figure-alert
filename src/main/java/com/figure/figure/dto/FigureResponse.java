package com.figure.figure.dto;

public class FigureResponse { // 서버가 사용자에게 보내는 데이터

    private Long id;
    private String name;
    private String manufactureName;
    private String characterName;

    public FigureResponse(
            Long id,
            String name,
            String ManufactureName,
            String charactername
    ) {
        this.id = id;
        this.name = name;
        this.manufactureName = manufactureName;
        this.characterName = charactername;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getManufactureName() {
        return manufactureName;
    }

    public String getCharacterName() {
        return characterName;
    }
}
