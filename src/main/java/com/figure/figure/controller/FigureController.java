package com.figure.figure.controller;

import com.figure.figure.dto.FigureCreateRequest;
import com.figure.figure.model.Figure;
import com.figure.figure.service.FigureService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RestController
public class FigureController {

    private final FigureService figureService;

    public FigureController(FigureService figureService) {
        this.figureService = figureService;
    }

    @GetMapping("/figures") // 모든 피규어 조회
    public List<Figure> figures() {
        return figureService.findAllFigures();
    }

    @GetMapping("/figures/{id}") // id로 피규어 조회
    public Figure getFigure(@PathVariable Long id) {
        return figureService.findFigure(id);
    }

    @GetMapping("/figures/name/{name}") // 이름으로 피규어 조회
    public Figure getFigureByName(@PathVariable String name) {
        return figureService.findFigureByName(name);
    }

    @PostMapping("/figures") // 피규어 등록
    @ResponseStatus(HttpStatus.CREATED)
    public Figure createFigure(@Valid @RequestBody FigureCreateRequest request) {
        return figureService.createFigure(request);
    }

    @GetMapping("/figures/search") // 키워드로 피규어 검색
    public List<Figure> searchFigures(
            @RequestParam String keyword
    ) {
        return figureService.searchFiguresByName(keyword);
    }

    @DeleteMapping("/figures/{id}") // 피규어 정보 삭제
    public void deleteFigure(@PathVariable Long id) {
        figureService.deleteFigure(id);
    }



}