package com.solver.solver_be.global.util.sse.repository;

import com.solver.solver_be.global.util.sse.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Optional<Notification> findById(Long NotificationId);

    void deleteById(Long notificationId);

    List<Notification> findByAdminIdOrderByIdDesc(Long adminId);

    void deleteAllByAdminId(Long receiverId);
}
