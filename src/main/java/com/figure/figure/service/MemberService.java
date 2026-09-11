package com.figure.figure.service;

import com.figure.figure.dto.MemberResponse;
import com.figure.figure.dto.SignupRequest;
import com.figure.figure.exception.DuplicateEmailException;
import com.figure.figure.model.Member;
import com.figure.figure.model.MemberRole;
import com.figure.figure.repository.MemberRepository;
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

    // 회원가입
    @Transactional
    public MemberResponse signup(SignupRequest request) {

        // 이메일 중복 확인
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException();
        }

        // 비밀번호 해싱
        String encodedPassword =
                passwordEncoder.encode(request.getPassword());

        // 회원 생성
        Member member = new Member(
                request.getEmail(),
                encodedPassword,
                request.getNickname(),
                MemberRole.USER
        );

        // 회원 저장
        Member savedMember = memberRepository.save(member);

        // API 응답 DTO로 변환
        return new MemberResponse(
                savedMember.getId(),
                savedMember.getEmail(),
                savedMember.getNickname(),
                savedMember.getRole()
        );
    }
}