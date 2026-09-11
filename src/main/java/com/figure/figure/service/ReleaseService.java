package com.figure.figure.service;

import com.figure.figure.dto.ReleaseCreateRequest;
import com.figure.figure.dto.ReleaseResponse;
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

    // 출시 정보 등록
    @Transactional
    public ReleaseResponse createRelease(ReleaseCreateRequest request) {

        Figure figure = figureRepository.findById(request.getFigureId())
                .orElseThrow((FigureNotFoundException::new));

        Release release = new Release(
                figure,
                request.getReleaseDate(),
                request.getPrice(),
                request.getType(),
                request.getStatus()
        );

        Release savedRelease = releaseRepository.save(release);

        return toResponse(savedRelease);
    }

    // 출시 정보 전체 조회
    public List<ReleaseResponse> findAllReleases() {
        return releaseRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // 출시 정보 하나 조회
    public ReleaseResponse findRelease(Long id) {
        Release release = releaseRepository.findById(id)
                .orElseThrow(ReleaseNotFoundException::new);

        return toResponse(release);
    }

    // 특정 피규어 출시 이력 조회
    public List<ReleaseResponse> findReleasesByFigure(Long figureId) {

        if (!figureRepository.existsById(figureId)) {
            throw new FigureNotFoundException();
        }

        return releaseRepository
                .findByFigure_IdOrderByReleaseDateDesc(figureId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Release엔티티 -> API 응답용 DTO 변환
    private ReleaseResponse toResponse(Release release) {
        return new ReleaseResponse(
                release.getId(),
                release.getFigure().getId(),
                release.getFigure().getName(),
                release.getReleaseDate(),
                release.getPrice(),
                release.getType(),
                release.getStatus()
        );
    }

}