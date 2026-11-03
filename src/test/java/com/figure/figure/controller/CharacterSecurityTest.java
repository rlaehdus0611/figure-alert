package com.figure.figure.controller;

import com.figure.figure.config.SecurityConfig;
import com.figure.figure.dto.CharacterCreateRequest;
import com.figure.figure.dto.CharacterResponse;
import com.figure.figure.security.JwtTokenProvider;
import com.figure.figure.service.CharacterService;
import com.figure.figure.service.FigureService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CharacterController.class)
@Import(SecurityConfig.class)
class CharacterSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CharacterService characterService;

    @MockitoBean
    private FigureService figureService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    private static final String REQUEST_BODY = """
            {
                "name": "하츠네 미쿠",
                "series": "캐릭터 등록 테스트"
            }
            """;

    @Test
    void create_withoutAuthentication_isRejected() throws Exception {
        // given: 인증 토큰 없는 요청

        // when & then: 접근 거부, 등록 로직 호출 없음
        mockMvc.perform(post("/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST_BODY))
                .andExpect(status().is4xxClientError());

        verifyNoInteractions(characterService, figureService);
    }

    @Test
    void create_withUserRole_returnsForbidden() throws Exception {
        // given: JWT 필터가 USER로 인증하도록 구성
        String token = "test-user-token";

        when(jwtTokenProvider.validateToken(token))
                .thenReturn(true);
        when(jwtTokenProvider.getMemberId(token))
                .thenReturn(1L);
        when(jwtTokenProvider.getRole(token))
                .thenReturn("USER");

        // when & then: 로그인했어도 관리자가 아니면 거부
        mockMvc.perform(post("/characters")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST_BODY))
                .andExpect(status().isForbidden());

        verifyNoInteractions(characterService, figureService);
    }

    @Test
    void create_withAdminRole_returnsCreated() throws Exception {
        // given: JWT 필터가 ADMIN으로 인증하도록 구성
        String token = "test-admin-token";

        when(jwtTokenProvider.validateToken(token))
                .thenReturn(true);
        when(jwtTokenProvider.getMemberId(token))
                .thenReturn(2L);
        when(jwtTokenProvider.getRole(token))
                .thenReturn("ADMIN");

        when(characterService.createCharacter(
                any(CharacterCreateRequest.class)
        )).thenReturn(new CharacterResponse(
                10L,
                "하츠네 미쿠",
                "캐릭터 등록 테스트"
        ));

        // when & then: 관리자는 Controller까지 도달
        mockMvc.perform(post("/characters")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST_BODY))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("하츠네 미쿠"))
                .andExpect(jsonPath("$.series")
                        .value("캐릭터 등록 테스트"));

        verify(characterService)
                .createCharacter(any(CharacterCreateRequest.class));
    }
}