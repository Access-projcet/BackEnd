package com.solver.solver_be.domain.user.dto;

import lombok.Getter;

@Getter
public class LobbyRequestDto {

    private String email;           // 메일 받을 이메일
    private String companyName;     // 로비 아이디 만들 회사 이름
}
