package com.solver.solver_be.domain.visitform.entity;

import com.solver.solver_be.domain.user.entity.Admin;
import com.solver.solver_be.domain.user.entity.Guest;
import com.solver.solver_be.domain.visitform.dto.VisitFormRequestDto;
import com.solver.solver_be.global.util.TimeStamped;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VisitForm extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)           // 방문 회사
    private String location;

    @Column(nullable = false)           // 세부 방문 장소
    private String place;

    @Column(nullable = false)           // 방문 목적
    private String purpose;

    @Column(nullable = false)           // 방문 시작 날짜
    private LocalDate startDate;

    @Column(nullable = false)           // 방문 시작 시간
    private LocalDateTime startTime;

    @Column(nullable = false)           // 방문 종료 날짜
    private LocalDate endDate;

    @Column(nullable = false)           // 방문 종료 시간
    private LocalDateTime endTime;

    @Column(nullable = false)           // 허가 여부
    private String status;

    @ManyToOne
    @JoinColumn(name = "GUEST_ID")      // 방문객 (Guest)
    private Guest guest;

    @ManyToOne
    @JoinColumn(name = "ADMIN_ID")      // 찾아갈 분 (Admin)
    private Admin admin;

    public static VisitForm of(VisitFormRequestDto visitorRequestDto, Guest guest, Admin admin) {
        return VisitForm.builder()
                .location(visitorRequestDto.getLocation())
                .place(visitorRequestDto.getPlace())
                .purpose(visitorRequestDto.getPurpose())
                .startDate(LocalDate.parse(visitorRequestDto.getStartDate()))
                .endDate(LocalDate.parse(visitorRequestDto.getEndDate()))
                .startTime(LocalDateTime.parse(visitorRequestDto.getStartTime()))
                .endTime(LocalDateTime.parse(visitorRequestDto.getEndTime()))
                .status("대기")
                .guest(guest)
                .admin(admin)
                .build();
    }

    public void update(VisitFormRequestDto visitorRequestDto) {
        this.location = visitorRequestDto.getLocation();
        this.place = visitorRequestDto.getPlace();
        this.purpose = visitorRequestDto.getPurpose();
        this.startDate = LocalDate.parse(visitorRequestDto.getStartDate());
        this.endDate = LocalDate.parse(visitorRequestDto.getEndDate());
        this.startTime = LocalDateTime.parse(visitorRequestDto.getStartTime());
        this.endTime = LocalDateTime.parse(visitorRequestDto.getEndTime());
    }

    public void updateStatus(String status) {
        this.status = status;
    }
}
