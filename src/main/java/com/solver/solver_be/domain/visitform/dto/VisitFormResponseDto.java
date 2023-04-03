package com.solver.solver_be.domain.visitform.dto;

import com.solver.solver_be.domain.user.entity.Guest;
import com.solver.solver_be.domain.visitform.entity.VisitForm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitFormResponseDto {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private Long id;
    private String location;
    private String place;
    private String target;
    private String purpose;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;
    private String visitor;
    private String phoneNum;
    private String status;

    public static VisitFormResponseDto of(VisitForm visitForm) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        String startDateStr = visitForm.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String startTimeStr = visitForm.getStartTime().format(formatter);
        String endDateStr = visitForm.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String endTimeStr = visitForm.getEndTime().format(formatter);

        return VisitFormResponseDto.builder()
                .id(visitForm.getId())
                .location(visitForm.getLocation())
                .target(visitForm.getAdmin().getName())
                .place(visitForm.getPlace())
                .purpose(visitForm.getPurpose())
                .startDate(startDateStr)
                .startTime(startTimeStr)                                     // LocalDateTime 값을 String 으로 변환하여 전달
                .endDate(endDateStr)
                .endTime(endTimeStr)                                         // LocalDateTime 값을 String 으로 변환하여 전달
                .visitor(visitForm.getGuest().getName())
                .phoneNum(visitForm.getGuest().getPhoneNum())
                .status(visitForm.getStatus())
                .build();
    }

    public static VisitFormResponseDto of(VisitForm visitForm, Guest guest) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        String startDateStr = visitForm.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String startTimeStr = visitForm.getStartTime().format(formatter);
        String endDateStr = visitForm.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String endTimeStr = visitForm.getEndTime().format(formatter);

        return VisitFormResponseDto.builder()
                .id(visitForm.getId())
                .location(visitForm.getLocation())
                .target(visitForm.getAdmin().getName())
                .place(visitForm.getPlace())
                .purpose(visitForm.getPurpose())
                .startDate(startDateStr)
                .startTime(startTimeStr)                                     // LocalDateTime 값을 String 으로 변환하여 전달
                .endDate(endDateStr)
                .endTime(endTimeStr)                                         // LocalDateTime 값을 String 으로 변환하여 전달
                .visitor(guest.getName())
                .phoneNum(guest.getPhoneNum())
                .status(visitForm.getStatus())
                .build();
    }
}
