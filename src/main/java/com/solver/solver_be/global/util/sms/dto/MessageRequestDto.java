package com.solver.solver_be.global.util.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequestDto {

    String to;                      // 메세지 받을 사용자
    String content;                 // 메세지 내용

}