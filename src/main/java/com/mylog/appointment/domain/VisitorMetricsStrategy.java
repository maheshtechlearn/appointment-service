package com.mylog.appointment.domain;

import java.time.LocalDateTime;

public interface VisitorMetricsStrategy {
    long calculate(LocalDateTime startDate, LocalDateTime endDate);
}
