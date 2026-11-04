package com.figure.figure.controller;

import com.figure.figure.dto.*;
import com.figure.figure.service.NotificationService;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notifications;

    @GetMapping
    public NotificationPageResponse list(Authentication auth,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        return notifications.list((Long) auth.getPrincipal(), page, size);
    }

    @PatchMapping("/{id}/read")
    public NotificationResponse read(Authentication auth, @PathVariable("id") @Positive Long id) {
        return notifications.read((Long) auth.getPrincipal(), id);
    }
}
