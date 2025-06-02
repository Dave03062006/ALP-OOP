import java.util.ArrayList;
import java.sql.*;

public class Patient extends User {
    protected int patientId;

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
        System.out.println("Patient Info:");
        System.out.println("Email: " + getEmail());
        System.out.println("Full Name: " + getFullName());
        System.out.println("Phone Number: " + getPhoneNumber());
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

    //  buat fitur filter pasien berdasarkan nama
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