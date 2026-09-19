package com.figure.figure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ManufacturerNameRequest {

    @NotBlank
    @Size(max = 100)
    private String name;

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}
