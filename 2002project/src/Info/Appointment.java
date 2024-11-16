package Info;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Appointment {
    private String appointmentID;
    private String patientID;
    private String doctorID;
    private String date; // Format: YYYY-MM-DD
    private String time; // Format: HH:MM
    private String status; // Scheduled, Rescheduled, Cancelled, or Completed
    private static final String FILE_PATH = "appointments.csv"; // Path to the CSV file
    private static Map<String, Map<String, String>> doctorSchedule = new HashMap<>(); // Doctor schedule by date and time

    // Constructor
    public Appointment(String appointmentID, String patientID, String doctorID, String date, String time) {
        this.appointmentID = appointmentID;
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.date = date;
        this.time = time;
        this.status = "Scheduled"; // Default status
        updateDoctorAvailability(doctorID, date, time, false); // Mark slot as unavailable
        saveToCSV(); // Save new appointment to CSV
    }

    // Getters and Setters
    public String getAppointmentID() {
        return appointmentID;
    }

    public String getPatientID() {
        return patientID;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String newDate) {
        this.date = newDate;
        this.status = "Rescheduled";
        updateCSV(); // Update CSV file
    }

    public String getTime() {
        return time;
    }

    public void setTime(String newTime) {
        this.time = newTime;
        this.status = "Rescheduled";
        updateCSV(); // Update CSV file
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        updateCSV(); // Update CSV file
    }

    // Check if the appointment is in the past
    public boolean isPast() {
        LocalDate appointmentDate = LocalDate.parse(date);
        LocalTime appointmentTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        return appointmentDate.isBefore(today) ||
                (appointmentDate.isEqual(today) && appointmentTime.isBefore(now));
    }

    // Save the appointment to the CSV file
    private void saveToCSV() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            bw.write(String.join(",", appointmentID, doctorID, patientID, date, time, status));
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Update the CSV file with the latest appointment data
    private void updateCSV() {
        File file = new File(FILE_PATH);
        File tempFile = new File("temp_appointments.csv");
        try (BufferedReader br = new BufferedReader(new FileReader(file));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(appointmentID)) { // Match by appointment ID
                    // Replace with updated details
                    line = String.join(",", appointmentID, doctorID, patientID, date, time, status);
                }
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Replace old file with updated file
        if (!file.delete()) {
            System.err.println("Could not delete original file.");
        }
        if (!tempFile.renameTo(file)) {
            System.err.println("Could not rename temporary file.");
        }
    }

    // Schedule a new appointment
    public static Appointment scheduleAppointment(String appointmentID, String patientID, String doctorID, String date, String time) {
        if (isSlotAvailable(doctorID, date, time)) {
            return new Appointment(appointmentID, patientID, doctorID, date, time);
        } else {
            System.err.println("The selected time slot is not available for the doctor.");
            return null;
        }
    }

    // Reschedule an existing appointment
    public static boolean rescheduleAppointment(String appointmentID, String doctorID, String newDate, String newTime) {
        if (!isSlotAvailable(doctorID, newDate, newTime)) {
            System.err.println("The selected time slot is not available for the doctor.");
            return false;
        }

        File file = new File(FILE_PATH);
        File tempFile = new File("temp_appointments.csv");
        boolean updated = false;

        try (BufferedReader br = new BufferedReader(new FileReader(file));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(appointmentID)) { // Match by appointment ID
                    updateDoctorAvailability(data[1], data[3], data[4], true); // Free the old slot
                    data[3] = newDate;
                    data[4] = newTime;
                    data[5] = "Rescheduled";
                    updateDoctorAvailability(doctorID, newDate, newTime, false); // Mark new slot as unavailable
                    line = String.join(",", data);
                    updated = true;
                }
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Replace old file with updated file
        if (!file.delete()) {
            System.err.println("Could not delete original file.");
        }
        if (!tempFile.renameTo(file)) {
            System.err.println("Could not rename temporary file.");
        }

        return updated;
    }

    // Cancel an appointment
    public static boolean cancelAppointment(String appointmentID) {
        File file = new File(FILE_PATH);
        File tempFile = new File("temp_appointments.csv");
        boolean canceled = false;

        try (BufferedReader br = new BufferedReader(new FileReader(file));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(appointmentID)) { // Match by appointment ID
                    updateDoctorAvailability(data[1], data[3], data[4], true); // Free the slot
                    data[5] = "Cancelled";
                    line = String.join(",", data);
                    canceled = true;
                }
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Replace old file with updated file
        if (!file.delete()) {
            System.err.println("Could not delete original file.");
        }
        if (!tempFile.renameTo(file)) {
            System.err.println("Could not rename temporary file.");
        }

        return canceled;
    }

    // Check if a slot is available
    public static boolean isSlotAvailable(String doctorID, String date, String time) {
        doctorSchedule.putIfAbsent(doctorID, new HashMap<>());
        return !doctorSchedule.get(doctorID).containsKey(date + time);
    }

    // Update doctor availability
    public static void updateDoctorAvailability(String doctorID, String date, String time, boolean available) {
        doctorSchedule.putIfAbsent(doctorID, new HashMap<>());
        if (available) {
            doctorSchedule.get(doctorID).remove(date + time);
        } else {
            doctorSchedule.get(doctorID).put(date + time, "Unavailable");
        }
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentID='" + appointmentID + '\'' +
                ", patientID='" + patientID + '\'' +
                ", doctorID='" + doctorID + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
