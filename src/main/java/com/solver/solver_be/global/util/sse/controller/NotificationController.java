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

    // 1. SSE Connection
    @GetMapping(value = "/subscribe/", produces = "text/event-stream")
    public SseEmitter subscribe(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return notificationService.subscribe(userDetails.getAdmin(), lastEventId);
    }

    // 2. View All Notifications
    @GetMapping("/notifications")
    public List<NotificationResponseDto> findAllNotifications(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return notificationService.findAllNotifications(userDetails.getAdmin());
    }

    // 3. Check Read Notification
    @PutMapping("/notification/{notificationId}")
    public ResponseEntity<GlobalResponseDto> isReadNotification(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                @PathVariable Long notificationId){
        return notificationService.isReadNotification(userDetails.getAdmin(), notificationId);
    }

    // 4. Delete a Notification
    @DeleteMapping(value = "/notifications/delete")
    public ResponseEntity<GlobalResponseDto> deleteNotifications(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return notificationService.deleteAllByNotifications(userDetails.getAdmin());
    }

    // 5. Delete All Notifications
    @DeleteMapping(value = "/notification/delete/{notificationId}")
    public ResponseEntity<GlobalResponseDto> deleteNotifications(@PathVariable Long notificationId) {
        return notificationService.deleteByNotifications(notificationId);
    }

}
