package com.solver.solver_be.domain.user.controller;

import com.solver.solver_be.domain.user.dto.requestDto.*;
import com.solver.solver_be.domain.user.service.GuestService;
import com.solver.solver_be.global.response.GlobalResponseDto;
import com.solver.solver_be.global.security.webSecurity.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GuestController {

    private final GuestService guestService;

    // 1. Guest Signup
    @PostMapping("/guest/signup")
    public ResponseEntity<GlobalResponseDto> signupGuest(@Valid @RequestBody GuestSignupRequestDto signupRequestDto) {
        return guestService.signupGuest(signupRequestDto);
    }

    // 2. Guest Login
    @PostMapping("/guest/login")
    public ResponseEntity<GlobalResponseDto> loginGuest(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        return guestService.login(loginRequestDto, response);
    }

    // 3. Change Guest Password
    @PutMapping("/guest/password")
    public ResponseEntity<GlobalResponseDto> changeGuest(@Valid @RequestBody PasswordChangeRequestDto passwordChangeRequestDto,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return guestService.changePassword(passwordChangeRequestDto, userDetails.getGuest());
    }

    // 4. Find Guest UserId
    @PostMapping("/guest/user-id")
    public ResponseEntity<GlobalResponseDto> guestUserData(@RequestBody UserSearchRequestDto userSearchRequestDto) throws MessagingException {
        return guestService.findGuestUserData(userSearchRequestDto);
    }

    // 5. Reset Guest Password
    @PostMapping("/guest/password")
    public ResponseEntity<GlobalResponseDto> resetGuestPassword(@RequestBody PasswordResetRequestDto passwordResetRequestDto) throws MessagingException {
        return guestService.resetGuestPassword(passwordResetRequestDto);
    }

}
