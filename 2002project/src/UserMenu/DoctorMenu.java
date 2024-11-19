package UserMenu;

import Info.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DoctorMenu {

    private static Doctor currentDoctor; // The currently logged-in doctor
    private static List<Patient> patients; // The list of patients
    String patientFilePath = "2002project/Patient_List.csv"; // Replace with actual path

    public static void setDoctor(Doctor doctor, List<Patient> patientList) {
        currentDoctor = doctor;
        patients = patientList;
    }

    static class PatientRecord {
        private String patientID;
        private String name;
        private String pastDiagnoses;  // Member variable for past diagnoses
        private String pastTreatments;
        private String prescriptions;

        public PatientRecord(String patientID, String name, String pastDiagnoses, String pastTreatments, String prescriptions) {
            this.patientID = patientID;
            this.name = name;
            this.pastDiagnoses = pastDiagnoses;
            this.pastTreatments = pastTreatments;
            this.prescriptions = prescriptions;
        }

        public String getPatientID() {
            return patientID;
        }

        public void updateDiagnosis(String newPastDiagnosis) {
            if (newPastDiagnosis != null && !newPastDiagnosis.trim().isEmpty()) {
                if (pastDiagnoses == null || pastDiagnoses.trim().isEmpty()) {
                    pastDiagnoses = newPastDiagnosis;  // Initialize if empty
                } else {
                    pastDiagnoses += ", " + newPastDiagnosis;  // Append if not empty
                }
            }
        }

        public void updateTreatment(String newTreatment) {
            if (newTreatment != null && !newTreatment.trim().isEmpty()) {
                if (pastTreatments == null || pastTreatments.trim().isEmpty()) {
                    pastTreatments = newTreatment;
                } else {
                    pastTreatments += ", " + newTreatment;
                }
            }
        }

        public void updatePrescription(String newPrescription) {
            if (newPrescription != null && !newPrescription.trim().isEmpty()) {
                if (prescriptions == null || prescriptions.trim().isEmpty()) {
                    prescriptions = newPrescription;
                } else {
                    prescriptions += ", " + newPrescription;
                }
            }
        }

        // Save Patients to CSV
        public static void savePatientsToCSV(List<PatientRecord> patientRecords) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("Patient_List.csv"))) {
                // Write header
                bw.write("PatientID,Name,PastDiagnoses,PastTreatments,Prescriptions");
                bw.newLine();

                for (PatientRecord record : patientRecords) {
                    bw.write(String.join(",",
                            record.patientID,
                            record.name,
                            record.pastDiagnoses,
                            record.pastTreatments,
                            record.prescriptions));
                    bw.newLine();
                }
            } catch (IOException e) {
                System.err.println("Error writing Patient_List.csv: " + e.getMessage());
            }
        }
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

    public static void handleMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1 -> viewPatientMedicalRecords();
                case 2 -> updatePatientMedicalRecords(scanner);
                case 3 -> currentDoctor.viewSchedule();
                case 4 -> setAvailability(scanner);
                case 5 -> acceptOrDeclineAppointments(scanner);
                case 6 -> viewUpcomingAppointments();
                case 7 -> recordAppointmentOutcome(scanner);
                case 8 -> {
                    System.out.println("Logging out...");
                    currentDoctor = null; // Clear the context
                    patients = null;
                    isRunning = false; // Exit the menu loop
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void viewPatientMedicalRecords() {
        System.out.println("\n--- Viewing Medical Records ---");
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

                System.out.println("Medical Record updated successfully!");
                return;
            }
        }
        System.out.println("Patient not found.");
    }

    private static void setAvailability(Scanner scanner) {
        System.out.println("\n--- Set Slot Availability ---");
        boolean addingSlots = true;

        while (addingSlots) {
            System.out.print("Enter the date (YYYY-MM-DD): ");
            String date = scanner.nextLine();

            System.out.print("Enter the time (HH:MM): ");
            String time = scanner.nextLine();

            System.out.print("Enter the status (Available/Unavailable): ");
            String status = scanner.nextLine();

            currentDoctor.setAvailability(date, time, status);

            System.out.print("Do you want to add another slot? (yes/no): ");
            String response = scanner.nextLine();
            if (response.equalsIgnoreCase("no")) {
                addingSlots = false;
            }
        }

        currentDoctor.loadScheduleFromCSV(); // Ensure schedule is up-to-date
        Doctor.saveScheduleToCSV(currentDoctor.getUserID(), currentDoctor.getSchedule());
        System.out.println("Availability slots updated successfully!");
    }

    private static void acceptOrDeclineAppointments(Scanner scanner) {
        System.out.println("\n--- Appointment Requests ---");
        currentDoctor.loadAppointmentsFromCSV(); // Reload appointments
        boolean hasRequests = false;

        for (Appointment appointment : currentDoctor.getAppointments()) {
            if (appointment.getStatus().equalsIgnoreCase("Pending")) {
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

        Doctor.saveAppointmentsToCSV(currentDoctor.getAppointments()); // Save updated appointments
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

                System.out.print("Enter consultation notes: ");
                String consultationNotes = scanner.nextLine();

                AppointmentOutcomeRecord outcome = new AppointmentOutcomeRecord(
                        appointmentID,
                        appointment.getDate(),
                        serviceType,
                        consultationNotes
                );

                System.out.println("Select a medicine to prescribe:");
                System.out.println("1. Paracetamol");
                System.out.println("2. Ibuprofen");
                System.out.println("3. Amoxicillin");
                System.out.print("Enter your choice (1-3): ");
                int medChoice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                String medicineName = switch (medChoice) {
                    case 1 -> "Paracetamol";
                    case 2 -> "Ibuprofen";
                    case 3 -> "Amoxicillin";
                    default -> {
                        System.out.println("Invalid choice.");
                        yield null;
                    }
                };

                if (medicineName == null) {
                    return; // Exit the method
                }

                String prescriptionID = "P" + System.currentTimeMillis();
                Prescription prescription = new Prescription(
                        prescriptionID,
                        appointmentID,
                        appointment.getPatientID(),
                        currentDoctor.getUserID(),
                        medicineName,
                        "Pending"
                );

                outcome.addPrescription(prescription);
                List<AppointmentOutcomeRecord> outcomes = AppointmentOutcomeRecord.loadAppointmentOutcomesFromCSV();
                outcomes.add(outcome);
                AppointmentOutcomeRecord.saveAppointmentOutcomesToCSV(outcomes);

                List<Prescription> prescriptions = Prescription.loadPrescriptionsFromCSV();
                prescriptions.add(prescription);
                Prescription.savePrescriptionsToCSV(prescriptions);

                appointment.setStatus("Completed");
                Doctor.saveAppointmentsToCSV(currentDoctor.getAppointments());

                System.out.println("\n--- Appointment Outcome Recorded Successfully ---");
                System.out.println("Service Type: " + serviceType);
                System.out.println("Consultation Notes: " + consultationNotes);
                System.out.println("Prescription added: " + medicineName);

                updatePatientRecord(appointment.getPatientID(), serviceType, "Treatment Details", medicineName);
                return;
            }
        }
        System.out.println("Appointment not found.");
    }

    public static List<PatientRecord> loadPatientsFromCSV(String filePath) {
        List<PatientRecord> patientRecords = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1); // Preserve empty fields

                if (data.length < 5) {
                    System.err.println("Invalid row in Patient_List.csv: " + line);
                    continue;
                }

                String patientID = data[0].trim();
                String name = data[1].trim();
                String pastDiagnoses = data[2].trim();
                String pastTreatments = data[3].trim();
                String prescriptions = data[4].trim();

                patientRecords.add(new PatientRecord(patientID, name, pastDiagnoses, pastTreatments, prescriptions));
            }
        } catch (IOException e) {
            System.err.println("Error reading Patient_List.csv: " + e.getMessage());
        }

        return patientRecords;
    }

    // Save Patients to CSV
    public static void savePatientsToCSV(List<PatientRecord> patientRecords) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("Patient_List.csv"))) {
            // Write header
            bw.write("PatientID,Name,PastDiagnoses,PastTreatments,Prescriptions");
            bw.newLine();

            for (PatientRecord record : patientRecords) {
                bw.write(String.join(",",
                        record.patientID,
                        record.name,
                        record.pastDiagnoses,
                        record.pastTreatments,
                        record.prescriptions));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing Patient_List.csv: " + e.getMessage());
        }
    }

    private static void updatePatientRecord(String patientID, String serviceType, String pastTreatment, String prescribedMedicine) {
        String patientFilePath = "Patient_List.csv";  // Use your correct path.
        List<PatientRecord> patientRecords = loadPatientsFromCSV(patientFilePath);
        for (PatientRecord patientRecord : patientRecords) {
            if (patientRecord.getPatientID().equals(patientID)) {
                patientRecord.updateDiagnosis(serviceType);
                patientRecord.updateTreatment(pastTreatment);
                patientRecord.updatePrescription(prescribedMedicine);
                break;
            }
        }
        PatientRecord.savePatientsToCSV(patientRecords);
    }
}