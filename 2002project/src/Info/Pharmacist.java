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
        if(records.isEmpty()){
            System.out.println("Appointment Outcome Records is empty!");
        }
        for (AppointmentOutcomeRecord record : records) {
            record.viewOutcomeDetails();
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
        //System.out.println(medicines.size() + " medicines");
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


}