package com.mylog.appointment.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mylog.appointment.dto.Appointment;
import com.mylog.appointment.dto.Visitor;
import com.mylog.appointment.repository.AppointmentRepository;
import com.mylog.appointment.service.AppointmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AppointmentListener {

    private static final String NOTIFICATION_SERVICE_URL = "http://notification-service/api/notifications";

    private static final Logger logger = LoggerFactory.getLogger(AppointmentListener.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AppointmentService appointmentService;

    @KafkaListener(topics = "visitorTopic", groupId = "appointment-service-group")
    public void processVisitorMessage(String visitorMessage) {
        try {
            Visitor visitor = objectMapper.readValue(visitorMessage, Visitor.class);

            Appointment appointment = new Appointment();
            appointment.setVisitor(visitor);
            appointment.setStatus("PENDING");
            appointment.setAppointmentDate(visitor.getCheckIn());
            appointmentRepository.save(appointment);

            appointmentService.sendNotification(visitor, appointment);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
