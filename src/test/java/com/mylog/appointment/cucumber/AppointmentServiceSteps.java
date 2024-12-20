package com.mylog.appointment.cucumber;

import com.mylog.appointment.dto.Appointment;
import com.mylog.appointment.dto.Visitor;
import com.mylog.appointment.service.AppointmentService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@SpringBootTest
public class AppointmentServiceSteps {

//    @Autowired
//    private AppointmentService appointmentService;
//
//    @Autowired
//    private SharedState sharedState;
//
//    private Visitor visitor;
//    private Appointment appointment;
//    private ResponseEntity<Appointment> response;
//
//    @Given("a visitor named {string} with contact number {string} and email {string}")
//    public void a_visitor_named_with_contact_number_and_email(String name, String contactNumber, String email) {
//        visitor = new Visitor();
//        visitor.setName(name);
//        visitor.setContactNumber(contactNumber);
//        visitor.setEmail(email);
//    }
//
//    @Given("the visitor has a purpose {string}")
//    public void the_visitor_has_a_purpose(String purpose) {
//        visitor.setPurpose(purpose);
//    }
//
//    @Given("the visitor checks in at {string} and checks out at {string}")
//    public void the_visitor_checks_in_and_out(String checkIn, String checkOut) {
//        visitor.setCheckIn(LocalDateTime.parse(checkIn));
//        visitor.setCheckOut(LocalDateTime.parse(checkOut));
//        visitor.setDuration(java.time.Duration.between(visitor.getCheckIn(), visitor.getCheckOut()).toMinutes());
//    }
//
//    @Given("the appointment date is {string}")
//    public void the_appointment_date_is(String appointmentDate) {
//        appointment = new Appointment();
//        appointment.setAppointmentDate(LocalDateTime.parse(appointmentDate));
//    }
//
//    @When("the visitor requests to create an appointment")
//    public void the_visitor_requests_to_create_an_appointment() {
//        appointment.setVisitor(visitor);
//        appointment.setStatus("PENDING");
//        appointment = appointmentService.createAppointment(visitor, "PENDING");
//        sharedState.setAppointmentId(appointment.getId()); // Store the appointment ID in shared state
//    }
//
//    @Then("the appointment should be created with status {string}")
//    public void the_appointment_should_be_created_with_status(String status) {
//        Assertions.assertNotNull(appointment);
//        Assertions.assertEquals(status, appointment.getStatus());
//    }
//
//    @Then("the appointment ID is stored")
//    public void the_appointment_id_is_stored() {
//        Assertions.assertNotNull(sharedState.getAppointmentId());
//    }
//
//    @Given("the appointment ID from the creation scenario")
//    public void the_appointment_id_from_the_creation_scenario() {
//        Assertions.assertNotNull(sharedState.getAppointmentId());
//    }
//
//    @When("the user requests the appointment")
//    public void the_user_requests_the_appointment() {
//        response = ResponseEntity.of(appointmentService.getAppointmentById(sharedState.getAppointmentId()));
//    }
//
//    @Then("the appointment should be returned with status {string}")
//    public void the_appointment_should_be_returned_with_status(String status) {
//        Assertions.assertNotNull(response.getBody());
//        Assertions.assertEquals(status, response.getBody().getStatus());
//    }
//
//    @When("the user updates the appointment status to {string}")
//    public void the_user_updates_the_appointment_status_to(String status) {
//        appointment = appointmentService.updateAppointmentStatus(sharedState.getAppointmentId(), status);
//    }
//
//    @Then("the appointment status should be updated to {string}")
//    public void the_appointment_status_should_be_updated_to(String status) {
//        Assertions.assertEquals(status, appointment.getStatus());
//    }
//
//    @When("the user deletes the appointment")
//    public void the_user_deletes_the_appointment() {
//        appointmentService.deleteAppointment(sharedState.getAppointmentId());
//    }
//
//    @Then("the appointment should no longer exist")
//    public void the_appointment_should_no_longer_exist() {
//        Assertions.assertThrows(RuntimeException.class, () -> appointmentService.getAppointmentById(sharedState.getAppointmentId()));
//    }
}
