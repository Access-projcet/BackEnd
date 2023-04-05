package com.solver.solver_be.domain.qrCode.controller;

import com.solver.solver_be.domain.qrCode.service.QRCodeService;
import com.solver.solver_be.global.response.GlobalResponseDto;
import com.solver.solver_be.global.security.webSecurity.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class QRCodeController {

    private final QRCodeService qrCodeService;

    @GetMapping("/qrCode")
    public ResponseEntity<GlobalResponseDto> createQRCode(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return qrCodeService.createQRCode(userDetails.getGuest());
    }

}
