package com.solver.solver_be.domain.qrCode.service;

import com.solver.solver_be.domain.qrCode.dto.QRCodeResponseDto;
import com.solver.solver_be.domain.qrCode.entity.QRCode;
import com.solver.solver_be.domain.qrCode.repository.QRCodeRepository;
import com.solver.solver_be.domain.user.entity.Guest;
import com.solver.solver_be.global.response.GlobalResponseDto;
import com.solver.solver_be.global.type.ErrorType;
import com.solver.solver_be.global.type.SuccessType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class QRCodeService {

    private final QRCodeRepository qrCodeRepository;

    // 1. Get Info for QRCode Create
    public ResponseEntity<GlobalResponseDto> createQRCode(Guest guest) {

        // QRCodeRepo save
        QRCode qrCode = qrCodeRepository.save(QRCode.of(true,guest));

        return ResponseEntity.ok(GlobalResponseDto.of(SuccessType.ACCESS_IN_SUCCESS, QRCodeResponseDto.of(qrCode)));
    }
}
