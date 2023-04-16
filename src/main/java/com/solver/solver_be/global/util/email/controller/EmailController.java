package com.solver.solver_be.global.util.email.controller;

import com.solver.solver_be.global.response.GlobalResponseDto;
import com.solver.solver_be.global.type.SuccessType;
import com.solver.solver_be.global.util.email.dto.EmailRequestDto;
import com.solver.solver_be.global.util.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    // 1. Send to Company token mail
    @PostMapping("/email")
    public ResponseEntity<GlobalResponseDto> sendEmailPath(@RequestBody EmailRequestDto emailRequestDto) throws MessagingException {
        emailService.sendEmail(emailRequestDto);
        return ResponseEntity.ok(GlobalResponseDto.of(SuccessType.EMAIL_CHECK));
    }

    // 2. Send email authentication code by mail
    @PostMapping("/email/authcode")
    public ResponseEntity<GlobalResponseDto> sendAuthCode(@RequestBody EmailRequestDto emailRequestDto) throws MessagingException {
        emailService.sendAuthCode(emailRequestDto);
        return ResponseEntity.ok(GlobalResponseDto.of(SuccessType.EMAIL_CHECK));
    }

}
