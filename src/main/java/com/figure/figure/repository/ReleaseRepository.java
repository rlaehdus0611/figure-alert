package com.figure.figure.repository;

import com.figure.figure.model.Release;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReleaseRepository extends JpaRepository<Release, Long> {
    List<Release> findByFigure_IdOrderByReleaseDateDesc(Long figureId);
}