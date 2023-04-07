package com.solver.solver_be.domain.user.entity;

import com.solver.solver_be.domain.user.dto.GuestSignupRequestDto;
import com.solver.solver_be.global.type.UserRoleEnum;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phoneNum;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    public static Guest of(GuestSignupRequestDto signupRequestDto, String password, UserRoleEnum role) {
        return Guest.builder()
                .userId(signupRequestDto.getUserId())
                .password(password)
                .phoneNum(signupRequestDto.getPhoneNum())
                .name(signupRequestDto.getName())
                .role(role)
                .build();
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
