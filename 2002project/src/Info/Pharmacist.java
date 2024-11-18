package Info;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Pharmacist extends User {

    private String name;
    private String gender;
    private int age;
    private static final String REPLENISHMENT_REQUEST_FILE = "Replenishment_Requests.csv";

    public Pharmacist(String userID, String password, String name, String role,  String gender, int age) {
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
        Scanner scanner = new Scanner(System.in);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(REPLENISHMENT_REQUEST_FILE, true))) {
            System.out.println("Enter Staff ID:");
            String staffID = scanner.nextLine();

            System.out.println("Enter Medicine Name:");
            String medicineName = scanner.nextLine();

            System.out.println("Enter Quantity to Replenish:");
            int newStock = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            // Get current date
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String currentDate = LocalDate.now().format(dateFormatter);

            // Write to CSV
            bw.write(medicineName + "," + newStock + "," + staffID + "," + currentDate);
            bw.newLine();
            System.out.println("Replenishment request submitted successfully for " + medicineName);
        } catch (IOException e) {
            System.err.println("Error writing replenishment request file: " + e.getMessage());
        }
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
