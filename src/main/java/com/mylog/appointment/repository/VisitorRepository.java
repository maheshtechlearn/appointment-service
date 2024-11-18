package com.mylog.appointment.repository;

import com.mylog.appointment.dto.Notification;
import com.mylog.appointment.dto.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitorRepository extends JpaRepository<Visitor, Long> {
    // Additional query methods can be defined here

    public List<Visitor> findByApprovedTrueAndCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate);


    public List<Visitor> findByCreatedDateBetween(LocalDateTime startDate,LocalDateTime endDate);


}