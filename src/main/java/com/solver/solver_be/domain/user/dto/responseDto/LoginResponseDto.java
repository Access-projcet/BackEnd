package com.solver.solver_be.domain.user.dto.responseDto;

import com.solver.solver_be.domain.user.entity.Admin;
import com.solver.solver_be.domain.user.entity.Guest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private String companyName;         // 회사 이름
    private String name;                // 로그인한 Guest/Admin 이름
    private String phoneNum;            // 로그인한 Guest/Admin 휴대폰 번호

    public static LoginResponseDto of(Admin admin) {
        return LoginResponseDto.builder()
                .name(admin.getName())
                .phoneNum(admin.getPhoneNum())
                .build();
    }

    public static LoginResponseDto of(Guest guest) {
        return LoginResponseDto.builder()
                .name(guest.getName())
                .phoneNum(guest.getPhoneNum())
                .build();
    }
}
