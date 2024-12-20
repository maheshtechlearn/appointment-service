package com.mylog.appointment.domain;


import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class MetricsReportBuilder {

    public MetricsReport buildReport(Map<String, Long> metrics) {
        return new MetricsReport.Builder()
                .visitorsRegisteredToday(getMetric(metrics, "VISITORS_TODAY"))
                .approvedVisitors(getMetric(metrics, "APPROVED_VISITORS"))
                .pendingAppointments(getMetric(metrics, "PENDING_APPOINTMENTS"))
                .pendingAppointmentsMoreThanOneHour(getMetric(metrics, "PENDING_APPOINTMENTS_MORE_THAN_ONE_HOUR"))
                .pendingAppointmentsLessThanOneHour(getMetric(metrics, "PENDING_APPOINTMENTS_LESS_THAN_ONE_HOUR"))
                .build();
    }

    private Long getMetric(Map<String, Long> metrics, String metricKey) {
        return Optional.ofNullable(metrics.get(metricKey))
                .orElseThrow(() -> new IllegalArgumentException("Missing required metric: " + metricKey));
    }
}
