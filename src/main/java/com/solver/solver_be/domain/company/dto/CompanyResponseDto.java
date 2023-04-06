package com.solver.solver_be.domain.company.dto;


import com.solver.solver_be.domain.company.entity.Company;
import com.solver.solver_be.domain.user.entity.Admin;
import com.solver.solver_be.domain.user.entity.Guest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CompanyResponseDto {

    private Long id;

    private String businessNum;         // 사업자 번호

    private String businessCode;        // 회사 인증 Token

    private String companyName;         // 회사 이름

    private String companyCallNum;      // 회사 전화 번호

    private String companyAddress;      // 회사 주소

    private Double x;                   // 회사 위도

    private Double y;                   // 회사 경도

    public static CompanyResponseDto of(Company company) {
        return CompanyResponseDto.builder()
                .id(company.getId())
                .businessNum(company.getBusinessNum())
                .businessCode(company.getBusinessCode())
                .companyName(company.getCompanyName())
                .companyAddress(company.getCompanyAddress())
                .companyCallNum(company.getCompanyCallNum())
                .x(company.getX().doubleValue())
                .y(company.getY().doubleValue())
                .build();
    }
}
