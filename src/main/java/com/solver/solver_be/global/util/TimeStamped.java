package com.solver.solver_be.global.util;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class TimeStamped {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;            // 생성 날짜

    @LastModifiedDate
    @Column
    private LocalDateTime modifiedAt;           // 수정 날짜

}