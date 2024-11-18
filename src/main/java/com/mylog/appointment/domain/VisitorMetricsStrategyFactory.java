package com.mylog.appointment.domain;

import com.mylog.appointment.domain.impl.ApprovedVisitorsStrategy;
import com.mylog.appointment.domain.impl.PendingAppointmentsLessThanOneHourStrategy;
import com.mylog.appointment.domain.impl.PendingAppointmentsMoreThanOneHourStrategy;
import com.mylog.appointment.domain.impl.PendingAppointmentsStrategy;
import com.mylog.appointment.domain.impl.VisitorsRegisteredTodayStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VisitorMetricsStrategyFactory {

    @Autowired
    private VisitorsRegisteredTodayStrategy visitorsRegisteredTodayStrategy;

    @Autowired
    private ApprovedVisitorsStrategy approvedVisitorsStrategy;

    @Autowired
    private PendingAppointmentsStrategy pendingAppointmentsStrategy;

    @Autowired
    private PendingAppointmentsMoreThanOneHourStrategy pendingAppointmentsMoreThanOneHourStrategy;

    @Autowired
    private PendingAppointmentsLessThanOneHourStrategy pendingAppointmentsLessThanOneHourStrategy;

    public VisitorMetricsStrategy getStrategy(String metricType) {
        return switch (metricType) {
            case "VISITORS_TODAY" -> visitorsRegisteredTodayStrategy;
            case "APPROVED_VISITORS" -> approvedVisitorsStrategy;
            case "PENDING_APPOINTMENTS" -> pendingAppointmentsStrategy;
            case "PENDING_APPOINTMENTS_MORE_THAN_ONE_HOUR" -> pendingAppointmentsMoreThanOneHourStrategy;
            case "PENDING_APPOINTMENTS_LESS_THAN_ONE_HOUR" -> pendingAppointmentsLessThanOneHourStrategy;
            default -> throw new IllegalArgumentException("Invalid metric type: " + metricType);
        };
    }
}
