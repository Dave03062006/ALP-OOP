import java.util.*;
import java.sql.*;

public class Patient extends User {
    protected int patientId;
    Scanner s = new Scanner(System.in);

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    // Constructor for new patients (before database insertion)
    public Patient(String email, String password, String fullName, String phoneNumber) {
        super(email, password, fullName, phoneNumber);
        this.patientId = 0; // 0 indicates not yet saved to database
    }

    // Constructor for existing patients (from database)
    public Patient(int patientId, String email, String password, String fullName, String phoneNumber) {
        super(email, password, fullName, phoneNumber);
        this.patientId = patientId;
    }

    @Override
    public void displayInfo() {
        System.out.println("\n--- PATIENT PROFILE ---");
        System.out.println("Patient ID: " + this.getPatientId());
        System.out.println("Name: " + this.getFullName());
        System.out.println("Email: " + this.getEmail());
        System.out.println("Phone: " + this.getPhoneNumber());
        System.out.println("-------------------");
    }

    @Override
    public void login(String email, String password) {
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT * FROM patients WHERE email = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // Login successful
                this.email = rs.getString("email");
                this.password = rs.getString("password");
                this.fullName = rs.getString("full_name");
                this.phoneNumber = rs.getString("phone_number");
                System.out.println("Login successful for patient: " + fullName);
            } else {
                // Login failed
                System.out.println("Login failed. Invalid email or password.");
            }

        } catch (SQLException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        System.out.println("Patient logged in with email: " + email);
    }

    @Override
    public void register(String email, String password, String fullName, String phoneNumber) {
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "INSERT INTO patients (email, password, full_name, phone_number) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            pstmt.setString(3, fullName);
            pstmt.setString(4, phoneNumber);

            int result = pstmt.executeUpdate();
            if (result > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    this.patientId = generatedKeys.getInt(1);
                }
                this.email = email;
                this.password = password;
                this.fullName = fullName;
                this.phoneNumber = phoneNumber;
                System.out.println("Patient registered successfully with ID: " + this.patientId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Patient registered with email: " + email);
    }

    public void updatePatientProfile() {
        System.out.println("\n--- UPDATE PATIENT PROFILE ---");
        System.out.println("Current Name: " + this.getFullName());
        System.out.print("Enter new name (or press Enter to keep current): ");
        String newName = s.nextLine();
        if (newName.trim().isEmpty()) {
            newName = this.getFullName();
        }
        System.out.println("Current Email: " + this.getEmail());
        System.out.print("Enter new email (or press Enter to keep current): ");
        String newEmail = s.nextLine();
        if (newEmail.trim().isEmpty()) {
            newEmail = this.getEmail();
        }

        System.out.println("Current Phone: " + this.getPhoneNumber());
        System.out.print("Enter new phone (or press Enter to keep current): ");
        String newPhone = s.nextLine();
        if (newPhone.trim().isEmpty()) {
            newPhone = this.getPhoneNumber();
        }

        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "UPDATE patients SET name = ?, email = ?, phone = ? WHERE patient_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newName);
            pstmt.setString(2, newEmail);
            pstmt.setString(3, newPhone);
            pstmt.setInt(4, this.getPatientId());
            int result = pstmt.executeUpdate();

            if (result > 0) {
                this.setFullName(newName);
                this.setEmail(newEmail);
                this.setPhoneNumber(newPhone);
                System.out.println("✅ Profile updated successfully!");
            }
        } catch (Exception e) {
            System.out.println("❌ Update failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void bookAppointment() {
        System.out.println("\n--- Book Appointment ---");
        System.out.println("Please select a specialist:");
        System.out.println("1. General Practitioner");
        System.out.println("2. Cardiologist");
        System.out.println("3. Dermatologist");
        System.out.println("4. Neurologist");
        System.out.println("5. Back to Dashboard");
        int doctor_choice = s.nextInt();
        System.out.println("\nEnter the date for your appointment (YYYY-MM-DD): ");
        String appointmentDate = s.next() + s.nextLine();
        System.out.println("Enter the time for your appointment, each appointment has a 15 minute interval (12.15): ");
        String appointmentTime = s.next() + s.nextLine();
        System.out.println("Enter your symptoms or reason for the appointment: ");
        String symptoms = s.next() + s.nextLine();
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "INSERT INTO appointments (patient_id, doctor_specialist, appointment_date, appointment_time, symptoms) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, this.getPatientId());
            pstmt.setString(2, getSpecialistByChoice(doctor_choice));
            pstmt.setString(3, appointmentDate);
            pstmt.setString(4, appointmentTime);
            pstmt.setString(5, symptoms);

            int result = pstmt.executeUpdate();
            if (result > 0) {
                System.out.println("✅ Appointment booked successfully!");
            } else {
                System.out.println("❌ Failed to book appointment.");
            }
        } catch (Exception e) {
            System.out.println("❌ Booking failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getSpecialistByChoice(int choice) {
        switch (choice) {
            case 1:
                return "General Practitioner";
            case 2:
                return "Cardiologist";
            case 3:
                return "Dermatologist";
            case 4:
                return "Neurologist";
            default:
                return "Unknown";
        }
    }

    public void viewMedicalHistory() {
        System.out.println("\n--- Medical History ---");
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT * FROM medical_history WHERE patient_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, this.getPatientId());
            ResultSet rs = pstmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("No medical history found for this patient.");
                return;
            }

            while (rs.next()) {
                System.out.println("Appointment ID: " + rs.getInt("appointment_id"));
                System.out.println("Doctor: " + rs.getString("doctor_name"));
                System.out.println("Date: " + rs.getString("appointment_date"));
                System.out.println("Symptoms: " + rs.getString("symptoms"));
                System.out.println("-------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewPatientAppointments() {
        System.out.println("\n--- Your Appointments ---");
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT * FROM appointments WHERE patient_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, this.getPatientId());
            ResultSet rs = pstmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("No appointments found for this patient.");
                return;
            }

            while (rs.next()) {
                System.out.println("--- Appointment Details ---");
                System.out.println("Appointment ID: " + rs.getInt("appointment_id"));
                System.out.println("Doctor: " + rs.getString("doctor_name"));
                System.out.println("Date: " + rs.getString("appointment_date"));
                System.out.println("Time: " + rs.getString("appointment_time"));
                System.out.println("Symptoms: " + rs.getString("symptoms"));
                System.out.println("-------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // buat fitur filter pasien berdasarkan nama
    public static ArrayList<Patient> getAllPatients() {
        ArrayList<Patient> patients = new ArrayList<>();
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT * FROM patients";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Patient patient = new Patient(
                        rs.getInt("patient_id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getString("phone_number"));
                patients.add(patient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    public static ArrayList<Patient> getPatientsByName(String fullName) {
        ArrayList<Patient> patients = new ArrayList<>();
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT * FROM patients WHERE full_name = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, fullName);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Patient patient = new Patient(
                        rs.getInt("patient_id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getString("phone_number"));
                patients.add(patient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

}