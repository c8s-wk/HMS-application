package Info;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Patient extends User {
    private MedicalRecord medicalRecord;
    private List<Appointment> appointments;

    private static final String APPOINTMENT_FILE_PATH = "2002project/Appointment.csv";
    private static final String MEDICAL_RECORD_FILE_PATH = "2002project/Patient_List.csv";

    // Constructor
    public Patient(String patientID, String password, String role, MedicalRecord medicalRecord) {
        super(patientID, password, role);
        this.medicalRecord = medicalRecord;
        this.appointments = new ArrayList<>();
        loadAppointmentsFromCSV();
    }

    // Getter for the MedicalRecord
    public MedicalRecord getMedicalRecord() {
        if (this.medicalRecord == null) {
            this.medicalRecord = MedicalRecord.fetchMedicalRecordFromCSV(getUserID());
        }
        return this.medicalRecord;
    }

    // Method to view available appointment slots for all doctors
    public void viewAvailableAppointmentSlots(List<Doctor> doctors) {
        System.out.println("\n--- Available Appointment Slots ---");
        boolean slotsFound = false;

        for (Doctor doctor : doctors) {
            System.out.println("Doctor ID: " + doctor.getUserID() + ", Name: " + doctor.getName());
            doctor.displayAvailableSlots(); // Assumes Doctor class has a displayAvailableSlots method
            slotsFound = true;
        }

        if (!slotsFound) {
            System.out.println("No available slots found for any doctor.");
        }
    }

    // Getters and setters using MedicalRecord attributes
    public String getName() {
        return medicalRecord.getName();
    }

    public String getDateOfBirth() {
        return medicalRecord.getDateOfBirth();
    }

    public String getGender() {
        return medicalRecord.getGender();
    }

    public String getBloodType() {
        return medicalRecord.getBloodType();
    }

    public String getEmailAddress() {
        return medicalRecord.getEmailAddress();
    }

    public void setEmailAddress(String newEmailAddress) {
        medicalRecord.setEmailAddress(newEmailAddress);
    }

    public String getContactNumber() {
        return medicalRecord.getContactNumber();
    }

    public void setContactNumber(String newContactNumber) {
        medicalRecord.setContactNumber(newContactNumber);
    }

    // Reschedule an appointment
    public boolean rescheduleAppointment(String appointmentID, String newDate, String newTime) {
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentID().equals(appointmentID) && appointment.getPatientID().equals(getUserID())) {
                // Check availability with the doctor
                Doctor doctor = Doctor.getDoctorById(appointment.getDoctorID());
                if (doctor != null && doctor.isSlotAvailable(newDate, newTime)) {
                    // Release old slot and book new slot
                    doctor.releaseSlot(appointment.getDate(), appointment.getTime());
                    doctor.bookSlot(newDate, newTime);

                    // Update appointment details
                    appointment.setDate(newDate);
                    appointment.setTime(newTime);

                    // Save the updated appointments back to the CSV file
                    Appointment.saveAppointmentsToCSV(APPOINTMENT_FILE_PATH, getAllAppointments());
                    System.out.println("Appointment rescheduled successfully.");
                    return true;
                } else {
                    System.out.println("The new time slot is not available.");
                    return false;
                }
            }
        }
        System.out.println("Appointment not found or does not belong to the current patient.");
        return false;
    }

    // Cancel an appointment
    public boolean cancelAppointment(String appointmentID) {
        for (int i = 0; i < appointments.size(); i++) {
            Appointment appointment = appointments.get(i);
            if (appointment.getAppointmentID().equals(appointmentID) && appointment.getPatientID().equals(getUserID())) {
                appointments.remove(i);

                // Update the CSV file
                Appointment.saveAppointmentsToCSV(APPOINTMENT_FILE_PATH, getAllAppointments());

                // Notify the doctor
                Doctor doctor = Doctor.getDoctorById(appointment.getDoctorID());
                if (doctor != null) {
                    doctor.releaseSlot(appointment.getDate(), appointment.getTime());
                }

                System.out.println("Appointment canceled successfully: " + appointmentID);
                return true;
            }
        }

        System.out.println("Appointment not found: " + appointmentID);
        return false;
    }

    // Load appointments from the CSV file
    private void loadAppointmentsFromCSV() {
        appointments = new ArrayList<>();
        List<Appointment> allAppointments = Appointment.loadAppointmentsFromCSV(APPOINTMENT_FILE_PATH);

        for (Appointment appointment : allAppointments) {
            if (appointment.getPatientID().equals(getUserID())) {
                appointments.add(appointment);
            }
        }
    }

    // Schedule an appointment
    public boolean scheduleAppointment(Appointment newAppointment) {
        // Check if the slot is available
        Doctor doctor = Doctor.getDoctorById(newAppointment.getDoctorID());
        if (doctor != null && doctor.isSlotAvailable(newAppointment.getDate(), newAppointment.getTime())) {
            // Book the slot
            doctor.bookSlot(newAppointment.getDate(), newAppointment.getTime());

            // Add to appointments list
            appointments.add(newAppointment);

            // Save to CSV
            Appointment.saveAppointmentsToCSV(APPOINTMENT_FILE_PATH, getAllAppointments());

            System.out.println("Appointment scheduled successfully.");
            return true;
        } else {
            System.out.println("The selected time slot is not available.");
            return false;
        }
    }

    // Get all appointments (for saving to CSV)
    private List<Appointment> getAllAppointments() {
        List<Appointment> allAppointments = Appointment.loadAppointmentsFromCSV(APPOINTMENT_FILE_PATH);

        // Remove old appointments for this patient
        allAppointments.removeIf(appointment -> appointment.getPatientID().equals(getUserID()));

        // Add updated appointments
        allAppointments.addAll(appointments);

        return allAppointments;
    }

    // Get list of patient's appointments
    public List<Appointment> getAppointments() {
        return appointments;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "userID='" + getUserID() + '\'' +
                ", role='" + getRole() + '\'' +
                ", medicalRecord=" + medicalRecord +
                '}';
    }
}
