package com.figure.figure.controller;

import com.figure.figure.dto.ReleaseCreateRequest;
import com.figure.figure.dto.ReleaseResponse;
import com.figure.figure.model.Release;
import com.figure.figure.service.ReleaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/releases")
@RequiredArgsConstructor
public class ReleaseController {

    private final ReleaseService releaseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReleaseResponse createRelease(
            @Valid @RequestBody ReleaseCreateRequest request
    ) {
        return releaseService.createRelease(request);
    }

    @GetMapping
    public List<ReleaseResponse> getReleases() {
        return releaseService.findAllReleases();
    }

    @GetMapping("/{id}")
    public ReleaseResponse getRelease(
            @PathVariable Long id
    ) {
        return releaseService.findRelease(id);
    }
}