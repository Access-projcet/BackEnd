package com.solver.solver_be.global.util.sms.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class SmsRequestDto {

    String type;
    String contentType;
    String countryCode;                         // 나라 고유 번호
    String from;                                // 메세지 보낼 매체
    String content;                             // 메세지 내용
    List<MessageRequestDto> messages;

}
