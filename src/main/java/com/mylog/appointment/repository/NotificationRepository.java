package com.mylog.appointment.repository;

import com.mylog.appointment.dto.Appointment;
import com.mylog.appointment.dto.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // Additional query methods can be defined here
}
