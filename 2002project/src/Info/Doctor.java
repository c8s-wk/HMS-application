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

    public Doctor(String userID, String password, String name, String role, String gender, int age) {
        super(userID, password, "Doctor");
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.appointments = new ArrayList<>();
        this.availableSlots = new ArrayList<>();
        loadAppointmentsFromCSV();
        loadScheduleFromCSV();
    }

    public String getUserID() {
        return super.getUserID();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getGender() {
        return gender;
    }

    @Override
    public int getAge() {
        return age;
    }

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
                    allDoctors.add(new Doctor(userID, "password", role, name, gender, age));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading doctor data: " + e.getMessage());
        }
        return allDoctors;
    }

    public static Doctor getDoctorById(String doctorId) {
        List<Doctor> allDoctors = getAllDoctors();
        for (Doctor doctor : allDoctors) {
            if (doctor.getUserID().equals(doctorId)) {
                return doctor;
            }
        }
        System.out.println("Doctor with ID " + doctorId + " not found.");
        return null;
    }

    public void loadAppointmentsFromCSV() {
        appointments.clear();
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

    // Expose this method as public to allow for updating the schedule
    public void loadScheduleFromCSV() {
        availableSlots.clear();
        List<Schedule> allSchedules = Schedule.loadSchedulesFromCSV();
        for (Schedule schedule : allSchedules) {
            if (schedule.getDoctorID().equals(getUserID())) {
                availableSlots.add(schedule);
            }
        }
    }

    public List<Schedule> getSchedule() {
        return availableSlots;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void saveAppointments() {
        saveAppointmentsToCSV(appointments);
    }

    public static void saveAppointmentsToCSV(List<Appointment> appointments) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(APPOINTMENT_FILE))) {
            bw.write("AppointmentID,PatientID,DoctorID,Date,Time,Status\n");
            for (Appointment appointment : appointments) {
                bw.write(appointment.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving appointments: " + e.getMessage());
        }
    }

    public void viewSchedule() {
        System.out.println("\n--- Doctor's Full Schedule ---");
        boolean hasSchedule = false;
        for (Schedule schedule : availableSlots) {
            String date = schedule.getDate();
            String time = schedule.getTime();
            String status = schedule.getStatus();
            String patientID = schedule.getPatientID() == null ? "N/A" : schedule.getPatientID();
            System.out.println("Date: " + date + " | Time: " + time + " | Status: " + status + " | Patient ID: " + patientID);
            hasSchedule = true;
        }

        if (!hasSchedule) {
            System.out.println("No schedule found for Doctor ID: " + getUserID());
        }
    }

    public void setAvailability(String date, String time, String status) {
        loadScheduleFromCSV();

        boolean slotUpdated = false;
        for (Schedule slot : availableSlots) {
            if (slot.getDate().equals(date) && slot.getTime().equals(time)) {
                slot.setStatus(status);
                if (!"Booked".equalsIgnoreCase(status)) {
                    slot.setPatientID(null);
                }
                slotUpdated = true;
                break;
            }
        }

        if (!slotUpdated) {
            availableSlots.add(new Schedule(getUserID(), date, time, status, null));
        }

        List<Schedule> allSchedules = Schedule.loadSchedulesFromCSV();
        allSchedules.removeIf(slot -> slot.getDoctorID().equals(getUserID()));
        allSchedules.addAll(availableSlots);
        Schedule.saveSchedulesToCSV(allSchedules);

        System.out.println("Slot updated or added: " + date + " | " + time + " | " + status);
    }

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

    public static void saveScheduleToCSV(String doctorID, List<Schedule> schedule) {
        List<Schedule> allSchedules = Schedule.loadSchedulesFromCSV();
        allSchedules.removeIf(slot -> slot.getDoctorID().equals(doctorID));
        allSchedules.addAll(schedule);
        Schedule.saveSchedulesToCSV(allSchedules);
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