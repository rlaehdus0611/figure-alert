package com.figure.figure.model;

import jakarta.persistence.*;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "release_type", nullable = false)
    private ReleaseType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "release_status", nullable = false)
    private ReleaseStatus status;

    private String note;

    public Release(
            Figure figure,
            LocalDate releaseDate,
            int price,
            ReleaseType type,
            ReleaseStatus status
    ) {
        this.figure = figure;
        this.releaseDate = releaseDate;
        this.price = price;
        this.type = type;
        this.status = status;
    }
}