package com.mylog.appointment.controller;

import com.mylog.appointment.dto.Appointment;
import com.mylog.appointment.dto.Visitor;
import com.mylog.appointment.exception.AppointmentNotFoundException;
import com.mylog.appointment.repository.AppointmentRepository;
import com.mylog.appointment.service.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AppointmentControllerTest {

    @InjectMocks
    private AppointmentController appointmentController;

    @Mock
    private AppointmentService appointmentService;

    private Visitor visitor;

    @Mock
    private AppointmentRepository appointmentRepository;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        visitor=new Visitor();
    }


    @Test
    public void testCreateAppointment(){
        Appointment appointment=new Appointment();
        when(appointmentService.createAppointment(any(),anyString())).thenReturn(appointment);
        assertNotNull(appointmentController.createAppointment(visitor));
    }

    @Test
    public void testGetAppointmentById(){
        Appointment appointment=new Appointment();
        when(appointmentService.getAppointmentById(anyLong())).thenReturn(Optional.of(appointment));
        assertNotNull(appointmentController.getAppointmentById(1L));
    }

    @Test
    public void testGetAppointmentById_Response_null(){
        Appointment appointment=new Appointment();
        when(appointmentService.getAppointmentById(anyLong())).thenReturn(Optional.empty());
        assertThrows(AppointmentNotFoundException.class,()->appointmentController.getAppointmentById(1L).getStatusCode());
    }



    @Test
    public void testUpdateAppointmentStatus(){
        Appointment appointment=new Appointment();
        when(appointmentService.updateAppointmentStatus(anyLong(),anyString())).thenReturn(appointment);
        assertNotNull(appointmentController.updateAppointmentStatus(1L,"pending"));
    }

    @Test
    public void testUpdateAppointmentStatus_Response_null(){
        when(appointmentRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(appointmentService.updateAppointmentStatus(anyLong(),anyString())).thenThrow(AppointmentNotFoundException.class);
        assertEquals(HttpStatus.NOT_FOUND,appointmentController.updateAppointmentStatus(0L,null).getStatusCode());
    }

    @Test
    void deleteAppointment_Success() {
        Long id = 1L;
        doNothing().when(appointmentService).deleteAppointment(id);

        ResponseEntity<Void> response = appointmentController.deleteAppointment(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(appointmentService, times(1)).deleteAppointment(id);
    }

    @Test
    void deleteAppointment_Failure() {
        Long id = 2L;
        doThrow(new RuntimeException("Appointment not found")).when(appointmentService).deleteAppointment(id);

        ResponseEntity<Void> response = appointmentController.deleteAppointment(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(appointmentService, times(1)).deleteAppointment(id);
    }

    @Test
    void updateAppointmentStatus_AppointmentNotFoundException() {
        Long id = 3L;
        String status = "PENDING";
        when(appointmentService.updateAppointmentStatus(id, status))
                .thenThrow(new AppointmentNotFoundException("Appointment not found"));

        ResponseEntity<Appointment> response = appointmentController.updateAppointmentStatus(id, status);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(appointmentService, times(1)).updateAppointmentStatus(id, status);
    }
}
