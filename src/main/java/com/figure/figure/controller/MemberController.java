package com.figure.figure.controller;

import com.figure.figure.dto.LoginRequest;
import com.figure.figure.dto.LoginResponse;
import com.figure.figure.dto.MemberResponse;
import com.figure.figure.dto.SignupRequest;
import com.figure.figure.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponse signup(
            @Valid @RequestBody SignupRequest request
    ) {
        return memberService.signup(request);
    }

    // 로그인
    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request
    ) {
        return memberService.login(request);
    }
}



