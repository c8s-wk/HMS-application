package Info;

import java.util.ArrayList;
import java.util.List;

public class Pharmacist extends User {

    private String name;
    private String gender;
    private int age;

    public Pharmacist(String userID, String password, String role, String name, String gender, int age) {
        super(userID, password, "Pharmacist");
        this.name = name;
        this.gender = gender;
        this.age = age;
    }

    // Getter for Name
    //public String getName() {
        //return this.name;
    //}

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getGender() {
        return gender;
    }

    @Override
    public int getAge() {
        return age;
    }

    // View Appointment Outcome Records (Prescriptions)
    public void viewAppointmentOutcomeRecords() {
        List<AppointmentOutcomeRecord> records = AppointmentOutcomeRecord.loadAppointmentOutcomesFromCSV();
        System.out.println("--- Appointment Outcome Records ---");
        if (records.isEmpty()) {
            System.out.println("Appointment Outcome Records are empty!");
        } else {
            for (AppointmentOutcomeRecord record : records) {
                record.viewOutcomeDetails();
            }
        }
    }

    // Update Prescription Status
    public boolean dispensedPrescriptionStatus(String prescriptionID) {
        // Load all prescriptions from CSV
        List<Prescription> prescriptions = Prescription.loadPrescriptionsFromCSV();
        boolean prescriptionUpdated = false;

        for (Prescription prescription : prescriptions) {
            if (prescription.getPrescriptionID().equals(prescriptionID)) {
                prescription.setStatus("dispensed");
                System.out.println("Prescription status updated to: " + prescription.getStatus());
                prescriptionUpdated = true;
                break;
            }
        }

        if (!prescriptionUpdated) {
            System.out.println("Prescription not found.");
            return false;
        }

        // Save updated prescriptions to CSV
        Prescription.savePrescriptionsToCSV(prescriptions);

        // Synchronize with AppointmentOutcomeRecord
        List<AppointmentOutcomeRecord> records = AppointmentOutcomeRecord.loadAppointmentOutcomesFromCSV();
        for (AppointmentOutcomeRecord record : records) {
            for (Prescription prescription : record.getPrescribedMedications()) {
                if (prescription.getPrescriptionID().equals(prescriptionID)) {
                    prescription.setStatus("dispensed");
                    System.out.println("Updated in AppointmentOutcomeRecord: " + record.getAppointmentID());
                    break;
                }
            }
        }

        // Save updated AppointmentOutcomeRecords to CSV
        AppointmentOutcomeRecord.saveAppointmentOutcomesToCSV(records);

        return true;
    }

    // View Medication Inventory
    public void viewMedicationInventory() {
        List<Medicine> medicines = Medicine.loadMedicinesFromCSV();
        System.out.println("--- Medication Inventory ---");
        for (Medicine medicine : medicines) {
            System.out.println("Name: " + medicine.getName() + ", Stock: " + medicine.getStock());
        }
        System.out.println("----------------------------------");
    }

    // Submit Replenishment Request
    public void submitReplenishmentRequest() {
        List<Medicine> medicines = Medicine.loadMedicinesFromCSV();
        for (Medicine medicine : medicines) {
            medicine.checkAndSetReplenishmentRequest();
        }
        System.out.println("Submit Replenishment Request Successfully");
    }

    @Override
    public String toString() {
        return "Pharmacist{" +
                "userID='" + getUserID() + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                '}';
    }
}
