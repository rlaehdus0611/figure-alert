package com.figure.figure.controller;

import com.figure.figure.dto.FigureCreateRequest;
import com.figure.figure.dto.FigureResponse;
import com.figure.figure.dto.ReleaseResponse;
import com.figure.figure.service.FigureService;
import com.figure.figure.service.ReleaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FigureController {

    private final FigureService figureService;
    private final ReleaseService releaseService;

    @GetMapping("/figures")
    public List<FigureResponse> getFigures(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long manufacturerId
    ) {
        return figureService.findFigures(keyword, manufacturerId);
    }

    @GetMapping("/figures/{id}")
    public FigureResponse getFigure(@PathVariable Long id) {
        return figureService.findFigure(id);
    }

    @PostMapping("/figures")
    @ResponseStatus(HttpStatus.CREATED)
    public FigureResponse createFigure(@Valid @RequestBody FigureCreateRequest request) {
        return figureService.createFigure(request);
    }

    @GetMapping("/figures/search")
    public List<FigureResponse> searchFigures(
            @RequestParam String keyword
    ) {
        return figureService.searchFiguresByName(keyword);
    }

    @DeleteMapping("/figures/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFigure(@PathVariable Long id) {
        figureService.deleteFigure(id);
    }

    @GetMapping("/figures/{figureId}/releases")
    public List<ReleaseResponse> getFigureReleases(
            @PathVariable Long figureId
    ) {
        return releaseService.findReleasesByFigure(figureId);
    }

}