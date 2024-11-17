package Info;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Prescription {
    private String prescriptionID;
    private String appointmentID;
    private String patientID;
    private String doctorID;
    private String medicineName;
    private String status;

    private static final String PRESCRIPTION_FILE_PATH = "2002project/prescription.csv"; // The CSV file path

    // Constructor
    public Prescription(String prescriptionID, String appointmentID, String patientID, String doctorID, String medicineName, String status) {
        this.prescriptionID = prescriptionID;
        this.appointmentID = appointmentID;
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.medicineName = medicineName;
        this.status = status;
    }

    // Getters and Setters
    public String getPrescriptionID() {
        return prescriptionID;
    }

    public String getAppointmentID() {
        return appointmentID;
    }

    public String getPatientID() {
        return patientID;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicationName(String medicineName) {
        this.medicineName = medicineName;
        updatePrescriptionInCSV();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        updatePrescriptionInCSV();
    }

    // Load prescriptions from CSV
    public static List<Prescription> loadPrescriptionsFromCSV() {
        List<Prescription> prescriptions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(PRESCRIPTION_FILE_PATH))) {
            String line;
            // Skip header
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length < 6) {
                    continue; // Skip invalid rows
                }
                String prescriptionID = data[0].trim();
                String appointmentID = data[1].trim();
                String patientID = data[2].trim();
                String doctorID = data[3].trim();
                String medicationName = data[4].trim();
                String status = data[5].trim();
                prescriptions.add(new Prescription(prescriptionID, appointmentID, patientID, doctorID, medicationName, status));
            }
        } catch (IOException e) {
            System.err.println("Error reading prescriptions CSV: " + e.getMessage());
        }
        return prescriptions;
    }

    // Save prescriptions to CSV
    public static void savePrescriptionsToCSV(List<Prescription> prescriptions) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PRESCRIPTION_FILE_PATH))) {
            // Write header
            bw.write("PrescriptionID,AppointmentID,PatientID,DoctorID,MedicationName,Status");
            bw.newLine();
            // Write data
            for (Prescription prescription : prescriptions) {
                bw.write(prescription.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing prescriptions CSV: " + e.getMessage());
        }
    }

    // Update this prescription in CSV
    private void updatePrescriptionInCSV() {
        List<Prescription> prescriptions = loadPrescriptionsFromCSV();
        for (int i = 0; i < prescriptions.size(); i++) {
            if (prescriptions.get(i).getPrescriptionID().equalsIgnoreCase(this.prescriptionID)) {
                prescriptions.set(i, this);
                break;
            }
        }
        savePrescriptionsToCSV(prescriptions);
    }

    // Convert to CSV format
    public String toCSV() {
        return prescriptionID + "," + appointmentID + "," + patientID + "," + doctorID + "," + medicineName + "," + status;
    }

    @Override
    public String toString() {
        return "Prescription{" +
                "prescriptionID='" + prescriptionID + '\'' +
                ", appointmentID='" + appointmentID + '\'' +
                ", patientID='" + patientID + '\'' +
                ", doctorID='" + doctorID + '\'' +
                ", medicationName='" + medicineName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}



