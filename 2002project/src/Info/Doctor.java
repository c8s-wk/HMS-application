package Info;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Doctor extends User {
    private String name;
    private String gender;
    private int age;
    private List<Appointment> appointments; // List of appointments
    private Map<String, Boolean> availableSlots; // Map of available slots (date-time as key, availability as value)
    private static final String APPOINTMENT_FILE = "appointments.csv"; // CSV file path

    // Constructor
    public Doctor(String userID, String password, String name, String gender, int age) {
        super(userID, password, "Doctor");
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.appointments = new ArrayList<>();
        this.availableSlots = new HashMap<>();
        loadAppointmentsFromCSV();
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public static Doctor getDoctorById(String doctorId) {
        for (Doctor doctor : doctors) {
            if (doctor.getUserID().equals(doctorId)) {
                return doctor; // Return the Doctor object if the IDs match
            }
        }
        System.out.println("Doctor with ID " + doctorId + " not found.");
        return null; // Return null if no matching doctor is found
    }


    // Set availability for appointments
    public void setAvailability(String date, String time) {
        String dateTime = date + " " + time; // Combine date and time into a standardized format
        availableSlots.put(dateTime, true); // Mark slot as available
        System.out.println("Slot added as available: " + dateTime);
    }

    // Check if a slot is available
    public boolean isSlotAvailable(String date, String time) {
        String dateTime = date + " " + time;
        return availableSlots.getOrDefault(dateTime, false);
    }

    // Book a slot (mark it as unavailable)
    public boolean bookSlot(String date, String time) {
        String dateTime = date + " " + time;
        if (isSlotAvailable(date, time)) {
            availableSlots.put(dateTime, false); // Mark the slot as booked
            System.out.println("Slot booked: " + dateTime);
            return true;
        }
        System.out.println("Slot not available: " + dateTime);
        return false;
    }

    // Release a slot (mark it as available)
    public void releaseSlot(String date, String time) {
        String dateTime = date + " " + time;
        if (availableSlots.containsKey(dateTime)) {
            availableSlots.put(dateTime, true); // Mark the slot as available again
            System.out.println("Slot released: " + dateTime);
        } else {
            System.out.println("Slot not found: " + dateTime);
        }
    }

    // Display available slots
    public void displayAvailableSlots() {
        System.out.println("Available Slots:");
        boolean hasAvailableSlots = false;

        for (Map.Entry<String, Boolean> entry : availableSlots.entrySet()) {
            if (entry.getValue()) { // Check if the slot is available (true)
                System.out.println("- " + entry.getKey()); // Display the slot
                hasAvailableSlots = true;
            }
        }

        if (!hasAvailableSlots) {
            System.out.println("No available slots at the moment.");
        }
    }

    // Add a new appointment and update the CSV file
    public void addAppointment(Appointment appointment) {
        if (bookSlot(appointment.getDate(), appointment.getTime())) {
            appointments.add(appointment);
            System.out.println("Appointment added for doctor: " + appointment);
            saveAppointmentsToCSV();
        } else {
            System.out.println("Failed to add appointment. Slot is not available.");
        }
    }

    // Remove an appointment and update the CSV file
    public void removeAppointment(String appointmentID) {
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentID().equals(appointmentID)) {
                appointments.remove(appointment);
                releaseSlot(appointment.getDate(), appointment.getTime());
                System.out.println("Appointment removed: " + appointmentID);
                saveAppointmentsToCSV();
                return;
            }
        }
        System.out.println("Appointment not found: " + appointmentID);
    }

    // View schedule
    public void viewSchedule() {
        System.out.println("Doctor's Schedule:");
        if (appointments.isEmpty()) {
            System.out.println("No scheduled appointments.");
        } else {
            for (Appointment appointment : appointments) {
                System.out.println(appointment);
            }
        }
    }

    // Load appointments from the CSV file
    private void loadAppointmentsFromCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader(APPOINTMENT_FILE))) {
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

                String appointmentID = data[0].trim();
                String doctorID = data[1].trim();
                String patientID = data[2].trim();
                String date = data[3].trim();
                String time = data[4].trim();
                String status = data.length > 5 ? data[5].trim() : "Scheduled";

                if (doctorID.equals(getUserID())) {
                    Appointment appointment = new Appointment(appointmentID, patientID, doctorID, date, time, status);
                    appointments.add(appointment);
                    bookSlot(date, time);
                }
            }
            System.out.println("Appointments loaded for doctor: " + getUserID());
        } catch (IOException e) {
            System.err.println("Error reading the appointments CSV file: " + e.getMessage());
        }
    }

    // Save appointments to the CSV file
    private void saveAppointmentsToCSV() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(APPOINTMENT_FILE))) {
            // Write the header
            bw.write("AppointmentID,DoctorID,PatientID,Date,Time,Status");
            bw.newLine();

            // Write each appointment
            for (Appointment appointment : appointments) {
                bw.write(String.join(",",
                        appointment.getAppointmentID(),
                        appointment.getDoctorID(),
                        appointment.getPatientID(),
                        appointment.getDate(),
                        appointment.getTime(),
                        appointment.getStatus()));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to the appointments CSV file: " + e.getMessage());
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
