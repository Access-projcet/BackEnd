package com.solver.solver_be.domain.company.dto;


import com.solver.solver_be.domain.company.entity.Company;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CompanyResponseDto {

    private Long id;

    private String businessNum;         // 사업자 번호

    private String companyName;         // 회사 이름

    private String companyCallNum;      // 회사 전화 번호

    private String companyAddress;      // 회사 주소

    private Double x;                   // 회사 위도

    private Double y;                   // 회사 경도

    public static CompanyResponseDto of(Company company) {
        return CompanyResponseDto.builder()
                .id(company.getId())
                .businessNum(company.getBusinessNum())
                .companyName(company.getCompanyName())
                .companyAddress(company.getCompanyAddress())
                .companyCallNum(company.getCompanyCallNum())
                .x(company.getX().doubleValue())
                .y(company.getY().doubleValue())
                .build();
    }

}
