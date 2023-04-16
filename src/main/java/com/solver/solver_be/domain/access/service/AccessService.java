package com.solver.solver_be.domain.access.service;

import com.solver.solver_be.domain.access.dto.AccessRequestDto;
import com.solver.solver_be.domain.access.dto.AccessResponseDto;
import com.solver.solver_be.domain.access.dto.AccessStatusResponseDto;
import com.solver.solver_be.domain.access.entity.Access;
import com.solver.solver_be.domain.access.repository.AccessRepository;
import com.solver.solver_be.domain.accessRecord.entity.AccessRecord;
import com.solver.solver_be.domain.accessRecord.repository.AccessRecordRepository;
import com.solver.solver_be.domain.user.entity.Admin;
import com.solver.solver_be.domain.user.entity.Guest;
import com.solver.solver_be.domain.user.repository.GuestRepository;
import com.solver.solver_be.domain.visitform.entity.VisitForm;
import com.solver.solver_be.domain.visitform.repository.VisitFormRepository;
import com.solver.solver_be.global.exception.exceptionType.AccessException;
import com.solver.solver_be.global.exception.exceptionType.AccessRecordException;
import com.solver.solver_be.global.exception.exceptionType.UserException;
import com.solver.solver_be.global.exception.exceptionType.VisitFormException;
import com.solver.solver_be.global.response.GlobalResponseDto;
import com.solver.solver_be.global.type.ErrorType;
import com.solver.solver_be.global.type.FormStatusType;
import com.solver.solver_be.global.type.SuccessType;
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

    private final GuestRepository guestRepository;
    private final AccessRepository accessRepository;
    private final VisitFormRepository visitFormRepository;
    private final AccessRecordRepository accessRecordRepository;

    // 1. Access In
    @Transactional
    public ResponseEntity<GlobalResponseDto> accessIn(AccessRequestDto accessInRequestDto, Admin admin) {

        // DateTime Init
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime startTimeBeforeOneHour = nowTime.minusHours(1);
        LocalDateTime startTimeAfterOneHour = nowTime.plusHours(1);

        // Guest check
        Guest guest = guestRepository.findByName(accessInRequestDto.getName()).orElseThrow(
                () -> new UserException(ErrorType.USER_NOT_FOUND)
        );

        // Get VisitFormList By Info
        List<VisitForm> visitForms = visitFormRepository.findByGuestAndStartTimeBetweenAndAdminCompany(guest, startTimeBeforeOneHour, startTimeAfterOneHour, admin.getCompany());
        if (visitForms == null) {
            throw new VisitFormException(ErrorType.VISITFORM_NOT_FOUND);
        }

        // Get VisitForm by Time
        VisitForm visitForm = null;
        if (visitForms.size() > 1) {
            long minDuration = Long.MAX_VALUE;
            for (VisitForm form : visitForms) {
                long duration = Math.abs(Duration.between(form.getStartTime(), nowTime).toMillis());
                if (duration < minDuration) {
                    visitForm = form;
                    minDuration = duration;
                }
            }
            if (visitForm == null) {
                throw new VisitFormException(ErrorType.VISITFORM_NOT_FOUND);
            }
        } else if (visitForms.size() == 1) {
            visitForm = visitForms.get(0);
        } else {
            throw new VisitFormException(ErrorType.VISITFORM_NOT_FOUND);
        }

        // Visit completed
        visitForm.updateStatus(FormStatusType.COMPLETE.getMessage());
        visitFormRepository.save(visitForm);

        // Already CheckIn Check
        Optional<Access> accessCheck = accessRepository.findByVisitFormId(visitForm.getId());
        if (accessCheck.isPresent()) {
            if (accessCheck.get().getStatus()) {
                throw new AccessException(ErrorType.ACCESS_IN_ALREADY_DONE);
            }
            accessCheck.get().setStatus(true);
            accessRepository.save(accessCheck.get());
            accessRecordRepository.save(AccessRecord.of(nowTime, null, accessCheck.get()));
        }
        else{
            // Save AccessRepo
            Access access = accessRepository.save(Access.of(guest, visitForm.getAdmin(), visitForm, true));
            // Save AccessRecordRepo
            accessRecordRepository.save(AccessRecord.of(nowTime, null, access));
        }

        return ResponseEntity.ok(GlobalResponseDto.of(SuccessType.ACCESS_IN_SUCCESS));
    }

    // 2. Access Out
    @Transactional
    public ResponseEntity<GlobalResponseDto> accessOut(AccessRequestDto accessRequestDto, Admin admin) {

        // DateTime Init
        LocalDateTime outTime = LocalDateTime.now();

        // Access Check
        Access access = accessRepository.findLatestByGuestNameAndGuestPhoneNum(accessRequestDto.getName(), accessRequestDto.getPhoneNum()).orElseThrow(
                () -> new UserException(ErrorType.USER_NOT_FOUND)
        );

        // Already Checkout Check
        if (!access.getStatus()) {
            throw new AccessException(ErrorType.ACCESS_OUT_ALREADY_DONE);
        }


        // AccessRecord Check (Latest)
        AccessRecord accessRecord = accessRecordRepository.findTopByAccessOrderByInTimeDesc(access).orElseThrow(
                () -> new AccessRecordException(ErrorType.ACCESS_RECORD_NOT_FOUND)
        );

        // Set AccessRecord Out and Save Repo
        accessRecord.setOutTime(outTime);
        accessRecordRepository.save(accessRecord);

        // Set Access Status false
        access.setStatus(false);
        accessRepository.save(access);

        return ResponseEntity.ok(GlobalResponseDto.of(SuccessType.ACCESS_OUT_SUCCESS));
    }

    // 3. Get AccessStatus
    @Transactional
    public ResponseEntity<GlobalResponseDto> getAccessStatus(Admin admin) {

        // Get visitFormList By AdminId
        List<VisitForm> visitFormList = visitFormRepository.findByAdminId(admin.getId());
        Map<LocalDate, List<VisitForm>> visitFormMap = new HashMap<>();

        // VisitFormList to VisitFormMap By startDate
        for (VisitForm visitForm : visitFormList) {
            LocalDate date = visitForm.getStartDate();
            List<VisitForm> visitFormByDate = visitFormMap.getOrDefault(date, new ArrayList<>());
            visitFormByDate.add(visitForm);
            visitFormMap.put(date, visitFormByDate);
        }

        // Make AccessStatusResponseDtoList
        List<AccessStatusResponseDto> accessStatusResponseDtoList = new ArrayList<>();
        for (LocalDate date : visitFormMap.keySet()) {
            List<VisitForm> visitFormByDate = visitFormMap.get(date);
            int applyCount = visitFormByDate.size();
            int approveCount = 0;
            int accessCount = 0;
            for (VisitForm visitForm : visitFormByDate) {
                if (FormStatusType.ACCEPT.getMessage().equals(visitForm.getStatus())) {
                    approveCount += 1;
                }
                if (FormStatusType.COMPLETE.getMessage().equals(visitForm.getStatus())) {
                    accessCount += 1;
                }
            }
            AccessStatusResponseDto accessStatusResponseDto = AccessStatusResponseDto.of(date, (long) applyCount, (long) approveCount, (long) (accessCount));
            accessStatusResponseDtoList.add(accessStatusResponseDto);
        }

        return ResponseEntity.ok(GlobalResponseDto.of(SuccessType.ACCESS_STATUS_SUCCESS, accessStatusResponseDtoList));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<GlobalResponseDto> getAccessList(Admin admin) {

        // Get VisitFormList By AdminId
        List<Access> accessList = accessRepository.findByAdminId(admin.getId());

        // Create VisitFormResponseDtoList
        List<AccessResponseDto> accessResponseDtoList = new ArrayList<>();
        for (Access access : accessList) {
            AccessRecord accessRecord = accessRecordRepository.findFirstByAccessOrderByInTimeDesc(access);
            accessResponseDtoList.add(AccessResponseDto.of(access.getVisitForm(), accessRecord));
        }

        return ResponseEntity.ok(GlobalResponseDto.of(SuccessType.VISITFORM_GET_SUCCESS, accessResponseDtoList));
    }

}

