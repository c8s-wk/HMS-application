package UserMenu;

import Info.Administrator;
import Info.Appointment;
import Info.Medicine;
import Info.User;
import Info.Doctor;
import Info.Pharmacist;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
public class AdministratorMenu {

    private static Administrator admin;

    public static void setAdministrator(Administrator administrator) {
        admin = administrator;
    }

    public static void displayMenu() {
        System.out.println("\n--- Administrator Menu ---");
        System.out.println("1. View and Manage Hospital Staff");
        System.out.println("2. View Appointments");
        System.out.println("3. View and Manage Medication Inventory");
        System.out.println("4. Approve Replenishment Requests");
        System.out.println("5. Logout");
        System.out.print("Please enter your choice: ");
    }

    public static void handleMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1 -> manageStaff(scanner);
                case 2 -> viewAppointments();
                case 3 -> manageInventory(scanner);
                case 4 -> admin.viewAndApproveReplenishmentRequests();
                case 5 -> {
                    System.out.println("Logging out...");
                    running = false; // Exit the menu loop
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }




    private static void manageStaff(Scanner scanner) {
        System.out.println("\n--- Manage Hospital Staff ---");
        System.out.println("1. View Staff");
        System.out.println("2. Add Staff");
        System.out.println("3. Remove Staff");
        System.out.print("Enter your choice: ");
        int staffChoice = scanner.nextInt();
        scanner.nextLine();

        switch (staffChoice) {
            case 1 -> {
                List<User> staff = admin.viewStaff();
                System.out.println("\n--- Staff List ---");
                for (User user : staff) {
                    System.out.println(user);
                }
            }
            case 2 -> addStaff(scanner);
            case 3 -> removeStaff(scanner);
            default -> System.out.println("Invalid choice.");
        }
    }

    private static void addStaff(Scanner scanner) {
        System.out.print("Enter Staff ID: ");
        String staffID = scanner.nextLine();
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Role (Doctor/Pharmacist/Administrator): ");
        String role = scanner.nextLine();
        System.out.print("Enter Gender: ");
        String gender = scanner.nextLine();
        System.out.print("Enter Age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        User newStaff;
        switch (role.toLowerCase()) {
            case "doctor" -> newStaff = new Doctor(staffID, "password", name, role, gender, age);
            case "pharmacist" -> newStaff = new Pharmacist(staffID, "password", name, role, gender, age);
            case "administrator" -> newStaff = new Administrator(staffID, "password", name, role, gender, age);
            default -> {
                System.out.println("Invalid role specified.");
                return;
            }
        }

        List<User> staffList = admin.viewStaff();
        admin.addStaff(staffList, newStaff);

        //admin.updateStaffCSV(newStaff);
        //admin.updatePasswordCSV(staffID, "password");

        System.out.println("Staff member added successfully.");
    }


    private static void removeStaff(Scanner scanner) {
        System.out.print("Enter Staff ID to remove: ");
        String staffID = scanner.nextLine();

        List<User> staffList = admin.viewStaff();
        admin.removeStaff(staffList, staffID);
    }



    private static void viewAppointments() {
        System.out.println("Currently testing/debugging.");
        List<Appointment> appointments = admin.viewAppointments();
        System.out.println("\n--- All Appointments ---");
        if (appointments.isEmpty()) {
                System.out.println("No appointments to display.");
        } else {
            for (Appointment appointment : appointments) {
                System.out.println(appointment);
            }
        }
    }

    private static void manageInventory(Scanner scanner) {
        System.out.println("\n--- Manage Medication Inventory ---");
        System.out.println("1. View Inventory");
        System.out.println("2. Update Stock");
        System.out.println("3. Delete Stock");
        System.out.println("4. Change Low Stock Alert Level");
        System.out.print("Enter your choice: ");
        int inventoryChoice = scanner.nextInt();
        scanner.nextLine();

        switch (inventoryChoice) {
            case 1 -> {
                List<Medicine> inventory = admin.viewInventory();
                for (Medicine medicine : inventory) {
                    System.out.println(medicine);
                }
            }
            case 2 -> {
                System.out.print("Enter Medicine Name: ");
                String medicineName = scanner.nextLine();
                System.out.print("Enter New Stock Quantity: ");
                int newStock = scanner.nextInt();
                admin.updateStock(medicineName, newStock);
            }
            case 3 -> {
                System.out.print("Enter Medicine Name to delete: "); //Not case sensitive, ibuprofen and Ibuprofen eg.
                String medicineName = scanner.nextLine();
                admin.deleteMedicine(medicineName);
            }
            case 4 -> {
                System.out.print("Enter Medicine Name to update low stock alert level: ");
                String medicineName = scanner.nextLine();
                System.out.print("Enter New Low Stock Alert Level: ");
                int newAlertLevel = scanner.nextInt();
                admin.changeStockAlertLevel(medicineName, newAlertLevel);
            }
            default -> System.out.println("Invalid choice.");
        }
    }

    private static void approveReplenishmentRequest(Scanner scanner) {
        System.out.print("Enter Medicine Name: ");
        String medicineName = scanner.nextLine();
        System.out.print("Enter Quantity to Replenish: ");
        int quantity = scanner.nextInt();
        admin.approveReplenishmentRequest(medicineName, quantity);
    }
}
