package com.solver.solver_be.global.util.sse.entity;

import com.solver.solver_be.domain.user.entity.Guest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @Embedded
    private NotificationContent content;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "GUEST_ID")
    private Guest guest;

    @Builder
    public Notification(Guest guest, String content){
        this.guest = guest;
        this.content = new NotificationContent(content);
    }

    public String getContent() {
        return content.getContent();
    }

}