package com.mylog.appointment.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mylog.appointment.dto.Appointment;
import com.mylog.appointment.dto.Visitor;
import com.mylog.appointment.repository.AppointmentRepository;
import com.mylog.appointment.service.AppointmentService;
import com.mylog.appointment.util.AppointmentStatus;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AppointmentListener {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentListener.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AppointmentService appointmentService;

    /**
     * Processes incoming visitor messages from Kafka and creates appointments.
     *
     * @param record the Kafka consumer record containing visitor message and metadata
     */
    @KafkaListener(topics = "visitorTopic", groupId = "appointment-service-group")
    @Retryable(
            value = {Exception.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    public void processVisitorMessage(ConsumerRecord<String, String> record) {
        String visitorMessage = record.value();
        int partitionId = record.partition();

        logger.info("Received visitor message from partition {}: {}", partitionId, visitorMessage);

        try {
            Visitor visitor = objectMapper.readValue(visitorMessage, Visitor.class);
            logger.debug("Parsed visitor: {}", visitor);

            Appointment appointment = new Appointment();
            appointment.setVisitor(visitor);
            appointment.setStatus(AppointmentStatus.PENDING.name());
            appointment.setAppointmentDate(visitor.getCheckIn());

            appointmentService.saveAppointment(appointment);
            logger.info("Appointment saved for visitor: {}", visitor.getId());

            appointmentService.sendNotification(visitor, appointment);
            logger.info("Notification sent for appointment: {}", appointment.getId());

        } catch (JsonProcessingException e) {
            logger.error("Failed to parse visitor message from partition {}: {}", partitionId, visitorMessage, e);
            // No retry for parsing errors
        } catch (Exception e) {
            logger.error("Error processing visitor message from partition {}: {}", partitionId, visitorMessage, e);
            throw e; // Trigger retry for other exceptions
        }
    }
}
