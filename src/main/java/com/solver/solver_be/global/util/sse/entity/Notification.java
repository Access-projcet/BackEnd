package com.solver.solver_be.global.util.sse.entity;

import com.solver.solver_be.domain.user.entity.Guest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "GUEST_ID")
    private Guest guest;

    @Builder
    public Notification(Guest guest, String contents){
        this.guest = guest;
        this.contents = contents;
    }
}