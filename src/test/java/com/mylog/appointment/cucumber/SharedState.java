package com.mylog.appointment.cucumber;

import org.springframework.stereotype.Component;

@Component
public class SharedState {
    private Long appointmentId;

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }
}
