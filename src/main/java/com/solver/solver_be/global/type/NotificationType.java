package com.solver.solver_be.global.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {

    NEW_GUEST_VISIT("새로운 %s님이 방문 작성을 완료했습니다.");

    private final String message;

}
