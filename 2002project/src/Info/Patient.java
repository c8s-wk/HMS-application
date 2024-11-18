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
    public String getPassword() {return this.password;}

    public String getDateOfBirth() {
        return medicalRecord.getDateOfBirth();
    }

    public String getGender() {
        return medicalRecord.getGender();
    }

    @Override
    public int getAge() {
        return 0;
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

            // Save updated appointment list and schedule
            List<Appointment> allAppointments = Appointment.loadAppointmentsFromCSV();
            allAppointments.add(newAppointment);
            Appointment.saveAppointmentsToCSV(allAppointments);
            Schedule.saveSchedulesToCSV(schedules);
            return true;
        }
        return false;
    }

    public boolean rescheduleAppointment(String appointmentID, String newDoctorID, String newDate, String newTime) {
        Appointment appointment = appointments.stream()
                .filter(a -> a.getAppointmentID().equals(appointmentID))
                .findFirst()
                .orElse(null);

        if (appointment == null) return false;

        List<Schedule> schedules = Schedule.loadSchedulesFromCSV();

        if (Schedule.releaseSlot(schedules, appointment.getDoctorID(), appointment.getDate(), appointment.getTime()) &&
                Schedule.bookSlot(schedules, newDoctorID, newDate, newTime, getUserID())) {
            appointment.setDoctorID(newDoctorID);
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
        else{
            appointment.cancelAnAppointment(appointmentID);
        }

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
        List<Appointment> findAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getPatientID().equals(getUserID())) {
                findAppointments.add(appointment);
            }
            //System.out.println("debug");
        }
        return findAppointments;
    }


    private void loadAppointments() {
        appointments.clear(); // Clear any existing appointments
        List<Appointment> allAppointments = Appointment.loadAppointmentsFromCSV();

        //System.out.println("Loaded appointments from CSV:"); // Debug print
        for (Appointment appointment : allAppointments) {
            //System.out.println(appointment); // Debug: Print all loaded appointments
            if (appointment.getPatientID().equals(getUserID())) {
                appointments.add(appointment);
            }
        }
        /*class PatientRecord {
            private String patientID;
            private String name;
            private String PastDiagnosis;
            private String pastTreatment;
            private String pastPrescription;


            public void updateDiagnosis(String newPastDiagnosis) {
                if (newPastDiagnosis != null && newPastDiagnosis.trim().length() > 0) {
                    if (pastDiagnoses.isEmpty()) {
                        pastDiagnoses += "," + newPastDiagnosis;
                    } else {
                        pastDiagnoses.add += newPastDiagnosis;
                    }
                }
            }
        }*/
        //System.out.println("Appointments for patient " + getUserID() + ": " + appointments); // Debug: Print filtered list
    }

}