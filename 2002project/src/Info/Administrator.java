package Info;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Administrator extends User {

    private static final String STAFF_FILE = "2002project/Staff_List.csv";
    private static final String MEDICINE_FILE = "2002project/Medicine_List.csv";

    // Constructor
    public Administrator(String userID, String password) {
        super(userID, password, "Administrator");
    }

    // View all staff
    public List<User> viewStaff() {
        List<User> staff = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(STAFF_FILE))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length < 5) continue;

                String staffID = data[0].trim();
                String name = data[1].trim();
                String role = data[2].trim();
                String gender = data[3].trim();
                int age = Integer.parseInt(data[4].trim());

                staff.add(new User(staffID, "password", role)); // Role can be Doctor, Pharmacist, etc.
            }
        } catch (IOException e) {
            System.err.println("Error reading staff file: " + e.getMessage());
        }
        return staff;
    }

    // Add a new staff member
    public void addStaff(String staffID, String name, String role, String gender, int age) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(STAFF_FILE, true))) {
            bw.write(String.join(",", staffID, name, role, gender, String.valueOf(age)));
            bw.newLine();
            System.out.println("Staff member added successfully.");
        } catch (IOException e) {
            System.err.println("Error adding staff: " + e.getMessage());
        }
    }

    // Remove a staff member
    public void removeStaff(String staffID) {
        List<String> lines = new ArrayList<>();
        boolean staffFound = false;
        try (BufferedReader br = new BufferedReader(new FileReader(STAFF_FILE))) {
            String line;
            String header = br.readLine(); // Read and store header
            lines.add(header);

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (!data[0].trim().equals(staffID)) {
                    lines.add(line); // Retain other lines
                } else {
                    staffFound = true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading staff file: " + e.getMessage());
        }

        if (!staffFound) {
            System.out.println("Staff ID not found.");
            return;
        }

        // Write updated data back to file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(STAFF_FILE))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
            System.out.println("Staff member removed successfully.");
        } catch (IOException e) {
            System.err.println("Error updating staff file: " + e.getMessage());
        }
    }

    // View medicine inventory
    public List<Medicine> viewInventory() {
        List<Medicine> inventory = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(MEDICINE_FILE))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length < 3) continue;

                String medicineName = data[0].trim();
                int stock = Integer.parseInt(data[1].trim());
                int lowStockAlert = Integer.parseInt(data[2].trim());

                inventory.add(new Medicine(medicineName, stock, lowStockAlert));
            }
        } catch (IOException e) {
            System.err.println("Error reading medicine file: " + e.getMessage());
        }
        return inventory;
    }

    // Update medicine stock
    public void updateStock(String medicineName, int newStock) {
        List<Medicine> inventory = viewInventory();
        boolean found = false;

        for (Medicine medicine : inventory) {
            if (medicine.getName().equalsIgnoreCase(medicineName)) {
                medicine.setStock(newStock);
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Medicine not found.");
            return;
        }

        saveInventory(inventory);
        System.out.println("Medicine stock updated successfully.");
    }

    // Approve replenishment requests
    public void approveReplenishmentRequest(String medicineName, int additionalStock) {
        List<Medicine> inventory = viewInventory();
        boolean found = false;

        for (Medicine medicine : inventory) {
            if (medicine.getName().equalsIgnoreCase(medicineName)) {
                int updatedStock = medicine.getStock() + additionalStock;
                medicine.setStock(updatedStock);
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Medicine not found.");
            return;
        }

        saveInventory(inventory);
        System.out.println("Replenishment request approved. Stock updated.");
    }

    // Save inventory back to the CSV file
    private void saveInventory(List<Medicine> inventory) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(MEDICINE_FILE))) {
            bw.write("Medicine Name,Initial Stock,Low Stock Level Alert");
            bw.newLine();

            for (Medicine medicine : inventory) {
                bw.write(String.join(",", medicine.getName(), String.valueOf(medicine.getStock()), String.valueOf(medicine.getLowStockAlertLevel())));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error updating inventory file: " + e.getMessage());
        }
    }
}
