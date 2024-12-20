Feature: Appointment Management

  Scenario: Create a new appointment
    Given a visitor named "John Doe" with contact number "1234567890" and email "john.doe@example.com"
    And the visitor has a purpose "Business Meeting"
    And the visitor checks in at "2023-10-25T09:00:00" and checks out at "2023-10-25T10:00:00"
    And the appointment date is "2023-10-25T09:30:00"
    When the visitor requests to create an appointment
    Then the appointment should be created with status "PENDING"
    And the appointment ID is stored

  Scenario: Retrieve an existing appointment
    Given the appointment ID from the creation scenario
    When the user requests the appointment
    Then the appointment should be returned with status "PENDING"

  Scenario: Update the status of an appointment
    Given the appointment ID from the creation scenario
    When the user updates the appointment status to "COMPLETED"
    Then the appointment status should be updated to "COMPLETED"

  Scenario: Delete an appointment
    Given the appointment ID from the creation scenario
    When the user deletes the appointment
    Then the appointment should no longer exist
