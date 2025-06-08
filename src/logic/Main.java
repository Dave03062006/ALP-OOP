package logic;

import java.util.Scanner;
import java.util.ArrayList;
import model.Doctor;
import model.Patient;
import model.Pharmacist;
import model.Medicine;
import java.sql.*;
import java.time.LocalDate;

public class Main {

    Scanner s = new Scanner(System.in);
    Patient currentPatient = null;
    Doctor currentDoctor = null;
    Pharmacist currentPharmacist = null;

    public static void main(String[] args) {
        Main mainApp = new Main();
        mainApp.menu();
    }

    public void menu() {
        logo();
        while (true) {
            System.out.println(
                    "Welcome to the application -- your comprehensive healthcare management platform!\nThis system connects patients, doctors, and pharmacists in one integrated environment. As a patient, you can easily register, with specialized doctors, track your medical history, and manage your healthcare journey. Doctors can efficiently manage their schedules, view patient appointments, provide diagnoses, and prescribe medications. Pharmacists can process prescriptions, manage medicine inventory, and track prescription status from pending to completion. Whether you're seeking medical care, providing treatment, or dispensing medications, our system streamlines healthcare operations with secure login, intuitive menus, and real-time database integration. Please select your role below to begin your healthcare experience.");
            System.out.println("----Please select your role to register----");
            System.out.println(
                    "1. Register as Patient 🤕\n2. Register as Doctor 🩺\n3. Register as Pharmacist 💊\n4. Already registered? Login\n5. Add Sample Medicines (Admin)\n6. Exit\nEnter your choice: ");

            int choice = 0;
            try {
                choice = s.nextInt();
                s.nextLine(); // Consume the newline
            } catch (Exception e) {
                System.out.println("❌ Invalid input. Please enter a number.");
                s.nextLine(); // Clear invalid input
                continue;
            }

            switch (choice) {
                case 1:
                    registerPatient();
                    break;
                case 2:
                    registerDoctor();
                    break;
                case 3:
                    registerPharmacist();
                    break;
                case 4:
                    System.out.println("Redirecting to login...");
                    loginGeneral();
                    break;
                case 5:
                    Medicine.addSampleMedicines();
                    break;
                case 6:
                    System.out.println("Thank you for using the Medical System. Goodbye!");
                    closeScanner();
                    System.exit(0);
                    break;
                default:
                    System.out.println("❌ Invalid choice. Please try again.");
                    break;
            }
        }
    }

    // REGISTRATION METHODS
    public void registerPatient() {
        System.out.println("Registering as Patient...");
        System.out.println("\n------ 🤕 Patient Registration 🤕 ------");

        System.out.print("Enter your full name 🪪: ");
        String fullName = s.nextLine();

        System.out.print("Enter your email 📧: ");
        String email = s.nextLine();

        System.out.print("Enter your phone number 📞: ");
        String phoneNumber = s.nextLine();

        System.out.print("Create a password 🔒: ");
        String password = s.nextLine();

        System.out.print("Confirm your password 🔐: ");
        String confirmPassword = s.nextLine();

        if (!password.equals(confirmPassword)) {
            System.out.println("❌ Passwords do not match. Registration failed.");
            return;
        }

        if (fullName.trim().isEmpty() || email.trim().isEmpty() || password.trim().isEmpty()) {
            System.out.println("❌ All fields are required. Registration failed.");
            return;
        }

        Patient newPatient = new Patient(email, password, fullName, phoneNumber);
        newPatient.register(email, password, fullName, phoneNumber);
    }

