package Info;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AppointmentOutcomeRecord {
    private String appointmentID;
    private String dateOfAppointment;
    private String typeOfService;
    private List<Prescription> prescribedMedications;
    private String consultationNotes;

    private static final String APPOINTMENT_OUTCOME_FILE_PATH = "2002project/Appointment_outcome.csv";

    // Constructor
    public AppointmentOutcomeRecord(String appointmentID, String dateOfAppointment, String typeOfService, String consultationNotes) {
        this.appointmentID = appointmentID;
        this.dateOfAppointment = dateOfAppointment;
        this.typeOfService = typeOfService;
        this.consultationNotes = consultationNotes;
        this.prescribedMedications = new ArrayList<>();
    }

    // Getters
    public String getAppointmentID() {
        return appointmentID;
    }

    public String getDateOfAppointment() {
        return dateOfAppointment;
    }

    public String getTypeOfService() {
        return typeOfService;
    }

    public String getConsultationNotes() {
        return consultationNotes;
    }

    public List<Prescription> getPrescribedMedications() {
        return prescribedMedications;
    }

    // Add prescription
    public void addPrescription(String prescriptionID, String patientID, String doctorID, String medicineName, String status) {
        prescribedMedications.add(new Prescription(prescriptionID, appointmentID, patientID, doctorID, medicineName, status));
    }

    // Add a prescription from predefined medicine choices
    public void addPrescriptionFromChoices(String patientID, String doctorID) {
        Scanner scanner = new Scanner(System.in);

        // Medicine options
        System.out.println("Select a medicine to prescribe:");
        System.out.println("1. Paracetamol");
        System.out.println("2. Ibuprofen");
        System.out.println("3. Amoxicillin");
        System.out.print("Enter your choice (1-3): ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String selectedMedicine;
        switch (choice) {
            case 1 -> selectedMedicine = "Paracetamol";
            case 2 -> selectedMedicine = "Ibuprofen";
            case 3 -> selectedMedicine = "Amoxicillin";
            default -> {
                System.out.println("Invalid choice. No medicine prescribed.");
                selectedMedicine = null;
            }
        }

        if (selectedMedicine != null) {
            addPrescription(
                    "P" + System.currentTimeMillis(), // Unique prescription ID
                    patientID,
                    doctorID,
                    selectedMedicine,
                    "Pending" // Default status
            );
            System.out.println("Prescription added: " + selectedMedicine);
        } else {
            System.out.println("No valid medicine was prescribed.");
        }
    }

    // View appointment outcome details
    public void viewOutcomeDetails() {
        System.out.println("===== Appointment Outcome Details =====");
        System.out.println("Appointment ID: " + appointmentID);
        System.out.println("Date of Appointment: " + dateOfAppointment);
        System.out.println("Type of Service: " + typeOfService);
        System.out.println("Consultation Notes: " + consultationNotes);
        System.out.println("Prescribed Medications:");
        if (prescribedMedications.isEmpty()) {
            System.out.println("  No medications prescribed.");
        } else {
            for (Prescription prescription : prescribedMedications) {
                System.out.println("  - Prescription ID: " + prescription.getPrescriptionID() +
                        ", Medicine Name: " + prescription.getMedicineName() +
                        " (Status: " + prescription.getStatus() + ")");
            }
        }
        System.out.println("=======================================");
    }

    // Dispense all prescriptions
    public void dispensePrescriptions() {
        if (prescribedMedications.isEmpty()) {
            System.out.println("No prescriptions to dispense for Appointment ID: " + appointmentID);
            return;
        }

        System.out.println("Dispensing prescriptions for Appointment ID: " + appointmentID + "...");
        for (Prescription prescription : prescribedMedications) {
            if (!prescription.getStatus().equalsIgnoreCase("dispensed")) {
                prescription.setStatus("dispensed");
                System.out.println("Dispensed: " + prescription.getMedicineName());
            } else {
                System.out.println("Already dispensed: " + prescription.getMedicineName());
            }
        }
        System.out.println("All prescriptions have been processed.");
    }

    // Convert to CSV format
    public String toCSV() {
        StringBuilder csv = new StringBuilder();
        csv.append(appointmentID).append(",")
                .append(dateOfAppointment).append(",")
                .append(typeOfService).append(",")
                .append(consultationNotes).append(",");

        if (prescribedMedications.isEmpty()) {
            csv.append("");
        } else {
            for (int i = 0; i < prescribedMedications.size(); i++) {
                Prescription prescription = prescribedMedications.get(i);
                csv.append(prescription.getPrescriptionID())
                        .append(":")
                        .append(prescription.getMedicineName())
                        .append(":")
                        .append(prescription.getStatus());
                if (i < prescribedMedications.size() - 1) {
                    csv.append(";"); // Separate multiple prescriptions with a semicolon
                }
            }
        }

        return csv.toString();
    }

    // Load appointment outcomes from CSV
    public static List<AppointmentOutcomeRecord> loadAppointmentOutcomesFromCSV() {
        List<AppointmentOutcomeRecord> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(APPOINTMENT_OUTCOME_FILE_PATH))) {
            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length < 4) continue;

                String appointmentID = data[0].trim();
                String dateOfAppointment = data[1].trim();
                String typeOfService = data[2].trim();
                String consultationNotes = data[3].trim();

                AppointmentOutcomeRecord record = new AppointmentOutcomeRecord(appointmentID, dateOfAppointment, typeOfService, consultationNotes);

                if (data.length > 4 && !data[4].isEmpty()) {
                    String[] prescriptions = data[4].split(";");
                    for (String prescriptionData : prescriptions) {
                        String[] parts = prescriptionData.split(":", 3);
                        if (parts.length == 3) {
                            String prescriptionID = parts[0].trim();
                            String medicineName = parts[1].trim();
                            String status = parts[2].trim();
                            record.addPrescription(prescriptionID, "somePatientID", "someDoctorID", medicineName, status);
                        }
                    }
                }

                records.add(record);
            }
        } catch (IOException e) {
            System.err.println("Error reading appointment outcomes CSV: " + e.getMessage());
        }
        return records;
    }

    // Save appointment outcomes to CSV
    public static void saveAppointmentOutcomesToCSV(List<AppointmentOutcomeRecord> records) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(APPOINTMENT_OUTCOME_FILE_PATH))) {
            bw.write("AppointmentID,DateOfAppointment,TypeOfService,ConsultationNotes,PrescribedMedications");
            bw.newLine();
            for (AppointmentOutcomeRecord record : records) {
                bw.write(record.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing appointment outcomes CSV: " + e.getMessage());
        }
    }

    public void addPrescription(Prescription prescription) {
    }

    public void saveOutcomeToCSV() {
    }
}
