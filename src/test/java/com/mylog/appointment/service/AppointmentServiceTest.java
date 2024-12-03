package com.mylog.appointment.service;

import com.mylog.appointment.dto.Appointment;
import com.mylog.appointment.dto.Notification;
import com.mylog.appointment.dto.Visitor;
import com.mylog.appointment.exception.AppointmentNotFoundException;
import com.mylog.appointment.repository.AppointmentRepository;
import com.mylog.appointment.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AppointmentServiceTest {

    @InjectMocks
    private AppointmentService appointmentService;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private NotificationRepository notificationRepository;

    private Visitor visitor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        visitor = new Visitor();
        visitor.setName("John Doe");
        visitor.setEmail("john.doe@example.com");
    }

    @Test
    public void testCreateAppointment() {
        Appointment appointment = new Appointment();
        when(appointmentRepository.save(any())).thenReturn(appointment);
        assertNotNull(appointmentService.createAppointment(visitor, "Pending"));
    }

    @Test
    public void testGetAppointmentById() {
        Appointment appointment = new Appointment();
        when(appointmentRepository.findById(anyLong())).thenReturn(Optional.of(appointment));
        assertNotNull(appointmentService.getAppointmentById(1L));
    }


    @Test
    void updateAppointmentStatus_ValidAppointment() {
        Long appointmentId = 1L;
        String newStatus = "CONFIRMED";
        Appointment existingAppointment = new Appointment();
        existingAppointment.setId(appointmentId);
        existingAppointment.setStatus("PENDING");

        Appointment updatedAppointment = new Appointment();
        updatedAppointment.setId(appointmentId);
        updatedAppointment.setStatus(newStatus);

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(existingAppointment));
        when(appointmentRepository.save(existingAppointment)).thenReturn(updatedAppointment);

        Appointment result = appointmentService.updateAppointmentStatus(appointmentId, newStatus);

        assertNotNull(result);
        assertEquals(newStatus, result.getStatus());
        verify(appointmentRepository, times(1)).findById(appointmentId);
        verify(appointmentRepository, times(1)).save(existingAppointment);
    }

    @Test
    void updateAppointmentStatus_AppointmentNotFound() {
        Long appointmentId = 1L;
        String newStatus = "CONFIRMED";

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        AppointmentNotFoundException exception = assertThrows(AppointmentNotFoundException.class,
                () -> appointmentService.updateAppointmentStatus(appointmentId, newStatus));

        assertEquals("Appointment not found with ID: " + appointmentId, exception.getMessage());
        verify(appointmentRepository, times(1)).findById(appointmentId);
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void updateAppointmentStatus_SaveFails() {
        Long appointmentId = 1L;
        String newStatus = "CONFIRMED";
        Appointment existingAppointment = new Appointment();
        existingAppointment.setId(appointmentId);
        existingAppointment.setStatus("PENDING");

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(existingAppointment));
        when(appointmentRepository.save(existingAppointment)).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> appointmentService.updateAppointmentStatus(appointmentId, newStatus));

        assertEquals("Database error", exception.getMessage());
        verify(appointmentRepository, times(1)).findById(appointmentId);
        verify(appointmentRepository, times(1)).save(existingAppointment);
    }

    @Test
    public void testSaveAppointment() {
        Appointment appointment = new Appointment();
        when(appointmentRepository.save(any())).thenReturn(appointment);
        assertNotNull(appointmentService.saveAppointment(appointment));
    }

    @Test
    void deleteAppointment_ValidId() {
        Long appointmentId = 1L;

        appointmentService.deleteAppointment(appointmentId);

        verify(appointmentRepository, times(1)).deleteById(appointmentId);
    }

    @Test
    void deleteAppointment_RepositoryThrowsException() {
        Long appointmentId = 1L;
        doThrow(new RuntimeException("Database error")).when(appointmentRepository).deleteById(appointmentId);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> appointmentService.deleteAppointment(appointmentId));

        assertEquals("Database error", exception.getMessage());
        verify(appointmentRepository, times(1)).deleteById(appointmentId);
    }



    @Test
    void sendNotification_Successful() {


        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setVisitor(visitor);
        appointment.setAppointmentDate(LocalDateTime.now());

        Notification notification = new Notification();

        appointmentService.sendNotification(visitor, appointment);

    }

}
