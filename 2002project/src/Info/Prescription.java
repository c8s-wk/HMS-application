package Info;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Prescription {
    private String prescriptionID;
    private String appointmentID;
    private String patientID;
    private String doctorID;
    private String medicineName;
    private String status;

    private static final String PRESCRIPTION_FILE_PATH = "2002project/prescription.csv";
    private static final String APPOINTMENT_FILE_PATH = "2002project/appointment.csv";

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        updatePrescriptionInCSV();
    }

    // Load appointments to map PatientID and DoctorID
    public static Map<String, String[]> loadAppointmentsFromCSV() {
        Map<String, String[]> appointments = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(APPOINTMENT_FILE_PATH))) {
            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length < 6) continue;

                String appointmentID = data[0].trim();
                String patientID = data[1].trim();
                String doctorID = data[2].trim();
                appointments.put(appointmentID, new String[]{patientID, doctorID});
            }
        } catch (IOException e) {
            System.err.println("Error reading appointments CSV: " + e.getMessage());
        }
        return appointments;
    }

    // Load prescriptions from CSV and fill missing PatientID/DoctorID
    public static List<Prescription> loadPrescriptionsFromCSV() {
        List<Prescription> prescriptions = new ArrayList<>();
        Map<String, String[]> appointments = loadAppointmentsFromCSV();

        try (BufferedReader br = new BufferedReader(new FileReader(PRESCRIPTION_FILE_PATH))) {
            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length < 6) continue;

                String prescriptionID = data[0].trim();
                String appointmentID = data[1].trim();
                String patientID = data[2].trim();
                String doctorID = data[3].trim();
                String medicineName = data[4].trim();
                String status = data[5].trim();

                // Fill missing PatientID and DoctorID
                if (patientID.isEmpty() || doctorID.isEmpty()) {
                    String[] appointmentData = appointments.getOrDefault(appointmentID, new String[]{"Unknown", "Unknown"});
                    patientID = patientID.isEmpty() ? appointmentData[0] : patientID;
                    doctorID = doctorID.isEmpty() ? appointmentData[1] : doctorID;
                }

                prescriptions.add(new Prescription(
                        prescriptionID, appointmentID, patientID, doctorID, medicineName, status
                ));
            }
        } catch (IOException e) {
            System.err.println("Error reading prescriptions CSV: " + e.getMessage());
        }
        return prescriptions;
    }

    // Save prescriptions to CSV
    public static void savePrescriptionsToCSV(List<Prescription> prescriptions) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PRESCRIPTION_FILE_PATH))) {
            bw.write("PrescriptionID,AppointmentID,PatientID,DoctorID,MedicationName,Status");
            bw.newLine();
            for (Prescription prescription : prescriptions) {
                bw.write(prescription.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing prescriptions CSV: " + e.getMessage());
        }
    }

    // Update a specific prescription in the CSV
    private void updatePrescriptionInCSV() {
        List<Prescription> prescriptions = loadPrescriptionsFromCSV();
        for (int i = 0; i < prescriptions.size(); i++) {
            if (prescriptions.get(i).getPrescriptionID().equals(this.prescriptionID)) {
                // Preserve original PatientID and DoctorID if status is being updated
                Prescription existingPrescription = prescriptions.get(i);
                if (existingPrescription.getPatientID().equals("Unknown")) {
                    existingPrescription.patientID = this.patientID;
                }
                if (existingPrescription.getDoctorID().equals("Unknown")) {
                    existingPrescription.doctorID = this.doctorID;
                }
                existingPrescription.status = this.status;
                break;
            }
        }
        savePrescriptionsToCSV(prescriptions);
    }

    // Convert Prescription object to CSV format
    public String toCSV() {
        return String.join(",", prescriptionID, appointmentID, patientID, doctorID, medicineName, status);
    }

    @Override
    public String toString() {
        return "Prescription{" +
                "prescriptionID='" + prescriptionID + '\'' +
                ", appointmentID='" + appointmentID + '\'' +
                ", patientID='" + patientID + '\'' +
                ", doctorID='" + doctorID + '\'' +
                ", medicineName='" + medicineName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}