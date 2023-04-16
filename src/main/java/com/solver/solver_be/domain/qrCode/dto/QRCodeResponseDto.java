package com.solver.solver_be.domain.qrCode.dto;

import com.solver.solver_be.domain.qrCode.entity.QRCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QRCodeResponseDto {

    private String name;            // Guest 이름
    private String phoneNum;        // Guest 휴대폰 번호

    public static QRCodeResponseDto of(QRCode qrCode) {
        return QRCodeResponseDto.builder()
                .name(qrCode.getGuest().getName())
                .phoneNum(qrCode.getGuest().getPhoneNum())
                .build();
    }

}
