package com.figure.figure.service;

import com.figure.figure.dto.ReleaseCreateRequest;
import com.figure.figure.exception.FigureNotFoundException;
import com.figure.figure.model.Figure;
import com.figure.figure.model.Release;
import com.figure.figure.repository.FigureRepository;
import com.figure.figure.repository.ReleaseRepository;
import com.figure.figure.exception.ReleaseNotFoundException;

import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service // 출시 정보 관련 비즈니스 로직 처리
public class ReleaseService {

    private final ReleaseRepository releaseRepository;
    private final FigureRepository figureRepository;

    public ReleaseService(
            ReleaseRepository releaseRepository,
            FigureRepository figureRepository
    ) {
        this.releaseRepository = releaseRepository;
        this.figureRepository = figureRepository;
    }

    // 출시 정보 등록
    public Release createRelease(ReleaseCreateRequest request) {

        Optional<Figure> figure =
                figureRepository.findById(request.getFigureId());

        if (figure.isEmpty()) {
            throw new FigureNotFoundException();
        }

        Release release = new Release(
                request.getId(),
                figure.get(),
                request.getReleaseDate(),
                request.getPrice(),
                request.getType(),
                request.getNote()
        );

        return releaseRepository.save(release);
    }

    // 출시 정보 전체 조회
    public List<Release> findAllReleases() {
        return releaseRepository.findAll();
    }

    // 출시 정보 하나 조회
    public Release findRelease(Long id) {

        Optional<Release> release =
                releaseRepository.findById(id);

        if (release.isEmpty()) {
            throw new ReleaseNotFoundException();
        }

        return release.get();
    }
}