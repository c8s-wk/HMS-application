package Info;

public class Pharmacist extends User{
    private String pharmacistID;
   

    
    // constructor
    public Pharmacist(String pharmacistID) {
        this.pharmacistID = pharmacistID;
      
    }
    // get method and set method
    public String getPharmacistID() {
        return pharmacistID;
    }
    public void setPharmacistID(String pharmacistID) {
        this.pharmacistID = pharmacistID;
    }


    public void viewAppointmentOutcome(AppointmentOutcomeRecord record) {
        System.out.println("Viewing appointment outcome details:");
        record.viewOutcomeDetails();
    }

    public void updatePrescriptionStatus(AppointmentOutcomeRecord record, String medicationName, String newStatus) {
        for (Prescription prescription : record.getPrescribedMedications()) {
            if (prescription.getMedicationName().equals(medicationName)) {
                prescription.updateStatus(newStatus);
                System.out.println("Prescription for " + medicationName + " updated to: " + newStatus);
            }
        }
    }
    public void viewInventory(){
        Inventory.viewInventory();
    }

    public void checkLowStock() {
        Inventory.checkLowStock();
    }

    public void submitReplenishmentRequest(String medicationName, int amount) {
        Medicine medicine = Inventory.getMedicine(medicationName);
        if (medicine != null) {
            medicine.updateStockLevel(amount);
        } else {
            System.out.println("Medicine not found in inventory: " + medicationName);
        }
    }
}
