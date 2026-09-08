package com.figure.figure.service;

import com.figure.figure.dto.ReleaseCreateRequest;
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
    public Release createRelease(ReleaseCreateRequest request) {

        Figure figure = figureRepository.findById(request.getFigureId())
                .orElseThrow((FigureNotFoundException::new));

        Release release = new Release(
                figure,
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
        return releaseRepository.findById(id)
                .orElseThrow(ReleaseNotFoundException::new);
    }
}