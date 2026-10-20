package com.figure.figure.controller;

import com.figure.figure.dto.CharacterSubscriptionRequest;
import com.figure.figure.dto.CharacterSubscriptionResponse;
import com.figure.figure.service.CharacterSubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/characters/{characterId}/subscription")
public class CharacterSubscriptionController {

    private final CharacterSubscriptionService subscriptionService;

    @PutMapping
    public ResponseEntity<CharacterSubscriptionResponse> subscribe(
            @PathVariable("characterId") Long characterId,
            @Valid @RequestBody CharacterSubscriptionRequest request,
            Authentication authentication
    ) {
        Long memberId = (Long) authentication.getPrincipal();

        CharacterSubscriptionService.SubscribeResult result =
                subscriptionService.subscribe(
                        memberId,
                        characterId,
                        request
                );

        CharacterSubscriptionResponse response =
                CharacterSubscriptionResponse.from(result.subscription());

        if (result.created()) {
            URI location = URI.create(
                    "/characters/" + characterId + "/subscription"
            );

            return ResponseEntity.created(location).body(response);
        }

        return ResponseEntity.ok(response);
    }
}