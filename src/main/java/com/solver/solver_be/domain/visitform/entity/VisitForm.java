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

    public static VisitForm of(VisitFormRequestDto visitFormRequestDto, Guest guest, Admin admin) {
        return VisitForm.builder()
                .location(visitFormRequestDto.getLocation())
                .place(visitFormRequestDto.getPlace())
                .purpose(visitFormRequestDto.getPurpose())
                .startDate(LocalDate.parse(visitFormRequestDto.getStartDate()))
                .endDate(LocalDate.parse(visitFormRequestDto.getEndDate()))
                .startTime(LocalDateTime.parse(visitFormRequestDto.getStartTime()))
                .endTime(LocalDateTime.parse(visitFormRequestDto.getEndTime()))
                .status("대기")
                .guest(guest)
                .admin(admin)
                .build();
    }

    public void update(VisitFormRequestDto visitFormRequestDto) {
        this.location = visitFormRequestDto.getLocation();
        this.place = visitFormRequestDto.getPlace();
        this.purpose = visitFormRequestDto.getPurpose();
        this.startDate = LocalDate.parse(visitFormRequestDto.getStartDate());
        this.endDate = LocalDate.parse(visitFormRequestDto.getEndDate());
        this.startTime = LocalDateTime.parse(visitFormRequestDto.getStartTime());
        this.endTime = LocalDateTime.parse(visitFormRequestDto.getEndTime());
    }

    public void updateStatus(String status) {
        this.status = status;
    }
}
