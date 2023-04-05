package com.solver.solver_be.domain.access.service;

import com.solver.solver_be.domain.access.dto.AccessRequestDto;
import com.solver.solver_be.domain.access.entity.Access;
import com.solver.solver_be.domain.access.repository.AccessRepository;
import com.solver.solver_be.domain.accessRecord.entity.AccessRecord;
import com.solver.solver_be.domain.accessRecord.repository.AccessRecordRepository;
import com.solver.solver_be.domain.user.entity.Admin;
import com.solver.solver_be.domain.user.entity.Guest;
import com.solver.solver_be.domain.access.dto.AccessStatusResponseDto;
import com.solver.solver_be.domain.user.repository.GuestRepository;
import com.solver.solver_be.domain.visitform.entity.VisitForm;
import com.solver.solver_be.domain.visitform.repository.VisitFormRepository;
import com.solver.solver_be.global.exception.exceptionType.UserException;
import com.solver.solver_be.global.exception.exceptionType.VisitFormException;
import com.solver.solver_be.global.response.GlobalResponseDto;
import com.solver.solver_be.global.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AccessService {

    private final AccessRecordRepository accessRecordRepository;
    private final VisitFormRepository visitFormRepository;
    private final AccessRepository accessRepository;
    private final GuestRepository guestRepository;

    // 1. Access In
    @Transactional
    public ResponseEntity<GlobalResponseDto> accessIn(AccessRequestDto accessInRequestDto, Admin admin) {

        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime startTimeBeforeOneHour = nowTime.minusHours(1);
        LocalDateTime startTimeAfterOneHour = nowTime.plusHours(1);

        Guest guest = guestRepository.findByName(accessInRequestDto.getName()).orElseThrow(
                () -> new UserException(ResponseCode.USER_NOT_FOUND)
        );

        // 1. 게스트 아이디를 통해서 이 친구가 visitForm 데이터에서 이미 신청했던 친구라는 것을 판단.
        // 이는 로비용 어드민 아이디를 하나 만들어서, 그 친구에 대한 데이터를 전달하는 식으로?
        List<VisitForm> visitForms = visitFormRepository.findByGuestAndStartTimeBetweenAndAdmin(guest, startTimeBeforeOneHour, startTimeAfterOneHour, admin);
        if (visitForms == null) {
            throw new VisitFormException(ResponseCode.VISITFORM_NOT_FOUND);
        }

        // 중복된 VisitForm이 있을 경우, 현재 시간과 가장 가까운 StartTime을 가진 VisitForm을 선택합니다.
        VisitForm visitForm;
        if (visitForms.size() > 1) {
            visitForm = visitForms.stream()
                    .min(Comparator.comparingLong(f -> Math.abs(Duration.between(f.getStartTime(), nowTime).toMillis())))
                    .orElseThrow(() -> new VisitFormException(ResponseCode.VISITFORM_NOT_FOUND));
        } else {
            visitForm = visitForms.get(0);
        }

        // 2  신청했고, 이게 승인이 되었다는 것이 판단이 되면 그 visitForm 에서 있었던 데이터들을 가져옴.
        Access access = accessRepository.save(Access.of(guest, admin, visitForm));

        // 3. 또한 이 친구가 언제 들어왔는지를 로그를 찍어주는 Log 를 DI 시켜서 찍어줌.
        accessRecordRepository.save(AccessRecord.of(nowTime, null, access));

        return ResponseEntity.ok(GlobalResponseDto.of(ResponseCode.ACCESS_IN_SUCCESS));
    }

    // 2. Access Out
    @Transactional
    public ResponseEntity<GlobalResponseDto> accessOut(AccessRequestDto accessRequestDto, Admin admin) {

        LocalDateTime outTime = LocalDateTime.now();

        // 1. 출입을 했는지를 판별
        Access access = accessRepository.findByGuestName(accessRequestDto.getName()).orElseThrow(
                () -> new UserException(ResponseCode.USER_NOT_FOUND)
        );

        AccessRecord accessRecord = accessRecordRepository.findLatestAccessRecordByAccess(access).orElseThrow(
                () -> new IllegalArgumentException("해당 손님의 출입 기록이 없습니다.")
        );

        accessRecord.setOutTime(outTime);
        accessRecordRepository.save(accessRecord);
        return ResponseEntity.ok(GlobalResponseDto.of(ResponseCode.ACCESS_OUT_SUCCESS));
    }

    // 3. Get AccessStatus
    @Transactional
    public ResponseEntity<GlobalResponseDto> getAccessStatus(Admin admin) {

        List<VisitForm> visitFormList = visitFormRepository.findByAdminId(admin.getId());
        Map<LocalDate, List<VisitForm>> visitFormMap = new HashMap<>();

        // Cleanup by Date VisitForm List.
        for (VisitForm visitForm : visitFormList) {
            LocalDate date = visitForm.getStartDate();
            List<VisitForm> visitFormByDate = visitFormMap.getOrDefault(date, new ArrayList<>());
            visitFormByDate.add(visitForm);
            visitFormMap.put(date, visitFormByDate);
        }

        // Check visit reservation history by date And AccessStatusResponseDto Creating an Object.
        List<AccessStatusResponseDto> accessStatusResponseDtoList = new ArrayList<>();
        for (LocalDate date : visitFormMap.keySet()) {
            List<VisitForm> visitFormByDate = visitFormMap.get(date);

            int applyCount = visitFormByDate.size();
            int approveCount = 0;
            for (VisitForm visitForm : visitFormByDate) {
                if ("3".equals(visitForm.getStatus())) {
                    approveCount += 1;
                }
            }
            AccessStatusResponseDto accessStatusResponseDto = AccessStatusResponseDto.of(date, (long) applyCount, (long) approveCount, (long) (applyCount + approveCount));
            accessStatusResponseDtoList.add(accessStatusResponseDto);
        }
        return ResponseEntity.ok(GlobalResponseDto.of(ResponseCode.ACCESS_STATUS_SUCCESS, accessStatusResponseDtoList));
    }
}
