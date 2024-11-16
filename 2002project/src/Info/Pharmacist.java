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
    public void dispenseAllPrescriptions(AppointmentOutcomeRecord record) {
        record.dispenseAllPrescriptions();
        record.viewOutcomeDetails();//Verify that the prescription status is updated, and the change is reflected in the patient's records.
    }
    public void viewInventory(){
        Inventory.viewInventory();
    }

    public void checkLowStock() {
        Inventory.checkLowStock();
    }

    public void submitReplenishmentRequest(String medicationName) {
        Medicine medicine = Inventory.getMedicine(medicationName);

    }
}
