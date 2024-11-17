package Info;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Administrator extends User {
    private String name;
    private String gender;
    private int age;

    private static final String STAFF_FILE = "2002project/Staff_List.csv";
    private static final String MEDICINE_FILE = "2002project/Medicine_List.csv";
    private static final String APPOINTMENT_FILE_PATH = "2002project/Appointment.csv";

    public Administrator(String userID, String password, String name, String role, String gender, int age) {
        super(userID, password, role);
        this.name = name;
        this.gender = gender;
        this.age = age;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
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

                switch (role.toLowerCase()) {
                    case "doctor" -> staff.add(new Doctor(staffID, "password",name,role, gender, age));
                    case "pharmacist" -> staff.add(new Pharmacist(staffID, "password",name,role, gender ,age));
                    case "administrator" -> staff.add(new Administrator(staffID, "password", name,role, gender, age));
                    default -> System.err.println("Unknown role: " + role + " for Staff ID: " + staffID);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading staff file: " + e.getMessage());
        }
        return staff;
    }

    // Add a new staff member
    public void addStaff(List<User> staff, User newStaff) {
        staff.add(newStaff);
        saveStaffToCSV(staff);
        System.out.println("Staff member added successfully.");
    }

    // Remove a staff member
    public void removeStaff(List<User> staff, String staffID) {
        boolean removed = staff.removeIf(user -> user.getUserID().equals(staffID));
        if (removed) {
            saveStaffToCSV(staff);
            System.out.println("Staff member removed successfully.");
        } else {
            System.out.println("Staff member with ID " + staffID + " not found.");
        }
    }

    public List<Appointment> viewAppointments() {
        List<Appointment> allAppointments = Appointment.loadAppointmentsFromCSV();
        if (allAppointments.isEmpty()) {
            System.out.println("No appointments found.");
        }
        return allAppointments;
    }

    // Save staff data to CSV
    private void saveStaffToCSV(List<User> staff) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(STAFF_FILE))) {
            bw.write("Staff ID,Name,Role,Gender,Age");
            bw.newLine();
            for (User user : staff) {
                if (user instanceof Doctor) {
                    Doctor doctor = (Doctor) user; // Explicit cast
                    bw.write(doctor.getUserID() + "," + doctor.getName() + ",Doctor," + doctor.getGender() + "," + doctor.getAge());
                } else if (user instanceof Pharmacist) {
                    Pharmacist pharmacist = (Pharmacist) user; // Explicit cast
                    bw.write(pharmacist.getUserID() + "," + pharmacist.getName() + ",Pharmacist,,");
                } else if (user instanceof Administrator) {
                    Administrator admin = (Administrator) user; // Explicit cast
                    bw.write(admin.getUserID() + "," + admin.getName() + ",Administrator," + admin.getGender() + "," + admin.getAge());
                }
                bw.newLine();
            }
            System.out.println("Staff data saved to file.");
        } catch (IOException e) {
            System.err.println("Error writing staff data to file: " + e.getMessage());
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

    @Override
    public String toString() {
        return "Administrator{" +
                "userID='" + getUserID() + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                '}';
    }
}
