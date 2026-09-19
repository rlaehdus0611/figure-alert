
package com.figure.figure.service;

import com.figure.figure.dto.ManufacturerNameRequest;
import com.figure.figure.dto.ManufacturerResponse;
import com.figure.figure.exception.ManufacturerConflictException;
import com.figure.figure.exception.ManufacturerNotFoundException;
import com.figure.figure.model.Manufacturer;
import com.figure.figure.repository.FigureRepository;
import com.figure.figure.repository.ManufacturerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ManufacturerService {

    private final ManufacturerRepository manufacturerRepository;
    private final FigureRepository figureRepository;

    @Transactional
    public ManufacturerResponse createManufacturer(ManufacturerNameRequest request) {
        String name = request.getName();

        if (manufacturerRepository.existsByName(name)) {
            throw new ManufacturerConflictException(
                    "이미 등록된 제조사 이름입니다."
            );
        }

        Manufacturer saved = manufacturerRepository.save(new Manufacturer(name));

        return toResponse(saved);
    }

    public List<ManufacturerResponse> findAllManufacturers() {
        return manufacturerRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ManufacturerResponse updateManufacturer(
            Long id,
            ManufacturerNameRequest request
    ) {
        Manufacturer manufacturer = manufacturerRepository.findById(id)
                .orElseThrow(ManufacturerNotFoundException::new);

        String name = request.getName();

        if (!manufacturer.getName().equals(name)
                && manufacturerRepository.existsByName(name)) {
            throw new ManufacturerConflictException(
                    "이미 등록된 제조사 이름입니다."
            );
        }

        manufacturer.changeName(name);

        return toResponse(manufacturer);
    }

    @Transactional
    public void deleteManufacturer(Long id) {
        Manufacturer manufacturer = manufacturerRepository.findById(id)
                .orElseThrow(ManufacturerNotFoundException::new);

        if (figureRepository.existsByManufacturer_Id(id)) {
            throw new ManufacturerConflictException(
                    "연결된 피규어가 있어 제조사를 삭제할 수 없습니다."
            );
        }

        manufacturerRepository.delete(manufacturer);
    }

    private ManufacturerResponse toResponse(Manufacturer manufacturer) {
        return new ManufacturerResponse(
                manufacturer.getId(),
                manufacturer.getName()
        );
    }
}
