package com.figure.figure.controller;

import com.figure.figure.dto.*;
import com.figure.figure.service.OfficialNewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/news")
public class OfficialNewsController {
    private final OfficialNewsService news;

    @PostMapping
    public ResponseEntity<OfficialNewsResponse> publish(@Valid @RequestBody OfficialNewsRequest request) {
        var result = news.publish(request);
        return result.created() ? ResponseEntity.created(URI.create("/admin/news/" + result.news().id())).body(result.news())
                : ResponseEntity.ok(result.news());
    }
}
