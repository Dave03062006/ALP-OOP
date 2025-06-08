package model;
import java.util.*;
import logic.DatabaseConnect;
import logic.ScheduleBST;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

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
    public boolean login(String email, String password) {
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT * FROM patients WHERE email = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                this.patientId = rs.getInt("patient_id");
                this.email = rs.getString("email");
                this.password = rs.getString("password");
                this.fullName = rs.getString("full_name");
                this.phoneNumber = rs.getString("phone_number");
                System.out.println("✅ Login successful for patient: " + fullName);
                return true;
            } else {
                System.out.println("❌ Login failed. Invalid email or password.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("❌ Login error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean register(String email, String password, String fullName, String phoneNumber) {
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
                System.out.println("✅ Patient registered successfully with ID: " + this.patientId);
                return true;
            } else {
                System.out.println("❌ Registration failed. Please try again.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("❌ Registration error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
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
            String sql = "UPDATE patients SET full_name = ?, email = ?, phone_number = ? WHERE patient_id = ?";
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
            } else {
                System.out.println("❌ Update failed.");
            }
        } catch (Exception e) {
            System.out.println("❌ Update failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Rest of the Patient methods remain the same...
    public void bookAppointment() {
        System.out.println("\n--- Book Appointment ---");
        System.out.println("Please select a specialist:");
        System.out.println("1. General Practitioner");
        System.out.println("2. Cardiologist");
        System.out.println("3. Dermatologist");
        System.out.println("4. Neurologist");
        System.out.println("5. Back to Dashboard");
        
        int doctorChoice = 0;
        try {
            doctorChoice = s.nextInt();
            s.nextLine(); // Consume newline
        } catch (Exception e) {
            System.out.println("❌ Invalid input. Please enter a number.");
            s.nextLine();
            return;
        }

        if (doctorChoice == 5) {
            return; // Return to dashboard
        }

        String specialist = getSpecialistByChoice(doctorChoice);
        if (specialist.equals("Unknown")) {
            System.out.println("❌ Invalid specialist choice.");
            return;
        }

        // Show only ON-DUTY doctors for this specialist
        ArrayList<Doctor> availableDoctors = Doctor.getDoctorsBySpecialist(specialist);
        if (availableDoctors.isEmpty()) {
            System.out.println("❌ No " + specialist + " doctors are currently available (on-duty)");
            System.out.println("💡 Please try again later or select a different specialist.");
            return;
        }

        System.out.println("\n✅ Available " + specialist + " doctors (ON DUTY):");
        for (int i = 0; i < availableDoctors.size(); i++) {
            Doctor doc = availableDoctors.get(i);
            System.out.println((i + 1) + ". Dr. " + doc.getFullName() + " (ID: " + doc.getDoctorId() + ") 🟢");
        }

        System.out.print("Select doctor (1-" + availableDoctors.size() + "): ");
        int doctorIndex = 0;
        try {
            doctorIndex = s.nextInt() - 1;
            s.nextLine();
        } catch (Exception e) {
            System.out.println("❌ Invalid input.");
            s.nextLine();
            return;
        }

        if (doctorIndex < 0 || doctorIndex >= availableDoctors.size()) {
            System.out.println("❌ Invalid doctor selection.");
            return;
        }

        Doctor selectedDoctor = availableDoctors.get(doctorIndex);

        // Verify doctor is still on duty (real-time check)
        if (!isDoctorCurrentlyOnDuty(selectedDoctor.getDoctorId())) {
            System.out.println("❌ Sorry, Dr. " + selectedDoctor.getFullName() + " is no longer available.");
            System.out.println("💡 Please select another doctor or try again later.");
            return;
        }

        // Get appointment date
        System.out.print("\nEnter the date for your appointment (YYYY-MM-DD): ");
        String appointmentDate = s.nextLine();
        
        // Validate date format
        try {
            LocalDate.parse(appointmentDate);
        } catch (Exception e) {
            System.out.println("❌ Invalid date format. Please use YYYY-MM-DD format.");
            return;
        }

        // Get available time slots for this doctor and date
        ArrayList<String> availableSlots = selectedDoctor.getAvailableTimeSlotsForDate(appointmentDate);
        
        if (availableSlots.isEmpty()) {
            System.out.println("❌ No available time slots for Dr. " + selectedDoctor.getFullName() + " on " + appointmentDate);
            System.out.println("💡 Possible reasons:");
            System.out.println("   • Doctor hasn't set availability for this date");
            System.out.println("   • All time slots are already booked");
            System.out.println("   • Please try a different date");
            return;
        }

        // Display available time slots
        System.out.println("\n✅ Available time slots for Dr. " + selectedDoctor.getFullName() + " on " + appointmentDate + ":");
        System.out.println("=".repeat(60));
        
        displayAvailableTimeSlotsForBooking(availableSlots);
        
        // Let patient select a time slot
        System.out.print("\nSelect time slot number (1-" + availableSlots.size() + "): ");
        int timeChoice = 0;
        try {
            timeChoice = s.nextInt() - 1;
            s.nextLine(); // Consume newline
        } catch (Exception e) {
            System.out.println("❌ Invalid input.");
            s.nextLine();
            return;
        }
        
        if (timeChoice < 0 || timeChoice >= availableSlots.size()) {
            System.out.println("❌ Invalid time slot selection.");
            return;
        }
        
        String selectedTime = availableSlots.get(timeChoice);
        
        // Get symptoms/reason
        System.out.print("Enter your symptoms or reason for the appointment: ");
        String symptoms = s.nextLine();

        // Final verification before booking
        if (!isDoctorCurrentlyOnDuty(selectedDoctor.getDoctorId())) {
            System.out.println("❌ Sorry, Dr. " + selectedDoctor.getFullName() + " just went off duty.");
            System.out.println("💡 Please start the booking process again.");
            return;
        }

        // Confirm booking details
        System.out.println("\n=== APPOINTMENT CONFIRMATION ===");
        System.out.println("Doctor: Dr. " + selectedDoctor.getFullName() + " (" + selectedDoctor.getSpecialist() + ") 🟢");
        System.out.println("Date: " + appointmentDate);
        System.out.println("Time: " + selectedTime);
        System.out.println("Reason: " + symptoms);
        System.out.print("\nConfirm booking? (Y/N): ");
        
        String confirm = s.nextLine().toUpperCase();
        if (!confirm.equals("Y")) {
            System.out.println("❌ Booking cancelled.");
            return;
        }

        // Book the appointment
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, symptoms, appointment_date_time) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, this.getPatientId());
            pstmt.setInt(2, selectedDoctor.getDoctorId());
            pstmt.setString(3, appointmentDate);
            pstmt.setString(4, selectedTime);
            pstmt.setString(5, symptoms);
            pstmt.setString(6, appointmentDate + " " + selectedTime + ":00");

            int result = pstmt.executeUpdate();
            if (result > 0) {
                System.out.println("\n🎉 Appointment booked successfully!");
                System.out.println("📋 Appointment Details:");
                System.out.println("   Doctor: Dr. " + selectedDoctor.getFullName() + " 🟢");
                System.out.println("   Specialist: " + selectedDoctor.getSpecialist());
                System.out.println("   Date: " + appointmentDate);
                System.out.println("   Time: " + selectedTime);
                System.out.println("   Duration: 15 minutes");
                System.out.println("\n💡 Important Notes:");
                System.out.println("   • Please arrive 10 minutes early");
                System.out.println("   • Bring a valid ID");
                System.out.println("   • Your doctor is currently on duty ✅");
            } else {
                System.out.println("❌ Failed to book appointment.");
            }
        } catch (Exception e) {
            System.out.println("❌ Booking failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean isDoctorCurrentlyOnDuty(int doctorId) {
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT onDuty FROM doctors WHERE doctor_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, doctorId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getBoolean("onDuty");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error checking doctor status: " + e.getMessage());
        }
        return false;
    }

    private String getSpecialistByChoice(int choice) {
        switch (choice) {
            case 1: return "General Practitioner";
            case 2: return "Cardiologist";
            case 3: return "Dermatologist";
            case 4: return "Neurologist";
            default: return "Unknown";
        }
    }

    public void viewPatientAppointments() {
        System.out.println("\n--- Your Appointments ---");
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT a.*, d.full_name as doctor_name, d.specialist " +
                        "FROM appointments a " +
                        "JOIN doctors d ON a.doctor_id = d.doctor_id " +
                        "WHERE a.patient_id = ? " +
                        "ORDER BY a.appointment_date, a.appointment_time";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, this.getPatientId());
            ResultSet rs = pstmt.executeQuery();

            boolean hasAppointments = false;
            while (rs.next()) {
                hasAppointments = true;
                System.out.println("--- Appointment Details ---");
                System.out.println("Appointment ID: " + rs.getInt("appointment_id"));
                System.out.println("Doctor: Dr. " + rs.getString("doctor_name") + " (" + rs.getString("specialist") + ")");
                System.out.println("Date: " + rs.getString("appointment_date"));
                System.out.println("Time: " + rs.getString("appointment_time"));
                System.out.println("Symptoms: " + rs.getString("symptoms"));
                
                String diagnosis = rs.getString("diagnose_result");
                if (diagnosis != null && !diagnosis.trim().isEmpty()) {
                    System.out.println("Diagnosis: " + diagnosis);
                }
                
                System.out.println("-------------------");
            }

            if (!hasAppointments) {
                System.out.println("No appointments found.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error retrieving appointments: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void viewMedicalHistory() {
        System.out.println("\n--- Medical History ---");
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT a.*, d.full_name as doctor_name, d.specialist, p.recipe_description " +
                        "FROM appointments a " +
                        "JOIN doctors d ON a.doctor_id = d.doctor_id " +
                        "LEFT JOIN prescriptions p ON a.prescription_id = p.prescription_id " +
                        "WHERE a.patient_id = ? AND a.diagnose_result IS NOT NULL " +
                        "ORDER BY a.appointment_date DESC";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, this.getPatientId());
            ResultSet rs = pstmt.executeQuery();

            boolean hasHistory = false;
            while (rs.next()) {
                hasHistory = true;
                System.out.println("=== Medical Record ===");
                System.out.println("Date: " + rs.getString("appointment_date"));
                System.out.println("Doctor: Dr. " + rs.getString("doctor_name") + " (" + rs.getString("specialist") + ")");
                System.out.println("Symptoms: " + rs.getString("symptoms"));
                System.out.println("Diagnosis: " + rs.getString("diagnose_result"));
                
                String prescription = rs.getString("recipe_description");
                if (prescription != null && !prescription.trim().isEmpty()) {
                    System.out.println("Prescription: " + prescription);
                }
                
                System.out.println("==================");
            }

            if (!hasHistory) {
                System.out.println("No medical history found. Complete your appointments to build your medical history.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error retrieving medical history: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Static methods for patient management
    public static ArrayList<Patient> getAllPatients() {
        ArrayList<Patient> patients = new ArrayList<>();
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT * FROM patients ORDER BY full_name";
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
            String sql = "SELECT * FROM patients WHERE full_name LIKE ? ORDER BY full_name";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + fullName + "%");
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

    /**
     * Display available time slots in a formatted way for booking
     */
    private void displayAvailableTimeSlotsForBooking(ArrayList<String> availableSlots) {
        if (availableSlots.isEmpty()) {
            System.out.println("No available time slots found.");
            return;
        }
        
        System.out.println("Available Time Slots:");
        System.out.println("-".repeat(30));
        
        for (int i = 0; i < availableSlots.size(); i++) {
            String timeSlot = availableSlots.get(i);
            
            // Parse time to format it nicely
            try {
                LocalTime time = LocalTime.parse(timeSlot);
                String formattedTime = time.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
                
                // Calculate end time (15 minutes later)
                LocalTime endTime = time.plusMinutes(15);
                String formattedEndTime = endTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
                
                System.out.printf("%2d. %s - %s (15 min slot)\n", 
                    (i + 1), formattedTime, formattedEndTime);
            } catch (Exception e) {
                // Fallback if time parsing fails
                System.out.printf("%2d. %s\n", (i + 1), timeSlot);
            }
        }
        
        System.out.println("-".repeat(30));
        System.out.println("💡 Each appointment slot is 15 minutes");
    }
}