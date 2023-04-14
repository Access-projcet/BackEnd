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
    private String location;                // 방문장소
    private String place;                   // 세부 방문위치
    private String target;                  // 방문할 분 (Admin)
    private String purpose;                 // 방문 목적
    private String startDate;               // 방문 시작 날짜
    private String startTime;               // 방문 시작 시간
    private String endDate;                 // 방문 종료 날짜
    private String endTime;                 // 방문 종료 시간
    private String visitor;                 // 방문자 (Guest)
    private String phoneNum;                // 방문자 휴대폰 번호
    private String status;                  // 방문자 상태

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
