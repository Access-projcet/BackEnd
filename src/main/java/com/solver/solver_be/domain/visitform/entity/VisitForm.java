package com.solver.solver_be.domain.visitform.entity;

import com.solver.solver_be.domain.user.entity.Guest;
import com.solver.solver_be.domain.visitform.dto.VisitFormRequestDto;
import com.solver.solver_be.global.util.TimeStamped;
import lombok.*;

import javax.persistence.*;

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

    @Column(nullable = false)           // Target
    private String target;

    @Column(nullable = false)           // Purpose
    private String purpose;

    @Column(nullable = false)           // Visit startDate
    private String startDate;

    @Column(nullable = false)           // Visit StartTime
    private String startTime;

    @Column(nullable = false)           // Visit EndDate
    private String endDate;

    @Column(nullable = false)           // Visit EndTime
    private String endTime;

    @Column(nullable = false)           // Approval Status
    private String status;

    @ManyToOne
    @JoinColumn(name = "GUEST_ID")      // Visitor
    private Guest guest;


    public static VisitForm of(VisitFormRequestDto visitorRequestDto, Guest guest) {
        return VisitForm.builder()
                .location(visitorRequestDto.getLocation())
                .target(visitorRequestDto.getTarget())
                .place(visitorRequestDto.getPlace())
                .purpose(visitorRequestDto.getPurpose())
                .startDate(visitorRequestDto.getStartDate())
                .endDate(visitorRequestDto.getEndDate())
                .startTime(visitorRequestDto.getStartTime())
                .endTime(visitorRequestDto.getEndTime())
                .status(visitorRequestDto.getStatus())
                .guest(guest)
                .build();
    }

    public void update(VisitFormRequestDto visitorRequestDto) {
        this.location = visitorRequestDto.getLocation();
        this.target = visitorRequestDto.getTarget();
        this.place = visitorRequestDto.getPlace();
        this.purpose = visitorRequestDto.getPurpose();
        this.startDate = visitorRequestDto.getStartDate();
        this.endDate = visitorRequestDto.getEndDate();
        this.startTime = visitorRequestDto.getStartTime();
        this.endTime = visitorRequestDto.getEndTime();
    }

    public void updateStatus(VisitFormRequestDto visitFormRequestDto){
        this.status = visitFormRequestDto.getStatus();
    }
}
