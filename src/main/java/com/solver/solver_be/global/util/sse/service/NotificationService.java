package com.solver.solver_be.global.util.sse.service;

import com.solver.solver_be.domain.user.entity.Guest;
import com.solver.solver_be.global.util.sse.dto.NotificationResponseDto;
import com.solver.solver_be.global.util.sse.entity.Notification;
import com.solver.solver_be.global.util.sse.repository.EmitterRepository;
import com.solver.solver_be.global.util.sse.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;

    public SseEmitter subscribe(Guest guest, String lastEventId) {
        // 1
        String id = guest.getUserId() + "_" + System.currentTimeMillis();

        // 2
        SseEmitter emitter = emitterRepository.save(id, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> emitterRepository.deleteById(id));
        emitter.onTimeout(() -> emitterRepository.deleteById(id));

        // 3
        // 503 에러를 방지하기 위한 더미 이벤트 전송
        sendToClient(emitter, id, "EventStream Created. [userId=" + guest.getUserId() + "]");

        // 4
        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (!lastEventId.isEmpty()) {
            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithByGuestId(String.valueOf(guest.getUserId()));
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
        }

        return emitter;
    }

    // 로그인 시에 알림 보내는 기능
    @Async
    public void send(Guest guest, String content) {
        Notification notification = notificationRepository.save(createNotification(guest, content));

        Long receiverId = guest.getId();
        String eventId = makeTimeIncludeUd(receiverId);
        Map<String, SseEmitter> emitterMap = emitterRepository.findAllEmitterStartWithByGuestId(String.valueOf(receiverId));
        emitterMap.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, emitter);
                    sendNotification(emitter, eventId, key, NotificationResponseDto.from(notification));
                }
        );
    }

    private Notification createNotification(Guest guest, String contents) {
        return Notification.builder()
                .guest(guest)
                .contents(contents)
                .build();
    }

    // 3
    private void sendToClient(SseEmitter emitter, String id, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name("sse")
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(id);
            throw new RuntimeException("연결 오류!");
        }
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
    private String makeTimeIncludeUd(Long memberId) {
        return memberId + "_" + System.currentTimeMillis();
    }
}
