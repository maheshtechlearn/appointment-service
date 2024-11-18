package com.mylog.appointment.domain.impl;

import com.mylog.appointment.domain.VisitorMetricsStrategy;
import com.mylog.appointment.repository.VisitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ApprovedVisitorsStrategy implements VisitorMetricsStrategy {

    @Autowired
    private VisitorRepository visitorRepository;

    @Override
    public long calculate(LocalDateTime startDate, LocalDateTime endDate) {
        return visitorRepository.findByApprovedTrueAndCreatedDateBetween(startDate, endDate).size();
    }
}
