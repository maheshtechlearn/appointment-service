package com.mylog.appointment.domain;

import com.mylog.appointment.dto.Visitor;
import com.mylog.appointment.dto.VisitorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VisitorMetricsFacade {

    @Autowired
    private VisitorMetricsStrategyFactory visitorMetricsStrategyFactory;

    public Map<String, Long> calculateAllMetrics(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Long> metrics = new HashMap<>();

        metrics.put("VISITORS_TODAY", visitorMetricsStrategyFactory.getStrategy("VISITORS_TODAY").calculate(startDate, endDate));
        metrics.put("APPROVED_VISITORS", visitorMetricsStrategyFactory.getStrategy("APPROVED_VISITORS").calculate(startDate, endDate));
        metrics.put("PENDING_APPOINTMENTS", visitorMetricsStrategyFactory.getStrategy("PENDING_APPOINTMENTS").calculate(startDate, endDate));
        metrics.put("PENDING_APPOINTMENTS_MORE_THAN_ONE_HOUR", visitorMetricsStrategyFactory.getStrategy("PENDING_APPOINTMENTS_MORE_THAN_ONE_HOUR").calculate(startDate, endDate));
        metrics.put("PENDING_APPOINTMENTS_LESS_THAN_ONE_HOUR", visitorMetricsStrategyFactory.getStrategy("PENDING_APPOINTMENTS_LESS_THAN_ONE_HOUR").calculate(startDate, endDate));

        return metrics;
    }

    public List<VisitorDTO> getVisitors(LocalDateTime startDate, LocalDateTime endDate) {
        return new ArrayList<>();
    }
}
