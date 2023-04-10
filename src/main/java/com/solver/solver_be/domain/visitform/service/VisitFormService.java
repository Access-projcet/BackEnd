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
import com.solver.solver_be.global.type.ErrorType;
import com.solver.solver_be.global.type.SuccessType;
import com.solver.solver_be.global.util.sse.service.NotificationService;
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
    private final NotificationService notificationService;

    // 1. Create VisitForm (Guest)
    @Transactional
    public ResponseEntity<GlobalResponseDto> createVisitForm(VisitFormRequestDto visitFormRequestDto, Guest guest) {

        // Admin Check
        Admin admin = adminRepository.findByName(visitFormRequestDto.getTarget());
        if (admin == null) {
            throw new VisitFormException(ErrorType.ADMIN_NOT_FOUND);
        }

        // Admin Company Equals location Check
        if (!admin.getCompany().getCompanyName().equals(visitFormRequestDto.getLocation())) {
            throw new VisitFormException(ErrorType.COMPANY_NOT_EQUALS);
        }

        // VisitForm Duplicated Check
        Optional<VisitForm> found = visitFormRepository.findByAdminIdAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                admin.getId(),
                LocalDateTime.parse(visitFormRequestDto.getEndTime()),
                LocalDateTime.parse(visitFormRequestDto.getStartTime())
        );
        if (found.isPresent()) {
            throw new VisitFormException(ErrorType.VISITFORM_EXIST);
        }

        // VisitFormRepo Save
        VisitForm visitForm = visitFormRepository.saveAndFlush(VisitForm.of(visitFormRequestDto, guest, admin));

        // SSE Send
        notificationService.send(admin, "새로운 " + guest.getName() + "님이 방문했습니다.");

        return ResponseEntity.ok(GlobalResponseDto.of(SuccessType.VISITFORM_WRITE_SUCCESS, VisitFormResponseDto.of(visitForm, guest)));
    }

    // 2-1. Get VisitForm List (Guest)
    @Transactional(readOnly = true)
    public ResponseEntity<GlobalResponseDto> getGuestVisitForms(Guest guest) {

        // Get VisitFormList By GuestId
        List<VisitForm> visitFormList = visitFormRepository.findByGuestId(guest.getId());

        // Create VisitFormResponseDtoList
        List<VisitFormResponseDto> visitFormResponseDtoList = new ArrayList<>();
        for (VisitForm visitForm : visitFormList) {
            visitFormResponseDtoList.add(VisitFormResponseDto.of(visitForm));
        }

        return ResponseEntity.ok(GlobalResponseDto.of(SuccessType.VISITFORM_GET_SUCCESS, visitFormResponseDtoList));
    }

    // 2-2 Get VisitForm List (Admin)
    @Transactional(readOnly = true)
    public ResponseEntity<GlobalResponseDto> getAdminVisitForms(Admin admin) {

        // Get VisitFormList By AdminId
        List<VisitForm> visitFormUserList = visitFormRepository.findByAdminId(admin.getId());

        // Create VisitFormResponseDtoList
        List<VisitFormResponseDto> visitFormResponseDtoList = new ArrayList<>();
        for (VisitForm visitForm : visitFormUserList) {
            visitFormResponseDtoList.add(VisitFormResponseDto.of(visitForm));
        }

        return ResponseEntity.ok(GlobalResponseDto.of(SuccessType.VISITFORM_GET_SUCCESS, visitFormResponseDtoList));
    }

    // 3-1. Update VisitForm (Guest)
    @Transactional
    public ResponseEntity<GlobalResponseDto> updateGuestVisitForm(Long id, VisitFormRequestDto visitFormRequestDto, Guest guest) {

        // Get VisitForm By id
        VisitForm visitForm = getVisitFormById(id);

        // VisitForm Validation
        if (!visitForm.getGuest().equals(guest)) {
            throw new VisitFormException(ErrorType.VISITFORM_UPDATE_FAILED);
        }

        // Update VisitForm Repo
        visitForm.update(visitFormRequestDto);

        // Create VisitFormResponseDto
        VisitFormResponseDto visitFormResponseDto = VisitFormResponseDto.of(visitForm, guest);

        return ResponseEntity.ok(GlobalResponseDto.of(SuccessType.VISITFORM_UPDATE_SUCCESS, visitFormResponseDto));
    }

    // 3-2. Update VisitForm (Admin) ... Change Status Only
    @Transactional
    public ResponseEntity<GlobalResponseDto> updateAdminVisitForm(Long id, VisitFormRequestDto visitFormRequestDto, Admin admin) {

        // Get VisitForm By AdminId
        VisitForm visitForm = visitFormRepository.findByIdAndAdminId(id, admin.getId());

        // VisitForm Validation
        if (!visitForm.getAdmin().getId().equals(admin.getId())) {
            throw new VisitFormException(ErrorType.VISITFORM_UPDATE_FAILED);
        }

        // Update VisitForm
        if (visitFormRequestDto.getStatus().equals("2")) {
            visitForm.updateStatus("승인");
        } else if (visitFormRequestDto.getStatus().equals("3")) {
            visitForm.updateStatus("거절");
        } else {
            throw new VisitFormException(ErrorType.NOT_VALID_STATUS);
        }

        // Update VisitForm Save
        visitFormRepository.save(visitForm);

        return ResponseEntity.ok(GlobalResponseDto.of(SuccessType.VISITFORM_STATUS_UPDATE_SUCCESS));
    }

    // 4. Delete VisitForm
    @Transactional
    public ResponseEntity<GlobalResponseDto> deleteVisitForm(Long id, Guest guest) {

        // Get visitForm By id
        VisitForm visitForm = getVisitFormById(id);

        // VisitForm Validation
        if (!visitForm.getGuest().equals(guest)) {
            throw new VisitFormException(ErrorType.VISITFORM_UPDATE_FAILED);
        }

        // Delete VisitFormRepo
        visitFormRepository.deleteById(id);

        return ResponseEntity.ok(GlobalResponseDto.of(SuccessType.VISITFORM_DELETE_SUCCESS));
    }

    // 5. Search VisitForms
    @Transactional
    public ResponseEntity<GlobalResponseDto> searchVisitForms(VisitFormSearchRequestDto requestDto, Admin admin) {

        // Get VisitFormListBy KeyWords
        List<VisitForm> visitFormList = visitFormRepository.findByGuestNameOrLocationOrAdminNameOrStartDateOrEndDateOrPurposeAndStatus(
                requestDto.getGuestName(),
                requestDto.getLocation(),
                requestDto.getAdminName(),
                requestDto.getStartDate(),
                requestDto.getEndDate(),
                requestDto.getPurpose(),
                requestDto.getStatus()
        );

        return ResponseEntity.ok(GlobalResponseDto.of(SuccessType.VISITFORM_SEARCH_SUCCESS, visitFormList));
    }

    // 6. Sort VisitForms
    @Transactional
    public ResponseEntity<GlobalResponseDto> sortVisitForms(Admin admin, String orderBy, Boolean isAsc) {
        List<VisitForm> visitFormList;
        if (isAsc) {
            // Sort VisitFormList By orderBy And Asc
            switch (orderBy) {
                case "guestName":
                    visitFormList = visitFormRepository.findByAdminIdOrderByGuestNameAsc(admin.getId());
                    break;
                case "location":
                    visitFormList = visitFormRepository.findByAdminIdOrderByLocationAsc(admin.getId());
                    break;
                case "target":
                    visitFormList = visitFormRepository.findByAdminIdOrderByAdminNameAsc(admin.getId());
                    break;
                case "startDate":
                    visitFormList = visitFormRepository.findByAdminIdOrderByStartDateAsc(admin.getId());
                    break;
                case "endDate":
                    visitFormList = visitFormRepository.findByAdminIdOrderByEndDateAsc(admin.getId());
                    break;
                case "purpose":
                    visitFormList = visitFormRepository.findByAdminIdOrderByPurposeAsc(admin.getId());
                    break;
                case "status":
                    visitFormList = visitFormRepository.findByAdminIdOrderByStatusAsc(admin.getId());
                    break;
                default:
                    visitFormList = visitFormRepository.findByAdminId(admin.getId());
            }
        } else {
            // Sort VisitFormList By orderBy And Desc
            switch (orderBy) {
                case "guestName":
                    visitFormList = visitFormRepository.findByAdminIdOrderByGuestNameDesc(admin.getId());
                    break;
                case "location":
                    visitFormList = visitFormRepository.findByAdminIdOrderByLocationDesc(admin.getId());
                    break;
                case "target":
                    visitFormList = visitFormRepository.findByAdminIdOrderByAdminNameDesc(admin.getId());
                    break;
                case "startDate":
                    visitFormList = visitFormRepository.findByAdminIdOrderByStartDateDesc(admin.getId());
                    break;
                case "endDate":
                    visitFormList = visitFormRepository.findByAdminIdOrderByEndDateDesc(admin.getId());
                    break;
                case "purpose":
                    visitFormList = visitFormRepository.findByAdminIdOrderByPurposeDesc(admin.getId());
                    break;
                case "status":
                    visitFormList = visitFormRepository.findByAdminIdOrderByStatusDesc(admin.getId());
                    break;
                default:
                    visitFormList = visitFormRepository.findByAdminId(admin.getId());
            }
        }

        return ResponseEntity.ok(GlobalResponseDto.of(SuccessType.VISITFORM_SEARCH_SUCCESS, visitFormList));
    }


    // Method : Get User's VisitForm List
    private VisitForm getVisitFormById(Long id) {
        return visitFormRepository.findById(id).orElseThrow(() -> new VisitFormException(ErrorType.VISITFORM_NOT_FOUND));
    }
}
