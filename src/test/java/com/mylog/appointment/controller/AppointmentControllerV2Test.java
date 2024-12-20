package com.mylog.appointment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mylog.appointment.dto.Appointment;
import com.mylog.appointment.dto.Visitor;
import com.mylog.appointment.exception.AppointmentNotFoundException;
import com.mylog.appointment.service.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
public class AppointmentControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentService appointmentService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testCreateAppointment() throws Exception {
        Visitor visitor = new Visitor();
        visitor.setName("John Doe");

        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setStatus("PENDING");

        when(appointmentService.createAppointment(any(Visitor.class), any(String.class)))
                .thenReturn(appointment);

        mockMvc.perform(post("/api/appointments/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(visitor)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    public void testGetAppointmentById() throws Exception {
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setStatus("CONFIRMED");

        when(appointmentService.getAppointmentById(anyLong()))
                .thenReturn(Optional.of(appointment));

        mockMvc.perform(get("/api/appointments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    public void testGetAppointmentById_NotFound() throws Exception {
        when(appointmentService.getAppointmentById(anyLong()))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/appointments/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateAppointmentStatus() throws Exception {
        Appointment updatedAppointment = new Appointment();
        updatedAppointment.setId(1L);
        updatedAppointment.setStatus("CONFIRMED");

        when(appointmentService.updateAppointmentStatus(anyLong(), any(String.class)))
                .thenReturn(updatedAppointment);

        mockMvc.perform(put("/api/appointments/1/status")
                        .param("status", "CONFIRMED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    public void testUpdateAppointmentStatus_NotFound() throws Exception {
        when(appointmentService.updateAppointmentStatus(anyLong(), any(String.class)))
                .thenThrow(new AppointmentNotFoundException("Appointment Not Found"));

        mockMvc.perform(put("/api/appointments/1/status")
                        .param("status", "CONFIRMED"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteAppointment() throws Exception {
        doNothing().when(appointmentService).deleteAppointment(anyLong());

        mockMvc.perform(delete("/api/appointments/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteAppointment_NotFound() throws Exception {
        Mockito.doThrow(new RuntimeException("Appointment Not Found"))
                .when(appointmentService).deleteAppointment(anyLong());

        mockMvc.perform(delete("/api/appointments/1"))
                .andExpect(status().isNotFound());
    }
}
