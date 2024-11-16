package Info;

import Info.Patient;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PatientDataReader {
    public static List<Patient> loadPatientsFromCSV(String filePath) {
        List<Patient> patients = new ArrayList<>();
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Skip the header
            br.readLine();

            // Read each line
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String patientID = data[0];
                String name = data[1];
                String dateOfBirth = data[2];
                String gender = data[3];
                String bloodType = data[4];
                String emailAddress = data[5];
                String contactNumber = data[6];

                // Default values for password and role
                String password = "defaultPassword";
                String role = "Patient";

                Patient patient = new Patient(patientID, password, role, name, dateOfBirth, gender, bloodType, emailAddress, contactNumber);
                patients.add(patient);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return patients;
    }
}
