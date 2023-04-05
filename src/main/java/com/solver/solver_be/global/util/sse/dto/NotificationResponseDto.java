package com.solver.solver_be.global.util.sse.dto;

import com.solver.solver_be.global.util.sse.entity.Notification;
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

    private String contents;

    public static NotificationResponseDto from(Notification notification) {
        return new NotificationResponseDto(notification.getId(), notification.getContents());
    }
}

