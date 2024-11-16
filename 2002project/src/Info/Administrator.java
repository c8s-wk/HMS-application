package Info;

import java.util.ArrayList;
import java.util.List;

public class Administrator extends User {

    // Constructor
    public Administrator(String userID, String password, String role) {
        super(userID, password, role);
    }

    // View and Manage Hospital Staff
    public void viewAndManageStaff(List<User> staff) {
        System.out.println("\n--- Hospital Staff ---");
        for (User user : staff) {
            System.out.println(user);
        }
    }

    public void addStaff(List<User> staff, User newStaff) {
        staff.add(newStaff);
        System.out.println("New staff member added: " + newStaff);
    }

    public void removeStaff(List<User> staff, String staffID) {
        staff.removeIf(user -> user.getUserID().equals(staffID));
        System.out.println("Staff member removed with ID: " + staffID);
    }

    // View Appointments
    public void viewAppointments(List<Appointment> appointments) {
        System.out.println("\n--- Appointments ---");
        for (Appointment appointment : appointments) {
            System.out.println(appointment);
        }
    }

    // View and Manage Inventory
    public void viewInventory(List<Medicine> inventory) {
        System.out.println("\n--- Medication Inventory ---");
        for (Medicine medicine : inventory) {
            System.out.println(medicine);
        }
    }

    public void updateStock(List<Medicine> inventory, String medicineName, int newStock) {
        for (Medicine medicine : inventory) {
            if (medicine.getName().equalsIgnoreCase(medicineName)) {
                medicine.setStock(newStock);
                System.out.println("Updated stock for " + medicineName + " to " + newStock);
                return;
            }
        }
        System.out.println("Medicine not found in inventory.");
    }

    public void updateLowStockAlert(List<Medicine> inventory, String medicineName, int newAlertLevel) {
        for (Medicine medicine : inventory) {
            if (medicine.getName().equalsIgnoreCase(medicineName)) {
                medicine.setLowStockAlertLevel(newAlertLevel);
                System.out.println("Updated low stock alert for " + medicineName + " to " + newAlertLevel);
                return;
            }
        }
        System.out.println("Medicine not found in inventory.");
    }

    // Approve Replenishment Requests
    public void approveReplenishmentRequest(List<Medicine> inventory, String medicineName, int additionalStock) {
        for (Medicine medicine : inventory) {
            if (medicine.getName().equalsIgnoreCase(medicineName)) {
                int newStock = medicine.getStock() + additionalStock;
                medicine.setStock(newStock);
                System.out.println("Approved replenishment for " + medicineName + ". New stock: " + newStock);
                return;
            }
        }
        System.out.println("Medicine not found in inventory.");
    }
}
