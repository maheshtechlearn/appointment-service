package com.mylog.appointment.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class MetricsService {

    @Autowired
    private VisitorMetricsFacade visitorMetricsFacade;

    public MetricsReport generateMetricsReport(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Long> metrics = visitorMetricsFacade.calculateAllMetrics(startDate, endDate);

        return new MetricsReport.Builder()
                .visitorsRegisteredToday(metrics.get("VISITORS_TODAY"))
                .approvedVisitors(metrics.get("APPROVED_VISITORS"))
                .pendingAppointments(metrics.get("PENDING_APPOINTMENTS"))
                .pendingAppointmentsMoreThanOneHour(metrics.get("PENDING_APPOINTMENTS_MORE_THAN_ONE_HOUR"))
                .pendingAppointmentsLessThanOneHour(metrics.get("PENDING_APPOINTMENTS_LESS_THAN_ONE_HOUR"))
                .build();
    }
}
