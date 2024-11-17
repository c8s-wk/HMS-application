import Info.Doctor;
import UserMenu.AdministratorMenu;
import UserMenu.DoctorMenu;
import UserMenu.PatientMenu;
import UserMenu.PharmacistMenu;
import Info.Pharmacist;
import Info.Patient;
import Info.MedicalRecord;

import java.io.*;
import java.util.*;

public class HMSApplication {
    private static List<Patient> patients = new ArrayList<>();
    private static Map<String, Map<String, String>> staffData = new HashMap<>(); // Staff data map (ID -> Details)
    private static Map<String, String> userPasswords = new HashMap<>(); // Store user passwords
    private static Scanner scanner = new Scanner(System.in); // Shared scanner instance

    public static void main(String[] args) {
        initializeUsers(); // Initialize patients, staff, and passwords from CSV files
        System.out.println("Welcome to the Hospital Management System!");

        // User authentication loop
        while (true) {
            System.out.print("Enter your User ID: ");
            String userID = scanner.nextLine();
            System.out.print("Enter your password: ");
            String password = scanner.nextLine();

            boolean isFirstLogin = "password".equals(password); // Check if default password is used

            if (!authenticateUser(userID, password)) {
                System.out.println("Invalid credentials. Please try again.");
                continue;
            }

            System.out.println("Login successful!");

            if (isFirstLogin) {
                System.out.println("It looks like this is your first login. Please change your password.");
                changePassword(userID);
                continue; // Restart the login process after password change
            }

            String userRole = getUserRole(userID);
            if ("Unknown".equals(userRole)) {
                System.out.println("Invalid user role. Access denied.");
            } else {
                displayUserMenu(userRole, userID);
            }
        }
    }

    // Allow users to change their password
    private static void changePassword(String userID) {
        System.out.print("Enter your new password: ");
        String newPassword = scanner.nextLine();

        // Update password for patients
        for (Patient patient : patients) {
            if (patient.getUserID().equals(userID)) {
                patient.setPassword(newPassword);
                System.out.println("Password updated successfully.");
                updatePasswordInCSV(userID, newPassword);
                return;
            }
        }

        // Update password for staff
        if (staffData.containsKey(userID)) {
            staffData.get(userID).put("Password", newPassword); // Update password in staffData map
            System.out.println("Password updated successfully.");
            updatePasswordInCSV(userID, newPassword);
            return;
        }

        System.out.println("User ID not found. Password change failed.");
    }

