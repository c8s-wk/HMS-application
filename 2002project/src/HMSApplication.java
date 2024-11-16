import UserMenu.AdministratorMenu;
import UserMenu.DoctorMenu;
import UserMenu.PatientMenu;
import UserMenu.PharmacistMenu;
import Info.Patient;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class HMSApplication {
    private static List<Patient> patients = new ArrayList<>();
    private static Map<String, Map<String, String>> staffData = new HashMap<>(); // Staff data as a map

    public static void main(String[] args) {
        initializeUsers(); // Initialize patients and staff from CSV files
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Hospital Management System!");
        boolean isAuthenticated = false;
        String userID = "";
        String password;

        // User authentication loop
        while (!isAuthenticated) {
            System.out.print("Enter your User ID: ");
            userID = scanner.nextLine();
            System.out.print("Enter your password: ");
            password = scanner.nextLine();

            if (!"password".equals(password)) {
                System.out.println("Invalid password. Please try again.");
                continue;
            }

            isAuthenticated = authenticateUser(userID);

            if (!isAuthenticated) {
                System.out.println("Invalid credentials. Please try again.");
            } else {
                System.out.println("Login successful!");
                String userRole = getUserRole(userID);
                displayUserMenu(userRole, userID);
            }
        }
        scanner.close();
    }

    // Initialize patients and staff from CSV files
    private static void initializeUsers() {
        initializePatientsFromCSV("patients.csv");
        initializeStaffFromCSV("staff_list.csv");
    }

    // Initialize patients from CSV file
    private static void initializePatientsFromCSV(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Skip the header
            br.readLine();

            // Read each line
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 7) {
                    System.err.println("Invalid row in CSV: " + line);
                    continue;
                }

                String patientID = data[0].trim();
                String name = data[1].trim();
                String dateOfBirth = data[2].trim();
                String gender = data[3].trim();
                String bloodType = data[4].trim();
                String email = data[5].trim();
                String contactNumber = data[6].trim();

                patients.add(new Patient(patientID, "password", "Patient", name, dateOfBirth, gender, bloodType, email, contactNumber));
            }
            System.out.println("Patient data loaded successfully.");
        } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
        }
    }

    // Initialize staff from CSV file
    private static void initializeStaffFromCSV(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Skip the header
            br.readLine();

            // Read each line
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 5) {
                    System.err.println("Invalid row in CSV: " + line);
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
            System.err.println("Error reading the CSV file: " + e.getMessage());
        }
    }

    // Authenticate the user based on User ID
    private static boolean authenticateUser(String userID) {
        for (Patient patient : patients) {
            if (patient.getUserID().equals(userID)) {
                return true;
            }
        }

        return staffData.containsKey(userID);
    }

    // Get the user role based on User ID
    private static String getUserRole(String userID) {
        for (Patient patient : patients) {
            if (patient.getUserID().equals(userID)) {
                return "Info.Patient";
            }
        }

        if (staffData.containsKey(userID)) {
            String role = staffData.get(userID).get("Role");
            switch (role) {
                case "Doctor":
                    return "Info.Doctor";
                case "Pharmacist":
                    return "Info.Pharmacist";
                case "Administrator":
                    return "Info.Administrator";
            }
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
                    scanner.nextLine(); // Consume the newline character
                    PatientMenu.handleChoice(patientChoice);
                    if (patientChoice == 9) running = false; // Logout option
                    break;
                case "Info.Doctor":
                    DoctorMenu.displayMenu();
                    int doctorChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character
                    DoctorMenu.handleChoice(doctorChoice);
                    if (doctorChoice == 8) running = false; // Logout option
                    break;
                case "Info.Pharmacist":
                    PharmacistMenu.displayMenu();
                    int pharmacistChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character
                    PharmacistMenu.handleChoice(pharmacistChoice);
                    if (pharmacistChoice == 5) running = false; // Logout option
                    break;
                case "Info.Administrator":
                    AdministratorMenu.displayMenu();
                    int adminChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character
                    AdministratorMenu.handleChoice(adminChoice);
                    if (adminChoice == 5) running = false; // Logout option
                    break;
                default:
                    System.out.println("Invalid role.");
                    running = false;
                    break;
            }
        }
        scanner.close();
    }
}
