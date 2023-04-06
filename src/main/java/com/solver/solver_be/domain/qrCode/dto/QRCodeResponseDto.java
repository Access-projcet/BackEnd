package com.solver.solver_be.domain.qrCode.dto;

import com.solver.solver_be.domain.access.dto.AccessStatusResponseDto;
import com.solver.solver_be.domain.qrCode.entity.QRCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QRCodeResponseDto {

    private String name;
    private String phoneNum;

    public static QRCodeResponseDto of(QRCode qrCode) {
        return QRCodeResponseDto.builder()
                .name(qrCode.getGuest().getName())
                .phoneNum(qrCode.getGuest().getPhoneNum())
                .build();
    }
}
