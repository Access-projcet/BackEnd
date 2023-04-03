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

    private LocalDate date;
    private Long applyNumber;
    private Long approveNumber;
    private Long sumNumber;

    public static AccessStatusResponseDto of(LocalDate date, Long applyNumber, Long approveNumber, Long sumNumber) {
        return AccessStatusResponseDto.builder()
                .applyNumber(applyNumber)
                .approveNumber(approveNumber)
                .date(date)
                .sumNumber(sumNumber)
                .build();
    }
}
