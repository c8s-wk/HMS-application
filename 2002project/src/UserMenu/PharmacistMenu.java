package UserMenu;

import Info.Pharmacist;
import java.util.Scanner;

public class PharmacistMenu {

    private static Pharmacist pharmacist;

    // Make sure to set the pharmacist object
    public static void setPharmacist(Pharmacist pharmacistUser) {
        pharmacist = pharmacistUser;
    }

    public static void displayMenu() {
        Scanner scanner = new Scanner(System.in); // Create Scanner instance here

        // Keep displaying the menu until the user chooses to logout
        while (true) {
            System.out.println("\n--- Pharmacist Menu ---");
            System.out.println("1. View Appointment Outcome Records");
            System.out.println("2. Update Prescription Status");
            System.out.println("3. View Medication Inventory");
            System.out.println("4. Submit Replenishment Request");
            System.out.println("5. Logout");
            System.out.print("Please enter your choice: ");

            int choice = scanner.nextInt();  // Get user choice
            scanner.nextLine(); // Consume newline

            // If user chooses to logout, break the loop and exit
            if (choice == 5) {
                System.out.println("Logging out...");
                break; // Break the loop and go back to login
            }

            handleChoice(choice);  // Pass the user's choice to handleChoice method
        }
    }

    public static void handleChoice(int choice) {
        // Check if pharmacist is null before performing any action
        if (pharmacist == null) {
            System.out.println("Error: Pharmacist is not initialized.");
            return;
        }

        switch (choice) {
            case 1:
                pharmacist.viewAppointmentOutcomeRecords();
                break;
            case 2:
                // Now that scanner is not passed, you can ask for input inside this case block
                Scanner scanner = new Scanner(System.in); // Create Scanner instance here
                System.out.print("Enter Prescription ID to update status: ");
                String prescriptionID = scanner.nextLine();
                pharmacist.dispensedPrescriptionStatus(prescriptionID);
                break;
            case 3:
                pharmacist.viewMedicationInventory();
                break;
            case 4:
                pharmacist.submitReplenishmentRequest();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
}
