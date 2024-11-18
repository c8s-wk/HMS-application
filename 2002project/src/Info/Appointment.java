package Info;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Appointment {
    private String appointmentID;
    private String patientID;
    private String doctorID;
    private String date;
    private String time;
    private String status; // Pending, Accepted, Declined, Completed

    private static final String FILE_PATH = "2002project/Appointment.csv"; // Path to the CSV file

    // Constructor
    public Appointment(String appointmentID, String patientID, String doctorID, String date, String time, String status) {
        this.appointmentID = appointmentID;
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.date = date;
        this.time = time;
        this.status = status;
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

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    public void setDate(String date) {
        this.date = date;
        updateAppointmentInCSV();
    }

    public void setTime(String time) {
        this.time = time;
        updateAppointmentInCSV();
    }

    public void setStatus(String status) {
        this.status = status;
        updateAppointmentInCSV();
    }

    public void setDoctorID(String doctorID) {this.doctorID = doctorID;}

    // Generate a unique Appointment ID
    public static String generateAppointmentID() {
        List<Appointment> appointments = loadAppointmentsFromCSV();
        int maxID = appointments.stream()
                .mapToInt(a -> Integer.parseInt(a.getAppointmentID().replace("A", "")))
                .max()
                .orElse(0);
        return "A" + (maxID + 1);
    }

    // Load appointments from CSV
    public static List<Appointment> loadAppointmentsFromCSV() {
        List<Appointment> appointments = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 6) continue;
                appointments.add(new Appointment(data[0], data[1], data[2], data[3], data[4], data[5]));
            }
        } catch (IOException e) {
            System.err.println("Error loading appointments: " + e.getMessage());
        }
        return appointments;
    }

    // Save appointments to CSV
    public static void saveAppointmentsToCSV(List<Appointment> appointments) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            bw.write("AppointmentID,PatientID,DoctorID,Date,Time,Status");
            bw.newLine();
            for (Appointment appointment : appointments) {
                if (appointment.isValid()) {
                    bw.write(appointment.toCSV());
                    bw.newLine();
                } else {
                    System.err.println("Invalid appointment data, skipping: " + appointment);
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving appointments: " + e.getMessage());
        }
    }

    private boolean isValid() {
        return patientID != null && !patientID.isEmpty() &&
                doctorID != null && !doctorID.isEmpty() &&
                date != null && !date.isEmpty() &&
                time != null && !time.isEmpty();
    }

    // Update appointment in CSV
    private void updateAppointmentInCSV() {
        List<Appointment> appointments = loadAppointmentsFromCSV();
        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getAppointmentID().equals(this.appointmentID)) {
                appointments.set(i, this);
                break;
            }
        }
        saveAppointmentsToCSV(appointments);
    }

    // Schedule an appointment
    public static boolean scheduleAppointment(Appointment appointment) {
        List<Appointment> appointments = loadAppointmentsFromCSV();
        for (Appointment existing : appointments) {
            if (existing.getDoctorID().equals(appointment.getDoctorID())
                    && existing.getDate().equals(appointment.getDate())
                    && existing.getTime().equals(appointment.getTime())
                    && existing.getStatus().equalsIgnoreCase("Accepted")) {
                System.out.println("The selected time slot is already booked.");
                return false;
            }
        }
        appointments.add(appointment);
        saveAppointmentsToCSV(appointments);
        return true;
    }

    // Reschedule an appointment
    public static boolean rescheduleAppointment(String appointmentID, String newDate, String newTime) {
        List<Appointment> appointments = loadAppointmentsFromCSV();
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentID().equals(appointmentID)) {
                appointment.setDate(newDate);
                appointment.setTime(newTime);
                saveAppointmentsToCSV(appointments);
                return true;
            }
        }
        return false;
    }

    // Cancel an appointment
    public static boolean cancelAnAppointment(String appointmentID) {
        List<Appointment> appointments = loadAppointmentsFromCSV();
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentID().equals(appointmentID)) {
                appointment.setStatus("Cancelled");
                saveAppointmentsToCSV(appointments);
                return true;
            }
        }
        return false;
    }

    // Convert to CSV format
    public String toCSV() {
        return String.join(",", appointmentID, patientID, doctorID, date, time, status);
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