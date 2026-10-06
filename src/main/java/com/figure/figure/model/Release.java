package com.figure.figure.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private Integer releaseYear;
    private Integer releaseMonth;
    private Integer releaseDay;

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
            Integer releaseYear,
            Integer releaseMonth,
            Integer releaseDay,
            int price,
            ReleaseType type,
            ReleaseStatus status
    ) {
        this.figure = figure;
        this.releaseYear = releaseYear;
        this.releaseMonth = releaseMonth;
        this.releaseDay = releaseDay;
        this.price = price;
        this.type = type;
        this.status = status;
    }

    public void updateSchedule(
            Integer releaseYear,
            Integer releaseMonth,
            Integer releaseDay,
            String note
    ) {
        this.releaseYear = releaseYear;
        this.releaseMonth = releaseMonth;
        this.releaseDay = releaseDay;
        this.note = note;
    }
}