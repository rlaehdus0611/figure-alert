package com.figure.figure.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.FetchType;

@Entity
public class Figure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    @NotBlank
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manufacturer_id", nullable = false)
    private Manufacturer manufacturer;

    public Figure() {
    }

    public Figure(
            String name,
            Manufacturer manufacturer
    ) {
        this.name = name;
        this.manufacturer = manufacturer;
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
}