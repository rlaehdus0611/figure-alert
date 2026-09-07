package com.figure.figure.service;

import com.figure.figure.model.Manufacturer;
import com.figure.figure.repository.ManufacturerRepository;
import org.springframework.stereotype.Service;

@Service
public class ManufacturerService {

    private final ManufacturerRepository manufacturerRepository;

    public ManufacturerService(ManufacturerRepository manufacturerRepository) {
        this.manufacturerRepository = manufacturerRepository;
    }

    public Manufacturer createManufacturer(Manufacturer manufacturer) {
        return manufacturerRepository.save(manufacturer);
    }
}