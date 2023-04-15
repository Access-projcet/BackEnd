package com.solver.solver_be.global.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FormStatusType {
    ACCEPT("승인"),
    DENY("거절"),
    WAIT("대기"),
    COMPLETE("완료");
    private final String message;
}
