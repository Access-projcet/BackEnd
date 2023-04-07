package com.solver.solver_be.global.util.sse.service;

import com.solver.solver_be.domain.user.entity.Admin;
import com.solver.solver_be.global.util.sse.dto.NotificationResponseDto;
import com.solver.solver_be.global.util.sse.entity.Notification;
import com.solver.solver_be.global.util.sse.repository.EmitterRepository;
import com.solver.solver_be.global.util.sse.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;

    // Set up an SSE connection
    public SseEmitter subscribe(Admin admin, String lastEventId) {

        String emitterId = admin.getId() + "_" + System.currentTimeMillis();

        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        String eventId = makeTimeIncludeUd(admin.getId());

        // Set up for Heartbeat
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

        // Sending a dummy Event to prevent 503 error
        sendNotification(emitter, eventId, emitterId,"EventStream Created. [userId=" + admin.getId() + "]");

        // Prevent lost events by sending a list of unreceived events, if one exists
        if (!lastEventId.isEmpty()) {
            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithByAdminId(admin.getId());
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
        }

        // Creating a task that periodically sends an empty event
        Runnable heartbeatTask = () -> {
            try {
                String heartbeatMessage = "event: heartbeat\ndata: \n\n";
                emitter.send(heartbeatMessage);
            } catch (IOException e) {
                emitter.complete();
                scheduledExecutorService.shutdown();
            }
        };

        // Sending heartbeat periodically
        scheduledExecutorService.scheduleAtFixedRate(heartbeatTask, 0, 44, TimeUnit.SECONDS);

        return emitter;
    }

    // Sending Alarm to Specified Admin with Message
    @Async
    public void send(Admin admin, String content) {

        Notification notification = notificationRepository.save(createNotification(admin, content));

        Long receiverId = admin.getId();
        String eventId = makeTimeIncludeUd(receiverId);
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByAdminId(admin.getId());
        emitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, emitter);
                    sendNotification(emitter, eventId, key, NotificationResponseDto.from(notification));
                }
        );
    }

    //==================================== Method ==========================================

    private Notification createNotification(Admin admin, String content) {
        return Notification.builder()
                .admin(admin)
                .content(content)
                .build();
    }

    // Sending Data
    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .name("message")
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
        }
    }

    // Making an Identity emitterId
    private String makeTimeIncludeUd(Long id) {
        return id + "_" + System.currentTimeMillis();
    }
}
