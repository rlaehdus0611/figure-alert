package com.figure.figure.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Figure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id값을 DB가 직접 만들어줌
    private Long id;

    @NotBlank
    private String name;

    @ManyToOne
    @JoinColumn(name = "manufacturer_id") // Manufacturer와 연결할 FK 컬럼
    private Manufacturer manufacturer;

    @ManyToOne
    @JoinColumn(name = "character_id") // Character와 연결할 FK 컬럼
    private Character character;

    public Figure() {
    }

    public Figure(
            Long id,
            String name,
            Manufacturer manufacturer,
            Character character
    ) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.character = character;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public Character getCharacter() {
        return character;
    }
}