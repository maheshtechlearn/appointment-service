package com.mylog.appointment.domain;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class MetricsServiceV2 {

    private final MetricsCalculator metricsCalculator; // Adhere to SRP
    private final MetricsReportBuilder reportBuilder; // Adhere to SRP

    // Constructor Injection for better testability and adherence to DIP
    @Autowired
    public MetricsServiceV2(MetricsCalculator metricsCalculator, MetricsReportBuilder reportBuilder) {
        this.metricsCalculator = metricsCalculator;
        this.reportBuilder = reportBuilder;
    }

    public MetricsReport generateMetricsReport(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Long> metrics = metricsCalculator.calculateAllMetrics(startDate, endDate);

        // Adhering to SRP, use the builder to generate the report.
        return reportBuilder.buildReport(metrics);
    }
}
