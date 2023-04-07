package com.solver.solver_be.domain.company.entity;

import com.solver.solver_be.domain.company.dto.CompanyRequestDto;
import com.solver.solver_be.domain.user.dto.AdminSignupRequestDto;
import com.solver.solver_be.global.util.TimeStamped;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Company extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String businessNum;

    @Column(unique = true)
    private String businessCode;

    @Column(nullable = false)
    private String companyName;

    @Column
    private String companyCallNum;

    @Column
    private String companyAddress;

    @Column(unique = true)
    private String companyToken;

    @Column(unique = true)
    private Boolean lobbyIdIssued;

    @Column(precision = 10, scale = 7)
    private BigDecimal x;

    @Column(precision = 10, scale = 7)
    private BigDecimal y;

    public static Company of(CompanyRequestDto companyRequestDto) {
        return Company.builder()
                .businessNum(companyRequestDto.getBusinessNum())
                .businessCode(companyRequestDto.getBusinessCode())
                .companyName(companyRequestDto.getCompanyName())
                .companyAddress(companyRequestDto.getCompanyAddress())
                .companyCallNum(companyRequestDto.getCompanyCallNum())
                .x(BigDecimal.valueOf(companyRequestDto.getX()))
                .y(BigDecimal.valueOf(companyRequestDto.getY()))
                .build();
    }

    public static Company of(CompanyRequestDto companyRequestDto, String companyToken) {
        return Company.builder()
                .businessNum(companyRequestDto.getBusinessNum())
                .businessCode(companyRequestDto.getBusinessCode())
                .companyName(companyRequestDto.getCompanyName())
                .companyAddress(companyRequestDto.getCompanyAddress())
                .companyCallNum(companyRequestDto.getCompanyCallNum())
                .x(BigDecimal.valueOf(companyRequestDto.getX()))
                .y(BigDecimal.valueOf(companyRequestDto.getY()))
                .companyToken(companyToken)
                .build();
    }

    public void update(CompanyRequestDto companyRequestDto) {
        this.businessNum = companyRequestDto.getBusinessNum();
        this.businessCode = companyRequestDto.getBusinessCode();
        this.companyName = companyRequestDto.getCompanyName();
        this.companyAddress = companyRequestDto.getCompanyAddress();
        this.companyCallNum = companyRequestDto.getCompanyCallNum();
        this.x = BigDecimal.valueOf(companyRequestDto.getX());
        this.y = BigDecimal.valueOf(companyRequestDto.getY());
    }
}
