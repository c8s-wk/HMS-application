package UserMenu;

import Info.Patient;
import Info.Appointment;
import Info.Schedule;
import Info.MedicalRecord;
import Info.AppointmentOutcomeRecord;

import java.util.List;
import java.util.Scanner;

public class PatientMenu {

    private static Patient currentPatient;

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
        System.out.println("8. View Past Appointments Outcome Record");
        System.out.println("9. Log out");
        System.out.print("Please enter your choice: ");
    }

    public static void handleMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1 -> viewMedicalRecord();
                case 2 -> updatePersonalInfo(scanner);
                case 3 -> viewAvailableSlots();
                case 4 -> scheduleAppointment(scanner);
                case 5 -> rescheduleAppointment(scanner);
                case 6 -> cancelAppointment(scanner);
                case 7 -> viewScheduledAppointments();
                case 8 -> viewPastAppointmentRecords();
                case 9 -> {
                    System.out.println("Logging out...");
                    isRunning = false; // Exit the menu loop
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
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

    private static void updatePersonalInfo(Scanner scanner) {
        System.out.print("Enter new email address: ");
        String newEmail = scanner.nextLine();
        currentPatient.setEmailAddress(newEmail);

        System.out.print("Enter new contact number: ");
        String newContact = scanner.nextLine();
        currentPatient.setContactNumber(newContact);

        System.out.println("Personal information updated successfully.");
    }

    private static void viewAvailableSlots() {
        List<Schedule> schedules = Schedule.loadSchedulesFromCSV();
        //Debug code
        /*System.out.println("\n--- All Schedules Loaded ---");
        for (Schedule schedule : schedules) {
            System.out.println(schedule);
        } */


        System.out.println("\n--- Available Appointment Slots ---");
        boolean slotsFound = false;

        for (Schedule schedule : schedules) {
            if ("Available".equalsIgnoreCase(schedule.getStatus())) {
                System.out.printf("Doctor ID: %s | Date: %s | Time: %s%n", schedule.getDoctorID(), schedule.getDate(), schedule.getTime());
                slotsFound = true;
            }
        }

        if (!slotsFound) {
            System.out.println("No available slots found.");
        }
    }

    private static void scheduleAppointment(Scanner scanner) {
        System.out.print("Enter doctor ID: ");
        String doctorID = scanner.nextLine();
        System.out.print("Enter appointment date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        System.out.print("Enter appointment time (HH:MM): ");
        String time = scanner.nextLine();

        Appointment newAppointment = new Appointment(
                Appointment.generateAppointmentID(),
                currentPatient.getUserID(),
                doctorID,
                date,
                time,
                "Pending"
        );

        if (currentPatient.scheduleAppointment(newAppointment)) {
            System.out.println("Appointment scheduled successfully. Appointment ID: " + newAppointment.getAppointmentID());
        } else {
            System.out.println("Failed to schedule the appointment. The slot may not be available.");
        }
    }

    private static void rescheduleAppointment(Scanner scanner) {
        System.out.print("Enter appointment ID: ");
        String appointmentID = scanner.nextLine();
        System.out.print("Enter new DoctorID: ");
        String newDoctorID = scanner.nextLine();
        System.out.print("Enter new appointment date (YYYY-MM-DD): ");
        String newDate = scanner.nextLine();
        System.out.print("Enter new appointment time (HH:MM): ");
        String newTime = scanner.nextLine();

        if (currentPatient.rescheduleAppointment(appointmentID, newDoctorID, newDate, newTime)) {
            System.out.println("Appointment rescheduled successfully.");
        } else {
            System.out.println("Failed to reschedule appointment.");
        }
    }

    private static void cancelAppointment(Scanner scanner) {
        System.out.print("Enter appointment ID: ");
        String appointmentID = scanner.nextLine();

        if (currentPatient.cancelAppointment(appointmentID)) {
            System.out.println("Appointment canceled successfully.");
        } else {
            System.out.println("Failed to cancel appointment.");
        }
    }

    private static void viewScheduledAppointments() {
        System.out.println("\n--- Scheduled Appointments ---");

        // Retrieve the list of appointments
        List<Appointment> appointments = currentPatient.getAppointments();

        if (appointments.isEmpty()) {
            System.out.println("No scheduled appointments.");
        } else {
            System.out.println("Scheduled Appointments:");
            //boolean hasApprovedAppointments = false;

            // Display only approved appointments
            for (Appointment appointment : appointments) {
                //if ("Approved".equalsIgnoreCase(appointment.getStatus())) {
                    System.out.println(appointment);
                   // hasApprovedAppointments = true;
                //}
            }

            //if (!hasApprovedAppointments) {
                //System.out.println("No approved appointments found.");
            //}
        }
    }


    private static void viewPastAppointmentRecords() {
        if (currentPatient != null) {
            System.out.println("\n--- Past Appointment Outcome Records ---");

            // Load all appointment outcomes
            List<AppointmentOutcomeRecord> outcomeRecords = AppointmentOutcomeRecord.loadAppointmentOutcomesFromCSV();

            // Filter records for the current patient
            boolean recordsFound = false;
            for (AppointmentOutcomeRecord record : outcomeRecords) {
                if (record.getAppointmentID().startsWith(currentPatient.getUserID())) {
                    recordsFound = true;
                    record.viewOutcomeDetails();
                }
            }

            if (!recordsFound) {
                System.out.println("No past appointment outcome records found.");
            }
        } else {
            System.out.println("No patient is currently logged in.");
        }
    }
}
