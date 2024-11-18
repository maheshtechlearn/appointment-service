package com.mylog.appointment.controller;

import com.mylog.appointment.domain.MetricsReport;
import com.mylog.appointment.domain.MetricsService;
import com.mylog.appointment.exception.InvalidDateRangeException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/api/appointments")
public class MetricsController {

    @Autowired
    private MetricsService metricsService;

    @GetMapping("/report")
    public ResponseEntity<MetricsReport> getMetricsReport(
            @RequestParam(value = "startDate",required = true) String startDateStr,
            @RequestParam(value = "endDate",required = false) String endDateStr) {

        validateDateRange(startDateStr, endDateStr);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startDate = LocalDateTime.parse(startDateStr, formatter);
        LocalDateTime endDate = LocalDateTime.parse(endDateStr, formatter);

        MetricsReport metricsReport = metricsService.generateMetricsReport(startDate, endDate);

        return new ResponseEntity<>(metricsReport, HttpStatus.OK);
    }



    private void validateDateRange(String startDateStr, String endDateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Null and empty checks
        if (StringUtils.isBlank(startDateStr)) {
            throw new InvalidDateRangeException("Start date cannot be null or empty.");
        }
        if (StringUtils.isBlank(endDateStr)) {
            throw new InvalidDateRangeException("End date cannot be null or empty.");
        }

        try {
            LocalDateTime startDate = LocalDateTime.parse(startDateStr, formatter);
            LocalDateTime endDate = LocalDateTime.parse(endDateStr, formatter);

            if (endDate.isBefore(startDate)) {
                throw new InvalidDateRangeException("End date must be after start date.");
            }
        } catch (DateTimeParseException e) {
            throw new InvalidDateRangeException("Invalid date format. Please use 'yyyy-MM-dd HH:mm'.", e);
        }
    }

}