    public void registerDoctor() {
        System.out.println("Registering as Doctor...");
        System.out.println("\n------ 🧑‍⚕️ Doctor Registration 🧑‍⚕️ ------");

        System.out.print("Enter your full name 🪪: ");
        String fullName = s.nextLine();

        System.out.print("Enter your email 📧: ");
        String email = s.nextLine();

        System.out.print("Enter your phone number 📞: ");
        String phoneNumber = s.nextLine();

        System.out.println("Select your specialist area:");
        System.out.println("1. General Practitioner");
        System.out.println("2. Cardiologist");
        System.out.println("3. Dermatologist");
        System.out.println("4. Neurologist");
        System.out.println("5. Other (custom)");
        System.out.print("Enter choice (1-5): ");

        String specialist = "";
        try {
            int specChoice = s.nextInt();
            s.nextLine(); // Consume newline

            switch (specChoice) {
                case 1:
                    specialist = "General Practitioner";
                    break;
                case 2:
                    specialist = "Cardiologist";
                    break;
                case 3:
                    specialist = "Dermatologist";
                    break;
                case 4:
                    specialist = "Neurologist";
                    break;
                case 5:
                    System.out.print("Enter your specialist area: ");
                    specialist = s.nextLine();
                    break;
                default:
                    System.out.println("❌ Invalid choice. Using General Practitioner.");
                    specialist = "General Practitioner";
                    break;
            }
        } catch (Exception e) {
            System.out.println("❌ Invalid input. Using General Practitioner.");
            specialist = "General Practitioner";
            s.nextLine(); // Clear invalid input
        }

        System.out.print("Create a password 🔒: ");
        String password = s.nextLine();

        System.out.print("Confirm your password 🔐: ");
        String confirmPassword = s.nextLine();

        if (!password.equals(confirmPassword)) {
            System.out.println("❌ Passwords do not match. Registration failed.");
            return;
        }

        if (fullName.trim().isEmpty() || email.trim().isEmpty() || password.trim().isEmpty()) {
            System.out.println("❌ All fields are required. Registration failed.");
            return;
        }

        Doctor newDoctor = new Doctor(email, password, fullName, phoneNumber, true, specialist);
        newDoctor.register(email, password, fullName, phoneNumber, specialist, true);
    }

    public void registerPharmacist() {
        System.out.println("Registering as Pharmacist...");
        System.out.println("\n------ 💊 Pharmacist Registration 💊 ------");

        System.out.print("Enter your full name 🪪: ");
        String fullName = s.nextLine();

        System.out.print("Enter your email 📧: ");
        String email = s.nextLine();

        System.out.print("Enter your phone number 📞: ");
        String phoneNumber = s.nextLine();

        System.out.print("Create a password 🔒: ");
        String password = s.nextLine();

        System.out.print("Confirm your password 🔐: ");
        String confirmPassword = s.nextLine();

        if (!password.equals(confirmPassword)) {
            System.out.println("❌ Passwords do not match. Registration failed.");
            return;
        }

        if (fullName.trim().isEmpty() || email.trim().isEmpty() || password.trim().isEmpty()) {
            System.out.println("❌ All fields are required. Registration failed.");
            return;
        }

        Pharmacist newPharmacist = new Pharmacist(email, password, fullName, phoneNumber, true);
        newPharmacist.register(email, password, fullName, phoneNumber);
    }

    // LOGIN METHODS
    public void loginGeneral() {
        System.out.println("Logging in...");
        System.out.println("What is your role?");
        System.out.println("1. Patient 🤕");
        System.out.println("2. Doctor 🩺");
        System.out.println("3. Pharmacist 💊");
        System.out.println("4. Back to Registration Menu ⬅️");
        System.out.print("Enter your choice: ");

        int choice = 0;
        try {
            choice = s.nextInt();
            s.nextLine(); // Consume newline
        } catch (Exception e) {
            System.out.println("❌ Invalid input. Please enter a number.");
            s.nextLine();
            return;
        }

        switch (choice) {
            case 1:
                if (loginPatient()) {
                    patientDashboard();
                }
                break;
            case 2:
                if (loginDoctor()) {
                    doctorDashboard();
                }
                break;
            case 3:
                if (loginPharmacist()) {
                    pharmacistDashboard();
                }
                break;
            case 4:
                return; // Back to main menu
            default:
                System.out.println("❌ Invalid choice. Please try again.");
                break;
        }
    }

