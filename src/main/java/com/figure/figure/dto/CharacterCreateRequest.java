package com.figure.figure.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CharacterCreateRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String series;
}
