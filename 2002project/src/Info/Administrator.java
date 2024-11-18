package Info;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Iterator;


public class Administrator extends User {
    private String name;
    private String gender;
    private int age;

    private static final String STAFF_FILE = "2002project/Staff_List.csv";
    private static final String MEDICINE_FILE = "2002project/Medicine_List.csv";
    private static final String APPOINTMENT_FILE_PATH = "2002project/Appointment.csv";
    private static final String PASSWORD_CSV = "2002project/password_List.csv";

    public Administrator(String userID, String password, String name, String role, String gender, int age) {
        super(userID, password, role);
        this.name = name;
        this.gender = gender;
        this.age = age;
    }

    // Getters and Setters
    /*public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }*/
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getGender() {
        return gender;
    }

    @Override
    public int getAge() {
        return age;
    }

    // View all staff
    /*public List<User> viewStaff() {
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
    }*/
    public List<User> viewStaff() {
        List<User> staffList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("2002project/Staff_List.csv"))) {
            String line;
            br.readLine(); // Skip the header

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 5) {
                    System.err.println("Invalid row in Staff_List.csv: " + line);
                    continue;
                }

                String userID = data[0].trim();
                String name = data[1].trim();
                String role = data[2].trim();
                String gender = data[3].trim();
                int age = Integer.parseInt(data[4].trim());

                User staffMember;
                switch (role.toLowerCase()) {
                    case "doctor" -> staffMember = new Doctor(userID, "password", name, role, gender, age);
                    case "pharmacist" -> staffMember = new Pharmacist(userID, "password", name, role, gender, age);
                    case "administrator" -> staffMember = new Administrator(userID, "password", name, role, gender, age);
                    default -> {
                        System.err.println("Invalid role for staff: " + role);
                        continue;
                    }
                }

                staffList.add(staffMember);
            }
        } catch (IOException e) {
            System.err.println("Error reading Staff_List.csv: " + e.getMessage());
        }

        return staffList;
    }


    // Add a new staff member
    public void addStaff(List<User> staff, User newStaff) {
        staff.add(newStaff); // Add the new staff to the list
        updateStaffCSV(staff); // Rewrite the staff CSV with the updated list
        updatePasswordCSV(newStaff.getUserID(), "password"); // Add the new staff's password to the password CSV
        System.out.println("Staff member added successfully.");
    }


    // Remove a staff member
    public void removeStaff(List<User> staff, String staffID) {
        boolean found = false;

        // Find and remove the staff member from the in-memory list
        for (int i = 0; i < staff.size(); i++) {
            if (staff.get(i).getUserID().equals(staffID)) {
                staff.remove(i);
                found = true;
                break;
            }
        }

        if (found) {
            updateStaffCSV(staff); // Update the staff CSV
            removePasswordFromCSV(staffID); // Remove password from the password CSV
            System.out.println("Staff member removed successfully.");
        } else {
            System.out.println("Staff member not found.");
        }
    }



    public List<Appointment> viewAppointments() {
        List<Appointment> allAppointments = Appointment.loadAppointmentsFromCSV();
        if (allAppointments.isEmpty()) {
            System.out.println("No appointments found.");
        }
        return allAppointments;
    }

    // Helper to update staff CSV
    /*public static void updateStaffCSV(User newStaff) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(STAFF_FILE, true))) {
            bw.write(String.join(",",
                    newStaff.getUserID(),
                    newStaff.getRole(),
                    ((newStaff instanceof Doctor || newStaff instanceof Pharmacist || newStaff instanceof Administrator) ? ((Doctor) newStaff).getName() : "N/A"),
                    ((newStaff instanceof Doctor || newStaff instanceof Pharmacist || newStaff instanceof Administrator) ? ((Doctor) newStaff).getGender() : "N/A"),
                    ((newStaff instanceof Doctor || newStaff instanceof Pharmacist || newStaff instanceof Administrator) ? String.valueOf(((Doctor) newStaff).getAge()) : "N/A")
            ));
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error updating Staff_List.csv: " + e.getMessage());
        }
    }*/
    private void updateStaffCSV(List<User> staff) {
        String filePath = "2002project/Staff_List.csv"; // Adjust the path as needed

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            // Write the header
            bw.write("Staff ID,Name,Role,Gender,Age");
            bw.newLine();

            // Write staff details
            for (User user : staff) {
                bw.write(user.getUserID() + "," +
                        user.getName() + "," +
                        user.getRole() + "," +
                        user.getGender() + "," +
                        user.getAge());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error updating Staff_List.csv: " + e.getMessage());
        }
    }



    // Helper to update the entire staff file
    private void updateStaffFile(List<User> staffList) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(STAFF_FILE))) {
            bw.write("StaffID,Name,Role,Gender,Age");
            bw.newLine();
            for (User staff : staffList) {
                bw.write(String.join(",",
                        staff.getUserID(),
                        staff.getRole(),
                        ((staff instanceof Doctor || staff instanceof Pharmacist || staff instanceof Administrator) ? ((Doctor) staff).getName() : "N/A"),
                        ((staff instanceof Doctor || staff instanceof Pharmacist || staff instanceof Administrator) ? ((Doctor) staff).getGender() : "N/A"),
                        ((staff instanceof Doctor || staff instanceof Pharmacist || staff instanceof Administrator) ? String.valueOf(((Doctor) staff).getAge()) : "N/A")
                ));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error updating Staff_List.csv: " + e.getMessage());
        }
    }

    // Helper to update password CSV
    private void updatePasswordCSV(String userID, String password) {
        List<String> lines = new ArrayList<>();
        String filePath = "2002project/password_List.csv"; // Adjust the path as needed
        boolean found = false;

        // Read and update existing passwords
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(userID + ",")) {
                    lines.add(userID + "," + password); // Update password
                    found = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading password_List.csv: " + e.getMessage());
        }

        // Add new password if not found
        if (!found) {
            lines.add(userID + "," + password);
        }

        // Write updated passwords back to the file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error updating password_List.csv: " + e.getMessage());
        }
    }


    private void removePasswordFromCSV(String userID) {
        List<String> lines = new ArrayList<>();
        String filePath = "2002project/password_List.csv"; // Adjust the path as needed

        // Read and filter out the entry to be removed
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith(userID + ",")) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading password_List.csv: " + e.getMessage());
        }

        // Write the updated list back to the file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error updating password_List.csv: " + e.getMessage());
        }
    }


    /*private void savePasswordCSV() {
        String filePath = "2002project/password_List.csv"; // Adjust the file path as necessary

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            // Write the header
            bw.write("UserID,Password");
            bw.newLine();

            // Write each user's password to the CSV
            for (Map.Entry<String, String> entry : userPasswords.entrySet()) {
                bw.write(entry.getKey() + "," + entry.getValue());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error updating password_List.csv: " + e.getMessage());
        }
    }*/



    // View medicine inventory
    public List<Medicine> viewInventory() {
        List<Medicine> inventory = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(Administrator.MEDICINE_FILE))) {
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