    public boolean loginPatient() {
        System.out.println("Logging in as Patient...");
        System.out.println("\n--- PATIENT LOGIN ---");
        System.out.print("Enter email: ");
        String email = s.nextLine();
        System.out.print("Enter password: ");
        String password = s.nextLine();

        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT * FROM patients WHERE email = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                currentPatient = new Patient(
                        rs.getInt("patient_id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getString("phone_number"));

                System.out.println("✅ Login successful! Welcome, " + currentPatient.getFullName());
                return true;
            } else {
                System.out.println("❌ Invalid credentials. Please try again.");
                return false;
            }

        } catch (Exception e) {
            System.out.println("❌ Login error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean loginDoctor() {
        System.out.println("Logging in as Doctor...");
        System.out.println("\n--- DOCTOR LOGIN ---");
        System.out.print("Enter email: ");
        String email = s.nextLine();
        System.out.print("Enter password: ");
        String password = s.nextLine();

        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT * FROM doctors WHERE email = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                currentDoctor = new Doctor(
                        rs.getInt("doctor_id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getString("phone_number"),
                        rs.getBoolean("onDuty"),
                        rs.getString("specialist"));

                System.out.println("✅ Login successful! Welcome, Dr. " + currentDoctor.getFullName());
                return true;
            } else {
                System.out.println("❌ Invalid credentials. Please try again.");
                return false;
            }

        } catch (Exception e) {
            System.out.println("❌ Login error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean loginPharmacist() {
        System.out.println("Logging in as Pharmacist...");
        System.out.println("\n--- PHARMACIST LOGIN ---");
        System.out.print("Enter email: ");
        String email = s.nextLine();
        System.out.print("Enter password: ");
        String password = s.nextLine();

        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT * FROM pharmacists WHERE email = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                currentPharmacist = new Pharmacist(
                        rs.getInt("pharmacist_id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getString("phone_number"),
                        true);

                System.out.println("✅ Login successful! Welcome, " + currentPharmacist.getFullName());
                return true;
            } else {
                System.out.println("❌ Invalid credentials. Please try again.");
                return false;
            }

        } catch (Exception e) {
            System.out.println("❌ Login error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // DASHBOARDS
    public void patientDashboard() {
        while (true) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("    PATIENT DASHBOARD - " + currentPatient.getFullName());
            System.out.println("=".repeat(50));
            System.out.println("1. View My Profile");
            System.out.println("2. Update My Profile");
            System.out.println("3. Book an Appointment");
            System.out.println("4. View My Appointments");
            System.out.println("5. View Medical History");
            System.out.println("6. Logout");
            System.out.println("=".repeat(50));
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
                    currentPatient.displayInfo();
                    break;
                case 2:
                    currentPatient.updatePatientProfile();
                    break;
                case 3:
                    currentPatient.bookAppointment();
                    break;
                case 4:
                    currentPatient.viewPatientAppointments();
                    break;
                case 5:
                    currentPatient.viewMedicalHistory();
                    break;
                case 6:
                    currentPatient = null;
                    System.out.println("✅ Logged out successfully.");
                    return;
                default:
                    System.out.println("❌ Invalid option. Please try again.");
            }
        }
    }

    public void doctorDashboard() {
        while (true) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("    DOCTOR DASHBOARD - Dr. " + currentDoctor.getFullName());

            // Show current on-duty status in dashboard header
            String statusIndicator = currentDoctor.isOnDuty() ? "🟢 ON DUTY" : "🔴 OFF DUTY";
            System.out.println("    Status: " + statusIndicator);
            System.out.println("=".repeat(50));

            System.out.println("1. View My Profile");
            System.out.println("2. Update My Profile");
            System.out.println("3. View Patient Appointments");
            System.out.println("4. Set My Availability (by Date)");
            System.out.println("5. View My Availability Schedule");
            System.out.println("6. Remove Availability for Date");
            System.out.println(
                    "7. Toggle On-Duty Status " + (currentDoctor.isOnDuty() ? "(Currently: ON)" : "(Currently: OFF)"));
            System.out.println("8. View Detailed Status");
            System.out.println("9. View All Patients");
            System.out.println("10. View All Doctors");
            System.out.println("11. Logout");
            System.out.println("=".repeat(50));
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
                    currentDoctor.displayInfo();
                    break;
                case 2:
                    currentDoctor.updateDoctorProfile();
                    break;
                case 3:
                    currentDoctor.viewAppointments();
                    break;
                case 4:
                    currentDoctor.setDoctorAvailability();
                    break;
                case 5:
                    currentDoctor.viewMyAvailabilitySchedule();
                    break;
                case 6:
                    removeAvailabilityForDate();
                    break;
                case 7:
                    currentDoctor.toggleOnDutyStatus(); // New toggle functionality
                    break;
                case 8:
                    currentDoctor.viewDetailedStatus(); // Enhanced status view
                    break;
                case 9:
                    viewAllPatients();
                    break;
                case 10:
                    viewAllDoctors();
                    break;
                case 11:
                    currentDoctor = null;
                    System.out.println("✅ Logged out successfully.");
                    return;
                default:
                    System.out.println("❌ Invalid option. Please try again.");
            }
        }
    }

    public void pharmacistDashboard() {
        while (true) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("    PHARMACIST DASHBOARD - " + currentPharmacist.getFullName());
            System.out.println("=".repeat(50));
            System.out.println("1. View My Profile");
            System.out.println("2. Update My Profile");
            System.out.println("3. Process Prescriptions");
            System.out.println("4. View Medicine Inventory");
            System.out.println("5. Manage Medicine Queue");
            System.out.println("6. View Expired Medicines");
            System.out.println("7. View All Pharmacists");
            System.out.println("8. Logout");
            System.out.println("=".repeat(50));
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
                    currentPharmacist.displayInfo();
                    break;
                case 2:
                    currentPharmacist.updatePharmacistInfo();
                    break;
                case 3:
                    currentPharmacist.processPrescriptions();
                    break;
                case 4:
                    currentPharmacist.viewMedicineInventory();
                    break;
                case 5:
                    currentPharmacist.manageQueue();
                    break;
                case 6:
                    viewExpiredMedicines();
                    break;
                case 7:
                    viewAllPharmacists();
                    break;
                case 8:
                    currentPharmacist = null;
                    System.out.println("✅ Logged out successfully.");
                    return;
                default:
                    System.out.println("❌ Invalid option. Please try again.");
            }
        }
    }

    // HELPER METHODS FOR VIEWING DATA
    private void viewAllPatients() {
        System.out.println("\n--- ALL PATIENTS ---");
        ArrayList<Patient> allPatients = Patient.getAllPatients();

        if (allPatients.isEmpty()) {
            System.out.println("No patients found in the system.");
            return;
        }

        System.out.printf("%-5s %-25s %-30s %-15s\n", "ID", "Name", "Email", "Phone");
        System.out.println("-".repeat(80));

        for (Patient patient : allPatients) {
            System.out.printf("%-5d %-25s %-30s %-15s\n",
                    patient.getPatientId(),
                    patient.getFullName().length() > 24 ? patient.getFullName().substring(0, 24)
                            : patient.getFullName(),
                    patient.getEmail().length() > 29 ? patient.getEmail().substring(0, 29) : patient.getEmail(),
                    patient.getPhoneNumber());
        }

        System.out.print("\nPress Enter to continue...");
        s.nextLine();
    }

    private void viewAllDoctors() {
        System.out.println("\n--- ALL DOCTORS ---");
        ArrayList<Doctor> allDoctors = Doctor.getAllDoctors();

        if (allDoctors.isEmpty()) {
            System.out.println("No doctors found in the system.");
            return;
        }

        System.out.printf("%-5s %-25s %-20s %-15s %-12s\n", "ID", "Name", "Specialist", "Email", "Status");
        System.out.println("-".repeat(85));

        for (Doctor doctor : allDoctors) {
            String statusText = doctor.isOnDuty() ? "🟢 ON DUTY" : "🔴 OFF DUTY";
            System.out.printf("%-5d %-25s %-20s %-15s %-12s\n",
                    doctor.getDoctorId(),
                    doctor.getFullName().length() > 24 ? doctor.getFullName().substring(0, 24) : doctor.getFullName(),
                    doctor.getSpecialist().length() > 19 ? doctor.getSpecialist().substring(0, 19)
                            : doctor.getSpecialist(),
                    doctor.getEmail().length() > 14 ? doctor.getEmail().substring(0, 14) : doctor.getEmail(),
                    statusText);
        }

        System.out.println("\n💡 Legend: 🟢 = Available for new appointments, 🔴 = Not accepting new appointments");
        System.out.print("Press Enter to continue...");
        s.nextLine();
    }

    private void viewAllPharmacists() {
        System.out.println("\n--- ALL PHARMACISTS ---");
        ArrayList<Pharmacist> allPharmacists = Pharmacist.loadAllFromDatabase();

        if (allPharmacists.isEmpty()) {
            System.out.println("No pharmacists found in the system.");
            return;
        }

        System.out.printf("%-5s %-25s %-30s %-15s\n", "ID", "Name", "Email", "Phone");
        System.out.println("-".repeat(80));

        for (Pharmacist pharmacist : allPharmacists) {
            System.out.printf("%-5d %-25s %-30s %-15s\n",
                    pharmacist.getPharmacistId(),
                    pharmacist.getFullName().length() > 24 ? pharmacist.getFullName().substring(0, 24)
                            : pharmacist.getFullName(),
                    pharmacist.getEmail().length() > 29 ? pharmacist.getEmail().substring(0, 29)
                            : pharmacist.getEmail(),
                    pharmacist.getPhoneNumber());
        }

        System.out.print("\nPress Enter to continue...");
        s.nextLine();
    }

    private void viewExpiredMedicines() {
        System.out.println("\n--- EXPIRED MEDICINES ---");
        ArrayList<Medicine> expiredMedicines = Medicine.getExpiredMedicines();

        if (expiredMedicines.isEmpty()) {
            System.out.println("✅ No expired medicines found. All medicines are valid!");
            return;
        }

        System.out.printf("%-5s %-25s %-15s %-15s %-15s\n",
                "ID", "Medicine Name", "Dosage", "Category", "Expired Date");
        System.out.println("-".repeat(80));

        for (Medicine medicine : expiredMedicines) {
            System.out.printf("%-5d %-25s %-15s %-15s %-15s\n",
                    medicine.getMedicineId(),
                    medicine.getMedicineName().length() > 24 ? medicine.getMedicineName().substring(0, 24)
                            : medicine.getMedicineName(),
                    medicine.getDose().length() > 14 ? medicine.getDose().substring(0, 14) : medicine.getDose(),
                    medicine.getCategory().length() > 14 ? medicine.getCategory().substring(0, 14)
                            : medicine.getCategory(),
                    medicine.getExpiredDate().toString());
        }

        System.out.println("\n⚠️ Warning: These medicines should be removed from inventory!");
        System.out.print("Press Enter to continue...");
        s.nextLine();
    }

    // UTILITY METHODS
    private void closeScanner() {
        if (s != null) {
            s.close();
        }
    }

    public void logo() {
        System.out.println("                     *******************");
        System.out.println("               /******,,.,.....,.....,,*******");
        System.out.println("           /****,...........................,*****");
        System.out.println("        #****...................................,****");
        System.out.println("      ****..............,((((.(.*(((*..............****");
        System.out.println("    ,***......*....//*(((/((**/,//(*(((**/...........,**/");
        System.out.println("   ***...........,........*(..(..,(....................***");
        System.out.println(" ***.........../////......./.,*(/..../(.////(((*........***");
        System.out.println("***..............////......((((,...((..///....*(.........***");
        System.out.println("***,.............././//...../(/,(..(((,**//.....*.........,**&");
        System.out.println(",**................/..///,....(*(...((((.,//................***");
        System.out.println("***................/...////....(.....*((((//,...............,**");
        System.out.println("***................/....,///......../....(((((,..............**");
        System.out.println("***................/......///....../......///((((/..........,**");
        System.out.println("***................/.......///..../.......///..((((.........***");
        System.out.println("&**,.............../........///.././......///...(((.......,.**,");
        System.out.println(" ,**.............../.........////...(,....///...((*........***");
        System.out.println("  ***.............///.........//....(((*..///,.(/.........***");
        System.out.println("   ,**,..................................................***");
        System.out.println("     ***..............................................,***");
        System.out.println("      ****...........................................****");
        System.out.println("        %***,.,.................................,.,***,");
        System.out.println("           .****...............................,***/");
        System.out.println("              &******...................,.,*****/");
        System.out.println("                    ************************");
        System.out.println();
    }

    // Add this method to the Main.java class

