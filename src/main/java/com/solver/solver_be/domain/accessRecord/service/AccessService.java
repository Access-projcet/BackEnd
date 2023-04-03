package com.solver.solver_be.domain.accessRecord.service;

import com.solver.solver_be.domain.accessRecord.dto.AccessRecordRequestDto;
import com.solver.solver_be.domain.accessRecord.entity.AccessRecord;
import com.solver.solver_be.domain.accessRecord.repository.AccessRecordRepository;
import com.solver.solver_be.domain.user.entity.Guest;
import com.solver.solver_be.domain.visitform.entity.VisitForm;
import com.solver.solver_be.domain.visitform.repository.VisitFormRepository;
import com.solver.solver_be.global.response.GlobalResponseDto;
import com.solver.solver_be.global.response.ResponseCode;
import com.solver.solver_be.global.util.TimeStamped;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AccessService extends TimeStamped {

    private final AccessRecordRepository accessRepository;
    private final VisitFormRepository visitFormRepository;

    // 1. Create a Record of Access
    @Transactional
    public ResponseEntity<GlobalResponseDto> AccessIn(Guest guest, AccessRecordRequestDto accessRecordRequestDto) {
        VisitForm visitForm = visitFormRepository.findByGuestIdAndStartDateAndLocation(guest.getId(), accessRecordRequestDto.getStartDate(), accessRecordRequestDto.getLocation());
        LocalDateTime inTime = LocalDateTime.now();
        accessRepository.save(AccessRecord.of(inTime, null, guest, visitForm));
        return ResponseEntity.ok(GlobalResponseDto.of(ResponseCode.ACCESS_IN_SUCCESS));
    }

    // 2. Update a Record of Access
    @Transactional
    public ResponseEntity<GlobalResponseDto> AccessOut(Guest guest) {
        LocalDateTime outTime = LocalDateTime.now();
        AccessRecord accessRecord = accessRepository.findLatestAccessRecordByGuest(guest)
                .orElseThrow(() -> new IllegalArgumentException("해당 손님의 출입 기록이 없습니다."));
        accessRecord.setOutTime(outTime);
        accessRepository.save(accessRecord);
        return ResponseEntity.ok(GlobalResponseDto.of(ResponseCode.ACCESS_OUT_SUCCESS));
    }
}
