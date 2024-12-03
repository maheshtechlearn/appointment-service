package com.mylog.appointment.controller;

import com.mylog.appointment.dto.Appointment;
import com.mylog.appointment.dto.Visitor;
import com.mylog.appointment.exception.AppointmentNotFoundException;
import com.mylog.appointment.service.AppointmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * REST Controller for handling Appointment operations such as creation, retrieval,
 * updating, and deletion of appointments.
 */
@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentController.class);

    @Autowired
    private AppointmentService appointmentService;

    /**
     * Endpoint to create a new appointment for a visitor.
     *
     * @param visitor the visitor details for whom the appointment is being created
     * @return the created appointment object
     */
    @PostMapping("/create")
    public ResponseEntity<Appointment> createAppointment(@RequestBody Visitor visitor) {
        logger.info("Creating appointment for visitor: {}", visitor.getName());
        Appointment appointment = appointmentService.createAppointment(visitor, "PENDING");
        logger.info("Appointment created with ID: {}", appointment.getId());
        return new ResponseEntity<>(appointment, HttpStatus.CREATED);
    }

    /**
     * Endpoint to retrieve an appointment by its ID.
     *
     * @param id the ID of the appointment to retrieve
     * @return the appointment object if found, or a 404 status if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        logger.info("Retrieving appointment with ID: {}", id);
        Optional<Appointment> appointment = appointmentService.getAppointmentById(id);

        if (appointment.isPresent()) {
            logger.info("Appointment found: {}", appointment.get());
            return new ResponseEntity<>(appointment.get(), HttpStatus.OK);
        } else {
            logger.warn("Appointment with ID {} not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint to update the status of an existing appointment.
     *
     * @param id     the ID of the appointment to update
     * @param status the new status for the appointment
     * @return the updated appointment object if found, or a 404 status if not found
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<Appointment> updateAppointmentStatus(@PathVariable Long id, @RequestParam String status) {
        logger.info("Updating status of appointment with ID {} to {}", id, status);
        Appointment updatedAppointment = null;
        try {
            updatedAppointment = appointmentService.updateAppointmentStatus(id, status);
            if (null == updatedAppointment) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            logger.info("Appointment status updated successfully: {}", updatedAppointment);
            return new ResponseEntity<>(updatedAppointment, HttpStatus.OK);
        } catch (AppointmentNotFoundException e) {
            logger.error("Failed to update appointment status for ID {}: {}", id, e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint to delete an appointment by its ID.
     *
     * @param id the ID of the appointment to delete
     * @return HTTP 204 status if deletion was successful
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        logger.info("Deleting appointment with ID: {}", id);
        try {
            appointmentService.deleteAppointment(id);
            logger.info("Appointment with ID {} deleted successfully", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            logger.error("Failed to delete appointment with ID {}: {}", id, e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
