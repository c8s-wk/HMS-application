package UserMenu;

import Info.Doctor;
import Info.Appointment;
import Info.MedicalRecord;
import Info.Patient;
import Info.Schedule;

import java.util.List;
import java.util.Scanner;

public class DoctorMenu {

    private static Doctor currentDoctor; // The currently logged-in doctor
    private static List<Patient> patients; // The list of patients

    // Method to set the current doctor and patients
    public static void setContext(Doctor doctor, List<Patient> patientList) {
        currentDoctor = doctor;
        patients = patientList;
    }

    public static void displayMenu() {
        System.out.println("\n--- Doctor Menu ---");
        System.out.println("1. View Patient Medical Records");
        System.out.println("2. Update Patient Medical Records");
        System.out.println("3. View Personal Schedule");
        System.out.println("4. Set Availability for Appointments");
        System.out.println("5. Accept or Decline Appointment Requests");
        System.out.println("6. View Upcoming Appointments");
        System.out.println("7. Record Appointment Outcome");
        System.out.println("8. Logout");
        System.out.print("Please enter your choice: ");
    }

    public static void handleChoice(int choice) {
        Scanner scanner = new Scanner(System.in);

        switch (choice) {
            case 1 -> viewPatientMedicalRecords();
            case 2 -> updatePatientMedicalRecords(scanner);
            case 3 -> currentDoctor.viewSchedule();
            case 4 -> setAvailability(scanner);
            case 5 -> acceptOrDeclineAppointments(scanner);
            case 6 -> viewUpcomingAppointments();
            case 7 -> recordAppointmentOutcome(scanner);
            case 8 -> logout();
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void viewPatientMedicalRecords() {
        System.out.println("\n--- Viewing Medical Records of Patients under Care ---");
        for (Appointment appointment : currentDoctor.getAppointments()) {
            for (Patient patient : patients) {
                if (patient.getUserID().equals(appointment.getPatientID())) {
                    System.out.println("\n--- Medical Record for Patient ID: " + patient.getUserID() + " ---");
                    System.out.println(patient.getMedicalRecord());
                }
            }
        }
    }

    private static void updatePatientMedicalRecords(Scanner scanner) {
        System.out.print("Enter the Patient ID to update medical records: ");
        String patientID = scanner.nextLine();

        for (Patient patient : patients) {
            if (patient.getUserID().equals(patientID)) {
                MedicalRecord record = patient.getMedicalRecord();
                System.out.println("Current Medical Record:\n" + record);

                System.out.print("Enter new diagnosis: ");
                String diagnosis = scanner.nextLine();
                record.addDiagnosis(diagnosis);

                System.out.print("Enter new treatment plan: ");
                String treatment = scanner.nextLine();
                record.addTreatment(treatment);

                System.out.print("Enter new prescription: ");
                String prescription = scanner.nextLine();
                record.addPrescription(prescription);

                System.out.println("Medical Record updated successfully!");
                return;
            }
        }
        System.out.println("Patient not found.");
    }

    private static void setAvailability(Scanner scanner) {
        System.out.println("\n--- Set Slot Availability ---");
        System.out.print("Enter the date (YYYY-MM-DD): ");
        String date = scanner.nextLine();

        System.out.print("Enter the time (HH:MM): ");
        String time = scanner.nextLine();

        System.out.print("Enter the status (Available/Unavailable): ");
        String status = scanner.nextLine();

        if (status.equalsIgnoreCase("Available") || status.equalsIgnoreCase("Unavailable")) {
            currentDoctor.setAvailability(date, time, status);
        } else {
            System.out.println("Invalid status. Please enter 'Available' or 'Unavailable'.");
        }
    }


    private static void acceptOrDeclineAppointments(Scanner scanner) {
        System.out.println("\n--- Appointment Requests ---");
        boolean hasRequests = false;

        for (Appointment appointment : currentDoctor.getAppointments()) {
            if (appointment.getStatus().equals("Pending")) {
                System.out.println(appointment);
                hasRequests = true;

                System.out.print("Accept this appointment? (yes/no): ");
                String response = scanner.nextLine();

                if (response.equalsIgnoreCase("yes")) {
                    appointment.setStatus("Accepted");
                    System.out.println("Appointment accepted.");
                } else {
                    appointment.setStatus("Declined");
                    currentDoctor.releaseSlot(appointment.getDate(), appointment.getTime());
                    System.out.println("Appointment declined.");
                }
            }
        }

        if (!hasRequests) {
            System.out.println("No pending appointment requests.");
        }
    }

    private static void viewUpcomingAppointments() {
        System.out.println("\n--- Upcoming Appointments ---");
        boolean hasUpcomingAppointments = false;

        for (Appointment appointment : currentDoctor.getAppointments()) {
            if (appointment.getStatus().equals("Accepted")) {
                System.out.println(appointment);
                hasUpcomingAppointments = true;
            }
        }

        if (!hasUpcomingAppointments) {
            System.out.println("No upcoming appointments.");
        }
    }

    private static void recordAppointmentOutcome(Scanner scanner) {
        System.out.print("Enter the Appointment ID to record outcome: ");
        String appointmentID = scanner.nextLine();

        for (Appointment appointment : currentDoctor.getAppointments()) {
            if (appointment.getAppointmentID().equals(appointmentID)) {
                System.out.print("Enter type of service provided: ");
                String serviceType = scanner.nextLine();

                System.out.print("Enter prescribed medication name: ");
                String medication = scanner.nextLine();

                System.out.print("Enter medication status (default is 'Pending'): ");
                String medicationStatus = scanner.nextLine();

                System.out.print("Enter consultation notes: ");
                String notes = scanner.nextLine();

                appointment.setStatus("Completed");
                System.out.println("\n--- Appointment Outcome Recorded Successfully ---");
                System.out.println("Details:");
                System.out.println("Service Type: " + serviceType);
                System.out.println("Medication: " + medication + " (" + medicationStatus + ")");
                System.out.println("Notes: " + notes);
                return;
            }
        }
        System.out.println("Appointment not found.");
    }

    private static void logout() {
        System.out.println("Logging out...");
        currentDoctor = null; // Clear the context
        patients = null;
    }
}
