package Info;

import java.util.List;

public class Pharmacist extends User {

    public Pharmacist(String userID, String password, String role) {
        super(userID, password, role);
    }

    // View Appointment Outcome Records (Prescriptions)
    public void viewAppointmentOutcomeRecords() {
        List<AppointmentOutcomeRecord> records = AppointmentOutcomeRecord.loadAppointmentOutcomesFromCSV();
        System.out.println("--- Appointment Outcome Records ---");
        for (AppointmentOutcomeRecord record : records) {
            record.viewOutcomeDetails();
        }
    }

    // Update Prescription Status
    public boolean updatePrescriptionStatus(String prescriptionID) {
        List<Prescription> prescriptions = Prescription.loadPrescriptionsFromCSV();
        for (Prescription prescription : prescriptions) {
            if (prescription.getPrescriptionID().equals(prescriptionID)) {
                prescription.setStatus("dispensed");
                Prescription.savePrescriptionsToCSV(prescriptions);
                System.out.println("Prescription status updated to: " + prescription.getStatus());
                return true;
            }
        }
        System.out.println("Prescription not found.");
        return false;
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
    public void submitReplenishmentRequest(String medicineName, int additionalStock) {
        List<Medicine> medicines = Medicine.loadMedicinesFromCSV();
        for (Medicine medicine : medicines) {
            if (medicine.getName().equalsIgnoreCase(medicineName)) {
                // For demonstration, we'll directly update the stock
                int newStock = medicine.getStock() + additionalStock;
                medicine.setStock(newStock);
                Medicine.saveMedicinesToCSV(medicines);
                System.out.println("Replenishment processed for " + additionalStock + " units of " + medicineName);
                return;
            }
        }
        System.out.println("Medicine not found in inventory: " + medicineName);
    }


}