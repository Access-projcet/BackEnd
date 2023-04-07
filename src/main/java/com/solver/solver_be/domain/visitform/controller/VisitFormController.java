package com.solver.solver_be.domain.visitform.controller;

import com.solver.solver_be.domain.visitform.dto.VisitFormRequestDto;
import com.solver.solver_be.domain.visitform.dto.VisitFormSearchRequestDto;
import com.solver.solver_be.domain.visitform.service.VisitFormService;
import com.solver.solver_be.global.response.GlobalResponseDto;
import com.solver.solver_be.global.security.webSecurity.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class VisitFormController {

    private final VisitFormService visitorService;

    // 1. Create VisitForm
    @PostMapping("/visit")
    public ResponseEntity<GlobalResponseDto> createVisitForm(@Valid @RequestBody VisitFormRequestDto visitFormRequestDto,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return visitorService.createVisitForm(visitFormRequestDto, userDetails.getGuest());
    }

    // 2-1. Get VisitForm ( Guest )
    @GetMapping("/visit/guest")
    public ResponseEntity<GlobalResponseDto> getGuestVisitForm(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return visitorService.getGuestVisitForms(userDetails.getGuest());
    }

    // 2-2 Get VisitForm ( Admin )
    @GetMapping("/visit/admin")
    public ResponseEntity<GlobalResponseDto> getAdminVisitForm(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return visitorService.getAdminVisitForms(userDetails.getAdmin());
    }

    // 3-1. Update VisitForm ( Guest )
    @PutMapping("/visit/guest/{id}")
    public ResponseEntity<GlobalResponseDto> updateGuestVisitForm(@PathVariable Long id,
                                                                  @RequestBody VisitFormRequestDto visitFormRequestDto,
                                                                  @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return visitorService.updateGuestVisitForm(id, visitFormRequestDto, userDetails.getGuest());
    }

    // 3-2. Update VisitForm ( Admin )
    @PutMapping("/visit/admin/{id}")
    public ResponseEntity<GlobalResponseDto> updateAdminVisitForm(@PathVariable Long id,
                                                                  @RequestBody VisitFormRequestDto visitFormRequestDto,
                                                                  @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return visitorService.updateAdminVisitForm(id, visitFormRequestDto, userDetails.getAdmin());
    }

    // 4. Delete VisitForm
    @DeleteMapping("/visit/{id}")
    public ResponseEntity<GlobalResponseDto> deleteVisitForm(@PathVariable Long id,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return visitorService.deleteVisitForm(id, userDetails.getGuest());
    }

    // 5. Search VisitForms
    @GetMapping("/visit-forms/search")
    public ResponseEntity<GlobalResponseDto> searchVisitForms(@RequestBody VisitFormSearchRequestDto visitFormSearchRequestDto,
                                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return visitorService.searchVisitForms(visitFormSearchRequestDto, userDetails.getAdmin());
    }

    // 6. Sort VisitForms
    @GetMapping("/visit-forms/sort")
    public ResponseEntity<GlobalResponseDto> getLocation(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                         @RequestParam(required = false, value = "orderBy") String orderBy,
                                                         @RequestParam(value = "isAsc") Boolean isAsc) {
        return visitorService.sortVisitForms(userDetails.getAdmin(), orderBy, isAsc);
    }
}
