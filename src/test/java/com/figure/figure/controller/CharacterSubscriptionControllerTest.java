package com.figure.figure.controller;

import com.figure.figure.dto.CharacterSubscriptionRequest;
import com.figure.figure.exception.GlobalExceptionHandler;
import com.figure.figure.model.Character;
import com.figure.figure.model.CharacterSubscription;
import com.figure.figure.model.Member;
import com.figure.figure.model.MemberRole;
import com.figure.figure.service.CharacterSubscriptionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CharacterSubscriptionControllerTest {

    @Mock
    private CharacterSubscriptionService subscriptionService;

    @InjectMocks
    private CharacterSubscriptionController controller;

    @Test
    void subscribe_newSubscription_returnsCreated() throws Exception {
        // given
        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        // given
        Long memberId = 1L;

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        memberId,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                );

        // given
        Member member = new Member(
                "test@example.com",
                "test-password",
                "테스트회원",
                MemberRole.USER
        );

        Character character = new Character("하츠네 미쿠", "Piapro");

        CharacterSubscription savedSubscription =
                new CharacterSubscription(
                        member,
                        character,
                        false, true, false, false, false
                );

        ReflectionTestUtils.setField(character, "id", 10L);
        ReflectionTestUtils.setField(savedSubscription, "id", 100L);

        when(subscriptionService.subscribe(
                eq(memberId),
                eq(10L),
                any(CharacterSubscriptionRequest.class)
        )).thenReturn(
                new CharacterSubscriptionService.SubscribeResult(
                        savedSubscription,
                        true
                )
        );

        String requestBody = """
                {
                    "announcementEnabled": false,
                    "preorderEnabled": true,
                    "deadlineEnabled": false,
                    "rereleaseEnabled": false,
                    "prizeEnabled": false
                }
                """;

        // when & then
        mockMvc.perform(
                        put("/characters/10/subscription")
                                .principal(authentication)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.characterId").value(10))
                .andExpect(jsonPath("$.announcementEnabled").value(false))
                .andExpect(jsonPath("$.preorderEnabled").value(true))
                .andExpect(jsonPath("$.deadlineEnabled").value(false))
                .andExpect(jsonPath("$.rereleaseEnabled").value(false))
                .andExpect(jsonPath("$.prizeEnabled").value(false));

        verify(subscriptionService).subscribe(
                eq(memberId),
                eq(10L),
                any(CharacterSubscriptionRequest.class)
        );
    }

    @Test
    void subscribe_existingSubscription_returnsOk() throws Exception {
        // given
        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        // given
        Long memberId = 1L;

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        memberId,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                );

        // given
        Member member = new Member(
                "test@example.com",
                "test-password",
                "테스트회원",
                MemberRole.USER
        );

        Character character = new Character("하츠네 미쿠", "Piapro");

        CharacterSubscription savedSubscription =
                new CharacterSubscription(
                        member,
                        character,
                        false, true, false, false, false
                );

        ReflectionTestUtils.setField(character, "id", 10L);
        ReflectionTestUtils.setField(savedSubscription, "id", 100L);

        when(subscriptionService.subscribe(
                eq(memberId),
                eq(10L),
                any(CharacterSubscriptionRequest.class)
        )).thenReturn(
                new CharacterSubscriptionService.SubscribeResult(
                        savedSubscription,
                        false
                )
        );

        String requestBody = """
                {
                    "announcementEnabled": false,
                    "preorderEnabled": true,
                    "deadlineEnabled": false,
                    "rereleaseEnabled": false,
                    "prizeEnabled": false
                }
                """;

        // when & then
        mockMvc.perform(
                        put("/characters/10/subscription")
                                .principal(authentication)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.characterId").value(10))
                .andExpect(jsonPath("$.announcementEnabled").value(false))
                .andExpect(jsonPath("$.preorderEnabled").value(true))
                .andExpect(jsonPath("$.deadlineEnabled").value(false))
                .andExpect(jsonPath("$.rereleaseEnabled").value(false))
                .andExpect(jsonPath("$.prizeEnabled").value(false));

        verify(subscriptionService).subscribe(
                eq(memberId),
                eq(10L),
                any(CharacterSubscriptionRequest.class)
        );
    }

    @Test
    void subscribe_missingDeadlineEnabled_returnsBadRequest() throws Exception {
        // given
        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        Long memberId = 1L;

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        memberId,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                );

        // given
        String requestBody = """
            {
                "announcementEnabled": false,
                "preorderEnabled": true,
                "rereleaseEnabled": false,
                "prizeEnabled": false
            }
            """;

        // when & then
        mockMvc.perform(
                        put("/characters/10/subscription")
                                .principal(authentication)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code")
                        .value("INVALID_SUBSCRIPTION_SETTINGS"));

        // then
        verifyNoInteractions(subscriptionService);
    }
}