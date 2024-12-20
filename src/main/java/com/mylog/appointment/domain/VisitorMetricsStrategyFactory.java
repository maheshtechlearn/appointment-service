package com.mylog.appointment.domain;

import com.mylog.appointment.domain.impl.ApprovedVisitorsStrategy;
import com.mylog.appointment.domain.impl.PendingAppointmentsLessThanOneHourStrategy;
import com.mylog.appointment.domain.impl.PendingAppointmentsMoreThanOneHourStrategy;
import com.mylog.appointment.domain.impl.PendingAppointmentsStrategy;
import com.mylog.appointment.domain.impl.VisitorsRegisteredTodayStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class VisitorMetricsStrategyFactory {

    // Dependency Injection through constructor ensures adherence to DIP
    private final Map<String, VisitorMetricsStrategy> strategyMap;

    @Autowired
    public VisitorMetricsStrategyFactory(
            VisitorsRegisteredTodayStrategy visitorsRegisteredTodayStrategy,
            ApprovedVisitorsStrategy approvedVisitorsStrategy,
            PendingAppointmentsStrategy pendingAppointmentsStrategy,
            PendingAppointmentsMoreThanOneHourStrategy pendingAppointmentsMoreThanOneHourStrategy,
            PendingAppointmentsLessThanOneHourStrategy pendingAppointmentsLessThanOneHourStrategy) {

        // Initialize the Map with metric types as keys and corresponding strategy implementations as values
        this.strategyMap = Map.of(
                "VISITORS_TODAY", visitorsRegisteredTodayStrategy,
                "APPROVED_VISITORS", approvedVisitorsStrategy,
                "PENDING_APPOINTMENTS", pendingAppointmentsStrategy,
                "PENDING_APPOINTMENTS_MORE_THAN_ONE_HOUR", pendingAppointmentsMoreThanOneHourStrategy,
                "PENDING_APPOINTMENTS_LESS_THAN_ONE_HOUR", pendingAppointmentsLessThanOneHourStrategy
        );
    }

    /**
     * Retrieves the appropriate strategy based on the metric type.
     * This method adheres to the Open/Closed Principle by allowing new strategies to be added without modifying this method.
     *
     * @param metricType the type of metric to retrieve the strategy for.
     * @return the corresponding strategy.
     * @throws IllegalArgumentException if an invalid metric type is provided.
     */
    public VisitorMetricsStrategy getStrategy(String metricType) {
        // Ensure that only valid strategies are returned.
        VisitorMetricsStrategy strategy = strategyMap.get(metricType);

        if (strategy == null) {
            // Adheres to SRP and gives more meaningful error messages for invalid metric types
            throw new IllegalArgumentException("Invalid metric type: " + metricType + ". Available types are: " + strategyMap.keySet());
        }

        return strategy;
    }
}
