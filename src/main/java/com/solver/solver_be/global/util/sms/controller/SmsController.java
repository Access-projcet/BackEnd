package com.solver.solver_be.global.util.sms.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.solver.solver_be.global.response.GlobalResponseDto;
import com.solver.solver_be.global.type.ErrorType;
import com.solver.solver_be.global.type.SuccessType;
import com.solver.solver_be.global.util.sms.dto.MessageRequestDto;
import com.solver.solver_be.global.util.sms.dto.SmsResponseDto;
import com.solver.solver_be.global.util.sms.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
public class SmsController {
    private final SmsService smsService;

    @PostMapping("/sms/send")
    public  ResponseEntity<GlobalResponseDto> sendSms(@RequestBody MessageRequestDto messageDto) throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        SmsResponseDto response = smsService.sendSms(messageDto);
        return ResponseEntity.ok(GlobalResponseDto.of(SuccessType.SIGN_UP_SUCCESS,response));
    }
}