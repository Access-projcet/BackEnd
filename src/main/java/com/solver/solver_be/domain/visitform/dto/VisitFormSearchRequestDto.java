package com.solver.solver_be.domain.visitform.dto;

import lombok.Getter;

@Getter
public class VisitFormSearchRequestDto {

    private String guestName;           // Guest 이름
    private String location;            // 방문 위치
    private String adminName;           // 방문할 분
    private String startDate;           // 방문 시작 날짜
    private String endDate;             // 방문 종료 날짜
    private String purpose;             // 방문 목적
    private String status;              // 상태

}
