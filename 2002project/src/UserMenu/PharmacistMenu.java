package UserMenu;

import Info.Pharmacist;

import java.util.Scanner;

public class PharmacistMenu {

    private static Pharmacist pharmacist;

    public static void setPharmacist(Pharmacist pharmacistUser) {
        pharmacist = pharmacistUser;
    }

    public static void displayMenu() {
        System.out.println("\n--- Pharmacist Menu ---");
        System.out.println("1. View Appointment Outcome Records");
        System.out.println("2. Update Prescription Status");
        System.out.println("3. View Medication Inventory");
        System.out.println("4. Submit Replenishment Request");
        System.out.println("5. Logout");
        System.out.print("Please enter your choice: ");
    }

    public static void handleChoice(int choice) {
        Scanner scanner = new Scanner(System.in);

        switch (choice) {
            case 1 -> pharmacist.viewAppointmentOutcomeRecords();
            case 2 -> {
                System.out.print("Enter Prescription ID to dispensed status: ");
                String prescriptionID = scanner.nextLine();
                //System.out.print("Enter new status (e.g., Dispensed): ");
                //String status = scanner.nextLine();
                pharmacist.dispensedPrescriptionStatus(prescriptionID);
            }
            case 3 -> pharmacist.viewMedicationInventory();
            case 4 -> {
                System.out.print("Enter Medicine Name: ");
                String medicineName = scanner.nextLine();
                System.out.print("Enter Additional Stock: ");
                int additionalStock = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                pharmacist.submitReplenishmentRequest();
            }
            case 5 -> {
                System.out.println("Logging out..." );
            }
            default -> System.out.println("Invalid choice. Please try again.");

        }
    }
}
