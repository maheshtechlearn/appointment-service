package com.mylog.appointment.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MetricsReport {

    private long visitorsRegisteredToday;
    private long approvedVisitors;
    private long pendingAppointments;
    private long pendingAppointmentsMoreThanOneHour;
    private long pendingAppointmentsLessThanOneHour;

    // Getters and Setters

    public static class Builder {

        private long visitorsRegisteredToday;
        private long approvedVisitors;
        private long pendingAppointments;
        private long pendingAppointmentsMoreThanOneHour;
        private long pendingAppointmentsLessThanOneHour;

        @JsonProperty("visitorsRegisteredToday")
        public Builder visitorsRegisteredToday(long visitorsRegisteredToday) {
            this.visitorsRegisteredToday = visitorsRegisteredToday;
            return this;
        }
        @JsonProperty("approvedVisitors")
        public Builder approvedVisitors(long approvedVisitors) {
            this.approvedVisitors = approvedVisitors;
            return this;
        }
        @JsonProperty("pendingAppointments")
        public Builder pendingAppointments(long pendingAppointments) {
            this.pendingAppointments = pendingAppointments;
            return this;
        }
        @JsonProperty("pendingAppointmentsMoreThanOneHour")
        public Builder pendingAppointmentsMoreThanOneHour(long pendingAppointmentsMoreThanOneHour) {
            this.pendingAppointmentsMoreThanOneHour = pendingAppointmentsMoreThanOneHour;
            return this;
        }
        @JsonProperty("pendingAppointmentsLessThanOneHour")
        public Builder pendingAppointmentsLessThanOneHour(long pendingAppointmentsLessThanOneHour) {
            this.pendingAppointmentsLessThanOneHour = pendingAppointmentsLessThanOneHour;
            return this;
        }

        public MetricsReport build() {
            MetricsReport report = new MetricsReport();
            report.visitorsRegisteredToday = this.visitorsRegisteredToday;
            report.approvedVisitors = this.approvedVisitors;
            report.pendingAppointments = this.pendingAppointments;
            report.pendingAppointmentsMoreThanOneHour = this.pendingAppointmentsMoreThanOneHour;
            report.pendingAppointmentsLessThanOneHour = this.pendingAppointmentsLessThanOneHour;
            return report;
        }
    }
}
