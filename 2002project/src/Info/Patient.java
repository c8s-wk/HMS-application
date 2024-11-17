package Info;

import java.util.ArrayList;
import java.util.List;

public class Patient extends User {
    private MedicalRecord medicalRecord;
    private List<Appointment> appointments;

    private static final String APPOINTMENT_FILE_PATH = "2002project/Appointment.csv";

    public Patient(String patientID, String password, String role, MedicalRecord medicalRecord) {
        super(patientID, password, role);
        this.medicalRecord = medicalRecord;
        this.appointments = new ArrayList<>();
        loadAppointments();
    }

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    // Getters and setters using MedicalRecord attributes
    public String getName() {
        return medicalRecord.getName();
    }

    public String getDateOfBirth() {
        return medicalRecord.getDateOfBirth();
    }

    public String getGender() {
        return medicalRecord.getGender();
    }

    public String getBloodType() {
        return medicalRecord.getBloodType();
    }

    public String getEmailAddress() {
        return medicalRecord.getEmailAddress();
    }

    public void setEmailAddress(String newEmailAddress) {
        medicalRecord.setEmailAddress(newEmailAddress);
    }

    public String getContactNumber() {
        return medicalRecord.getContactNumber();
    }

    public void setContactNumber(String newContactNumber) {
        medicalRecord.setContactNumber(newContactNumber);
    }

    public boolean scheduleAppointment(Appointment newAppointment) {
        List<Schedule> schedules = Schedule.loadSchedulesFromCSV();
        if (Schedule.bookSlot(schedules, newAppointment.getDoctorID(), newAppointment.getDate(), newAppointment.getTime(), getUserID())) {
            appointments.add(newAppointment);
            Appointment.saveAppointmentsToCSV(Appointment.loadAppointmentsFromCSV());
            Schedule.saveSchedulesToCSV(schedules);
            return true;
        }
        return false;
    }

    public boolean rescheduleAppointment(String appointmentID, String newDate, String newTime) {
        Appointment appointment = appointments.stream()
                .filter(a -> a.getAppointmentID().equals(appointmentID))
                .findFirst()
                .orElse(null);

        if (appointment == null) return false;

        List<Schedule> schedules = Schedule.loadSchedulesFromCSV();

        if (Schedule.releaseSlot(schedules, appointment.getDoctorID(), appointment.getDate(), appointment.getTime()) &&
                Schedule.bookSlot(schedules, appointment.getDoctorID(), newDate, newTime, getUserID())) {
            appointment.setDate(newDate);
            appointment.setTime(newTime);
            Appointment.saveAppointmentsToCSV(Appointment.loadAppointmentsFromCSV());
            Schedule.saveSchedulesToCSV(schedules);
            return true;
        }
        return false;
    }

    public boolean cancelAppointment(String appointmentID) {
        Appointment appointment = appointments.stream()
                .filter(a -> a.getAppointmentID().equals(appointmentID))
                .findFirst()
                .orElse(null);

        if (appointment == null) return false;

        List<Schedule> schedules = Schedule.loadSchedulesFromCSV();

        if (Schedule.releaseSlot(schedules, appointment.getDoctorID(), appointment.getDate(), appointment.getTime())) {
            appointments.remove(appointment);
            Appointment.saveAppointmentsToCSV(Appointment.loadAppointmentsFromCSV());
            Schedule.saveSchedulesToCSV(schedules);
            return true;
        }
        return false;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    private void loadAppointments() {
        List<Appointment> allAppointments = Appointment.loadAppointmentsFromCSV();
        for (Appointment appointment : allAppointments) {
            if (appointment.getPatientID().equals(getUserID())) {
                appointments.add(appointment);
            }
        }
    }
}
