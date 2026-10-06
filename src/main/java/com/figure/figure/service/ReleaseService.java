package com.figure.figure.service;

import com.figure.figure.dto.ReleaseCreateRequest;
import com.figure.figure.dto.ReleaseResponse;
import com.figure.figure.dto.ReleaseScheduleUpdateRequest;
import com.figure.figure.exception.FigureNotFoundException;
import com.figure.figure.model.Figure;
import com.figure.figure.model.Release;
import com.figure.figure.repository.FigureRepository;
import com.figure.figure.repository.ReleaseRepository;
import com.figure.figure.exception.ReleaseNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReleaseService {

    private final ReleaseRepository releaseRepository;
    private final FigureRepository figureRepository;

    @Transactional
    public ReleaseResponse createRelease(ReleaseCreateRequest request) {

        Figure figure = figureRepository.findById(request.getFigureId())
                .orElseThrow((FigureNotFoundException::new));

        Release release = new Release(
                figure,
                request.getReleaseYear(),
                request.getReleaseMonth(),
                request.getReleaseDay(),
                request.getPrice(),
                request.getType(),
                request.getStatus()
        );

        Release savedRelease = releaseRepository.save(release);

        return toResponse(savedRelease);
    }

    public List<ReleaseResponse> findAllReleases() {
        return releaseRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ReleaseResponse findRelease(Long id) {
        Release release = releaseRepository.findById(id)
                .orElseThrow(ReleaseNotFoundException::new);

        return toResponse(release);
    }

    public List<ReleaseResponse> findReleasesByFigure(Long figureId) {

        if (!figureRepository.existsById(figureId)) {
            throw new FigureNotFoundException();
        }

        return releaseRepository
                .findByFigure_IdOrderByReleaseYearDescReleaseMonthDescReleaseDayDesc(
                        figureId
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ReleaseResponse updateSchedule(
            Long id,
            ReleaseScheduleUpdateRequest request
    ) {
        Release release = releaseRepository.findById(id)
                .orElseThrow(ReleaseNotFoundException::new);

        release.updateSchedule(
                request.getReleaseYear(),
                request.getReleaseMonth(),
                request.getReleaseDay(),
                request.getNote()
        );

        return toResponse(release);
    }

    private ReleaseResponse toResponse(Release release) {
        return new ReleaseResponse(
                release.getId(),
                release.getFigure().getId(),
                release.getFigure().getName(),
                release.getReleaseYear(),
                release.getReleaseMonth(),
                release.getReleaseDay(),
                release.getPrice(),
                release.getType(),
                release.getStatus(),
                release.getNote()
        );
    }

}