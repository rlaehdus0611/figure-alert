package com.figure.figure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class FigureCreateRequest {

    @NotBlank
    private String name;

    @NotNull
    private Long manufacturerId;

    @NotEmpty
    private List<Long> characterIds;
}