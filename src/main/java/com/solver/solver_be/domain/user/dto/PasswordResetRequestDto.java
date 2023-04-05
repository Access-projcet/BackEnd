package com.solver.solver_be.domain.user.dto;

import lombok.Getter;

@Getter
public class PasswordResetRequestDto {

    private String userId;
    private String name;
    private String phoneNum;
    private String email;
    private String code;
}
