package com.solver.solver_be.domain.access.dto;

import com.solver.solver_be.domain.accessRecord.entity.AccessRecord;
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
public class AccessResponseDto {

    private Long id;
    private String location;                // 방문장소
    private String place;                   // 세부 방문위치
    private String target;                  // 방문할 분 (Admin)
    private String purpose;                 // 방문 목적
    private String visitor;                 // 방문자 (Guest)
    private String inTime;                  // 출입시간
    private String outTime;                 // 나간시간

    public static AccessResponseDto of(VisitForm visitForm, AccessRecord accessRecord) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        String startTimeStr = null;
        String endTimeStr = null;

        if (accessRecord.getInTime() != null) {
            startTimeStr = accessRecord.getInTime().format(formatter);
        }
        if (accessRecord.getOutTime() != null) {
            endTimeStr = accessRecord.getOutTime().format(formatter);
        }
        return AccessResponseDto.builder()
                .id(visitForm.getId())
                .location(visitForm.getLocation())
                .place(visitForm.getPlace())
                .target(visitForm.getAdmin().getName())
                .purpose(visitForm.getPurpose())
                .inTime(startTimeStr)
                .outTime(endTimeStr)
                .visitor(visitForm.getGuest().getName())
                .build();
    }

}
