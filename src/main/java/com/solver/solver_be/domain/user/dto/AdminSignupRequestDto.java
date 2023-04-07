package com.solver.solver_be.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Builder
public class AdminSignupRequestDto {

    @NotBlank(message = "아이디는 필수입니다.")
    @Pattern(regexp = "^[a-z0-9]{4,10}$", message = "아이디는 4~10자 영문 대 소문자, 숫자를 사용하세요.")
    private String userId;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9~!@#$%^&*()_+=?,./<>{}\\[\\]\\-]{8,15}$", message = "비밀번호는 8~15자리 영문 대소문자(a~z, A~Z), 숫자(0~9), 특수문자를 사용하세요.")
    private String password;

    @NotBlank(message = "개인 전화번호는 필수입니다.")
    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "전화번호는 010-XXXX-XXXX 형식입니다.")
    private String phoneNum;

    @NotBlank(message = "담당자명은 필수입니다.")
    private String name;

    @NotBlank(message = "사업자 등록 번호")
    @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{5}$", message = "사업자 등록 번호는 XXX-XX-XXXXX 형식입니다.")
    private String businessNum;

    @NotBlank(message = "회사명은 필수입니다.")
    private String companyName;

    @NotBlank
    private String companyToken = "";

    public static AdminSignupRequestDto of(String userId, String name, String phoneNum, String companyToken){
        return AdminSignupRequestDto.builder()
                .userId(userId)
                .name(name)
                .phoneNum(phoneNum)
                .companyToken(companyToken)
                .build();
    }
}