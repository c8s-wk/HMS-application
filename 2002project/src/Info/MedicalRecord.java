package Info;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecord {
    private String patientID;
    private String name;
    private String dateOfBirth;
    private String gender;
    private String bloodType;
    private String emailAddress;
    private String contactNumber;
    private List<String> pastDiagnoses;
    private List<String> pastTreatments;
    private List<String> prescriptions;
    private String additionalNotes;

    private static final String FILE_PATH = "2002project/Patient_List.csv"; // Path to the CSV file

    // Constructor
    public MedicalRecord(String patientID, String name, String dateOfBirth, String gender, String bloodType, String emailAddress, String contactNumber,String pastDiagnoses, String presciption, String additionalNotes ) {
        this.patientID = patientID;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.bloodType = bloodType;
        this.emailAddress = emailAddress;
        this.contactNumber = contactNumber;
        this.pastDiagnoses = new ArrayList<>();
        this.pastTreatments = new ArrayList<>();
        this.prescriptions = new ArrayList<>();
        this.additionalNotes = "";
    }

    // Fetch the medical record from the CSV file by patient ID
    static MedicalRecord fetchMedicalRecordFromCSV(String patientID) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;

            // Skip the header
            br.readLine();

            // Read each line
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1); // Handle empty fields
                if (data[0].equals(patientID)) { // Match by patient ID
                    String name = data[1];
                    String dateOfBirth = data[2];
                    String gender = data[3];
                    String bloodType = data[4];
                    String email = data[5];
                    String contactNumber = data[6];
                    String pastDiagnosis = data.length > 7 ? data[7] : "";
                    String pastTreatment = data.length > 8 ? data[8] : "";
                    String prescriptions = data.length > 9 ? data[9] : "";
                    String additionalNotes = data.length > 10 ? data[10] : "";

                    return new MedicalRecord(patientID, name, dateOfBirth, gender, bloodType, email, contactNumber, pastDiagnosis, pastTreatment, prescriptions, additionalNotes);

                }
            }
        } catch (IOException e) {
            System.err.println("Error reading medical record CSV: " + e.getMessage());
        }

        System.err.println("No medical record found for patient ID: " + patientID);
        return null;
    }

    // Overloaded constructor with diagnosis, treatments, and prescriptions
    public MedicalRecord(String patientID, String name, String dateOfBirth, String gender, String bloodType, String emailAddress, String contactNumber, String pastDiagnoses, String pastTreatments, String prescriptions, String additionalNotes) {
        this(patientID, name, dateOfBirth, gender, bloodType, emailAddress, contactNumber, pastDiagnoses, prescriptions, additionalNotes);
        if (pastDiagnoses != null && !pastDiagnoses.isEmpty()) {
            for (String diagnosis : pastDiagnoses.split(";")) {
                this.pastDiagnoses.add(diagnosis.trim());
            }
        }
        if (pastTreatments != null && !pastTreatments.isEmpty()) {
            for (String treatment : pastTreatments.split(";")) {
                this.pastTreatments.add(treatment.trim());
            }
        }
        if (prescriptions != null && !prescriptions.isEmpty()) {
            for (String prescription : prescriptions.split(";")) {
                this.prescriptions.add(prescription.trim());
            }
        }
    }

    // Getters
    public String getPatientID() {
        return patientID;
    }

    public String getName() {
        return name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public String getBloodType() {
        return bloodType;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    // Setters with CSV update
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        updateCSVFile();
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
        updateCSVFile();
    }

    // Add methods for diagnosis, treatments, and prescriptions
    public void addDiagnosis(String diagnosis) {
        if (diagnosis != null && !diagnosis.isEmpty()) {
            this.pastDiagnoses.add(diagnosis.trim());
            updateCSVFile();
        }
    }

    public void addTreatment(String treatment) {
        if (treatment != null && !treatment.isEmpty()) {
            this.pastTreatments.add(treatment.trim());
            updateCSVFile();
        }
    }

    public void addPrescription(String prescription) {
        if (prescription != null && !prescription.isEmpty()) {
            this.prescriptions.add(prescription.trim());
            updateCSVFile();
        }
    }

    // Update CSV file with the latest changes
    void updateCSVFile() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(patientID)) { // Match patient ID
                    line = String.join(",", patientID, name, dateOfBirth, gender, bloodType, emailAddress, contactNumber,
                            String.join(";", pastDiagnoses), String.join(";", pastTreatments), String.join(";", prescriptions), additionalNotes);
                }
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Write updated data back to the file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "MedicalRecord{" +
                "patientID='" + patientID + '\'' +
                ", name='" + name + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", gender='" + gender + '\'' +
                ", bloodType='" + bloodType + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", pastDiagnoses=" + pastDiagnoses +
                ", pastTreatments=" + pastTreatments +
                ", prescriptions=" + prescriptions +
                ", additionalNotes='" + additionalNotes + '\'' +
                '}';
    }
}
