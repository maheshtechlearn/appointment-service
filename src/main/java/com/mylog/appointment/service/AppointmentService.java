package com.mylog.appointment.service;

import com.mylog.appointment.dto.Appointment;
import com.mylog.appointment.dto.Notification;
import com.mylog.appointment.dto.Visitor;
import com.mylog.appointment.repository.AppointmentRepository;
import com.mylog.appointment.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@EnableRetry
public class AppointmentService {

    private static final String NOTIFICATION_SERVICE_URL = "http://notification-service/api/notifications";

    private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RetryTemplate retryTemplate;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private NotificationRepository notificationRepository;



    public Appointment createAppointment(Visitor visitor, String status) {
        Appointment appointment = new Appointment();
        appointment.setVisitor(visitor);
        appointment.setStatus(status);
        Appointment savedAppointment = appointmentRepository.save(appointment);
        logger.info("Created new appointment with status: {}", status);
        return savedAppointment;
    }

    public Optional<Appointment> getAppointmentById(Long id) {
        logger.info("Fetching appointment with ID: {}", id);
        return appointmentRepository.findById(id);
    }

    public Appointment updateAppointmentStatus(Long id, String newStatus) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(id);
        if (optionalAppointment.isPresent()) {
            Appointment appointment = optionalAppointment.get();
            appointment.setStatus(newStatus);
            Appointment updatedAppointment = appointmentRepository.save(appointment);
            logger.info("Updated appointment status to: {}", newStatus);
            return updatedAppointment;
        } else {
            throw new RuntimeException("Appointment not found with ID: " + id);
        }
    }

    public Appointment saveAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }


    public void deleteAppointment(Long id) {
        logger.info("Deleting appointment with ID: {}", id);
        appointmentRepository.deleteById(id);
    }

    /**
     * Sends a notification for the given visitor and appointment.
     *
     * @param visitor    the visitor for whom the notification is sent
     * @param appointment the appointment details
     */
    @Async
    public void sendNotification(Visitor visitor, Appointment appointment) {
        Notification notification = new Notification();
        notification.setRecipient(visitor.getEmail());
        notification.setMessage(formatScheduleMessage(appointment));
        notification.setType("EMAIL");
        notification.setAppointment(appointment);

        try {
            logger.info("Attempting to send notification to {}", visitor.getEmail());
            boolean success = retrySendNotification(notification);

            if (success) {
                logger.error("Notification sent successfully for visitor: {}", visitor.getName());
                notificationRepository.save(notification);
            } else {
                logger.warn("Failed to send notification after retries for visitor: {}", visitor.getName());
            }
        } catch (Exception e) {
            logger.error("Failed to send notification for visitor: {}", visitor.getName(), e);
        }
    }

    private String formatScheduleMessage(Appointment appointment) {
        Visitor visitor = appointment.getVisitor();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return String.format(
                "Dear %s, your appointment is scheduled for %s at %s. Please arrive on time.",
                visitor.getName(),
                visitor.getPurpose(),
                appointment.getAppointmentDate().format(formatter)
        );
    }




    /**
     * Retry logic for sending notifications.
     *
     * @param notification the notification to be sent
     * @return true if the notification was sent successfully, false otherwise
     */
    //@CircuitBreaker(maxAttempts = 3, openTimeout = 5000, resetTimeout = 20000)
    public boolean retrySendNotification(Notification notification) {
        try {
            return retryTemplate.execute(context -> {
                ResponseEntity<?> response = restTemplate.postForObject(
                        NOTIFICATION_SERVICE_URL, notification, ResponseEntity.class);
                return response != null && response.getStatusCode().is2xxSuccessful();
            }, context -> {
                logger.error("Failed to send notification after {} attempts", context.getRetryCount());
                return false;
            });
        } catch (Exception e) {
            logger.error("Failed to send Notification: {}", e.getMessage());
        }
        return false;
    }

    //@Recover
    public boolean fallbackSendNotification(RuntimeException e, Notification notification) {
        logger.error("CircuitBreaker is open. Fallback triggered: {}", e.getMessage());
        return false;
    }

    private String formatScheduleMessage1(Appointment appointment) {
        Visitor visitor = appointment.getVisitor();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return String.format(
                "<html>" +
                        "<body style='font-family: Arial, sans-serif; color: #333;'>" +
                        "<h2 style='color: #4CAF50;'>Appointment Scheduled</h2>" +
                        "<p>Dear <strong>%s</strong>,</p>" +
                        "<p>Your appointment has been scheduled as per the following details:</p>" +
                        "<table style='border-collapse: collapse; width: 100%%;'>" +
                        "<tr>" +
                        "<td style='padding: 8px; border: 1px solid #ddd;'>Date</td>" +
                        "<td style='padding: 8px; border: 1px solid #ddd;'>%s</td>" +
                        "</tr>" +
                        "<tr>" +
                        "<td style='padding: 8px; border: 1px solid #ddd;'>Purpose</td>" +
                        "<td style='padding: 8px; border: 1px solid #ddd;'>%s</td>" +
                        "</tr>" +
                        "</table>" +
                        "<p>Please arrive on time and feel free to contact us for further assistance.</p>" +
                        "<p>Best regards,</p>" +
                        "<p><strong>Visitor Management Team</strong></p>" +
                        "</body>" +
                        "</html>",
                visitor.getName(),
                appointment.getAppointmentDate().format(formatter),
                visitor.getPurpose()
        );
    }


}
