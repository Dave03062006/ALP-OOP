import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Doctor extends Staff {
    protected int doctorId;
    protected String specialist;

    //
    public Doctor(String email, String password, String fullName, String phoneNumber,
            boolean onDuty, String specialist) {
        super(email, password, fullName, phoneNumber, onDuty);
        this.specialist = specialist;
    }

    public Doctor(int doctorId, String email, String password, String fullName, String phoneNumber,
            boolean onDuty, String specialist) {
        super(email, password, fullName, phoneNumber, onDuty);
        this.doctorId = doctorId;
        this.specialist = specialist;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getSpecialist() {
        return specialist;
    }

    public void setSpecialist(String specialist) {
        this.specialist = specialist;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Doctor ID: " + this.getDoctorId());
        System.out.println("Role: Doctor");
        System.out.println("Specialist: " + this.getSpecialist());
        System.out.println("================");
    }

    public void updateDoctorProfile(String email, String password, String fullName, String phoneNumber, String specialist) {
        System.out.println("Updating doctor profile...");
        System.out.println("Current Name: " + this.getFullName());


        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.specialist = specialist;

        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "UPDATE doctors SET email = ?, password = ?, full_name = ?, phone_number = ?, specialist = ? WHERE doctor_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            pstmt.setString(3, fullName);
            pstmt.setString(4, phoneNumber);
            pstmt.setString(5, specialist);
            pstmt.setInt(6, doctorId);

            int result = pstmt.executeUpdate();
            if (result > 0) {
                System.out.println("Profile updated successfully!");
            } else {
                System.out.println("Failed to update profile.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // onDuty kenapa harus dipake: dokter bisa aja onDuty tapi tidak terima pasien/
    // kalo misal ada emergency

    @Override
    public void login(String email, String password) {
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT * FROM doctors WHERE email = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                this.doctorId = rs.getInt("doctor_id");
                this.email = rs.getString("email");
                this.fullName = rs.getString("full_name");
                this.phoneNumber = rs.getString("phone_number");
                this.specialist = rs.getString("specialist");
                System.out.println("Doctor login successful!");
            } else {
                System.out.println("Invalid credentials!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // overriding since the base class User or staff defines a general register
    // method without the specialist parameter
    @Override
    public void register(String email, String password, String fullName, String phoneNumber) {
        // For doctors, we need specialist info, so we'll create a separate method
        register(email, password, fullName, phoneNumber, this.specialist);
    }

    // THIS overloaded register method is specific for doctors, including specialist
    // information
    public void register(String email, String password, String fullName, String phoneNumber, String specialist) {
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "INSERT INTO doctors (email, password, full_name, phone_number, specialist) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            pstmt.setString(3, fullName);
            pstmt.setString(4, phoneNumber);
            pstmt.setString(5, specialist);

            int result = pstmt.executeUpdate();
            if (result > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    this.doctorId = generatedKeys.getInt(1);
                }
                this.email = email;
                this.password = password;
                this.fullName = fullName;
                this.phoneNumber = phoneNumber;
                this.specialist = specialist;
                System.out.println("Doctor registered successfully with ID: " + this.doctorId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get all doctors from database
    public static List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT * FROM doctors";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Doctor doctor = new Doctor(
                        rs.getInt("doctor_id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getString("phone_number"),
                        true, // Default onDuty status
                        rs.getString("specialist"));
                doctors.add(doctor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    public static List<Doctor> getDoctorsBySpecialist(String specialist) {
        List<Doctor> doctors = new ArrayList<>();
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT * FROM doctors WHERE specialist = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, specialist);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Doctor doctor = new Doctor(
                        rs.getInt("doctor_id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getString("phone_number"),
                        true, // Default onDuty status
                        rs.getString("specialist"));
                doctors.add(doctor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    // doctor specific methodssss
    public void diagnosePatient(Appointment appointment, String diagnosis) {
        appointment.setDiagnoseResult(diagnosis);
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "UPDATE appointments SET diagnosis = ? WHERE appointment_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, diagnosis);
            pstmt.setInt(2, appointment.getAppointmentId());

            int result = pstmt.executeUpdate();
            if (result > 0) {
                System.out.println(
                        "Diagnosis updated successfully for appointment ID: " + appointment.getAppointmentId());
            } else {
                System.out.println("Failed to update diagnosis.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
