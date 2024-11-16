package Info;

import java.util.List;

public class Pharmacist extends User {

    public Pharmacist(String userID, String password, String role) {
        super(userID, password, role);
    }

    // View Appointment Outcome Records (Prescriptions)
    public void viewAppointmentOutcomeRecords() {
        List<Prescription> prescriptions = Prescription.loadPrescriptionsFromCSV();
        System.out.println("--- Appointment Outcome Records ---");
        for (Prescription prescription : prescriptions) {
            System.out.println(prescription);
        }
    }

    // Update Prescription Status
    public boolean updatePrescriptionStatus(String prescriptionID, String newStatus) {
        List<Prescription> prescriptions = Prescription.loadPrescriptionsFromCSV();
        for (Prescription prescription : prescriptions) {
            if (prescription.getPrescriptionID().equals(prescriptionID)) {
                prescription.setStatus(newStatus);
                Prescription.savePrescriptionsToCSV(prescriptions);
                System.out.println("Prescription status updated to: " + newStatus);
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
            System.out.println(medicine);
        }
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

