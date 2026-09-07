package com.figure.figure.controller;

import com.figure.figure.dto.ReleaseCreateRequest;
import com.figure.figure.model.Release;
import com.figure.figure.service.ReleaseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/releases")
public class ReleaseController {

    private final ReleaseService releaseService;

    public ReleaseController(ReleaseService releaseService) {
        this.releaseService = releaseService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Release createRelease(
            @Valid
            @RequestBody
            ReleaseCreateRequest request
    ) {
        return releaseService.createRelease(request);
    }

    @GetMapping
    public List<Release> getReleases() {
        return releaseService.findAllReleases();
    }

    @GetMapping("/{id}")
    public Release getRelease(
            @PathVariable Long id
    ) {
        return releaseService.findRelease(id);
    }
}