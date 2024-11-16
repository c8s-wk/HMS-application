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

    public void setDate(String date) {
        this.date = date;
        updateAppointmentInCSV();
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
        updateAppointmentInCSV();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        updateAppointmentInCSV();
    }

    // Load all appointments from CSV
    public static List<Appointment> loadAppointmentsFromCSV(String filePath) {
        List<Appointment> appointments = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Skip header
            br.readLine();

            // Read each record
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 6) {
                    System.err.println("Invalid row in Appointment CSV: " + line);
                    continue;
                }

                String appointmentID = data[0];
                String patientID = data[1];
                String doctorID = data[2];
                String date = data[3];
                String time = data[4];
                String status = data[5];

                appointments.add(new Appointment(appointmentID, patientID, doctorID, date, time, status));
            }
        } catch (IOException e) {
            System.err.println("Error reading Appointment CSV: " + e.getMessage());
        }
        return appointments;
    }

    // Save all appointments to CSV
    public static void saveAppointmentsToCSV(String filePath, List<Appointment> appointments) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            // Write header
            bw.write("AppointmentID,PatientID,DoctorID,Date,Time,Status");
            bw.newLine();

            // Write data
            for (Appointment appointment : appointments) {
                bw.write(appointment.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving Appointment CSV: " + e.getMessage());
        }
    }

    // Update appointment in CSV
    private void updateAppointmentInCSV() {
        List<Appointment> appointments = loadAppointmentsFromCSV(FILE_PATH);
        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getAppointmentID().equals(this.appointmentID)) {
                appointments.set(i, this); // Update the appointment
                break;
            }
        }
        saveAppointmentsToCSV(FILE_PATH, appointments);
    }

    // Schedule a new appointment
    public static boolean scheduleAppointment(Appointment appointment) {
        List<Appointment> appointments = loadAppointmentsFromCSV(FILE_PATH);

        // Check for slot availability
        for (Appointment existing : appointments) {
            if (existing.getDoctorID().equals(appointment.getDoctorID())
                    && existing.getDate().equals(appointment.getDate())
                    && existing.getTime().equals(appointment.getTime())
                    && existing.getStatus().equals("Accepted")) {
                System.out.println("The selected time slot is already booked.");
                return false;
            }
        }

        appointments.add(appointment);
        saveAppointmentsToCSV(FILE_PATH, appointments);
        System.out.println("Appointment scheduled successfully.");
        return true;
    }

    // Reschedule an existing appointment
    public static boolean rescheduleAppointment(String appointmentID, String newDate, String newTime) {
        List<Appointment> appointments = loadAppointmentsFromCSV(FILE_PATH);

        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentID().equals(appointmentID)) {
                // Check for conflicts
                for (Appointment existing : appointments) {
                    if (existing.getDoctorID().equals(appointment.getDoctorID())
                            && existing.getDate().equals(newDate)
                            && existing.getTime().equals(newTime)
                            && existing.getStatus().equals("Accepted")) {
                        System.out.println("The new time slot is already booked.");
                        return false;
                    }
                }
                // Update the appointment
                appointment.setDate(newDate);
                appointment.setTime(newTime);
                saveAppointmentsToCSV(FILE_PATH, appointments);
                System.out.println("Appointment rescheduled successfully.");
                return true;
            }
        }
        System.out.println("Appointment not found.");
        return false;
    }

    // Cancel an appointment
    public static boolean cancelAppointment(String appointmentID) {
        List<Appointment> appointments = loadAppointmentsFromCSV(FILE_PATH);

        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getAppointmentID().equals(appointmentID)) {
                appointments.get(i).setStatus("Cancelled");
                saveAppointmentsToCSV(FILE_PATH, appointments);
                System.out.println("Appointment cancelled successfully.");
                return true;
            }
        }
        System.out.println("Appointment not found.");
        return false;
    }

    // Convert appointment to CSV format
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
