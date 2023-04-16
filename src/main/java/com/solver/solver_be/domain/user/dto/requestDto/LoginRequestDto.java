package com.solver.solver_be.domain.user.dto.requestDto;

import lombok.Getter;

@Getter
public class LoginRequestDto {

    private String userId;          // Guest/Admin Id
    private String password;        // Guest/Admin Password

}