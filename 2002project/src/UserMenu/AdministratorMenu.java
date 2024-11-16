package UserMenu;

import Info.Administrator;
import Info.Appointment;
import Info.Medicine;
import Info.User;

import java.util.List;
import java.util.Scanner;

public class AdministratorMenu {

    private static Administrator admin; // The logged-in administrator
    private static List<User> staff; // List of all hospital staff
    private static List<Appointment> appointments; // List of all appointments
    private static List<Medicine> inventory; // List of all medicines in inventory

    // Method to set the administrator context
    public static void setAdministratorContext(Administrator administrator, List<User> hospitalStaff, List<Appointment> appointmentList, List<Medicine> medicineInventory) {
        admin = administrator;
        staff = hospitalStaff;
        appointments = appointmentList;
        inventory = medicineInventory;
    }

    public static void displayMenu() {
        System.out.println("\n--- Administrator Menu ---");
        System.out.println("1. View and Manage Hospital Staff");
        System.out.println("2. View Appointment Details");
        System.out.println("3. View and Manage Medication Inventory");
        System.out.println("4. Approve Replenishment Requests");
        System.out.println("5. Logout");
        System.out.print("Please enter your choice: ");
    }

    public static void handleChoice() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1 -> manageHospitalStaff(scanner);
                case 2 -> admin.viewAppointments(appointments);
                case 3 -> manageInventory(scanner);
                case 4 -> approveReplenishmentRequest(scanner);
                case 5 -> {
                    System.out.println("Logging out...");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void manageHospitalStaff(Scanner scanner) {
        System.out.println("\n--- Manage Hospital Staff ---");
        System.out.println("1. View Staff");
        System.out.println("2. Add Staff");
        System.out.println("3. Remove Staff");
        System.out.print("Enter your choice: ");
        int staffChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (staffChoice) {
            case 1 -> admin.viewAndManageStaff(staff);
            case 2 -> addStaff(scanner);
            case 3 -> removeStaff(scanner);
            default -> System.out.println("Invalid choice.");
        }
    }

    private static void addStaff(Scanner scanner) {
        System.out.print("Enter Staff ID: ");
        String staffID = scanner.nextLine();
        System.out.print("Enter Staff Name: ");
        String staffName = scanner.nextLine();
        System.out.print("Enter Role: ");
        String role = scanner.nextLine();
        System.out.print("Enter Gender: ");
        String gender = scanner.nextLine();
        System.out.print("Enter Age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        User newStaff = new User(staffID, "password", role);
        admin.addStaff(staff, newStaff);
        System.out.println("Staff member added successfully.");
    }

    private static void removeStaff(Scanner scanner) {
        System.out.print("Enter Staff ID to remove: ");
        String staffID = scanner.nextLine();
        admin.removeStaff(staff, staffID);
        System.out.println("Staff member removed successfully.");
    }

    private static void manageInventory(Scanner scanner) {
        System.out.println("\n--- Manage Inventory ---");
        System.out.println("1. View Inventory");
        System.out.println("2. Update Stock");
        System.out.println("3. Update Low Stock Alert");
        System.out.print("Enter your choice: ");
        int inventoryChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (inventoryChoice) {
            case 1 -> admin.viewInventory(inventory);
            case 2 -> updateStock(scanner);
            case 3 -> updateLowStockAlert(scanner);
            default -> System.out.println("Invalid choice.");
        }
    }

    private static void updateStock(Scanner scanner) {
        System.out.print("Enter Medicine Name: ");
        String medicineName = scanner.nextLine();
        System.out.print("Enter New Stock Quantity: ");
        int newStock = scanner.nextInt();
        admin.updateStock(inventory, medicineName, newStock);
        System.out.println("Stock updated successfully.");
    }

    private static void updateLowStockAlert(Scanner scanner) {
        System.out.print("Enter Medicine Name: ");
        String medicineName = scanner.nextLine();
        System.out.print("Enter New Low Stock Alert Level: ");
        int newAlertLevel = scanner.nextInt();
        admin.updateLowStockAlert(inventory, medicineName, newAlertLevel);
        System.out.println("Low stock alert level updated successfully.");
    }

    private static void approveReplenishmentRequest(Scanner scanner) {
        System.out.print("Enter Medicine Name for Replenishment: ");
        String medicineName = scanner.nextLine();
        System.out.print("Enter Additional Stock Quantity: ");
        int additionalStock = scanner.nextInt();
        admin.approveReplenishmentRequest(inventory, medicineName, additionalStock);
        System.out.println("Replenishment request approved successfully.");
    }
}
