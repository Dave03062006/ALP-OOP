import java.sql.*;
import java.util.*;
import java.time.*;

import javax.xml.crypto.Data;

public class Doctor extends Staff {
    protected int doctorId;
    protected String specialist;
    protected Medicine medicines;
    private Appointment currAppointment;
    private Prescription prescription;
    Scanner s = new Scanner(System.in);

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

    public void updateDoctorProfile() {
        System.out.println("Updating doctor profile...");
        System.out.println("Current Name: " + this.getFullName());
        String fullName = s.next() + s.nextLine();
        if (fullName.trim().isEmpty()) {
            fullName = this.getFullName();
        }
        System.out.println("Current Email: " + this.getEmail());
        String email = s.next() + s.nextLine();
        if (email.trim().isEmpty()) {
            email = this.getEmail();
        }
        System.out.println("Current Password: " + this.getPassword());
        String password = s.next() + s.nextLine();
        if (password.trim().isEmpty()) {
            password = this.getPassword();
        }
        System.out.println("Current Phone Number: " + this.getPhoneNumber());
        String phoneNumber = s.next() + s.nextLine();
        if (phoneNumber.trim().isEmpty()) {
            phoneNumber = this.getPhoneNumber();
        }
        System.out.println("Current Specialist: " + this.getSpecialist());
        String specialist = s.next() + s.nextLine();
        if (specialist.trim().isEmpty()) {
            specialist = this.getSpecialist();
        }
        System.out.println("Current On Duty Status: " + this.isOnDuty());
        boolean onDuty = s.nextBoolean();
        if (!s.hasNextBoolean()) {
            onDuty = this.isOnDuty();
        }

        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "UPDATE doctors SET email = ?, password = ?, full_name = ?, phone_number = ?, specialist = ?, onDuty = ? WHERE doctor_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            pstmt.setString(3, fullName);
            pstmt.setString(4, phoneNumber);
            pstmt.setString(5, specialist);
            pstmt.setBoolean(6, onDuty);
            pstmt.setInt(7, this.getDoctorId());

            int result = pstmt.executeUpdate();
            if (result > 0) {
                this.setEmail(email);
                this.setPassword(password);
                this.setFullName(fullName);
                this.setPhoneNumber(phoneNumber);
                this.setSpecialist(specialist);
                this.setOnDuty(onDuty);
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
        this.onDuty = true; // Default onDuty status for doctors
        register(email, password, fullName, phoneNumber, this.specialist, onDuty);
    }

    // THIS overloaded register method is specific for doctors, including specialist
    // information
    public void register(String email, String password, String fullName, String phoneNumber, String specialist,
            boolean onDuty) {
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "INSERT INTO doctors (email, password, full_name, phone_number, specialist, onDuty) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            pstmt.setString(3, fullName);
            pstmt.setString(4, phoneNumber);
            pstmt.setString(5, specialist);
            pstmt.setBoolean(6, onDuty);

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
                this.onDuty = onDuty;
                System.out.println("Doctor registered successfully with ID: " + this.doctorId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewAppointments() {
        this.doctorId = this.getDoctorId();
        System.out.println("Viewing appointments for Doctor ID: " + this.doctorId);
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT * FROM appointments WHERE doctor_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, this.doctorId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println("--------------------------");
                System.out.println("Appointment ID: " + rs.getInt("appointment_id"));
                System.out.println("Patient ID: " + rs.getInt("patient_id"));
                System.out.println("Date: " + rs.getDate("appointment_date"));
                System.out.println("Time: " + rs.getTime("appointment_time"));
                System.out.println("Symptom: " + rs.getString("symptoms"));
                System.out.println("-------------------------");
            }
            if (!rs.isBeforeFirst()) {
                System.out.println("No appointments found.");
            }

            System.out.println("Select an appointment ID to create a prescription or diagnose:");
            int appointmentId = s.nextInt();
            sql = "SELECT * FROM appointments WHERE appointment_id = ? AND doctor_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, appointmentId);
            createPrescription(currAppointment);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createPrescription(Appointment appointment) {
        System.out.println("Creating prescription for appointment ID: " + appointment.getAppointmentId());
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT * FROM medicines";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, medicines.getMedicineId());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println("--------------------------");
                System.out.println("Medicine ID: " + rs.getInt("medicine_id"));
                System.out.println("Medicine Name: " + rs.getString("medicine_name"));
                System.out.println("Dosage: " + rs.getString("dosage"));
                System.out.println("Description: " + rs.getString("description"));
                System.out.println("-------------------------");
            }

            if (!rs.isBeforeFirst()) {
                System.out.println("No medicines found.");
            }

            System.out.println("Select a medicine ID to prescribe:");
            int medicineId = s.nextInt();
            sql = "SELECT * FROM medicines WHERE medicine_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, medicineId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                String medicineName = rs.getString("medicine_name");
                String dosage = rs.getString("dosage");
                String description = rs.getString("description");

                // Create a new prescription
                this.prescription = new Prescription(currAppointment.getPatient(), currAppointment.getDoctor(),
                        description, medicineName, dosage);
                System.out.println("Prescription created successfully for appointment ID: "
                        + appointment.getAppointmentId());
                sql = "INSERT INTO prescriptions (prescription_id, doctor_id, patient_id, recipe_description) VALUES (?, ?, ?, ?);";
                pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, prescription.getPrescriptionId());
                pstmt.setInt(2, appointment.getDoctor().getDoctorId());
                pstmt.setInt(3, appointment.getPatient().getPatientId());
                pstmt.setString(4, prescription.getReceiptDescription());
                int result = pstmt.executeUpdate();
            } else {
                System.out.println("Invalid medicine ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void prescribeMedicine(Appointment appointment, String recipe_description) {
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "UPDATE appointments SET recipe_description = ? WHERE appointment_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, recipe_description);
            pstmt.setInt(2, appointment.getAppointmentId());

            int result = pstmt.executeUpdate();
            if (result > 0) {
                System.out.println(
                        "Medicine prescribed successfully for appointment ID: " + appointment.getAppointmentId());
            } else {
                System.out.println("Failed to prescribe medicine.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        prescription.saveToDatabase();
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
                        rs.getBoolean("onDuty"), // Default onDuty status
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

    public void setWorkHours(String dayOfWeek, LocalTime startTime, LocalTime endTime) {
        try {
            Connection conn = DatabaseConnect.getConnection();
            // First check if a schedule already exists for this doctor and day
            String checkSql = "SELECT * FROM doctor_schedule WHERE doctor_id = ? AND day_of_week = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, this.doctorId);
            checkStmt.setString(2, dayOfWeek);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // Update existing schedule
                String updateSql = "UPDATE doctor_schedule SET start_time = ?, end_time = ? WHERE doctor_id = ? AND day_of_week = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setString(1, startTime.toString());
                updateStmt.setString(2, endTime.toString());
                updateStmt.setInt(3, this.doctorId);
                updateStmt.setString(4, dayOfWeek);
                int result = updateStmt.executeUpdate();
                if (result > 0) {
                    System.out.println("Schedule updated for " + dayOfWeek);
                }
            } else {
                // Insert new schedule
                String insertSql = "INSERT INTO doctor_schedule (doctor_id, day_of_week, start_time, end_time) VALUES (?, ?, ?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                insertStmt.setInt(1, this.doctorId);
                insertStmt.setString(2, dayOfWeek);
                insertStmt.setString(3, startTime.toString());
                insertStmt.setString(4, endTime.toString());
                int result = insertStmt.executeUpdate();
                if (result > 0) {
                    System.out.println("New schedule set for " + dayOfWeek);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void manageWeeklySchedule() {
        System.out.println("\n--- Set Weekly Schedule ---");
        String[] daysOfWeek = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };

        for (String day : daysOfWeek) {
            System.out.println("\nSetting hours for " + day);
            System.out.println("Do you want to set hours for this day? (Y/N)");
            String response = s.next().toUpperCase();

            if (response.equals("Y")) {
                System.out.println("Enter start time (HH:MM format):");
                String startTimeStr = s.next();
                System.out.println("Enter end time (HH:MM format):");
                String endTimeStr = s.next();

                try {
                    LocalTime startTime = LocalTime.parse(startTimeStr);
                    LocalTime endTime = LocalTime.parse(endTimeStr);

                    // Create a BST for available time slots
                    ScheduleBST daySchedule = new ScheduleBST();

                    // Add time slots in 15-minute increments
                    LocalTime current = startTime;
                    while (!current.isAfter(endTime.minusMinutes(15))) {
                        daySchedule.insert(current);
                        current = current.plusMinutes(15);
                    }

                    // Save to database
                    setWorkHours(day, startTime, endTime);

                    System.out.println("Schedule for " + day + " set from " + startTime + " to " + endTime);
                } catch (Exception e) {
                    System.out.println("Invalid time format. Please use HH:MM format.");
                }
            } else {
                System.out.println("No hours set for " + day);
                // Optionally remove any existing hours for this day
                try {
                    Connection conn = DatabaseConnect.getConnection();
                    String sql = "DELETE FROM doctor_schedule WHERE doctor_id = ? AND day_of_week = ?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, this.doctorId);
                    pstmt.setString(2, day);
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("\nWeekly schedule has been updated.");
    }

    public boolean isAvailableAt(String dayOfWeek, LocalTime time) {
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT * FROM doctor_schedule WHERE doctor_id = ? AND day_of_week = ? AND ? BETWEEN start_time AND end_time";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, this.doctorId);
            pstmt.setString(2, dayOfWeek);
            pstmt.setString(3, time.toString());
            ResultSet rs = pstmt.executeQuery();

            // Also check if there's already an appointment at this time
            if (rs.next()) {
                String checkAppointmentSql = "SELECT * FROM appointments WHERE doctor_id = ? AND appointment_date = ? AND appointment_time = ?";
                PreparedStatement appStmt = conn.prepareStatement(checkAppointmentSql);
                appStmt.setInt(1, this.doctorId);
                // You'd need to convert dayOfWeek to an actual date
                // For demo purposes assuming we pass the actual date string
                appStmt.setString(2, LocalDate.now().toString());
                appStmt.setString(3, time.toString());
                ResultSet appRs = appStmt.executeQuery();

                // Doctor is available if there's a schedule entry but no appointment
                return !appRs.next();
            }
            return false; // No schedule entry for this time
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
