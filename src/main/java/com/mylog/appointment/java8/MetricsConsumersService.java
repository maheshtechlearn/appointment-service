package com.mylog.appointment.java8;

import com.mylog.appointment.domain.MetricsReport;
import com.mylog.appointment.domain.VisitorMetricsFacade;
import com.mylog.appointment.dto.VisitorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.mylog.appointment.java8.MetricsFiltersService.hasPendingForLessThanOneHour;
import static com.mylog.appointment.java8.MetricsFiltersService.hasPendingForMoreThanOneHour;
import static com.mylog.appointment.java8.MetricsFiltersService.isApproved;
import static com.mylog.appointment.java8.MetricsFiltersService.isPendingAppointment;

@Service
public class MetricsConsumersService {

    @Autowired
    private VisitorMetricsFacade visitorMetricsFacade;

    // Consumer to log visitor details
    private final Consumer<VisitorDTO> logVisitor = visitor -> System.out.println("Logging Visitor: " + visitor.getName());

    // Supplier to provide a default visitor for metrics report
    private final Supplier<VisitorDTO> defaultVisitorSupplier = VisitorDTO::new;

    // Example usage in generateMetricsReport
    public MetricsReport generateMetricsReport(LocalDateTime startDate, LocalDateTime endDate) {
        List<VisitorDTO> visitors = visitorMetricsFacade.getVisitors(startDate, endDate);

        // Log each visitor's details
        visitors.forEach(logVisitor);

        // Use default visitor when no visitors exist
        if (visitors.isEmpty()) {
            VisitorDTO defaultVisitor = defaultVisitorSupplier.get();
            System.out.println("Default Visitor used: " + defaultVisitor.getName());
            visitors.add(defaultVisitor);
        }

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
