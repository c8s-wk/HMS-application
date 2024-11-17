package Info;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Schedule {
    private String doctorID;
    private String date;
    private String time;
    private String status;
    private String patientID;

    private static final String SCHEDULE_FILE_PATH = "2002project/Schedule.csv";

    public Schedule(String doctorID, String date, String time, String status, String patientID) {
        this.doctorID = doctorID;
        this.date = date;
        this.time = time;
        this.status = status;
        this.patientID = patientID;
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

    public String getPatientID() {
        return patientID;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public static List<Schedule> loadSchedulesFromCSV() {
        List<Schedule> schedules = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(SCHEDULE_FILE_PATH))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                schedules.add(new Schedule(data[0], data[1], data[2], data[3], data[4].isEmpty() ? null : data[4]));
            }
        } catch (IOException e) {
            System.err.println("Error loading schedule: " + e.getMessage());
        }
        return schedules;
    }

    public static void saveSchedulesToCSV(List<Schedule> schedules) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SCHEDULE_FILE_PATH))) {
            bw.write("DoctorID,Date,Time,Status,PatientID");
            bw.newLine();
            for (Schedule schedule : schedules) {
                bw.write(schedule.getDoctorID() + "," + schedule.getDate() + "," + schedule.getTime() + "," + schedule.getStatus() + "," + (schedule.getPatientID() == null ? "" : schedule.getPatientID()));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving schedule: " + e.getMessage());
        }
    }

    public static boolean bookSlot(List<Schedule> schedules, String doctorID, String date, String time, String patientID) {
        for (Schedule schedule : schedules) {
            if (schedule.getDoctorID().equals(doctorID) && schedule.getDate().equals(date) && schedule.getTime().equals(time) && "Available".equalsIgnoreCase(schedule.getStatus())) {
                schedule.setStatus("Booked");
                schedule.setPatientID(patientID);
                return true;
            }
        }
        return false;
    }

    public static boolean releaseSlot(List<Schedule> schedules, String doctorID, String date, String time) {
        for (Schedule schedule : schedules) {
            if (schedule.getDoctorID().equals(doctorID) && schedule.getDate().equals(date) && schedule.getTime().equals(time) && "Booked".equalsIgnoreCase(schedule.getStatus())) {
                schedule.setStatus("Available");
                schedule.setPatientID(null);
                return true;
            }
        }
        return false;
    }
}
