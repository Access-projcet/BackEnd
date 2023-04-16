package com.solver.solver_be.global.util.sms.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class SmsResponseDto {

    String requestId;
    LocalDateTime requestTime;
    String statusCode;
    String statusName;

}
