package com.solver.solver_be.domain.accessRecord.entity;

import com.solver.solver_be.domain.access.entity.Access;
import com.solver.solver_be.global.util.TimeStamped;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccessRecord extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime inTime;           // 출입시간

    @Column(unique = true)
    private LocalDateTime outTime;          // 나간시간

    @ManyToOne
    @JoinColumn(name = "ACCESS_ID", nullable = false)
    private Access access;

    public static AccessRecord of(LocalDateTime inTime, LocalDateTime outTime, Access access) {
        return AccessRecord.builder()
                .inTime(inTime)
                .outTime(outTime)
                .access(access)
                .build();
    }

}
