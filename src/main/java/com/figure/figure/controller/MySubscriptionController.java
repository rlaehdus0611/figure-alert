package com.figure.figure.controller;

import com.figure.figure.dto.CharacterSubscriptionResponse;
import com.figure.figure.service.CharacterSubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MySubscriptionController {
    private final CharacterSubscriptionService subscriptions;

    @GetMapping("/members/me/subscriptions")
    public List<CharacterSubscriptionResponse> list(Authentication authentication) {
        return subscriptions.list((Long) authentication.getPrincipal());
    }
}
