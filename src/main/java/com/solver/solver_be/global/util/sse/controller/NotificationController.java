package com.solver.solver_be.global.util.sse.controller;

import com.solver.solver_be.global.response.GlobalResponseDto;
import com.solver.solver_be.global.security.webSecurity.UserDetailsImpl;
import com.solver.solver_be.global.util.sse.dto.NotificationResponseDto;
import com.solver.solver_be.global.util.sse.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(value = "/subscribe/", produces = "text/event-stream")
    public SseEmitter subscribe(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return notificationService.subscribe(userDetails.getAdmin(), lastEventId);
    }

    @GetMapping("/notifications")
    public List<NotificationResponseDto> findAllNotifications(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return notificationService.findAllNotifications(userDetails.getAdmin());
    }

    @DeleteMapping(value = "/notifications/delete")
    public ResponseEntity<GlobalResponseDto> deleteNotifications(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return notificationService.deleteAllByNotifications(userDetails.getAdmin());
    }

    @DeleteMapping(value = "/notifications/delete/{notificationId}")
    public ResponseEntity<GlobalResponseDto> deleteNotifications(@PathVariable Long notificationId) {
        return notificationService.deleteByNotifications(notificationId);
    }
}
