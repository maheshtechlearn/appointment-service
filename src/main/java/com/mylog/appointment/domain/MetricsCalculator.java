package com.mylog.appointment.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class MetricsCalculator {

    private final VisitorMetricsFacade visitorMetricsFacade;

    @Autowired
    public MetricsCalculator(VisitorMetricsFacade visitorMetricsFacade) {
        this.visitorMetricsFacade = visitorMetricsFacade;
    }

    public Map<String, Long> calculateAllMetrics(LocalDateTime startDate, LocalDateTime endDate) {
        return visitorMetricsFacade.calculateAllMetrics(startDate, endDate);
    }
}
