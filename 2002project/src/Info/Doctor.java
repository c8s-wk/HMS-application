package Info;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Doctor extends User {
    private String name;
    private String gender;
    private int age;
    private List<Appointment> appointments;
    private List<Schedule> availableSlots;

    private static final String STAFF_FILE = "2002project/Staff_List.csv";
    private static final String SCHEDULE_FILE = "2002project/Schedule.csv";
    private static final String APPOINTMENT_FILE = "2002project/Appointment.csv";

    private static List<Doctor> allDoctors = new ArrayList<>();

    public Doctor(String userID, String password, String name, String gender, int age) {
        super(userID, password, "Doctor");
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.appointments = new ArrayList<>();
        this.availableSlots = new ArrayList<>();
        loadAppointmentsFromCSV();
        loadAvailableSlotsFromCSV();
    }

    // Static method to retrieve all doctors
    public static List<Doctor> getAllDoctors() {
        if (!allDoctors.isEmpty()) {
            return allDoctors;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(STAFF_FILE))) {
            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 5) {
                    continue;
                }

                String userID = data[0].trim();
                String name = data[1].trim();
                String role = data[2].trim();
                String gender = data[3].trim();
                int age = Integer.parseInt(data[4].trim());

                if ("Doctor".equalsIgnoreCase(role)) {
                    allDoctors.add(new Doctor(userID, "password", name, gender, age));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading doctor data: " + e.getMessage());
        }
        return allDoctors;
    }

    public static Doctor getDoctorById(String doctorId) {
        // Ensure the list of all doctors is loaded
        List<Doctor> allDoctors = getAllDoctors();

        for (Doctor doctor : allDoctors) {
            if (doctor.getUserID().equals(doctorId)) {
                return doctor; // Found the doctor, return the object
            }
        }
        System.out.println("Doctor with ID " + doctorId + " not found.");
        return null; // Return null if not found
    }

    // Load appointments for this doctor from Appointment.csv
    private void loadAppointmentsFromCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader(APPOINTMENT_FILE))) {
            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 6) {
                    continue;
                }

                String appointmentID = data[0].trim();
                String patientID = data[1].trim();
                String doctorID = data[2].trim();
                String date = data[3].trim();
                String time = data[4].trim();
                String status = data[5].trim();

                if (doctorID.equals(getUserID())) {
                    appointments.add(new Appointment(appointmentID, patientID, doctorID, date, time, status));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading appointments: " + e.getMessage());
        }
    }

    // Load available slots for this doctor from Schedule.csv
    private void loadAvailableSlotsFromCSV() {
        availableSlots.clear(); // Prevent duplicates or accumulation

        try (BufferedReader br = new BufferedReader(new FileReader(SCHEDULE_FILE))) {
            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 5) {
                    continue;
                }

                String doctorID = data[0].trim();
                String date = data[1].trim();
                String time = data[2].trim();
                String status = data[3].trim();
                String patientID = data[4].trim();

                if (doctorID.equals(getUserID()) && status.equalsIgnoreCase("Available")) {
                    availableSlots.add(new Schedule(doctorID, date, time, status, patientID.isEmpty() ? null : patientID));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading available slots: " + e.getMessage());
        }
    }

    // Get appointments for the doctor
    public List<Appointment> getAppointments() {
        return appointments;
    }

    // View the doctor's schedule
    public void viewSchedule() {
        System.out.println("\n--- Doctor's Schedule ---");
        if (appointments.isEmpty()) {
            System.out.println("No scheduled appointments.");
        } else {
            for (Appointment appointment : appointments) {
                System.out.println(appointment);
            }
        }
    }

    // Set availability for a specific date and time
    public void setAvailability(String date, String time, String status) {
        boolean slotExists = false;

        for (Schedule schedule : availableSlots) {
            if (schedule.getDate().equals(date) && schedule.getTime().equals(time)) {
                if (schedule.getStatus().equalsIgnoreCase(status)) {
                    System.out.println("The slot is already marked as " + status + ".");
                } else {
                    schedule.setStatus(status);
                    schedule.setPatientID(null); // Clear patient ID if slot is being made available or unavailable
                    Schedule.saveSchedulesToCSV(availableSlots);
                    System.out.println("Slot updated to '" + status + "': " + date + " | " + time);
                }
                slotExists = true;
                break;
            }
        }

        if (!slotExists) {
            if (status.equalsIgnoreCase("Available") || status.equalsIgnoreCase("Unavailable")) {
                availableSlots.add(new Schedule(getUserID(), date, time, status, null));
                Schedule.saveSchedulesToCSV(availableSlots);
                System.out.println("New slot added with status '" + status + "': " + date + " | " + time);
            } else {
                System.out.println("Invalid status. Please use 'Available' or 'Unavailable'.");
            }
        }
    }


    // Release a booked slot
    public void releaseSlot(String date, String time) {
        boolean slotReleased = false;

        for (Schedule schedule : availableSlots) {
            if (schedule.getDate().equals(date) && schedule.getTime().equals(time)) {
                if (schedule.getStatus().equalsIgnoreCase("Booked")) {
                    schedule.setStatus("Available");
                    schedule.setPatientID(null);
                    Schedule.saveSchedulesToCSV(availableSlots);
                    System.out.println("Slot released: " + date + " | " + time);
                    slotReleased = true;
                }
                break;
            }
        }

        if (!slotReleased) {
            System.out.println("Slot not found or not booked.");
        }
    }

    // Display available slots for the doctor
    public void displayAvailableSlots() {
        System.out.println("\nAvailable Slots for Doctor ID: " + getUserID());
        if (availableSlots.isEmpty()) {
            System.out.println("No available slots.");
        } else {
            for (Schedule slot : availableSlots) {
                System.out.println("Date: " + slot.getDate() + " | Time: " + slot.getTime());
            }
        }
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "userID='" + getUserID() + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                '}';
    }
}

