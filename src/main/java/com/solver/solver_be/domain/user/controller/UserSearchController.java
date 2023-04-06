package com.solver.solver_be.domain.user.controller;

import com.solver.solver_be.domain.user.dto.PasswordResetRequestDto;
import com.solver.solver_be.domain.user.dto.UserSearchRequestDto;
import com.solver.solver_be.domain.user.service.PasswordResetService;
import com.solver.solver_be.domain.user.service.UserSearchService;
import com.solver.solver_be.global.response.GlobalResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RestController
@RequiredArgsConstructor
public class UserSearchController {

    private final UserSearchService searchService;
    private final PasswordResetService passwordResetService;

    // 1. Found Guest userId
    @PostMapping("/guest/search")
    public ResponseEntity<GlobalResponseDto> guestIdSearch(@RequestBody UserSearchRequestDto userSearchRequestDto) throws MessagingException {
        return searchService.findGuestSearchId(userSearchRequestDto);
    }

    // 2. Found Admin userId
    @PostMapping("/admin/search")
    public ResponseEntity<GlobalResponseDto> adminIdSearch(@RequestBody UserSearchRequestDto userSearchRequestDto) throws MessagingException {
        return searchService.findAdminSearchId(userSearchRequestDto);
    }

    // 3. Reset Guest Password
    @PostMapping("/guest/password")
    public ResponseEntity<GlobalResponseDto> resetGuestPassword(@RequestBody PasswordResetRequestDto passwordResetRequestDto) throws MessagingException {
        return passwordResetService.resetGuestPassword(passwordResetRequestDto);
    }

    // 4. Reset Admin Password
    @PostMapping("/admin/password")
    public ResponseEntity<GlobalResponseDto> resetAdminPassword(@RequestBody PasswordResetRequestDto passwordResetRequestDto) throws MessagingException {
        return passwordResetService.resetAdminPassword(passwordResetRequestDto);
    }
}
