package com.solver.solver_be.domain.company.dto;

import lombok.Getter;

import javax.persistence.Column;

@Getter
public class CompanyRequestDto {

    private String businessNum;         // 사업자 번호

    private String businessCode;        // 회사 인증 Token

    private String companyName;         // 회사 이름

    private String companyCallNum;      // 회사 전화번호

    private String companyAddress;      // 회사 주소

    private Double x;                   // 회사 위도

    private Double y;                   // 회사 경도

}
