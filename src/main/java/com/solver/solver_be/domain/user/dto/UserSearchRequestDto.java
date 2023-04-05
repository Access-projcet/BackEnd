package com.solver.solver_be.domain.user.dto;

import lombok.Getter;

@Getter
public class UserSearchRequestDto {

    private String name;
    private String phoneNum;
    private String email;
    private String code;
}
