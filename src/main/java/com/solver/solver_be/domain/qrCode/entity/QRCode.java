package com.solver.solver_be.domain.qrCode.entity;

import com.solver.solver_be.domain.user.entity.Guest;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QRCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Boolean isIssued;           // QRCode 발급 이력

    @ManyToOne
    @JoinColumn(name = "GUEST_ID", nullable = false)
    private Guest guest;

    public static QRCode of(Boolean isIssued, Guest guest) {
        return QRCode.builder()
                .isIssued(isIssued)
                .guest(guest)
                .build();
    }

}
