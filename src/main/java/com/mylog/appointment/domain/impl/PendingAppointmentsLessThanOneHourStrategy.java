package com.mylog.appointment.domain.impl;

import com.mylog.appointment.domain.VisitorMetricsStrategy;
import com.mylog.appointment.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PendingAppointmentsLessThanOneHourStrategy implements VisitorMetricsStrategy {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    public long calculate(LocalDateTime startDate, LocalDateTime endDate) {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        return appointmentRepository.findByStatusAndAppointmentDateBetween("PENDING", startDate, oneHourAgo).size();
    }
}
