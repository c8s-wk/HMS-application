package Info;

import java.util.ArrayList;
import java.util.List;

public class Patient extends User implements PersonalInfo {
    private String name;
    private String dateOfBirth;
    private String gender;
    private String bloodType;
    private String contactInfo;
    private MedicalRecord medicalRecord;
    private List<Appointment> appointments;

    // Constructor
    public Patient(String patientID, String password, String role, String name, String dateOfBirth, String gender, String bloodType, String contactInfo) {
        super(patientID, password, role);
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.bloodType = bloodType;
        this.contactInfo = contactInfo;
        this.medicalRecord = new MedicalRecord(patientID); // Initialize medical record with patient ID
        this.appointments = new ArrayList<>();

    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    /*public void setName(String name) {
        this.name = name;
    } */

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /* public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    } */

    public String getGender() {
        return gender;
    }

    /* public void setGender(String gender) {
        this.gender = gender;
    } */

    public String getBloodType() {
        return bloodType;
    }

    /* public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    } */

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String newcontactInfo) {
        this.contactInfo = newcontactInfo;
    }

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    /* public void setMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
    } */
    // Display available appointment slots
    public void viewAvailableAppointmentSlots(List<Doctor> doctors) {
        System.out.println("Available Appointment Slots:");
        for (Doctor doctor : doctors) {
            doctor.displayAvailableSlots();
        }
    }

    // Schedule an appointment
    public boolean scheduleAppointment(Doctor doctor, String date, String time) {
        if (doctor.isSlotAvailable(date, time)) {
            Appointment appointment = new Appointment(
                    generateAppointmentId(),
                    doctor.getDoctorId(),
                    getUserID(), // Retrieve the patient ID from the superclass
                    date,
                    time,
                    "Scheduled"
            );
            appointments.add(appointment);
            doctor.bookSlot(date, time);
            System.out.println("Appointment scheduled successfully!");
            return true;
        } else {
            System.out.println("Selected slot is not available. Please choose another slot.");
            return false;
        }
    }


    // Reschedule an appointment
    public boolean rescheduleAppointment(String appointmentId, String newDate, String newTime) {
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId().equals(appointmentId)) {
                Doctor doctor = getDoctorById(appointment.getDoctorId());
                if (doctor != null && doctor.isSlotAvailable(newDate,newTime)) {
                    doctor.releaseSlot(appointment.getDate(), appointment.getTime());
                    appointment.setDate(newDate);
                    appointment.setTime(newTime);
                    doctor.bookSlot(newDate, newTime);
                    System.out.println("Appointment rescheduled successfully!");
                    return true;
                } else {
                    System.out.println("Selected slot is not available. Please choose another slot.");
                    return false;
                }
            }
        }
        System.out.println("Appointment not found.");
        return false;
    }

    // Cancel an appointment
    public boolean cancelAppointment(String appointmentId) {
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId().equals(appointmentId)) {
                Doctor doctor = getDoctorById(appointment.getDoctorId());
                if (doctor != null) {
                    doctor.releaseSlot(appointment.getDate(), appointment.getTime());
                }
                appointments.remove(appointment);
                System.out.println("Appointment canceled successfully!");
                return true;
            }
        }
        System.out.println("Appointment not found.");
        return false;
    }

    // Generate a unique appointment ID
    private String generateAppointmentId() {
        return "A" + (appointments.size() + 1);
    }

    // Get doctor by ID (Assuming there's a way to fetch doctors)
    private Doctor getDoctorById(String doctorId) {
        for (Doctor doctor : doctors) {
            if (doctor.getDoctorId().equals(doctorId)) {
                return doctor; // Return the Doctor object if IDs match
            }
        }
        System.out.println("Doctor with ID " + doctorId + " not found.");
        return null;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "userID='" + getUserID() + '\'' +
                ", role='" + getRole() + '\'' +
                ", name='" + name + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", gender='" + gender + '\'' +
                ", bloodType='" + bloodType + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                ", medicalRecord=" + medicalRecord +
                '}';
    }


}
