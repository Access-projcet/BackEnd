package com.solver.solver_be.global.util.sse.dto;

import com.solver.solver_be.global.util.sse.entity.Notification;
import com.solver.solver_be.global.util.sse.entity.NotificationContent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDto {

    private Long id;

    private String content;

    public static NotificationResponseDto from(Notification notification) {
        return new NotificationResponseDto(notification.getId(), notification.getContent());
    }
}

