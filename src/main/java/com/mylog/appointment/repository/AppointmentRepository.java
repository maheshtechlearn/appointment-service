package com.mylog.appointment.repository;

import com.mylog.appointment.dto.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    public List<Appointment> findByStatusAndAppointmentDateBetween(String status, LocalDateTime startDate, LocalDateTime endDate);

}

