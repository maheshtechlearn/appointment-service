package com.mylog.appointment.repository;

import com.mylog.appointment.dto.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
