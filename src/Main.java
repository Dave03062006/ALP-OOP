import java.util.Scanner;
import java.sql.*;

public class Main {

    Scanner s = new Scanner(System.in);
    Patient currentPatient = null;
    Doctor currentDoctor = null;
    Pharmacist currentPharmacist = null;

    public static void main(String[] args) {

    }

    public void menu() {
        do {
            System.out.println("Welcome to the application -- your comprehensive healthcare management platform!");
            System.out.println("This system connects patients, doctors, and pharmacists in one integrated environment. As a  patient, you can easily register, book appointments with specialized doctors, track your 
medical history, and manage your healthcare journey. Doctors can efficiently manage their 
schedules, view patient appointments, provide diagnoses, and prescribe medications. 
Pharmacists can process prescriptions, manage medicine inventory, and track prescription 
status from pending to completion. Whether you're seeking medical care, providing treatment, 
or dispensing medications, our system streamlines healthcare operations with secure login, 
intuitive menus, and real-time database integration. Please select your role below to begin 
your healthcare experience.");
            System.out.println("Please select your role to register");
            System.out.println("(1) Register as Patient");
            System.out.println("(2) Register as Doctor");
            System.out.println("(3) Register as Pharmacist");
            System.out.println("(4) Already registered? Login");
            System.out.println("(5) Exit");
            System.out.print("Enter your choice: ");
            int choice = s.nextInt();
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
                default:
                    break;
            }
        } while (true);
    }

    // registrations
    public void registerPatient() {
        System.out.println("Registering as Patient...");
        System.out.println("\n------ Patient Registration ---");
        System.out.print("Enter your full name: ");
        String fullName = s.next() + s.nextLine();
        System.out.print("Enter your email: ");
        String email = s.next() + s.nextLine();
        System.out.print("Enter your phone number: ");
        String phoneNumber = s.next() + s.nextLine();
        System.out.println("Create a password: ");
        String password = s.next() + s.nextLine();
        System.out.println("Confirm your password: ");
        String confirmPassword = s.next() + s.nextLine();

        if (!password.equals(confirmPassword)) {
            System.out.println("❌ Passwords do not match. Registration failed.");
            return;
        }

        Patient newPatient = new Patient(email, confirmPassword, fullName, phoneNumber);
        newPatient.register(email, confirmPassword, fullName, phoneNumber);
    }

    public void registerDoctor() {
        System.out.println("Registering as Doctor...");

    }

    public void registerPharmacist() {
        System.out.println("Registering as Pharmacist...");
        // Logic for pharmacist registration
    }

    // logins
    public void loginGeneral() {
        System.out.println("Logging in...");
        System.out.println("What is your role?");
        System.out.println("(1) Patient");
        System.out.println("(2) Doctor");
        System.out.println("(3) Pharmacist");
        System.out.println("(4) Back to Registration Menu");
        System.out.print("Enter your choice: ");
        int choice = s.nextInt();
        switch (choice) {
            case 1:
                loginPatient();
                break;
            case 2:
                loginDoctor();
                break;
            case 3:
                loginPharmacist();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                loginGeneral();
                break;
        }
        while (choice != 4)
            ;
    }

    public boolean loginPatient() {
        System.out.println("Logging in as Patient...");
        System.out.println("\n--- PATIENT LOGIN ---");
        System.out.print("Enter email: ");
        String email = s.nextLine();
        System.out.print("Enter password: ");
        String password = s.nextLine();

        Patient temPatient = new Patient(email, password, "", "");
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT * FROM patient WHERE email = ? AND password = ?";
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

    public void loginDoctor() {
        System.out.println("Logging in as Doctor...");
        // Logic for doctor login
    }

    public void loginPharmacist() {
        System.out.println("Logging in as Pharmacist...");
        // Logic for pharmacist login
    }

    // daashboard for patient
    public void patientDashboard() {
        while (true) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("    PATIENT DASHBOARD - " + currentPatient.getFullName());
            System.out.println("=".repeat(50));
            System.out.println("(1) View My Profile");
            System.out.println("2. Update My Profile");
            System.out.println("3. Book an Appointment");
            System.out.println("4. View My Appointments");
            System.out.println("5. View Medical History");
            System.out.println("6. Logout");
            System.out.println("=".repeat(50));

            int choice = s.nextInt();

            switch (choice) {
                case 1:
                    viewPatientProfile();
                    break;
                case 2:
                    updatePatientProfile();
                    break;
                case 3:
                    bookAppointment();
                    break;
                case 4:
                    viewPatientAppointments();
                    break;
                case 5:
                    viewMedicalHistory();
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

    public void viewPatientProfile() {
        System.out.println("\n--- MY PROFILE ---");
        System.out.println("Patient ID: " + currentPatient.getPatientId());
        System.out.println("Name: " + currentPatient.getFullName());
        System.out.println("Email: " + currentPatient.getEmail());
        System.out.println("Phone: " + currentPatient.getPhoneNumber());

        System.out.println("(0) Back to Dashboard");
        int choice = s.nextInt();
        if (choice == 0) {
            patientDashboard();
        } else {
            System.out.println("invalidChoice");
        }
    }

    public void updatePatientProfile() {
        System.out.println("\n--- UPDATE PROFILE ---");
        System.out.println("Current Name: " + currentPatient.getFullName());
        System.out.print("Enter new name (or press Enter to keep current): ");
        String newName = s.nextLine();
        if (newName.trim().isEmpty()) {
            newName = currentPatient.getFullName();
        }
        
        System.out.println("Current Email: " + currentPatient.getEmail());
        System.out.print("Enter new email (or press Enter to keep current): ");
        String newEmail = s.nextLine();
        if (newEmail.trim().isEmpty()) {
            newEmail = currentPatient.getEmail();
        }
        
        System.out.println("Current Phone: " + currentPatient.getPhoneNumber());
        System.out.print("Enter new phone (or press Enter to keep current): ");
        String newPhone = s.nextLine();
        if (newPhone.trim().isEmpty()) {
            newPhone = currentPatient.getPhoneNumber();
        }

        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "UPDATE patients SET name = ?, email = ?, phone = ? WHERE patient_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newName);
            pstmt.setString(2, newEmail);
            pstmt.setString(3, newPhone);
            pstmt.setInt(4, currentPatient.getPatientId());
            int result = pstmt.executeUpdate();

            if (result > 0) {
                currentPatient.setFullName(newName);
                currentPatient.setEmail(newEmail);
                currentPatient.setPhoneNumber(newPhone);
                System.out.println("✅ Profile updated successfully!");
            }
        } catch (Exception e) {
            System.out.println("❌ Update failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void bookAppointment() {
        
    }

    public void viewPatientAppointments() {
    }

    public void viewMedicalHistory() {
    }
}
