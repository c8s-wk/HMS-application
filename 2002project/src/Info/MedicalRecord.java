package Info;

import java.util.ArrayList;
import java.util.List;

public class MedicalRecord {
    private String patientID;
    private String name;
    private String dateOfBirth;
    private String gender;
    private String bloodType;
    private String emailAddress; // Added email address
    private String contactNumber; // Added contact number
    private List<String> pastDiagnoses; // List of past diagnoses
    private List<String> pastTreatments; // List of past treatments
    private List<String> prescriptions; // List of prescribed medications
    private String additionalNotes;

    // Constructor
    public MedicalRecord(String patientID, String name, String dateOfBirth, String gender, String bloodType, String emailAddress, String contactNumber) {
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

    // Add past diagnosis, treatment, and prescriptions during construction
    public MedicalRecord(String patientID, String name, String dateOfBirth, String gender, String bloodType, String emailAddress, String contactNumber, String pastDiagnosis, String pastTreatment, String pastPrescriptions) {
        this(patientID, name, dateOfBirth, gender, bloodType, emailAddress, contactNumber);
        if (pastDiagnosis != null && !pastDiagnosis.isEmpty()) {
            for (String diagnosis : pastDiagnosis.split(";")) {
                this.pastDiagnoses.add(diagnosis.trim());
            }
        }
        if (pastTreatment != null && !pastTreatment.isEmpty()) {
            for (String treatment : pastTreatment.split(";")) {
                this.pastTreatments.add(treatment.trim());
            }
        }
        if (pastPrescriptions != null && !pastPrescriptions.isEmpty()) {
            for (String prescription : pastPrescriptions.split(";")) {
                this.prescriptions.add(prescription.trim());
            }
        }
    }

    // Add new diagnosis
    public void addDiagnosis(String diagnosis) {
        if (diagnosis != null && !diagnosis.isEmpty()) {
            this.pastDiagnoses.add(diagnosis.trim());
            System.out.println("Diagnosis added: " + diagnosis);
        } else {
            System.out.println("Invalid diagnosis input.");
        }
    }

    // Add new treatment plan
    public void addTreatment(String treatment) {
        if (treatment != null && !treatment.isEmpty()) {
            this.pastTreatments.add(treatment.trim());
            System.out.println("Treatment plan added: " + treatment);
        } else {
            System.out.println("Invalid treatment input.");
        }
    }

    // Add new prescription
    public void addPrescription(String prescription) {
        if (prescription != null && !prescription.isEmpty()) {
            this.prescriptions.add(prescription.trim());
            System.out.println("Prescription added: " + prescription);
        } else {
            System.out.println("Invalid prescription input.");
        }
    }

    // Getters and Setters
    public String getPatientID() {
        return patientID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public List<String> getPastDiagnoses() {
        return pastDiagnoses;
    }

    public List<String> getPastTreatments() {
        return pastTreatments;
    }

    public List<String> getPrescriptions() {
        return prescriptions;
    }

    public String getAdditionalNotes() {
        return additionalNotes;
    }

    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
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
