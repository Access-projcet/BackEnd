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

    @Column(nullable = false)           // Visit Company
    private String location;

    @Column(nullable = false)           // Visit Company Space
    private String place;

    @Column(nullable = false)           // Purpose
    private String purpose;

    @Column(nullable = false)           // Visit startDate
    private LocalDate startDate;

    @Column(nullable = false)           // Visit StartTime
    private LocalDateTime startTime;

    @Column(nullable = false)           // Visit EndDate
    private LocalDate endDate;

    @Column(nullable = false)           // Visit EndTime
    private LocalDateTime endTime;

    @Column(nullable = false)           // Approval Status
    private String status;

    @ManyToOne
    @JoinColumn(name = "GUEST_ID")      // Visitor
    private Guest guest;

    @ManyToOne
    @JoinColumn(name = "ADMIN_ID")      // Admin
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
                .status(visitorRequestDto.getStatus())
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

    public void updateStatus(VisitFormRequestDto visitFormRequestDto) {
        this.status = visitFormRequestDto.getStatus();
    }
}
