package com.solver.solver_be.global.util.sse.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface EmitterRepository {
    SseEmitter save(String emitterId, SseEmitter sseEmitter);

    void saveEventCache(String emitterId, Object event);

    void deleteById(String id);

    void deleteAllEmitterStartWithMemberId(String memberId);

    void deleteAllEventCacheStartWithMemberId(String memberId);

    Map<String, Object> findAllEventCacheStartWithByAdminId(Long id);

    Map<String, SseEmitter> findAllEmitterStartWithByAdminId(Long id);
}
