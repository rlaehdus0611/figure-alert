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

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/members/signup",
                                "/members/login"
                        ).permitAll()

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
                                HttpMethod.DELETE,
                                "/figures/{id}"
                        ).hasRole("ADMIN")

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
                                HttpMethod.POST,
                                "/characters/{characterId}/subscriptions"
                        ).hasRole("USER")
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/characters/{characterId}/subscriptions"
                        ).hasRole("USER")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/releases/*/schedule"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/figures/*"
                        ).hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}