package com.solver.solver_be.domain.visitform.service;

import com.solver.solver_be.domain.user.entity.Admin;
import com.solver.solver_be.domain.user.entity.Guest;
import com.solver.solver_be.domain.user.repository.AdminRepository;
import com.solver.solver_be.domain.visitform.dto.VisitFormRequestDto;
import com.solver.solver_be.domain.visitform.dto.VisitFormResponseDto;
import com.solver.solver_be.domain.visitform.dto.VisitFormSearchRequestDto;
import com.solver.solver_be.domain.visitform.entity.VisitForm;
import com.solver.solver_be.domain.visitform.repository.VisitFormRepository;
import com.solver.solver_be.global.exception.exceptionType.VisitFormException;
import com.solver.solver_be.global.response.GlobalResponseDto;
import com.solver.solver_be.global.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class VisitFormService {

    private final AdminRepository adminRepository;
    private final VisitFormRepository visitFormRepository;

    // 1. Create VisitForm
    @Transactional
    public ResponseEntity<GlobalResponseDto> createVisitForm(VisitFormRequestDto visitFormRequestDto, Guest guest) {

        Optional<Admin> admin = adminRepository.findByName(visitFormRequestDto.getTarget());
        if (admin.isEmpty()) {
            throw new VisitFormException(ResponseCode.ADMIN_NOT_FOUND);
        }

        // 중복 방지
        Optional<VisitForm> found = visitFormRepository.findByAdminIdAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                admin.get().getId(),
                LocalDateTime.parse(visitFormRequestDto.getEndTime()),
                LocalDateTime.parse(visitFormRequestDto.getStartTime())
        );
        if (found.isPresent()) {
            throw new VisitFormException(ResponseCode.VISITFORM_EXIST);
        }

        VisitForm visitForm = visitFormRepository.saveAndFlush(VisitForm.of(visitFormRequestDto, guest, admin.get()));

        return ResponseEntity.ok(GlobalResponseDto.of(ResponseCode.VISITFORM_WRITE_SUCCESS, VisitFormResponseDto.of(visitForm, guest)));
    }

    // 2-1. Get VisitForm List ( Guest )
    @Transactional(readOnly = true)
    public ResponseEntity<GlobalResponseDto> getGuestVisitForms(Guest guest) {

        List<VisitForm> visitFormUserList = visitFormRepository.findByGuestId(guest.getId());

        List<VisitFormResponseDto> visitFormResponseDtoList = new ArrayList<>();
        for (VisitForm visitForm : visitFormUserList) {
            visitFormResponseDtoList.add(VisitFormResponseDto.of(visitForm));
        }

        return ResponseEntity.ok(GlobalResponseDto.of(ResponseCode.VISITFORM_GET_SUCCESS, visitFormResponseDtoList));
    }

    // 2-2 Get VisitForm List ( Admin )
    @Transactional(readOnly = true)
    public ResponseEntity<GlobalResponseDto> getAdminVisitForms(Admin admin) {

        List<VisitForm> visitFormUserList = visitFormRepository.findByAdminId(admin.getId());

        List<VisitFormResponseDto> visitFormResponseDtoList = new ArrayList<>();
        for (VisitForm visitForm : visitFormUserList) {
            visitFormResponseDtoList.add(VisitFormResponseDto.of(visitForm));
        }

        return ResponseEntity.ok(GlobalResponseDto.of(ResponseCode.VISITFORM_GET_SUCCESS, visitFormResponseDtoList));
    }

    // 3-1. Update VisitForm ( Guest )
    @Transactional
    public ResponseEntity<GlobalResponseDto> updateGuestVisitForm(Long id, VisitFormRequestDto visitFormRequestDto, Guest guest) {

        VisitForm visitForm = getVisitFormById(id);

        if (!visitForm.getGuest().equals(guest)) {
            throw new VisitFormException(ResponseCode.VISITFORM_UPDATE_FAILED);
        }

        visitForm.update(visitFormRequestDto);

        VisitFormResponseDto visitFormResponseDto = VisitFormResponseDto.of(visitForm, guest);

        return ResponseEntity.ok(GlobalResponseDto.of(ResponseCode.VISITFORM_UPDATE_SUCCESS, visitFormResponseDto));
    }

    // 3-2. Update VisitForm ( Admin )
    @Transactional
    public ResponseEntity<GlobalResponseDto> updateAdminVisitForm(Long id, VisitFormRequestDto visitFormRequestDto, Admin admin){

        VisitForm visitForm = visitFormRepository.findByIdAndAdminId(id, admin.getId());

        if(!visitForm.getAdmin().equals(admin)){
            throw new VisitFormException(ResponseCode.VISITFORM_UPDATE_FAILED);
        }

        visitForm.updateStatus(visitFormRequestDto);

        return ResponseEntity.ok(GlobalResponseDto.of(ResponseCode.VISITFORM_STATUS_UPDATE_SUCCESS));
    }

    // 4. Delete VisitForm
    @Transactional
    public ResponseEntity<GlobalResponseDto> deleteVisitForm(Long id, Guest guest) {

        VisitForm visitForm = getVisitFormById(id);

        if (!visitForm.getGuest().equals(guest)) {
            throw new VisitFormException(ResponseCode.VISITFORM_UPDATE_FAILED);
        }

        visitFormRepository.deleteById(id);

        return ResponseEntity.ok(GlobalResponseDto.of(ResponseCode.VISITFORM_DELETE_SUCCESS));
    }

    // 5. Search VisitForms
    @Transactional
    public ResponseEntity<GlobalResponseDto> searchVisitForms(VisitFormSearchRequestDto requestDto, Admin admin) {

        List<VisitForm> visitFormList = visitFormRepository.findByGuestNameOrLocationOrAdminNameOrStartDateOrEndDateOrPurposeAndStatus(
                requestDto.getGuestName(),
                requestDto.getLocation(),
                requestDto.getAdminName(),
                requestDto.getStartDate(),
                requestDto.getEndDate(),
                requestDto.getPurpose(),
                requestDto.getStatus()
        );
        return ResponseEntity.ok(GlobalResponseDto.of(ResponseCode.VISITFORM_SEARCH_SUCCESS, visitFormList));
    }

    // 6. Sort VisitForms
    @Transactional
    public ResponseEntity<GlobalResponseDto> sortVisitForms(Admin admin, String orderBy){
        List<VisitForm> visitFormList;
        switch (orderBy) {
            case "guestName":
                visitFormList = visitFormRepository.findAllByOrderByGuestNameDesc();
                break;
            case "location":
                visitFormList = visitFormRepository.findAllByOrderByLocationAsc();
                break;
            case "target":
                visitFormList = visitFormRepository.findAllByOrderByAdminNameAsc();
                break;
            case "startDate":
                visitFormList = visitFormRepository.findAllByOrderByStartDateDesc();
                break;
            case "endDate":
                visitFormList = visitFormRepository.findAllByOrderByEndDateAsc();
                break;
            case "purpose":
                visitFormList = visitFormRepository.findAllByOrderByPurposeAsc();
                break;
            default:
                visitFormList = visitFormRepository.findAllByOrderByStatusDesc();
        }

        return ResponseEntity.ok(GlobalResponseDto.of(ResponseCode.VISITFORM_SEARCH_SUCCESS, visitFormList));
    }


    // Get User's VisitForm List
    private VisitForm getVisitFormById(Long id) {
        return visitFormRepository.findById(id).orElseThrow(() -> new VisitFormException(ResponseCode.VISITFORM_NOT_FOUND));
    }

//    @Scheduled(fixedDelay = 1000)
//    public void scheduleFixedDelay()throws InterruptedException {
//        log.info("Log Test");
//        Thread.sleep(1000L);
//    }
}
