package UserMenu;

import Info.Administrator;
import Info.Appointment;
import Info.Medicine;
import Info.User;

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

    public static void handleChoice(int choice) {
        Scanner scanner = new Scanner(System.in);

        switch (choice) {
            case 1 -> manageStaff(scanner);
            case 2 -> viewAppointments();
            case 3 -> manageInventory(scanner);
            case 4 -> approveReplenishmentRequest(scanner);
            case 5 -> System.out.println("Logging out...");
            default -> System.out.println("Invalid choice. Please try again.");
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
                for (User user : staff) {
                    System.out.println(user);
                }
            }
            case 2 -> {
                System.out.print("Enter Staff ID: ");
                String staffID = scanner.nextLine();
                System.out.print("Enter Name: ");
                String name = scanner.nextLine();
                System.out.print("Enter Role: ");
                String role = scanner.nextLine();
                System.out.print("Enter Gender: ");
                String gender = scanner.nextLine();
                System.out.print("Enter Age: ");
                int age = scanner.nextInt();
                admin.addStaff(staffID, name, role, gender, age);
            }
            case 3 -> {
                System.out.print("Enter Staff ID to remove: ");
                String staffID = scanner.nextLine();
                admin.removeStaff(staffID);
            }
            default -> System.out.println("Invalid choice.");
        }
    }

    private static void viewAppointments() {
        System.out.println("Appointment details viewing not implemented yet.");
    }

    private static void manageInventory(Scanner scanner) {
        System.out.println("\n--- Manage Medication Inventory ---");
        System.out.println("1. View Inventory");
        System.out.println("2. Update Stock");
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
