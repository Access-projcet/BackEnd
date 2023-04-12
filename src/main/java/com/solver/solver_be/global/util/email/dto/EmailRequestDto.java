package com.solver.solver_be.global.util.email.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmailRequestDto {

    private String companyName;         // 회사 토큰 발급을 위한 회사 이름
    private String email;               // 회사 토큰을 받을 이메일 / 이메일 인증 토큰을 받을 이메일
    private String code;                // 이메일 인증 토큰
}
