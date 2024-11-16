package Info;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentOutcomeRecord {
    private String appointmentID;
    private String dateOfAppointment;
    private String typeOfService;
    private List<Prescription> prescribedMedications;
    private String consultationNotes;

    private static final String APPOINTMENT_OUTCOME_FILE_PATH = "appointment_outcomes.csv"; // CSV file path

    // Constructor
    public AppointmentOutcomeRecord(String appointmentID, String dateOfAppointment, String typeOfService, String consultationNotes) {
        this.appointmentID = appointmentID;
        this.dateOfAppointment = dateOfAppointment;
        this.typeOfService = typeOfService;
        this.consultationNotes = consultationNotes;
        this.prescribedMedications = new ArrayList<>();
    }

    // Getters and Setters
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
                System.out.println("  - " + prescription.getMedicineName() + " (Status: " + prescription.getStatus() + ")");
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

        // For prescribed medications, concatenate each medicine name separated by a semicolon (;)
        if (prescribedMedications.isEmpty()) {
            csv.append(""); // Empty if no prescribed medications
        } else {
            for (int i = 0; i < prescribedMedications.size(); i++) {
                // Append each medication's name to the CSV string, separated by a semicolon
                csv.append(prescribedMedications.get(i).getMedicineName());
                if (i < prescribedMedications.size() - 1) {
                    csv.append(";");
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
            // Skip header
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length < 4) {
                    continue; // Skip invalid rows
                }

                String appointmentID = data[0].trim();
                String dateOfAppointment = data[1].trim();
                String typeOfService = data[2].trim();
                String consultationNotes = data[3].trim();

                AppointmentOutcomeRecord record = new AppointmentOutcomeRecord(appointmentID, dateOfAppointment, typeOfService, consultationNotes);

                // Parse prescribed medications (if any)
                if (data.length > 4 && !data[4].isEmpty()) {
                    String[] prescriptions = data[4].split(";");
                    for (String medication : prescriptions) {
                        record.addPrescription("somePrescriptionID", "somePatientID", "someDoctorID", medication.trim(), "pending"); // Add sample IDs, status
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
            // Write header
            bw.write("AppointmentID,DateOfAppointment,TypeOfService,ConsultationNotes,PrescribedMedications");
            bw.newLine();
            // Write each record
            for (AppointmentOutcomeRecord record : records) {
                bw.write(record.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing appointment outcomes CSV: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AppointmentOutcomeRecord{")
                .append("appointmentID='").append(appointmentID).append('\'')
                .append(", dateOfAppointment='").append(dateOfAppointment).append('\'')
                .append(", typeOfService='").append(typeOfService).append('\'')
                .append(", consultationNotes='").append(consultationNotes).append('\'')
                .append(", prescribedMedications=[");

        for (int i = 0; i < prescribedMedications.size(); i++) {
            sb.append(prescribedMedications.get(i).getMedicineName());
            if (i < prescribedMedications.size() - 1) {
                sb.append(", ");
            }
        }

        sb.append("]}");
        return sb.toString();
    }

}
