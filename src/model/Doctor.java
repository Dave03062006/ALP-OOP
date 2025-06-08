package model;

import java.sql.*;
import java.util.*;
import java.time.*;
import logic.DatabaseConnect;

public class Doctor extends Staff {
    protected int doctorId;
    protected String specialist;
    Scanner s = new Scanner(System.in);

    // Constructors
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

    // Getters and Setters
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
        System.out.println("\n=== DOCTOR PROFILE ===");
        System.out.println("Doctor ID: " + this.getDoctorId());
        System.out.println("Name: Dr. " + this.getFullName());
        System.out.println("Email: " + this.getEmail());
        System.out.println("Phone: " + this.getPhoneNumber());
        System.out.println("Specialist: " + this.getSpecialist());
        System.out.println("On Duty: " + (this.isOnDuty() ? "Yes" : "No"));
        System.out.println("=====================");
    }

    public void updateDoctorProfile() {
        System.out.println("\n--- UPDATE DOCTOR PROFILE ---");
        System.out.println("Current Name: " + this.getFullName());
        System.out.print("Enter new name (or press Enter to keep current): ");
        String fullName = s.nextLine();
        if (fullName.trim().isEmpty()) {
            fullName = this.getFullName();
        }

        System.out.println("Current Email: " + this.getEmail());
        System.out.print("Enter new email (or press Enter to keep current): ");
        String email = s.nextLine();
        if (email.trim().isEmpty()) {
            email = this.getEmail();
        }

        System.out.println("Current Phone Number: " + this.getPhoneNumber());
        System.out.print("Enter new phone number (or press Enter to keep current): ");
        String phoneNumber = s.nextLine();
        if (phoneNumber.trim().isEmpty()) {
            phoneNumber = this.getPhoneNumber();
        }

        System.out.println("Current Specialist: " + this.getSpecialist());
        System.out.print("Enter new specialist (or press Enter to keep current): ");
        String specialist = s.nextLine();
        if (specialist.trim().isEmpty()) {
            specialist = this.getSpecialist();
        }

        System.out.println("Current On Duty Status: " + this.isOnDuty());
        System.out.print("Are you on duty? (true/false, or press Enter to keep current): ");
        String onDutyStr = s.nextLine();
        boolean onDuty = this.isOnDuty();
        if (!onDutyStr.trim().isEmpty()) {
            onDuty = Boolean.parseBoolean(onDutyStr);
        }

        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "UPDATE doctors SET email = ?, full_name = ?, phone_number = ?, specialist = ?, onDuty = ? WHERE doctor_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, fullName);
            pstmt.setString(3, phoneNumber);
            pstmt.setString(4, specialist);
            pstmt.setBoolean(5, onDuty);
            pstmt.setInt(6, this.getDoctorId());

            int result = pstmt.executeUpdate();
            if (result > 0) {
                this.setEmail(email);
                this.setFullName(fullName);
                this.setPhoneNumber(phoneNumber);
                this.setSpecialist(specialist);
                this.setOnDuty(onDuty);
                System.out.println("✅ Profile updated successfully!");
            } else {
                System.out.println("❌ Failed to update profile.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Update failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

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
                this.password = rs.getString("password");
                this.fullName = rs.getString("full_name");
                this.phoneNumber = rs.getString("phone_number");
                this.specialist = rs.getString("specialist");
                this.onDuty = rs.getBoolean("onDuty");
                System.out.println("Doctor login successful!");
            } else {
                System.out.println("Invalid credentials!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void register(String email, String password, String fullName, String phoneNumber) {
        this.onDuty = true; // Default onDuty status for doctors
        register(email, password, fullName, phoneNumber, this.specialist, onDuty);
    }

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
        System.out.println("\n--- MY PATIENT APPOINTMENTS ---");
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT a.*, p.full_name as patient_name, p.phone_number as patient_phone " +
                    "FROM appointments a " +
                    "JOIN patients p ON a.patient_id = p.patient_id " +
                    "WHERE a.doctor_id = ? " +
                    "ORDER BY a.appointment_date, a.appointment_time";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, this.doctorId);
            ResultSet rs = pstmt.executeQuery();

            boolean hasAppointments = false;
            ArrayList<Integer> appointmentIds = new ArrayList<>();

            while (rs.next()) {
                hasAppointments = true;
                int appointmentId = rs.getInt("appointment_id");
                appointmentIds.add(appointmentId);

                System.out.println("--- Appointment #" + appointmentId + " ---");
                System.out.println("Patient: " + rs.getString("patient_name"));
                System.out.println("Phone: " + rs.getString("patient_phone"));
                System.out.println("Date: " + rs.getString("appointment_date"));
                System.out.println("Time: " + rs.getString("appointment_time"));
                System.out.println("Symptoms: " + rs.getString("symptoms"));

                String diagnosis = rs.getString("diagnose_result");
                if (diagnosis != null && !diagnosis.trim().isEmpty()) {
                    System.out.println("Diagnosis: " + diagnosis);
                    System.out.println("Status: Completed");
                } else {
                    System.out.println("Status: Pending Diagnosis");
                }
                System.out.println("-------------------------");
            }

            if (!hasAppointments) {
                System.out.println("No appointments found.");
                return;
            }

            // Allow doctor to select an appointment to diagnose
            System.out.println("\nSelect an appointment ID to diagnose (or 0 to return): ");
            try {
                int selectedId = s.nextInt();
                s.nextLine(); // Consume newline

                if (selectedId == 0) {
                    return;
                }

                if (appointmentIds.contains(selectedId)) {
                    diagnosePatient(selectedId);
                } else {
                    System.out.println("❌ Invalid appointment ID.");
                }
            } catch (Exception e) {
                System.out.println("❌ Invalid input.");
                s.nextLine();
            }

        } catch (SQLException e) {
            System.out.println("❌ Error retrieving appointments: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void diagnosePatient(int appointmentId) {
        try {
            Connection conn = DatabaseConnect.getConnection();

            // Get appointment details
            String getApptSql = "SELECT a.*, p.full_name as patient_name " +
                    "FROM appointments a " +
                    "JOIN patients p ON a.patient_id = p.patient_id " +
                    "WHERE a.appointment_id = ?";
            PreparedStatement getApptStmt = conn.prepareStatement(getApptSql);
            getApptStmt.setInt(1, appointmentId);
            ResultSet rs = getApptStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("❌ Appointment not found.");
                return;
            }

            String patientName = rs.getString("patient_name");
            String symptoms = rs.getString("symptoms");
            int patientId = rs.getInt("patient_id");

            System.out.println("\n--- DIAGNOSING PATIENT ---");
            System.out.println("Patient: " + patientName);
            System.out.println("Symptoms: " + symptoms);

            System.out.print("Enter your diagnosis: ");
            String diagnosis = s.nextLine();

            System.out.print("Do you want to create a prescription? (Y/N): ");
            String createPrescription = s.nextLine();

            // Update appointment with diagnosis
            String updateSql = "UPDATE appointments SET diagnose_result = ? WHERE appointment_id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateSql);
            updateStmt.setString(1, diagnosis);
            updateStmt.setInt(2, appointmentId);
            updateStmt.executeUpdate();

            System.out.println("✅ Diagnosis saved successfully!");

            if (createPrescription.equalsIgnoreCase("Y")) {
                createPrescription(appointmentId, patientId, diagnosis);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error during diagnosis: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createPrescription(int appointmentId, int patientId, String diagnosis) {
        try {
            System.out.println("\n--- CREATING PRESCRIPTION ---");
            System.out.print("Enter prescription details/medications: ");
            String prescriptionDetails = s.nextLine();

            Connection conn = DatabaseConnect.getConnection();

            // Create prescription
            String prescSql = "INSERT INTO prescriptions (doctor_id, patient_id, recipe_description, status) VALUES (?, ?, ?, 'PENDING')";
            PreparedStatement prescStmt = conn.prepareStatement(prescSql, Statement.RETURN_GENERATED_KEYS);
            prescStmt.setInt(1, this.doctorId);
            prescStmt.setInt(2, patientId);
            prescStmt.setString(3, prescriptionDetails);

            int result = prescStmt.executeUpdate();
            if (result > 0) {
                ResultSet generatedKeys = prescStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int prescriptionId = generatedKeys.getInt(1);

                    // Update appointment with prescription ID
                    String updateApptSql = "UPDATE appointments SET prescription_id = ? WHERE appointment_id = ?";
                    PreparedStatement updateApptStmt = conn.prepareStatement(updateApptSql);
                    updateApptStmt.setInt(1, prescriptionId);
                    updateApptStmt.setInt(2, appointmentId);
                    updateApptStmt.executeUpdate();

                    System.out.println("✅ Prescription created successfully! Prescription ID: " + prescriptionId);

                    // Ask if doctor wants to add specific medicines
                    System.out.print("Do you want to add specific medicines to this prescription? (Y/N): ");
                    String addMedicines = s.nextLine();

                    if (addMedicines.equalsIgnoreCase("Y")) {
                        addMedicinesToPrescription(prescriptionId, patientId);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Error creating prescription: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addMedicinesToPrescription(int prescriptionId, int patientId) {
        try {
            Connection conn = DatabaseConnect.getConnection();

            // Show available medicines
            String medicinesSql = "SELECT * FROM medicines WHERE prescription_id IS NULL OR prescription_id = 0 ORDER BY medicine_name";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(medicinesSql);

            ArrayList<Integer> availableMedicineIds = new ArrayList<>();
            System.out.println("\n--- AVAILABLE MEDICINES ---");

            boolean hasMedicines = false;
            while (rs.next()) {
                hasMedicines = true;
                int medicineId = rs.getInt("medicine_id");
                availableMedicineIds.add(medicineId);
                System.out.println(medicineId + ". " + rs.getString("medicine_name") +
                        " - " + rs.getString("dose") +
                        " (" + rs.getString("category") + ")");
            }

            if (!hasMedicines) {
                System.out.println("No available medicines in inventory.");
                return;
            }

            System.out.println("\nEnter medicine IDs to prescribe (comma-separated, e.g., 1,3,5): ");
            String medicineInput = s.nextLine();

            String[] medicineIdStrings = medicineInput.split(",");
            for (String idStr : medicineIdStrings) {
                try {
                    int medicineId = Integer.parseInt(idStr.trim());
                    if (availableMedicineIds.contains(medicineId)) {
                        // Assign medicine to prescription
                        String updateMedSql = "UPDATE medicines SET prescription_id = ?, patient_id = ? WHERE medicine_id = ?";
                        PreparedStatement updateMedStmt = conn.prepareStatement(updateMedSql);
                        updateMedStmt.setInt(1, prescriptionId);
                        updateMedStmt.setInt(2, patientId);
                        updateMedStmt.setInt(3, medicineId);
                        updateMedStmt.executeUpdate();

                        System.out.println("✅ Medicine ID " + medicineId + " added to prescription.");
                    } else {
                        System.out.println("❌ Invalid medicine ID: " + medicineId);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("❌ Invalid medicine ID format: " + idStr);
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Error adding medicines: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Updated Doctor.java - Add this method to replace the commented
    // manageWeeklySchedule method

    public void manageWeeklySchedule() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("     DOCTOR AVAILABILITY SCHEDULE");
        System.out.println("=".repeat(50));

        while (true) {
            System.out.println("\n1. Set Availability for a Date");
            System.out.println("2. View My Schedule");
            System.out.println("3. Update Existing Schedule");
            System.out.println("4. Delete Schedule");
            System.out.println("5. Back to Dashboard");
            System.out.print("Enter your choice: ");

            int choice = 0;
            try {
                choice = s.nextInt();
                s.nextLine(); // Consume newline
            } catch (Exception e) {
                System.out.println("❌ Invalid input. Please enter a number.");
                s.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    setAvailability();
                    break;
                case 2:
                    viewMySchedule();
                    break;
                case 3:
                    updateSchedule();
                    break;
                case 4:
                    deleteSchedule();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("❌ Invalid choice. Please try again.");
            }
        }
    }

    private void setAvailability() {
        System.out.println("\n--- SET AVAILABILITY ---");

        // Get date input
        String date = getValidDateInput();
        if (date == null)
            return;

        // Check if schedule already exists for this date
        if (scheduleExistsForDate(date)) {
            System.out.println("⚠️ You already have a schedule for " + date);
            System.out.print("Do you want to update it instead? (Y/N): ");
            String response = s.nextLine();
            if (response.equalsIgnoreCase("Y")) {
                updateScheduleForDate(date);
            }
            return;
        }

        // Get time inputs
        String startTime = getValidTimeInput("start");
        if (startTime == null)
            return;

        String endTime = getValidTimeInput("end");
        if (endTime == null)
            return;

        // Validate time range
        if (!isValidTimeRange(startTime, endTime)) {
            System.out.println("❌ End time must be after start time.");
            return;
        }

        // Save to database
        if (saveAvailabilityToDatabase(date, startTime, endTime)) {
            System.out.println("✅ Availability set successfully!");
            System.out.println("Date: " + date);
            System.out.println("Time: " + startTime + " - " + endTime);
        } else {
            System.out.println("❌ Failed to save availability.");
        }
    }

    private String getValidDateInput() {
        while (true) {
            System.out.print("Enter date (YYYY-MM-DD): ");
            String dateStr = s.nextLine().trim();

            if (dateStr.isEmpty()) {
                System.out.println("❌ Date cannot be empty.");
                continue;
            }

            try {
                LocalDate date = LocalDate.parse(dateStr);

                // Check if date is in the past
                if (date.isBefore(LocalDate.now())) {
                    System.out.println("❌ Cannot set availability for past dates.");
                    continue;
                }

                // Check if date is too far in future (optional: limit to 3 months)
                if (date.isAfter(LocalDate.now().plusMonths(3))) {
                    System.out.println("❌ Cannot set availability more than 3 months in advance.");
                    continue;
                }

                return dateStr;
            } catch (Exception e) {
                System.out.println("❌ Invalid date format. Please use YYYY-MM-DD format.");
            }
        }
    }

    private String getValidTimeInput(String timeType) {
        while (true) {
            System.out.print("Enter " + timeType + " time (HH:mm, 24-hour format, 15-minute intervals): ");
            String timeStr = s.nextLine().trim();

            if (timeStr.isEmpty()) {
                System.out.println("❌ Time cannot be empty.");
                continue;
            }

            try {
                LocalTime time = LocalTime.parse(timeStr);

                // Check if time is in 15-minute intervals
                if (time.getMinute() % 15 != 0) {
                    System.out.println("❌ Time must be in 15-minute intervals (00, 15, 30, 45).");
                    System.out.println("Examples: 09:00, 09:15, 09:30, 09:45");
                    continue;
                }

                // Optional: Check reasonable working hours (6 AM to 10 PM)
                if (time.isBefore(LocalTime.of(6, 0)) || time.isAfter(LocalTime.of(22, 0))) {
                    System.out.println("⚠️ Time is outside typical working hours (06:00 - 22:00).");
                    System.out.print("Do you want to continue? (Y/N): ");
                    String response = s.nextLine();
                    if (!response.equalsIgnoreCase("Y")) {
                        continue;
                    }
                }

                return timeStr;
            } catch (Exception e) {
                System.out.println("❌ Invalid time format. Please use HH:mm format (e.g., 09:30).");
            }
        }
    }

    private boolean isValidTimeRange(String startTime, String endTime) {
        try {
            LocalTime start = LocalTime.parse(startTime);
            LocalTime end = LocalTime.parse(endTime);

            if (!end.isAfter(start)) {
                return false;
            }

            // Check minimum duration (at least 30 minutes)
            Duration duration = Duration.between(start, end);
            if (duration.toMinutes() < 30) {
                System.out.println("❌ Availability duration must be at least 30 minutes.");
                return false;
            }

            // Check maximum duration (at most 12 hours)
            if (duration.toHours() > 12) {
                System.out.println("❌ Availability duration cannot exceed 12 hours.");
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean scheduleExistsForDate(String date) {
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT COUNT(*) FROM doctor_availability WHERE doctor_id = ? AND availability_date = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, this.doctorId);
            pstmt.setString(2, date);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("❌ Error checking existing schedule: " + e.getMessage());
        }
        return false;
    }

    private boolean saveAvailabilityToDatabase(String date, String startTime, String endTime) {
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "INSERT INTO doctor_availability (doctor_id, availability_date, start_time, end_time) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, this.doctorId);
            pstmt.setString(2, date);
            pstmt.setString(3, startTime);
            pstmt.setString(4, endTime);

            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error saving availability: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void viewMySchedule() {
        System.out.println("\n--- MY AVAILABILITY SCHEDULE ---");

        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT * FROM doctor_availability WHERE doctor_id = ? AND availability_date >= CURRENT_DATE ORDER BY availability_date, start_time";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, this.doctorId);

            ResultSet rs = pstmt.executeQuery();

            boolean hasSchedule = false;
            System.out.printf("%-15s %-10s %-10s %-10s\n", "Date", "Start Time", "End Time", "Duration");
            System.out.println("-".repeat(50));

            while (rs.next()) {
                hasSchedule = true;
                String date = rs.getString("availability_date");
                String startTime = rs.getString("start_time");
                String endTime = rs.getString("end_time");

                // Calculate duration
                LocalTime start = LocalTime.parse(startTime);
                LocalTime end = LocalTime.parse(endTime);
                Duration duration = Duration.between(start, end);
                String durationStr = String.format("%dh %dm", duration.toHours(), duration.toMinutes() % 60);

                System.out.printf("%-15s %-10s %-10s %-10s\n", date, startTime, endTime, durationStr);
            }

            if (!hasSchedule) {
                System.out.println("No upcoming availability schedule found.");
            }

            System.out.print("\nPress Enter to continue...");
            s.nextLine();

        } catch (SQLException e) {
            System.out.println("❌ Error retrieving schedule: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateSchedule() {
        System.out.println("\n--- UPDATE SCHEDULE ---");

        // Show existing schedules
        viewMySchedule();

        String date = getValidDateInput();
        if (date == null)
            return;

        if (!scheduleExistsForDate(date)) {
            System.out.println("❌ No schedule found for " + date);
            return;
        }

        updateScheduleForDate(date);
    }

    private void updateScheduleForDate(String date) {
        try {
            Connection conn = DatabaseConnect.getConnection();

            // Show current schedule for this date
            String getCurrentSql = "SELECT * FROM doctor_availability WHERE doctor_id = ? AND availability_date = ?";
            PreparedStatement getCurrentStmt = conn.prepareStatement(getCurrentSql);
            getCurrentStmt.setInt(1, this.doctorId);
            getCurrentStmt.setString(2, date);

            ResultSet rs = getCurrentStmt.executeQuery();
            if (rs.next()) {
                String currentStart = rs.getString("start_time");
                String currentEnd = rs.getString("end_time");

                System.out.println("Current schedule for " + date + ": " + currentStart + " - " + currentEnd);
            }

            String startTime = getValidTimeInput("new start");
            if (startTime == null)
                return;

            String endTime = getValidTimeInput("new end");
            if (endTime == null)
                return;

            if (!isValidTimeRange(startTime, endTime)) {
                System.out.println("❌ End time must be after start time.");
                return;
            }

            // Update the schedule
            String updateSql = "UPDATE doctor_availability SET start_time = ?, end_time = ? WHERE doctor_id = ? AND availability_date = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateSql);
            updateStmt.setString(1, startTime);
            updateStmt.setString(2, endTime);
            updateStmt.setInt(3, this.doctorId);
            updateStmt.setString(4, date);

            int result = updateStmt.executeUpdate();
            if (result > 0) {
                System.out.println("✅ Schedule updated successfully!");
                System.out.println("Date: " + date);
                System.out.println("New time: " + startTime + " - " + endTime);
            } else {
                System.out.println("❌ Failed to update schedule.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Error updating schedule: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void deleteSchedule() {
        System.out.println("\n--- DELETE SCHEDULE ---");

        // Show existing schedules
        viewMySchedule();

        String date = getValidDateInput();
        if (date == null)
            return;

        if (!scheduleExistsForDate(date)) {
            System.out.println("❌ No schedule found for " + date);
            return;
        }

        System.out.print("Are you sure you want to delete the schedule for " + date + "? (Y/N): ");
        String confirm = s.nextLine();

        if (!confirm.equalsIgnoreCase("Y")) {
            System.out.println("Operation cancelled.");
            return;
        }

        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "DELETE FROM doctor_availability WHERE doctor_id = ? AND availability_date = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, this.doctorId);
            pstmt.setString(2, date);

            int result = pstmt.executeUpdate();
            if (result > 0) {
                System.out.println("✅ Schedule deleted successfully!");
            } else {
                System.out.println("❌ Failed to delete schedule.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Error deleting schedule: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper method to check if doctor is available at specific date and time
    public boolean isAvailableAt(String date, String time) {
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT * FROM doctor_availability WHERE doctor_id = ? AND availability_date = ? AND start_time <= ? AND end_time > ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, this.doctorId);
            pstmt.setString(2, date);
            pstmt.setString(3, time);
            pstmt.setString(4, time);

            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("❌ Error checking availability: " + e.getMessage());
            return false;
        }
    }

    // Static method to get available doctors for a specific date and time
    public static ArrayList<Doctor> getAvailableDoctors(String date, String time, String specialist) {
        ArrayList<Doctor> availableDoctors = new ArrayList<>();
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT d.* FROM doctors d " +
                    "JOIN doctor_availability da ON d.doctor_id = da.doctor_id " +
                    "WHERE d.specialist = ? AND d.onDuty = true " +
                    "AND da.availability_date = ? AND da.start_time <= ? AND da.end_time > ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, specialist);
            pstmt.setString(2, date);
            pstmt.setString(3, time);
            pstmt.setString(4, time);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Doctor doctor = new Doctor(
                        rs.getInt("doctor_id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getString("phone_number"),
                        rs.getBoolean("onDuty"),
                        rs.getString("specialist"));
                availableDoctors.add(doctor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return availableDoctors;
    }

    private void removeWorkHours(String dayOfWeek) {
        // Remove schedule for the day
        System.out.println("Removed schedule for " + dayOfWeek);
    }

    // Static methods for doctor management
    public static ArrayList<Doctor> getAllDoctors() {
        ArrayList<Doctor> doctors = new ArrayList<>();
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT * FROM doctors ORDER BY full_name";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Doctor doctor = new Doctor(
                        rs.getInt("doctor_id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getString("phone_number"),
                        rs.getBoolean("onDuty"),
                        rs.getString("specialist"));
                doctors.add(doctor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    public static ArrayList<Doctor> getDoctorsBySpecialist(String specialist) {
        ArrayList<Doctor> doctors = new ArrayList<>();
        try {
            Connection conn = DatabaseConnect.getConnection();
            // Modified query to only get doctors who are on duty
            String sql = "SELECT * FROM doctors WHERE specialist = ? AND onDuty = true ORDER BY full_name";
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
                        rs.getBoolean("onDuty"),
                        rs.getString("specialist"));
                doctors.add(doctor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    public boolean isAvailableAt(String dayOfWeek, LocalTime time) {
        // Simplified availability check
        // In a real application, you'd check against a schedule table
        return this.isOnDuty();
    }

    public void toggleOnDutyStatus() {
        System.out.println("\n--- TOGGLE ON-DUTY STATUS ---");
        System.out.println("Current Status: " + (this.isOnDuty() ? "ON DUTY ✅" : "OFF DUTY ❌"));

        String newStatus = this.isOnDuty() ? "OFF DUTY" : "ON DUTY";
        String emoji = this.isOnDuty() ? "❌" : "✅";

        System.out.println("You are about to change your status to: " + newStatus + " " + emoji);

        if (!this.isOnDuty()) {
            System.out.println("\n💡 Note: Setting yourself ON DUTY will:");
            System.out.println("   • Make you visible to patients for appointment booking");
            System.out.println("   • Allow you to accept new appointments");
            System.out.println("   • Show you as available in the doctor directory");
        } else {
            System.out.println("\n⚠️ Warning: Setting yourself OFF DUTY will:");
            System.out.println("   • Hide you from new patient bookings");
            System.out.println("   • Prevent new appointment requests");
            System.out.println("   • Keep existing appointments unchanged");

            // Check if doctor has upcoming appointments
            if (hasUpcomingAppointments()) {
                System.out.println("   • You have upcoming appointments that will remain scheduled");
            }
        }

        System.out.print("\nDo you want to proceed with this change? (Y/N): ");
        String confirm = s.nextLine().toUpperCase();

        if (confirm.equals("Y")) {
            boolean newOnDutyStatus = !this.isOnDuty();

            if (updateOnDutyStatusInDatabase(newOnDutyStatus)) {
                this.setOnDuty(newOnDutyStatus);
                System.out.println("\n✅ Status updated successfully!");
                System.out.println("Your new status: " + (newOnDutyStatus ? "ON DUTY ✅" : "OFF DUTY ❌"));

                if (newOnDutyStatus) {
                    System.out.println("🎉 You are now available for new patient appointments!");
                } else {
                    System.out.println("💤 You are now unavailable for new appointments.");
                    System.out.println("💡 You can turn ON DUTY anytime from this menu.");
                }
            } else {
                System.out.println("❌ Failed to update status. Please try again.");
            }
        } else {
            System.out.println("❌ Status change cancelled.");
        }

        System.out.print("\nPress Enter to continue...");
        s.nextLine();
    }

    // Helper method to update on-duty status in database
    private boolean updateOnDutyStatusInDatabase(boolean newStatus) {
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "UPDATE doctors SET onDuty = ? WHERE doctor_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setBoolean(1, newStatus);
            pstmt.setInt(2, this.doctorId);

            int result = pstmt.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            System.out.println("❌ Database error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Helper method to check if doctor has upcoming appointments
    private boolean hasUpcomingAppointments() {
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date >= CURDATE()";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, this.doctorId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("❌ Error checking appointments: " + e.getMessage());
        }
        return false;
    }

    // Enhanced method to view doctor's current status with more details
    public void viewDetailedStatus() {
        System.out.println("\n--- MY DETAILED STATUS ---");
        System.out.println("=".repeat(40));

        // Basic info
        System.out.println("👨‍⚕️ Doctor: Dr. " + this.getFullName());
        System.out.println("🏥 Specialist: " + this.getSpecialist());
        System.out.println("📧 Email: " + this.getEmail());
        System.out.println("📞 Phone: " + this.getPhoneNumber());

        // On-duty status with visual indicator
        String statusIcon = this.isOnDuty() ? "✅" : "❌";
        String statusText = this.isOnDuty() ? "ON DUTY" : "OFF DUTY";
        String statusColor = this.isOnDuty() ? "🟢" : "🔴";

        System.out.println("\n📋 CURRENT STATUS:");
        System.out.println("   Status: " + statusText + " " + statusIcon);
        System.out.println("   Indicator: " + statusColor);

        // Get statistics
        try {
            Connection conn = DatabaseConnect.getConnection();

            // Count total appointments
            String totalSql = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ?";
            PreparedStatement totalStmt = conn.prepareStatement(totalSql);
            totalStmt.setInt(1, this.doctorId);
            ResultSet totalRs = totalStmt.executeQuery();
            int totalAppointments = totalRs.next() ? totalRs.getInt(1) : 0;

            // Count upcoming appointments
            String upcomingSql = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date >= CURDATE()";
            PreparedStatement upcomingStmt = conn.prepareStatement(upcomingSql);
            upcomingStmt.setInt(1, this.doctorId);
            ResultSet upcomingRs = upcomingStmt.executeQuery();
            int upcomingAppointments = upcomingRs.next() ? upcomingRs.getInt(1) : 0;

            // Count availability dates
            String availabilitySql = "SELECT COUNT(*) FROM doctor_availability WHERE doctor_id = ? AND availability_date >= CURDATE()";
            PreparedStatement availabilityStmt = conn.prepareStatement(availabilitySql);
            availabilityStmt.setInt(1, this.doctorId);
            ResultSet availabilityRs = availabilityStmt.executeQuery();
            int availabilityDates = availabilityRs.next() ? availabilityRs.getInt(1) : 0;

            System.out.println("\n📊 STATISTICS:");
            System.out.println("   Total Appointments: " + totalAppointments);
            System.out.println("   Upcoming Appointments: " + upcomingAppointments);
            System.out.println("   Available Dates Set: " + availabilityDates);

            // Show impact of current status
            System.out.println("\n💼 STATUS IMPACT:");
            if (this.isOnDuty()) {
                System.out.println("   ✅ Visible to patients for booking");
                System.out.println("   ✅ Can accept new appointments");
                System.out.println("   ✅ Listed in doctor directory");
            } else {
                System.out.println("   ❌ Hidden from new patient bookings");
                System.out.println("   ❌ Cannot accept new appointments");
                System.out.println("   ✅ Existing appointments remain active");
            }

        } catch (SQLException e) {
            System.out.println("❌ Error retrieving statistics: " + e.getMessage());
        }

        System.out.println("=".repeat(40));
        System.out.print("Press Enter to continue...");
        s.nextLine();
    }

    // Add these methods to the Doctor.java class

/**
 * Get available time slots for a specific doctor on a given date
 */
public ArrayList<String> getAvailableTimeSlotsForDate(String date) {
    ArrayList<String> availableSlots = new ArrayList<>();
    
    try {
        Connection conn = DatabaseConnect.getConnection();
        
        // Get doctor's availability for this date
        String availabilitySql = "SELECT start_time, end_time FROM doctor_availability " +
                               "WHERE doctor_id = ? AND availability_date = ?";
        PreparedStatement availabilityStmt = conn.prepareStatement(availabilitySql);
        availabilityStmt.setInt(1, this.doctorId);
        availabilityStmt.setString(2, date);
        ResultSet availabilityRs = availabilityStmt.executeQuery();
        
        while (availabilityRs.next()) {
            String startTime = availabilityRs.getString("start_time");
            String endTime = availabilityRs.getString("end_time");
            
            // Generate 15-minute time slots
            LocalTime start = LocalTime.parse(startTime);
            LocalTime end = LocalTime.parse(endTime);
            
            LocalTime current = start;
            while (current.isBefore(end)) {
                String timeSlot = current.toString();
                
                // Check if this time slot is not already booked
                if (isTimeSlotAvailableForDoctor(date, timeSlot)) {
                    availableSlots.add(timeSlot);
                }
                
                current = current.plusMinutes(15);
            }
        }
        
    } catch (SQLException e) {
        System.out.println("❌ Error getting available time slots: " + e.getMessage());
        e.printStackTrace();
    }
    
    return availableSlots;
}

/**
 * Check if a specific time slot is available for this doctor
 */
private boolean isTimeSlotAvailableForDoctor(String date, String time) {
    try {
        Connection conn = DatabaseConnect.getConnection();
        String sql = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ? AND appointment_time = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, this.doctorId);
        pstmt.setString(2, date);
        pstmt.setString(3, time);
        
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) == 0; // Return true if no appointments found
        }
        
    } catch (SQLException e) {
        System.out.println("❌ Error checking time slot availability: " + e.getMessage());
    }
    
    return false;
}

/**
 * Set doctor availability for a specific date
 */
public void setDoctorAvailability() {
    System.out.println("\n--- SET DOCTOR AVAILABILITY ---");
    
    String date = getValidDateInput();
    if (date == null) return;
    
    // Check if availability already exists for this date
    if (scheduleExistsForDate(date)) {
        System.out.println("⚠️ You already have availability set for " + date);
        System.out.print("Do you want to update it? (Y/N): ");
        String response = s.nextLine();
        if (response.equalsIgnoreCase("Y")) {
            updateScheduleForDate(date);
        }
        return;
    }
    
    String startTime = getValidTimeInput("start");
    if (startTime == null) return;
    
    String endTime = getValidTimeInput("end");
    if (endTime == null) return;
    
    if (!isValidTimeRange(startTime, endTime)) {
        System.out.println("❌ End time must be after start time.");
        return;
    }
    
    if (saveAvailabilityToDatabase(date, startTime, endTime)) {
        System.out.println("✅ Availability set successfully!");
        System.out.println("Date: " + date);
        System.out.println("Time: " + startTime + " - " + endTime);
    } else {
        System.out.println("❌ Failed to save availability.");
    }
}

/**
 * View doctor's availability schedule
 */
public void viewMyAvailabilitySchedule() {
    System.out.println("\n--- MY AVAILABILITY SCHEDULE ---");
    
    try {
        Connection conn = DatabaseConnect.getConnection();
        String sql = "SELECT * FROM doctor_availability WHERE doctor_id = ? AND availability_date >= CURRENT_DATE ORDER BY availability_date, start_time";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, this.doctorId);
        
        ResultSet rs = pstmt.executeQuery();
        
        boolean hasSchedule = false;
        System.out.printf("%-15s %-10s %-10s %-10s %-15s\n", "Date", "Start Time", "End Time", "Duration", "Status");
        System.out.println("-".repeat(70));
        
        while (rs.next()) {
            hasSchedule = true;
            String date = rs.getString("availability_date");
            String startTime = rs.getString("start_time");
            String endTime = rs.getString("end_time");
            
            // Calculate duration
            LocalTime start = LocalTime.parse(startTime);
            LocalTime end = LocalTime.parse(endTime);
            Duration duration = Duration.between(start, end);
            String durationStr = String.format("%dh %dm", duration.toHours(), duration.toMinutes() % 60);
            
            // Check if date has any appointments
            String statusStr = hasAppointmentsOnDate(date) ? "Has Bookings" : "Available";
            
            System.out.printf("%-15s %-10s %-10s %-10s %-15s\n", date, startTime, endTime, durationStr, statusStr);
        }
        
        if (!hasSchedule) {
            System.out.println("No upcoming availability schedule found.");
            System.out.println("💡 Use 'Set My Availability' to add available dates and times.");
        }
        
        System.out.print("\nPress Enter to continue...");
        s.nextLine();
        
    } catch (SQLException e) {
        System.out.println("❌ Error retrieving schedule: " + e.getMessage());
        e.printStackTrace();
    }
}

/**
 * Check if doctor has appointments on a specific date
 */
private boolean hasAppointmentsOnDate(String date) {
    try {
        Connection conn = DatabaseConnect.getConnection();
        String sql = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, this.doctorId);
        pstmt.setString(2, date);
        
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    } catch (SQLException e) {
        System.out.println("❌ Error checking appointments: " + e.getMessage());
    }
    return false;
}
}