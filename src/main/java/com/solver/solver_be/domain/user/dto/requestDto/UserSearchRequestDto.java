package com.solver.solver_be.domain.user.dto.requestDto;

import lombok.Getter;

@Getter
public class UserSearchRequestDto {

    private String name;            // Guest/Admin 이름
    private String phoneNum;        // Guest/Admin 휴대폰 번호
    private String email;           // Guest/Admin 이메일
}
