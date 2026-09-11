package com.figure.figure.dto;

import com.figure.figure.model.ReleaseStatus;
import com.figure.figure.model.ReleaseType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ReleaseCreateRequest {

    @NotNull
    private Long figureId;

    private LocalDate releaseDate;

    @Positive
    private int price;

    @NotNull
    private ReleaseType type;

    @NotNull
    private ReleaseStatus status;
}