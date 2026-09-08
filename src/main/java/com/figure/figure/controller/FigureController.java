package com.figure.figure.controller;

import com.figure.figure.dto.FigureCreateRequest;
import com.figure.figure.dto.FigureResponse;
import com.figure.figure.model.Figure;
import com.figure.figure.service.FigureService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FigureController {

    private final FigureService figureService;

    public FigureController(FigureService figureService) {
        this.figureService = figureService;
    }

    // 모든 피규어 조회
    @GetMapping("/figures")
    public List<FigureResponse> getFigures() {
        return figureService.findAllFigures();
    }

    // id로 피규어 조회
    @GetMapping("/figures/{id}")
    public FigureResponse getFigure(@PathVariable Long id) {
        return figureService.findFigure(id);
    }

    // 피규어 등록
    @PostMapping("/figures")
    @ResponseStatus(HttpStatus.CREATED)
    public FigureResponse createFigure(@Valid @RequestBody FigureCreateRequest request) {
        return figureService.createFigure(request);
    }

    // 키워드로 피규어 검색
    @GetMapping("/figures/search")
    public List<FigureResponse> searchFigures(
            @RequestParam String keyword
    ) {
        return figureService.searchFiguresByName(keyword);
    }

    // 피규어 정보 삭제
    @DeleteMapping("/figures/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFigure(@PathVariable Long id) {
        figureService.deleteFigure(id);
    }

}