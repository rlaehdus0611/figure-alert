package com.figure.figure.controller;

import com.figure.figure.dto.ManufacturerNameRequest;
import com.figure.figure.dto.ManufacturerResponse;
import com.figure.figure.model.Manufacturer;
import com.figure.figure.service.ManufacturerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/manufacturers")
@RequiredArgsConstructor
public class ManufacturerController {

    private final ManufacturerService manufacturerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ManufacturerResponse createManufacturer(
            @Valid @RequestBody ManufacturerNameRequest request
    ) {
        return manufacturerService.createManufacturer(request);
    }

    @PatchMapping("/{id}")
    public ManufacturerResponse updateManufacturer(
            @PathVariable Long id,
            @Valid @RequestBody ManufacturerNameRequest request
    ) {
        return manufacturerService.updateManufacturer(id, request);
    }

    @GetMapping
    public List<ManufacturerResponse> getManufacturers() {
        return manufacturerService.findAllManufacturers();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteManufacturer(@PathVariable Long id) {
        manufacturerService.deleteManufacturer(id);
    }
}