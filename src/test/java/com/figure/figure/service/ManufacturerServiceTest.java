
package com.figure.figure.service;

import com.figure.figure.dto.ManufacturerNameRequest;
import com.figure.figure.exception.ManufacturerConflictException;
import com.figure.figure.model.Manufacturer;
import com.figure.figure.repository.FigureRepository;
import com.figure.figure.repository.ManufacturerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ManufacturerServiceTest {

    @Mock
    private ManufacturerRepository manufacturerRepository;

    @Mock
    private FigureRepository figureRepository;

    @InjectMocks
    private ManufacturerService manufacturerService;

    @Test
    void duplicateName_throwsConflict_andDoesNotSave() {
        ManufacturerNameRequest request = new ManufacturerNameRequest();
        request.setName("굿스마일 컴퍼니");

        when(manufacturerRepository.existsByName("굿스마일 컴퍼니"))
                .thenReturn(true);

        assertThrows(
                ManufacturerConflictException.class,
                () -> manufacturerService.createManufacturer(request)
        );

        verify(manufacturerRepository, never()).save(any(Manufacturer.class));
    }
}
