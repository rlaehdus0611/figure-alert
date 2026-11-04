package com.figure.figure.config;

import com.figure.figure.security.JwtAuthenticationFilter;
import com.figure.figure.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;
import jakarta.servlet.DispatcherType;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        JwtAuthenticationFilter jwtAuthenticationFilter =
                new JwtAuthenticationFilter(jwtTokenProvider);

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .exceptionHandling(errors -> errors
                        .authenticationEntryPoint((request, response, error) -> {
                            response.setStatus(401);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("{\"status\":401,\"code\":\"UNAUTHORIZED\",\"message\":\"로그인이 필요합니다.\"}");
                        })
                        .accessDeniedHandler((request, response, error) -> {
                            response.setStatus(403);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("{\"status\":403,\"code\":\"FORBIDDEN\",\"message\":\"접근 권한이 없습니다.\"}");
                        }))

                .authorizeHttpRequests(auth -> auth

                                .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()

                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/notifications", "/notifications/**").hasRole("USER")

                                // 회원가입, 로그인
                                .requestMatchers(
                                        "/members/signup",
                                        "/members/login"
                                ).permitAll()

                                // 캐릭터
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/characters",
                                        "/characters/search",
                                        "/characters/{id}",
                                        "/characters/{id}/figures"
                                ).permitAll()
                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/characters"
                                ).hasRole("ADMIN")

                                // 피규어
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/figures",
                                        "/figures/**"
                                ).permitAll()
                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/figures"
                                ).hasRole("ADMIN")
                                .requestMatchers(
                                        HttpMethod.PUT,
                                        "/figures/*"
                                ).hasRole("ADMIN")
                                .requestMatchers(
                                        HttpMethod.DELETE,
                                        "/figures/{id}"
                                ).hasRole("ADMIN")

                                // 제조사
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/manufacturers"
                                ).permitAll()
                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/manufacturers"
                                ).hasRole("ADMIN")
                                .requestMatchers(
                                        HttpMethod.PATCH,
                                        "/manufacturers/{id}"
                                ).hasRole("ADMIN")
                                .requestMatchers(
                                        HttpMethod.DELETE,
                                        "/manufacturers/{id}"
                                ).hasRole("ADMIN")

                                // 발매 정보
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/releases",
                                        "/releases/**"
                                ).permitAll()
                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/releases"
                                ).hasRole("ADMIN")
                                .requestMatchers(
                                        HttpMethod.PUT,
                                        "/releases/*/schedule"
                                ).hasRole("ADMIN")

                                // 개인 구독은 USER 계정만 허용
                                .requestMatchers(
                                HttpMethod.PUT,
                                "/characters/{characterId}/subscription"
                                ).hasRole("USER")

                                .requestMatchers(HttpMethod.GET,
                                        "/characters/{characterId}/subscription",
                                        "/members/me/subscriptions").hasRole("USER")
                                .requestMatchers(HttpMethod.DELETE,
                                        "/characters/{characterId}/subscription").hasRole("USER")

                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
