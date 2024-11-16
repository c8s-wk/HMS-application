package Info;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Patient extends User{
    private MedicalRecord medicalRecord;
    private List<Appointment> appointments;

    private static final String FILE_PATH = "Patient_List.csv"; // Path to the CSV file

    // Constructor
    public Patient(String patientID, String password, String role, MedicalRecord medicalRecord) {
        super(patientID, password, role);
        this.medicalRecord = medicalRecord;
        this.appointments = new ArrayList<>();
    }

    // Getters for MedicalRecord attributes
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

    public String getContactNumber() {
        return medicalRecord.getContactNumber();
    }

    // Update email address
    public void setEmailAddress(String newEmailAddress) {
        medicalRecord.setEmailAddress(newEmailAddress);
        updateCSVFile(); // Update the CSV file
        System.out.println("Email address updated to: " + newEmailAddress);
    }

    // Update contact number
    public void setContactNumber(String newContactInfo) {
        medicalRecord.setContactNumber(newContactInfo);
        updateCSVFile(); // Update the CSV file
        System.out.println("Contact information updated to: " + newContactInfo);
    }

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    // Update CSV File
    private void updateCSVFile() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(getUserID())) { // Match patient ID
                    // Update the row with new values
                    line = String.join(",",
                            getUserID(),
                            getName(),
                            getDateOfBirth(),
                            getGender(),
                            getBloodType(),
                            getEmailAddress(),
                            getContactNumber()
                    );
                }
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Write updated lines back to the file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Display available appointment slots
    public void viewAvailableAppointmentSlots(List<Doctor> doctors) {
        System.out.println("Available Appointment Slots:");
        for (Doctor doctor : doctors) {
            doctor.displayAvailableSlots();
        }
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
