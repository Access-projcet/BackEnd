package com.solver.solver_be.global.util.sse.entity;

import com.solver.solver_be.global.exception.exceptionType.GlobalException;
import com.solver.solver_be.global.type.ErrorType;
import com.solver.solver_be.global.type.SuccessType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor
public class NotificationContent {

    private static final int Max_LENGTH = 100;

    @Column(nullable = false, length = Max_LENGTH)
    private String content;

    public NotificationContent(String content){
        if (ValidNotify(content)){
            throw new GlobalException(ErrorType.CONTENT_NOT_VALID);
        }
        this.content = content;
    }

    private boolean ValidNotify(String content){
        return Objects.isNull(content) || content.length() > Max_LENGTH || content.isEmpty();
    }
}