    // Update password in the userPasswords map
    private static void updatePasswordInCSV(String userID, String newPassword) {
        userPasswords.put(userID, newPassword);  // Update in-memory password map
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("2002project/password_List.csv"))) {
            bw.write("UserID,Password\n");
            for (Map.Entry<String, String> entry : userPasswords.entrySet()) {
                bw.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error updating password list: " + e.getMessage());
        }
    }

    // Authenticate the user based on User ID and password
    private static boolean authenticateUser(String userID, String password) {
        // Check if the password matches the stored password
        if (userPasswords.containsKey(userID) && userPasswords.get(userID).equals(password)) {
            return true;
        }

        // Check if the user is a patient
        for (Patient patient : patients) {
            if (patient.getUserID().equals(userID) && patient.getPassword().equals(password)) {
                return true;
            }
        }

        // Check if the user is staff
        return staffData.containsKey(userID) && staffData.get(userID).get("Password").equals(password);
    }

    // Initialize patients, staff, and passwords from CSV files
    private static void initializeUsers() {
        initializePatientsFromCSV("2002project/Patient_List.csv");
        initializeStaffFromCSV("2002project/Staff_List.csv");
        initializePasswordsFromCSV("2002project/password_List.csv");
    }

    // Initialize passwords from the password_List.csv file
    private static void initializePasswordsFromCSV(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 2) {
                    userPasswords.put(data[0].trim(), data[1].trim());
                }
            }
            System.out.println("Password data loaded successfully.");
        } catch (IOException e) {
            System.err.println("Error reading the password CSV file: " + e.getMessage());
        }
    }

    // Initialize patients from CSV file
    private static void initializePatientsFromCSV(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1); // Handle empty fields gracefully
                if (data.length < 7) {
                    System.err.println("Invalid row in Patient CSV: " + line);
                    continue;
                }

                String patientID = data[0].trim();
                String name = data[1].trim();
                String dateOfBirth = data[2].trim();
                String gender = data[3].trim();
                String bloodType = data[4].trim();
                String email = data[5].trim();
                String contactNumber = data[6].trim();
                String pastDiagnoses = data[7].trim();
                String pastTreatments = data[8].trim();
                String additionalNotes = data[9].trim();

                MedicalRecord medicalRecord = new MedicalRecord(patientID, name, dateOfBirth, gender, bloodType, email, contactNumber, pastDiagnoses, pastTreatments, additionalNotes);
                patients.add(new Patient(patientID, "password", "Patient", medicalRecord));
            }
            System.out.println("Patient data loaded successfully.");
        } catch (IOException e) {
            System.err.println("Error reading the Patient CSV file: " + e.getMessage());
        }
    }

    // Initialize staff from CSV file
    private static void initializeStaffFromCSV(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1); // Handle empty fields gracefully
                if (data.length < 5) {
                    System.err.println("Invalid row in Staff CSV: " + line);
                    continue;
                }

                Map<String, String> staff = new HashMap<>();
                staff.put("ID", data[0].trim());
                staff.put("Name", data[1].trim());
                staff.put("Role", data[2].trim());
                staff.put("Gender", data[3].trim());
                staff.put("Age", data[4].trim());
                staff.put("Password", "password"); // Initialize with default password

                staffData.put(data[0].trim(), staff);
            }
            System.out.println("Staff data loaded successfully.");
        } catch (IOException e) {
            System.err.println("Error reading the Staff CSV file: " + e.getMessage());
        }
    }

    // Get the user role based on User ID
    private static String getUserRole(String userID) {
        // Check for patient role
        for (Patient patient : patients) {
            if (patient.getUserID().equals(userID)) {
                PatientMenu.setCurrentPatient(patient);
                return "Info.Patient";
            }
        }

        // Check for staff role
        if (staffData.containsKey(userID)) {
            String role = staffData.get(userID).get("Role");
            return switch (role) {
                case "Doctor" -> "Info.Doctor";
                case "Pharmacist" -> "Info.Pharmacist";
                case "Administrator" -> "Info.Administrator";
                default -> "Unknown";
            };
        }

        return "Unknown"; // Invalid user
    }

    // Display the user menu based on role
    private static void displayUserMenu(String role, String userID) {
        boolean running = true;

        switch (role) {
            case "Info.Patient" -> {
                PatientMenu.displayMenu();
                int patientChoice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                PatientMenu.handleChoice(patientChoice);
                if (patientChoice == 9) running = false; // Logout
            }

            case "Info.Doctor" -> {
                DoctorMenu.displayMenu();
                int doctorChoice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                DoctorMenu.handleChoice(doctorChoice);
                if (doctorChoice == 9) running = false; // Logout
            }

            case "Info.Pharmacist" -> {
                Map<String, String> staff = staffData.get(userID);
                if (staff != null && "Pharmacist".equals(staff.get("Role"))) {
                    Pharmacist pharmacist = new Pharmacist(userID, staff.get("Name"), "password");
                    PharmacistMenu.setPharmacist(pharmacist);
                    PharmacistMenu.displayMenu();
                    int pharmacistChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    PharmacistMenu.handleChoice(pharmacistChoice);
                    if (pharmacistChoice == 9) running = false; // Logout
                } else {
                    System.out.println("Error: Pharmacist not found.");
                    running = false; // Stop running
                }
            }

            case "Info.Administrator" -> {
                AdministratorMenu.displayMenu();
                int adminChoice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                AdministratorMenu.handleChoice(adminChoice);
                if (adminChoice == 9) running = false; // Logout
            }
        }
    }
}
