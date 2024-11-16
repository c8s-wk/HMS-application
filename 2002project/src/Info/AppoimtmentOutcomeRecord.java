package Info;

public class AppoimtmentOutcomeRecord {
    private String appointmentID;
    private String dateOfAppointment;
    private String typeOfService;
    private List<Prescription> prescribedMedications;
    private String consultationNotes;

    // constructor
    public AppointmentOutcomeRecord(String appointmentID, String dateOfAppointment, String typeOfService, String consultationNotes) {
        this.appointmentID = appointmentID;
        this.dateOfAppointment = dateOfAppointment;
        this.typeOfService = typeOfService;
        this.consultationNotes = consultationNotes;
        this.prescribedMedications = new ArrayList<>();
    }

    public void viewOutcomeDetails() {
        System.out.println("===== Appointment Outcome Details =====");
        System.out.println("Appointment ID: " + appointmentID);
        System.out.println("Date of Appointment: " + dateOfAppointment);
        System.out.println("Type of Service: " + typeOfService);
        System.out.println("Consultation Notes: " + consultationNotes);
        System.out.println("Prescribed Medications:");
        if (prescribedMedications.isEmpty()) {
            System.out.println("  No medications prescribed.");
        } else {
            for (Prescription prescription : prescribedMedications) {
                System.out.println("  - " + prescription.getMedicationName() + " (Status: " + prescription.getStatus() + ")");
            }
        }
        System.out.println("=======================================");
    }

    public void dispenseAllPrescriptions() {
        if (prescribedMedications.isEmpty()) {
            System.out.println("No prescriptions to dispense for Appointment ID: " + appointmentID);
            return;
        }

        System.out.println("Dispensing all prescriptions for Appointment ID: " + appointmentID + "...");
        for (Prescription prescription : prescribedMedications) {
            if (!prescription.getStatus().equalsIgnoreCase("dispensed")) {
                prescription.dispense();
                System.out.println("Dispensed: " + prescription.getMedicineName());
            } else {
                System.out.println("Already dispensed: " + prescription.getMedicineName());
            }
        }
        System.out.println("All prescriptions have been processed.");
    }

}
