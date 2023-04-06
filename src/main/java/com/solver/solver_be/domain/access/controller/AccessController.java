package com.solver.solver_be.domain.access.controller;

import com.solver.solver_be.domain.access.dto.AccessRequestDto;
import com.solver.solver_be.domain.access.service.AccessService;
import com.solver.solver_be.global.response.GlobalResponseDto;
import com.solver.solver_be.global.security.webSecurity.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AccessController {

    private final AccessService accessService;

    // 1. Access In
    @PostMapping("/access-in")
    public ResponseEntity<GlobalResponseDto> accessIn(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                      @RequestBody AccessRequestDto accessInRequestDto) {
        return accessService.accessIn(accessInRequestDto, userDetails.getAdmin());
    }

    // 2. Access Out
    @PutMapping("/access-out")
    public ResponseEntity<GlobalResponseDto> accessOut(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                       @RequestBody AccessRequestDto accessInRequestDto) {
        return accessService.accessOut(accessInRequestDto, userDetails.getAdmin());
    }

    // 3. Access Status
    @GetMapping("/access-status")
    public ResponseEntity<GlobalResponseDto> getAccessStatus(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return accessService.getAccessStatus(userDetails.getAdmin());
    }
}
