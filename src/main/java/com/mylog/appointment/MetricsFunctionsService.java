package com.mylog.appointment;

import com.mylog.appointment.domain.MetricsReport;
import com.mylog.appointment.domain.VisitorMetricsFacade;
import com.mylog.appointment.dto.VisitorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.function.BiFunction;

import static com.mylog.appointment.java8.MetricsFiltersService.hasPendingForLessThanOneHour;
import static com.mylog.appointment.java8.MetricsFiltersService.hasPendingForMoreThanOneHour;
import static com.mylog.appointment.java8.MetricsFiltersService.isApproved;
import static com.mylog.appointment.java8.MetricsFiltersService.isPendingAppointment;

@Service
public class MetricsFunctionsService {

    @Autowired
    private VisitorMetricsFacade visitorMetricsFacade;

    // Function to calculate the total duration of all visitors' stays
    private Function<List<VisitorDTO>, Long> calculateTotalDuration() {
        return visitors -> visitors.stream()
                .mapToLong(visitor -> Duration.between(visitor.getCheckIn(), visitor.getCheckOut()).toHours())
                .sum();
    }

    // BiFunction to calculate the difference in duration between two visitors
    private BiFunction<VisitorDTO, VisitorDTO, Long> calculateDurationDifference() {
        return (visitor1, visitor2) -> {
            long duration1 = Duration.between(visitor1.getCheckIn(), visitor1.getCheckOut()).toHours();
            long duration2 = Duration.between(visitor2.getCheckIn(), visitor2.getCheckOut()).toHours();
            return Math.abs(duration1 - duration2);
        };
    }

    public MetricsReport generateMetricsReport(LocalDateTime startDate, LocalDateTime endDate) {
        List<VisitorDTO> visitors = visitorMetricsFacade.getVisitors(startDate, endDate);

        // Calculate total duration of all visitors' stays
        long totalDuration = calculateTotalDuration().apply(visitors);

        // Example: Calculate the duration difference between the first two visitors
        long durationDifference = calculateDurationDifference().apply(visitors.get(0), visitors.get(1));

        // Calculate metrics
        long visitorsToday = visitors.stream().filter(isApproved()).count();
        long approvedVisitors = visitors.stream().filter(isApproved()).count();
        long pendingAppointments = visitors.stream().filter(isPendingAppointment()).count();
        long pendingMoreThanOneHour = visitors.stream().filter(hasPendingForMoreThanOneHour()).count();
        long pendingLessThanOneHour = visitors.stream().filter(hasPendingForLessThanOneHour()).count();

        return new MetricsReport.Builder()
                .visitorsRegisteredToday(visitorsToday)
                .approvedVisitors(approvedVisitors)
                .pendingAppointments(pendingAppointments)
                .pendingAppointmentsMoreThanOneHour(pendingMoreThanOneHour)
                .pendingAppointmentsLessThanOneHour(pendingLessThanOneHour)
                .build();
    }
}
