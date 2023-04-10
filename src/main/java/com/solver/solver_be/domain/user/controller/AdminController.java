package com.solver.solver_be.domain.user.controller;

import com.solver.solver_be.domain.user.dto.requestDto.*;
import com.solver.solver_be.domain.user.service.AdminService;
import com.solver.solver_be.global.response.GlobalResponseDto;
import com.solver.solver_be.global.security.webSecurity.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // 1. Admin SignUp
    @PostMapping("/admin/signup")
    public ResponseEntity<GlobalResponseDto> signupBusiness(@Valid @RequestBody AdminSignupRequestDto signupRequestDto) {
        return adminService.signupBusiness(signupRequestDto);
    }

    // 2. Admin Login
    @PostMapping("/admin/login")
    public ResponseEntity<GlobalResponseDto> loginBusiness(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        return adminService.login(loginRequestDto, response);
    }

    // 3. Change Admin Password
    @PutMapping("/admin/password")
    public ResponseEntity<GlobalResponseDto> changeBusiness(@Valid @RequestBody PasswordChangeRequestDto passwordChangeRequestDto,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return adminService.changePassword(passwordChangeRequestDto, userDetails.getAdmin());
    }

    // 4. Find Admin UserId
    @PostMapping("/admin/user-id")
    public ResponseEntity<GlobalResponseDto> adminIdSearch(@RequestBody UserSearchRequestDto userSearchRequestDto) throws MessagingException {
        return adminService.findAdminSearchId(userSearchRequestDto);
    }

    // 5. Reset Admin Password
    @PostMapping("/admin/password")
    public ResponseEntity<GlobalResponseDto> resetAdminPassword(@RequestBody PasswordResetRequestDto passwordResetRequestDto) throws MessagingException {
        return adminService.resetAdminPassword(passwordResetRequestDto);
    }

    // 6. Create Admin LobbyId
    @PostMapping("/admin/lobby")
    public ResponseEntity<GlobalResponseDto> createLobbyId(@RequestBody LobbyRequestDto lobbyRequestDto,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) throws MessagingException {
        return adminService.createLobbyId(lobbyRequestDto, userDetails.getAdmin());
    }
}