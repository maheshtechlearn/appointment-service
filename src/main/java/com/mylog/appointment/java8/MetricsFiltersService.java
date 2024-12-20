package com.mylog.appointment.java8;

import com.mylog.appointment.domain.MetricsReport;
import com.mylog.appointment.domain.VisitorMetricsFacade;
import com.mylog.appointment.dto.VisitorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;

@Service
public class MetricsFiltersService {

    @Autowired
    private VisitorMetricsFacade visitorMetricsFacade;

    // Predicate to check if a visitor's appointment is pending
    public static Predicate<VisitorDTO> isPendingAppointment() {
        return visitor -> !visitor.isApproved() && visitor.getCheckOut() == null;
    }

    // Predicate to check if a visitor's appointment is approved
    public static Predicate<VisitorDTO> isApproved() {
        return VisitorDTO::isApproved;
    }

    // Predicate to check if a visitor's appointment has been pending for more than one hour
    public static Predicate<VisitorDTO> hasPendingForMoreThanOneHour() {
        return visitor -> {
            long duration = Duration.between(visitor.getCheckIn(), LocalDateTime.now()).toHours();
            return !visitor.isApproved() && visitor.getCheckOut() == null && duration > 1;
        };
    }

    // Predicate to check if a visitor's appointment has been pending for less than one hour
    public static Predicate<VisitorDTO> hasPendingForLessThanOneHour() {
        return visitor -> {
            long duration = Duration.between(visitor.getCheckIn(), LocalDateTime.now()).toHours();
            return !visitor.isApproved() && visitor.getCheckOut() == null && duration < 1;
        };
    }

    // Example of applying predicates
    public MetricsReport generateMetricsReport(LocalDateTime startDate, LocalDateTime endDate) {
        List<VisitorDTO> visitors = visitorMetricsFacade.getVisitors(startDate, endDate);

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
