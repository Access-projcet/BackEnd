package com.solver.solver_be.global.util.sse.entity;

import com.solver.solver_be.domain.user.entity.Admin;
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
    @JoinColumn(name = "ADMIN_ID")
    private Admin admin;

    @Column
    private Boolean isRead;

    @Builder
    public Notification(Admin admin, Boolean isRead, String content){
        this.admin = admin;
        this.isRead = isRead;
        this.content = new NotificationContent(content);
    }

    public String getContent() {
        return content.getContent();
    }

    public void read() {
        isRead = true;
    }

}