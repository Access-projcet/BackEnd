package com.solver.solver_be.domain.user.controller;

import com.solver.solver_be.domain.user.dto.AdminSignupRequestDto;
import com.solver.solver_be.domain.user.dto.GuestSignupRequestDto;
import com.solver.solver_be.domain.user.dto.LoginRequestDto;
import com.solver.solver_be.domain.user.dto.PasswordChangeRequestDto;
import com.solver.solver_be.domain.user.service.AdminService;
import com.solver.solver_be.domain.user.service.GuestService;
import com.solver.solver_be.global.response.GlobalResponseDto;
import com.solver.solver_be.global.security.webSecurity.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final AdminService adminService;
    private final GuestService guestService;

    // 1. Admin SignUp
    @PostMapping("/signup/business")
    public ResponseEntity<GlobalResponseDto> signupBusiness(@Valid @RequestBody AdminSignupRequestDto signupRequestDto) {
        return adminService.signupBusiness(signupRequestDto);
    }

    // 2. Admin Login
    @PostMapping("/login/business")
    public ResponseEntity<GlobalResponseDto> loginBusiness(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        return adminService.login(loginRequestDto, response);
    }

    // 4. Admin change password
    @PutMapping("/change/password/business")
    public ResponseEntity<GlobalResponseDto> changeBusiness(@Valid @RequestBody PasswordChangeRequestDto passwordChangeRequestDto,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return adminService.changePassword(passwordChangeRequestDto, userDetails.getAdmin());
    }

    // 5. Guest Signup
    @PostMapping("/signup/guest")
    public ResponseEntity<GlobalResponseDto> signupGuest(@Valid @RequestBody GuestSignupRequestDto signupRequestDto) {
        return guestService.signupGuest(signupRequestDto);
    }

    // 6. Guest Login
    @PostMapping("/login/guest")
    public ResponseEntity<GlobalResponseDto> loginGuest(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        return guestService.login(loginRequestDto, response);
    }

    // 7. Guest change password
    @PutMapping("/change/password/guest")
    public ResponseEntity<GlobalResponseDto> changeGuest(@Valid @RequestBody PasswordChangeRequestDto passwordChangeRequestDto,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return guestService.changePassword(passwordChangeRequestDto, userDetails.getGuest());
    }
}