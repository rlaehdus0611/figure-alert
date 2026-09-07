package com.figure.figure.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Manufacturer {

    @Id
    private Long id;

    @NotBlank
    private String name;

    public Manufacturer() {
    }

    public Manufacturer(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}