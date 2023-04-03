package com.solver.solver_be.domain.access.controller;

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

    @PostMapping("/access-in/{guestId}")
    public ResponseEntity<GlobalResponseDto> accessIn(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                      @PathVariable Long guestId) {
        return accessService.accessIn(guestId, userDetails.getAdmin());
    }

    @PutMapping("/access-out/{guestId}")
    public ResponseEntity<GlobalResponseDto> accessOut(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                       @PathVariable Long guestId) {
        return accessService.accessOut(guestId, userDetails.getAdmin());
    }

    @GetMapping("/access-status")
    public ResponseEntity<GlobalResponseDto> getAccessStatus(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return accessService.getAccessStatus(userDetails.getAdmin());
    }
}
