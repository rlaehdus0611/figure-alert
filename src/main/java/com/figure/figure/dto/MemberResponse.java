package com.figure.figure.dto;

import com.figure.figure.model.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberResponse {

    private Long id;
    private String email;
    private String nickname;
    private MemberRole role;
}
