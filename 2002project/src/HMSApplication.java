import UserMenu.AdministratorMenu;
import UserMenu.DoctorMenu;
import UserMenu.PatientMenu;
import UserMenu.PharmacistMenu;
import Info.Patient;
import Info.MedicalRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class HMSApplication {
    private static List<Patient> patients = new ArrayList<>();
    private static Map<String, Map<String, String>> staffData = new HashMap<>(); // Staff data map (ID -> Details)

    public static void main(String[] args) {
        initializeUsers(); // Initialize patients and staff from CSV files
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Hospital Management System!");

        // User authentication loop
        while (true) {
            System.out.print("Enter your User ID: ");
            String userID = scanner.nextLine();
            System.out.print("Enter your password: ");
            String password = scanner.nextLine();

            // Password validation
            if (!"password".equals(password)) {
                System.out.println("Invalid password. Please try again.");
                continue;
            }

            // User authentication
            if (authenticateUser(userID)) {
                System.out.println("Login successful!");
                String userRole = getUserRole(userID);
                if ("Unknown".equals(userRole)) {
                    System.out.println("Invalid user role. Access denied.");
                } else {
                    displayUserMenu(userRole, userID);
                }
            } else {
                System.out.println("Invalid credentials. Please try again.");
            }
        }
    }

    // Initialize patients and staff from CSV files
    private static void initializeUsers() {
        initializePatientsFromCSV("2002project/Patient_List.csv");
        initializeStaffFromCSV("2002project/Staff_List.csv");
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

                staffData.put(data[0].trim(), staff);
            }
            System.out.println("Staff data loaded successfully.");
        } catch (IOException e) {
            System.err.println("Error reading the Staff CSV file: " + e.getMessage());
        }
    }

    // Authenticate the user based on User ID
    private static boolean authenticateUser(String userID) {
        // Check if the user is a patient
        for (Patient patient : patients) {
            if (patient.getUserID().equals(userID)) {
                return true;
            }
        }

        // Check if the user is staff
        return staffData.containsKey(userID);
    }

    // Get the user role based on User ID
    private static String getUserRole(String userID) {
        // Check for patient role
        for (Patient patient : patients) {
            if (patient.getUserID().equals(userID)) {
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

        return "Unknown";
    }

    // Display the menu based on the user's role
    private static void displayUserMenu(String userRole, String userID) {
        boolean running = true;
        Scanner scanner = new Scanner(System.in);

        while (running) {
            switch (userRole) {
                case "Info.Patient":
                    PatientMenu.displayMenu();
                    int patientChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    PatientMenu.handleChoice(patientChoice);
                    if (patientChoice == 9) running = false; // Logout
                    break;

                case "Info.Doctor":
                    DoctorMenu.displayMenu();
                    int doctorChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    DoctorMenu.handleChoice(doctorChoice);
                    if (doctorChoice == 8) running = false; // Logout
                    break;

                case "Info.Pharmacist":
                    PharmacistMenu.displayMenu();
                    int pharmacistChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    PharmacistMenu.handleChoice(pharmacistChoice);
                    if (pharmacistChoice == 5) running = false; // Logout
                    break;

                case "Info.Administrator":
                    AdministratorMenu.displayMenu();
                    int adminChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    AdministratorMenu.handleChoice(adminChoice);
                    if (adminChoice == 5) running = false; // Logout
                    break;

                default:
                    System.out.println("Invalid role. Returning to login.");
                    running = false;
                    break;
            }
        }
    }
}
