package com.figure.figure.dto;

import com.figure.figure.model.ReleaseStatus;
import com.figure.figure.model.ReleaseType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class ReleaseResponse {

    private Long id;
    private Long figureId;
    private String figureName;
    private LocalDate releaseDate;
    private int price;
    private ReleaseType type;
    private ReleaseStatus status;
    private String note;

}
