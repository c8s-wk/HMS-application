package Info;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentOutcomeRecord {
    private String appointmentID;
    private String dateOfAppointment;
    private String typeOfService;
    private String consultationNotes;
    private List<Prescription> prescribedMedications;

    private static final String APPOINTMENT_OUTCOME_FILE_PATH = "2002project/Appointment_outcome.csv";
    private static final String PATIENT_FILE_PATH = "2002project/Patient_List.csv";

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

    // Add a prescription to the record
    public void addPrescription(Prescription prescription) {
        prescribedMedications.add(prescription);
    }

    // Save or update the current record in CSV
    public void saveOutcomeToCSV() {
        List<AppointmentOutcomeRecord> records = loadAppointmentOutcomesFromCSV();
        boolean found = false;

        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getAppointmentID().equals(appointmentID)) {
                records.set(i, this);
                found = true;
                break;
            }
        }

        if (!found) {
            records.add(this);
        }

        saveAppointmentOutcomesToCSV(records);
        updatePatientMedicalRecord();
    }

    // Update Patient Medical Record in Patient_List.csv
    private void updatePatientMedicalRecord() {
        List<String[]> patientData = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(PATIENT_FILE_PATH))) {
            String header = br.readLine();
            patientData.add(header.split(",")); // Add header
            String line;

            while ((line = br.readLine()) != null) {
                String[] row = line.split(",", -1);
                if (row[0].equals(getPatientIDFromAppointment())) { // Match Patient ID
                    row[6] = appendValue(row[6], consultationNotes); // Past Diagnoses
                    row[7] = appendValue(row[7], typeOfService);     // Past Treatments
                    row[8] = appendValue(row[8], getPrescriptionsSummary()); // Prescriptions
                }
                patientData.add(row);
            }
        } catch (IOException e) {
            System.err.println("Error reading Patient_List.csv: " + e.getMessage());
        }

        savePatientData(patientData);
    }

    private String getPatientIDFromAppointment() {
        String patientID = "Unknown"; // Default value
        List<Appointment> appointments = Appointment.loadAppointmentsFromCSV();
        for (Appointment appt : appointments) {
            if (appt.getAppointmentID().equals(this.appointmentID)) {
                patientID = appt.getPatientID();
                break;
            }
        }
        return patientID;
    }

    private String appendValue(String existing, String newValue) {
        if (newValue == null || newValue.isEmpty()) return existing;
        return existing.isEmpty() ? newValue : existing + ";" + newValue;
    }

    private String getPrescriptionsSummary() {
        StringBuilder summary = new StringBuilder();
        for (Prescription p : prescribedMedications) {
            summary.append(p.getMedicineName())
                    .append(" (")
                    .append(p.getStatus())
                    .append("); ");
        }
        return summary.toString().trim();
    }

    private void savePatientData(List<String[]> patientData) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PATIENT_FILE_PATH))) {
            for (String[] row : patientData) {
                bw.write(String.join(",", row));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing Patient_List.csv: " + e.getMessage());
        }
    }

    // Convert to CSV format
    public String toCSV() {
        StringBuilder csv = new StringBuilder();
        csv.append(appointmentID).append(",")
                .append(dateOfAppointment).append(",")
                .append(typeOfService).append(",")
                .append(consultationNotes).append(",");

        if (!prescribedMedications.isEmpty()) {
            for (int i = 0; i < prescribedMedications.size(); i++) {
                Prescription prescription = prescribedMedications.get(i);
                csv.append(prescription.getPrescriptionID()).append(":")
                        .append(prescription.getMedicineName()).append(":")
                        .append(prescription.getStatus());
                if (i < prescribedMedications.size() - 1) {
                    csv.append(";"); // Separate multiple prescriptions
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

                AppointmentOutcomeRecord record = new AppointmentOutcomeRecord(
                        data[0].trim(), data[1].trim(), data[2].trim(), data[3].trim());

                if (data.length > 4 && !data[4].isEmpty()) {
                    String[] prescriptions = data[4].split(";");
                    for (String prescriptionData : prescriptions) {
                        String[] parts = prescriptionData.split(":");
                        if (parts.length == 3) {
                            record.addPrescription(new Prescription(parts[0], data[0], "", "", parts[1], parts[2]));
                        }
                    }
                }

                records.add(record);
            }
        } catch (IOException e) {
            System.err.println("Error loading Appointment_outcome.csv: " + e.getMessage());
        }
        return records;
    }

    public static void saveAppointmentOutcomesToCSV(List<AppointmentOutcomeRecord> records) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(APPOINTMENT_OUTCOME_FILE_PATH))) {
            bw.write("AppointmentID,DateOfAppointment,TypeOfService,ConsultationNotes,PrescribedMedications");
            bw.newLine();
            for (AppointmentOutcomeRecord record : records) {
                bw.write(record.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving Appointment_outcome.csv: " + e.getMessage());
        }
    }

    // View the appointment outcome details
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
}