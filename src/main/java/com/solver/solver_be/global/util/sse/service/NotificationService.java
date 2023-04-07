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
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;

    // SSE 연결 대상자 설정
    public SseEmitter subscribe(Admin admin, String lastEventId) {
        // 1
        String emitterId = admin.getId() + "_" + System.currentTimeMillis();

        // 2
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        // 3
        String eventId = makeTimeIncludeUd(admin.getId());

        // 503 에러를 방지하기 위한 더미 이벤트 전송
        sendNotification(emitter, eventId, emitterId,"EventStream Created. [userId=" + admin.getId() + "]");

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (!lastEventId.isEmpty()) {
            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithByAdminId(admin.getId());
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
        }

        return emitter;
    }

    // 알림 보내는 기능
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

    // 데이터 전송
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

    // emitterId 고유값만드는 메서드
    private String makeTimeIncludeUd(Long id) {
        return id + "_" + System.currentTimeMillis();
    }
}
