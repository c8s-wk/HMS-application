package UserMenu;

import Info.Patient;
import Info.MedicalRecord;
import Info.Appointment;
import Info.Doctor;

import java.util.List;
import java.util.Scanner;

public class PatientMenu {

    private static Patient currentPatient; // The currently logged-in patient

    // Assign the logged-in patient
    public static void setCurrentPatient(Patient patient) {
        currentPatient = patient;
    }

    public static void displayMenu() {
        System.out.println("\n--- Patient Menu ---");
        System.out.println("1. View Medical Record");
        System.out.println("2. Update Personal Information");
        System.out.println("3. View Available Appointment Slots");
        System.out.println("4. Schedule an Appointment");
        System.out.println("5. Reschedule an Appointment");
        System.out.println("6. Cancel an Appointment");
        System.out.println("7. View Scheduled Appointments");
        System.out.println("8. View Past Appointment Outcome Records");
        System.out.println("9. Logout");
        System.out.print("Please enter your choice: ");
    }

    public static void handleChoice(int choice) {
        switch (choice) {
            case 1:
                viewMedicalRecord();
                break;
            case 2:
                updatePersonalInfo();
                break;
            case 3:
                viewAvailableAppointments();
                break;
            case 4:
                scheduleAppointment();
                break;
            case 5:
                rescheduleAppointment();
                break;
            case 6:
                cancelAppointment();
                break;
            case 7:
                viewScheduledAppointments();
                break;
            case 8:
                viewPastAppointmentRecords();
                break;
            case 9:
                System.out.println("Logging out...");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void viewMedicalRecord() {
        if (currentPatient != null) {
            System.out.println("\n--- Medical Record ---");
            MedicalRecord record = currentPatient.getMedicalRecord();
            System.out.println(record);
        } else {
            System.out.println("No patient is currently logged in.");
        }
    }

    private static void updatePersonalInfo() {
        if (currentPatient != null) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("\n--- Update Personal Information ---");
            System.out.print("Enter new email address: ");
            String newEmail = scanner.nextLine();
            currentPatient.setEmailAddress(newEmail);

            System.out.print("Enter new contact number: ");
            String newContactNumber = scanner.nextLine();
            currentPatient.setContactNumber(newContactNumber);

            System.out.println("Personal information updated successfully.");
        } else {
            System.out.println("No patient is currently logged in.");
        }
    }

    private static void viewAvailableAppointments() {
        if (currentPatient != null) {
            List<Doctor> doctors = Doctor.getAllDoctors(); // Retrieve list of all doctors
            currentPatient.viewAvailableAppointmentSlots(doctors);
        } else {
            System.out.println("No patient is currently logged in.");
        }
    }

    private static void scheduleAppointment() {
        if (currentPatient != null) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("\n--- Schedule an Appointment ---");
            System.out.print("Enter doctor ID: ");
            String doctorId = scanner.nextLine();
            System.out.print("Enter appointment date (YYYY-MM-DD): ");
            String date = scanner.nextLine();
            System.out.print("Enter appointment time (HH:MM): ");
            String time = scanner.nextLine();

            Doctor doctor = Doctor.getDoctorById(doctorId); // Retrieve doctor by ID
            if (doctor != null) {
                Appointment newAppointment = new Appointment(
                        generateAppointmentID(),
                        currentPatient.getUserID(),
                        doctorId,
                        date,
                        time,
                        "Pending"
                );
                boolean success = Appointment.scheduleAppointment(newAppointment);
                if (success) {
                    System.out.println("Appointment scheduled successfully.");
                }
            } else {
                System.out.println("Invalid doctor ID.");
            }
        } else {
            System.out.println("No patient is currently logged in.");
        }
    }

    private static void rescheduleAppointment() {
        if (currentPatient != null) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("\n--- Reschedule an Appointment ---");
            System.out.print("Enter appointment ID: ");
            String appointmentId = scanner.nextLine();
            System.out.print("Enter new appointment date (YYYY-MM-DD): ");
            String newDate = scanner.nextLine();
            System.out.print("Enter new appointment time (HH:MM): ");
            String newTime = scanner.nextLine();

            boolean success = currentPatient.rescheduleAppointment(appointmentId, newDate, newTime);
            if (success) {
                System.out.println("Appointment rescheduled successfully.");
            } else {
                System.out.println("Failed to reschedule appointment.");
            }
        } else {
            System.out.println("No patient is currently logged in.");
        }
    }

    private static void cancelAppointment() {
        if (currentPatient != null) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("\n--- Cancel an Appointment ---");
            System.out.print("Enter appointment ID: ");
            String appointmentId = scanner.nextLine();

            boolean success = currentPatient.cancelAppointment(appointmentId);
            if (success) {
                System.out.println("Appointment canceled successfully.");
            } else {
                System.out.println("Failed to cancel appointment.");
            }
        } else {
            System.out.println("No patient is currently logged in.");
        }
    }

    private static void viewScheduledAppointments() {
        if (currentPatient != null) {
            System.out.println("\n--- Scheduled Appointments ---");
            List<Appointment> appointments = currentPatient.getAppointments();
            if (appointments.isEmpty()) {
                System.out.println("No scheduled appointments.");
            } else {
                for (Appointment appointment : appointments) {
                    System.out.println(appointment);
                }
            }
        } else {
            System.out.println("No patient is currently logged in.");
        }
    }

    private static void viewPastAppointmentRecords() {
        if (currentPatient != null) {
            System.out.println("\n--- Past Appointment Outcome Records ---");
            List<Appointment> appointments = currentPatient.getAppointments();
            for (Appointment appointment : appointments) {
                if ("Completed".equals(appointment.getStatus())) {
                    System.out.println(appointment);
                }
            }
        } else {
            System.out.println("No patient is currently logged in.");
        }
    }

    private static String generateAppointmentID() {
        return "A" + System.currentTimeMillis(); // Generate a unique ID based on timestamp
    }
}
