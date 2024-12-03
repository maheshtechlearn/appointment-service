package com.mylog.appointment.controller;

import com.mylog.appointment.domain.MetricsReport;
import com.mylog.appointment.domain.MetricsService;
import com.mylog.appointment.exception.InvalidDateRangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class MetricsControllerTest {

    @InjectMocks
    private MetricsController metricsController;

    @Mock
    private MetricsService metricsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getMetricsReport_ValidDates() {
        String startDateStr = "2024-11-01 00:00";
        String endDateStr = "2024-11-30 23:59";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startDate = LocalDateTime.parse(startDateStr, formatter);
        LocalDateTime endDate = LocalDateTime.parse(endDateStr, formatter);

        MetricsReport mockReport = new MetricsReport();
        when(metricsService.generateMetricsReport(startDate, endDate)).thenReturn(mockReport);

        ResponseEntity<MetricsReport> response = metricsController.getMetricsReport(startDateStr, endDateStr);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockReport, response.getBody());
        verify(metricsService, times(1)).generateMetricsReport(startDate, endDate);
    }

    @Test
    void getMetricsReport_InvalidDateRange() {
        String startDateStr = "2024-12-01 00:00";
        String endDateStr = "2024-11-30 23:59";

        assertThrows(InvalidDateRangeException.class,
                () -> metricsController.getMetricsReport(startDateStr, endDateStr));
        verifyNoInteractions(metricsService);
    }

    @Test
    void getMetricsReport_InvalidDateFormat() {
        String startDateStr = "invalid-date";
        String endDateStr = "2024-11-30 23:59";

        assertThrows(Exception.class,
                () -> metricsController.getMetricsReport(startDateStr, endDateStr));
        verifyNoInteractions(metricsService);
    }

    @Test
    void getMetricsReport_EndDateMissing() {
        String startDateStr = "2024-11-01 00:00";
        String endDateStr = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startDate = LocalDateTime.parse(startDateStr, formatter);
        LocalDateTime endDate = LocalDateTime.now(); // Assuming the controller defaults to now

        MetricsReport mockReport = new MetricsReport();
        when(metricsService.generateMetricsReport(startDate, endDate)).thenReturn(mockReport);

        assertThrows(InvalidDateRangeException.class, () -> metricsController.getMetricsReport(startDateStr, endDateStr));
    }

    @Test
    void getMetricsReport_ServiceException() {
        String startDateStr = "2024-11-01 00:00";
        String endDateStr = "2024-11-30 23:59";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startDate = LocalDateTime.parse(startDateStr, formatter);
        LocalDateTime endDate = LocalDateTime.parse(endDateStr, formatter);

        when(metricsService.generateMetricsReport(startDate, endDate))
                .thenThrow(new RuntimeException("Service exception"));

        assertThrows(RuntimeException.class,
                () -> metricsController.getMetricsReport(startDateStr, endDateStr));
        verify(metricsService, times(1)).generateMetricsReport(startDate, endDate);
    }

    @Test
    void getMetricsReport_StartDateMissing() {
        String endDateStr = "2024-11-01 00:00";
        String startDateStr = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startDate = LocalDateTime.parse(endDateStr, formatter);
        LocalDateTime endDate = LocalDateTime.now();

        MetricsReport mockReport = new MetricsReport();
        when(metricsService.generateMetricsReport(startDate, endDate)).thenReturn(mockReport);

        assertThrows(InvalidDateRangeException.class, () -> metricsController.getMetricsReport(startDateStr, endDateStr));
    }
}
