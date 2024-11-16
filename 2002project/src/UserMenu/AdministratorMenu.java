package UserMenu;

import Utils.CSVUtils;
import Info.Administrator;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AdministratorMenu {

    //private static final List<String> hospitalStaff = new ArrayList<>();
    //private static final List<String> appointments = new ArrayList<>();
    //private static final List<String> medicationInventory = new ArrayList<>();
    //private static final List<String> replenishmentRequests = new ArrayList<>();
    private static final String STAFF_FILE = "Staff_List.csv";
    private static final String APPOINTMENTS_FILE = "Appointments.csv";
    private static final String INVENTORY_FILE = "Medicine_List.csv";
    private final Administrator admin;
    private static final Scanner scanner = new Scanner(System.in);

    public AdministratorMenu(Administrator admin) {
        this.admin = admin;
    }

    public static void displayMenu() {
        System.out.println("\nAdministrator Menu:");
        System.out.println("1. View and Manage Hospital Staff");
        System.out.println("2. View Appointment Details");
        System.out.println("3. View and Manage Medication Inventory");
        System.out.println("4. Approve Replenishment Requests");
        System.out.println("5. Logout");
        System.out.print("Enter your choice: ");
    }

    public void handleChoice(int choice) {
        switch (choice) {
            case 1:
                manageHospitalStaff();
                break;
            case 2:
                viewAppointmentDetails();
                break;
            case 3:
                manageMedicalInventory();
                break;
            case 4:
                approveReplenishmentRequests();
                break;
            case 5:
                System.out.println("Logging out...");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private void manageHospitalStaff() {
        System.out.println("\nManaging hospital staff...");
        System.out.println("1. View Staff");
        System.out.println("2. Add Staff");
        System.out.println("3. Update Staff");
        System.out.println("4. Remove Staff");
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                viewStaff();
                break;
            case 2:  
                addStaff();
                break;
            case 3:
                updateStaff();
                break;
            case 4:
                removeStaff();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void viewStaff() {
        List<String[]> staff = admin.viewStaff(STAFF_FILE);
        System.out.println("\n=== Hospital Staff ===");
        for (String[] record : staff) {
            System.out.println(String.join(", ", record));
        }
    }

    private void addStaff() {
        System.out.print("Enter new staff details (ID,Name,Role): ");
        String[] newStaff = scanner.nextLine().split(",");
        admin.addStaff(STAFF_FILE, newStaff);
        System.out.println("Staff added successfully.");
    }

    private void updateStaff() {
        System.out.print("Enter staff ID to update: ");
        String staffID = scanner.nextLine();
        System.out.print("Enter updated details (ID,Name,Role): ");
        String[] updatedStaff = scanner.nextLine().split(",");
        if (admin.updateStaff(STAFF_FILE, staffID, updatedStaff)) {
            System.out.println("Staff updated successfully.");
        } else {
            System.out.println("Staff not found.");
        }
    }

    private void removeStaff() {
        System.out.print("Enter staff ID to remove: ");
        String staffID = scanner.nextLine();
        if (admin.removeStaff(STAFF_FILE, staffID)) {
            System.out.println("Staff removed successfully.");
        } else {
            System.out.println("Staff not found.");
        }
    }

        /*private void viewAppointmentDetails() {
        List<String[]> appointmentRecords = CSVUtils.readCSV(APPOINTMENTS_FILE);
        viewRecords(appointmentRecords, "Appointments");
    }*/

    private void manageMedicalInventory() {
        System.out.println("\nManaging medical inventory...");
        System.out.println("1. View Inventory");
        System.out.println("2. Add Medication");
        System.out.println("3. Update Medication");
        System.out.println("4. Remove Medication");
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                viewInventory();
                break;
            case 2:
                addInventory();
                break;
            case 3:
                updateInventory();
                break;
            case 4:
                removeInventory();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void viewInventory() {
        List<String[]> inventory = admin.viewInventory(INVENTORY_FILE);
        System.out.println("\n=== Medication Inventory ===");
        for (String[] record : inventory) {
            System.out.println(String.join(", ", record));
        }
    }

    private void addInventory() {
        System.out.print("Enter new medication details (Name,Stock,LowStockAlert): ");
        String[] newInventory = scanner.nextLine().split(",");
        admin.addInventory(INVENTORY_FILE, newInventory);
        System.out.println("Medication added successfully.");
    }

    private void updateInventory() {
        System.out.print("Enter medication name to update: ");
        String itemName = scanner.nextLine();
        System.out.print("Enter updated details (Name,Stock,LowStockAlert): ");
        String[] updatedInventory = scanner.nextLine().split(",");
        if (admin.updateInventory(INVENTORY_FILE, itemName, updatedInventory)) {
            System.out.println("Medication updated successfully.");
        } else {
            System.out.println("Medication not found.");
        }
    }

    private void removeInventory() {
        System.out.print("Enter medication name to remove: ");
        String itemName = scanner.nextLine();
        if (admin.removeInventory(INVENTORY_FILE, itemName)) {
            System.out.println("Medication removed successfully.");
        } else {
            System.out.println("Medication not found.");
        }
    }
}

   /* private static void viewStaff() {
        System.out.println("Hospital Staff List:");
        for (String staff : hospitalStaff) {
            System.out.println("- " + staff);
        }
    }

    private static void addStaff() {
        System.out.print("Enter staff name to add: ");
        String newStaff = scanner.nextLine();
        hospitalStaff.add(newStaff);
        System.out.println(newStaff + " added.");
    }

    private static void updateStaff() {
        System.out.print("Enter staff name to update: ");
        String staffName = scanner.nextLine();
        if (hospitalStaff.contains(staffName)) {
            System.out.print("Enter new information for staff: ");
            String updatedInfo = scanner.nextLine();
            hospitalStaff.set(hospitalStaff.indexOf(staffName), updatedInfo);
            System.out.println(staffName + " updated to " + updatedInfo);
        } else {
            System.out.println("Staff not found.");
        }
    }

    private static void removeStaff() {
        System.out.print("Enter staff name to remove: ");
        String staffToRemove = scanner.nextLine();
        if (hospitalStaff.remove(staffToRemove)) {
            System.out.println(staffToRemove + " removed.");
        } else {
            System.out.println("Staff not found.");
        }
    }

    private static void viewAppointmentDetails() {
        System.out.println("Viewing appointment details...");
        for (String appointment : appointments) {
            System.out.println(appointment);
        }
    }

    private static void manageMedicationInventory() {
        System.out.println("Managing medication inventory...");
        System.out.println("1. View Inventory");
        System.out.println("2. Add Medication");
        System.out.println("3. Update Medication");
        System.out.println("4. Remove Medication");
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                viewInventory();
                break;
            case 2:
                addMedication();
                break;
            case 3:
                updateMedication();
                break;
            case 4:
                removeMedication();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void viewInventory() {
        System.out.println("Medication Inventory:");
        for (String medication : medicationInventory) {
            System.out.println("- " + medication);
        }
    }

    private static void addMedication() {
        System.out.print("Enter medication name to add: ");
        String newMedication = scanner.nextLine();
        medicationInventory.add(newMedication);
        System.out.println(newMedication + " added to inventory.");
    }

    private static void updateMedication() {
        System.out.print("Enter medication name to update: ");
        String medicationName = scanner.nextLine();
        if (medicationInventory.contains(medicationName)) {
            System.out.print("Enter new information for medication: ");
            String updatedInfo = scanner.nextLine();
            medicationInventory.set(medicationInventory.indexOf(medicationName), updatedInfo);
            System.out.println(medicationName + " updated to " + updatedInfo);
        } else {
            System.out.println("Medication not found.");
        }
    }

    private static void removeMedication() {
        System.out.print("Enter medication name to remove: ");
        String medicationToRemove = scanner.nextLine();
        if (medicationInventory.remove(medicationToRemove)) {
            System.out.println(medicationToRemove + " removed from inventory.");
        } else {
            System.out.println("Medication not found.");
        }
    }*/

    /*private static void approveReplenishmentRequests() {
        System.out.println("Approving replenishment requests...");
        if (replenishmentRequests.isEmpty()) {
            System.out.println("No replenishment requests pending.");
        } else {
            for (String request : replenishmentRequests) {
                System.out.println("Approving request for: " + request);
                replenishmentRequests.remove(request);
                // Here, update inventory levels if necessary
            }
        }
    }
}*/
/*public class AdministratorMenu {

    public static void displayMenu() {
        System.out.println("\nInfo.Administrator Menu:");
        System.out.println("1. View and Manage Hospital Staff");
        System.out.println("2. View Appointments Details");
        System.out.println("3. View and Manage Medication Inventory");
        System.out.println("4. Approve Replenishment Requests");
        System.out.println("5. Logout");
        System.out.print("Enter your choice: ");
    }

    public static void handleChoice(int choice) {
        switch (choice) {
            case 1:
                manageHospitalStaff();
                break;
            case 2:
                viewAppointmentDetails();
                break;
            case 3:
                manageMedicationInventory();
                break;
            case 4:
                approveReplenishmentRequests();
                break;
            case 5:
                System.out.println("Logging out...");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void manageHospitalStaff() {
        System.out.println("Managing hospital staff...");
        // Implement managing hospital staff logic
    }

    private static void viewAppointmentDetails() {
        System.out.println("Viewing appointment details...");
        // Implement viewing appointment details logic
    }

    private static void manageMedicationInventory() {
        System.out.println("Managing medication inventory...");
        // Implement managing medication inventory logic
    }

    private static void approveReplenishmentRequests() {
        System.out.println("Approving replenishment requests...");
        // Implement approving replenishment requests logic
    }
}*/



