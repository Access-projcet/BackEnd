package com.solver.solver_be.domain.user.dto.requestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public class PasswordChangeRequestDto {

    @NotBlank(message = "현재 비밀번호 확인은 필수입니다.")
    private String password;

    @NotBlank(message = "새로운 비밀번호 확인은 필수입니다.")
    private String checkPassword;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9~!@#$%^&*()_+=?,./<>{}\\[\\]\\-]{8,15}$", message = "비밀번호는 8~15자리 영문 대소문자(a~z, A~Z), 숫자(0~9), 특수문자를 사용하세요.")
    private String newPassword;
}