/**
 * Remove availability for a specific date
 */
private void removeAvailabilityForDate() {
    System.out.println("\n--- REMOVE AVAILABILITY ---");
    
    // Show current availability first
    currentDoctor.viewMyAvailabilitySchedule();
    
    System.out.print("Enter the date to remove availability (YYYY-MM-DD): ");
    String date = s.nextLine().trim();
    
    if (date.isEmpty()) {
        System.out.println("❌ Date cannot be empty.");
        return;
    }
    
    // Validate date format
    try {
        LocalDate.parse(date);
    } catch (Exception e) {
        System.out.println("❌ Invalid date format. Please use YYYY-MM-DD format.");
        return;
    }
    
    // Check if availability exists for this date
    if (!checkIfAvailabilityExists(date)) {
        System.out.println("❌ No availability found for " + date);
        return;
    }
    
    // Check if there are any appointments on this date
    if (hasAppointmentsOnDate(date)) {
        System.out.println("⚠️ WARNING: There are existing appointments on " + date);
        System.out.print("Removing availability will not cancel existing appointments. Continue? (Y/N): ");
        String confirm = s.nextLine();
        if (!confirm.equalsIgnoreCase("Y")) {
            System.out.println("Operation cancelled.");
            return;
        }
    }
    
    System.out.print("Are you sure you want to remove availability for " + date + "? (Y/N): ");
    String finalConfirm = s.nextLine();
    
    if (!finalConfirm.equalsIgnoreCase("Y")) {
        System.out.println("Operation cancelled.");
        return;
    }
    
    // Remove the availability
    try {
        Connection conn = DatabaseConnect.getConnection();
        String sql = "DELETE FROM doctor_availability WHERE doctor_id = ? AND availability_date = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, currentDoctor.getDoctorId());
        pstmt.setString(2, date);
        
        int result = pstmt.executeUpdate();
        if (result > 0) {
            System.out.println("✅ Availability removed successfully for " + date);
        } else {
            System.out.println("❌ Failed to remove availability. No matching record found.");
        }
        
    } catch (SQLException e) {
        System.out.println("❌ Error removing availability: " + e.getMessage());
        e.printStackTrace();
    }
}

/**
 * Check if availability exists for a specific date
 */
private boolean checkIfAvailabilityExists(String date) {
    try {
        Connection conn = DatabaseConnect.getConnection();
        String sql = "SELECT COUNT(*) FROM doctor_availability WHERE doctor_id = ? AND availability_date = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, currentDoctor.getDoctorId());
        pstmt.setString(2, date);
        
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    } catch (SQLException e) {
        System.out.println("❌ Error checking availability: " + e.getMessage());
    }
    return false;
}

/**
 * Check if doctor has appointments on a specific date
 */
private boolean hasAppointmentsOnDate(String date) {
    try {
        Connection conn = DatabaseConnect.getConnection();
        String sql = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, currentDoctor.getDoctorId());
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