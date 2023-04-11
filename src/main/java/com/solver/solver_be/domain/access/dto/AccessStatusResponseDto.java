package com.solver.solver_be.domain.access.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessStatusResponseDto {

    private LocalDate date;             // 출입한 날짜
    private Long applyNumber;           // 신청 Guest 수
    private Long approveNumber;         // 허가 Guest 수
    private Long accessNumber;             // 실제 출입 수

    public static AccessStatusResponseDto of(LocalDate date, Long applyNumber, Long approveNumber, Long accessNumber) {
        return AccessStatusResponseDto.builder()
                .applyNumber(applyNumber)
                .approveNumber(approveNumber)
                .accessNumber(accessNumber)
                .date(date)
                .build();
    }
}
