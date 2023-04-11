package com.solver.solver_be.domain.qrCode.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.solver.solver_be.domain.qrCode.dto.QRCodeRequestDto;
import com.solver.solver_be.domain.qrCode.service.QRCodeService;
import com.solver.solver_be.global.response.GlobalResponseDto;
import com.solver.solver_be.global.security.webSecurity.UserDetailsImpl;
import com.solver.solver_be.global.type.SuccessType;
import com.solver.solver_be.global.util.sms.dto.MessageRequestDto;
import com.solver.solver_be.global.util.sms.dto.SmsResponseDto;
import com.solver.solver_be.global.util.sms.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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
public class QRCodeController {

    private final QRCodeService qrCodeService;
    private final SmsService smsService;

    // 1. Get Info for QRCode Create
    @GetMapping("/qrCode")
    public ResponseEntity<GlobalResponseDto> createQRCode(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return qrCodeService.createQRCode(userDetails.getGuest());
    }

    // 2. Send QRCode Message
    @PostMapping("/qrCode")
    public ResponseEntity<GlobalResponseDto> sendQRCode(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody QRCodeRequestDto qrCodeRequestDto) throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        String sendNumber = userDetails.getGuest().getPhoneNum().replaceAll("-", "");
        MessageRequestDto messageRequestDto = MessageRequestDto.builder()
                .to(sendNumber)
                .content(qrCodeRequestDto.getImgUrl())
                .build();
        SmsResponseDto response = smsService.sendSms(messageRequestDto);
        return ResponseEntity.ok(GlobalResponseDto.of(SuccessType.SIGN_UP_SUCCESS, response));
    }
}
