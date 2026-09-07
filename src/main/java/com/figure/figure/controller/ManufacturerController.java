package com.figure.figure.controller;

import com.figure.figure.model.Manufacturer;
import com.figure.figure.service.ManufacturerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manufacturers") // HTTP 요청을 처리하고 결과를 JSON으로 응답
public class ManufacturerController {

    private final ManufacturerService manufacturerService;

    public ManufacturerController(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    @PostMapping // POST 요청을 메서드와 연결
    @ResponseStatus(HttpStatus.CREATED)
    public Manufacturer createManufacturer(
            @Valid @RequestBody Manufacturer manufacturer
    ) {
        return manufacturerService.createManufacturer(manufacturer);
    }
}