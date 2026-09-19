package com.figure.figure.service;

import com.figure.figure.dto.LoginRequest;
import com.figure.figure.dto.LoginResponse;
import com.figure.figure.dto.MemberResponse;
import com.figure.figure.dto.SignupRequest;
import com.figure.figure.exception.DuplicateEmailException;
import com.figure.figure.exception.InvalidLoginException;
import com.figure.figure.model.Member;
import com.figure.figure.model.MemberRole;
import com.figure.figure.repository.MemberRepository;
import com.figure.figure.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원가입
    @Transactional
    public MemberResponse signup(SignupRequest request) {

        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException();
        }

        String encodedPassword =
                passwordEncoder.encode(request.getPassword());

        Member member = new Member(
                request.getEmail(),
                encodedPassword,
                request.getNickname(),
                MemberRole.USER
        );

        Member savedMember = memberRepository.save(member);

        return new MemberResponse(
                savedMember.getId(),
                savedMember.getEmail(),
                savedMember.getNickname(),
                savedMember.getRole()
        );
    }

    // 로그인 처리
    public LoginResponse login(LoginRequest request) {

        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(InvalidLoginException::new);

        if (!passwordEncoder.matches(
                request.getPassword(),
                member.getPassword()
        )) {
            throw new InvalidLoginException();
        }

        // JWT 발급
        String accessToken = jwtTokenProvider.createToken(member);

        // 로그인 응답 반환
        return new LoginResponse(accessToken);
    }
}