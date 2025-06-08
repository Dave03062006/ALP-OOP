import java.util.Scanner;

import javax.print.Doc;

import java.sql.*;

public class Main {

    Scanner s = new Scanner(System.in);
    Patient currentPatient = null;
    Doctor currentDoctor = null;
    Pharmacist currentPharmacist = null;

    public static void main(String[] args) {

        Main mainApp = new Main();
        mainApp.menu();
    }

    // public String getEmoji(String emoji, String fallback) {
    // String term = System.getenv("TERM");
    // String termProgram = System.getenv("TERM_PROGRAM");

    // boolean supportsEmoji = (term != null && (term.contains("xterm") ||
    // term.contains("256color")))
    // || (termProgram != null && (termProgram.contains("iTerm") ||
    // termProgram.contains("vscode")));
    // return supportsEmoji ? emoji : fallback;
    // }

    public void menu() {
        logo();
        do {
            System.out.println(
                    "Welcome to the application -- your comprehensive healthcare management platform!\nThis system connects patients, doctors, and pharmacists in one integrated environment. As a  patient, you can easily register, book appointments with specialized doctors, track your \r\n"
                            + //
                            "medical history, and manage your healthcare journey. Doctors can efficiently manage their \r\n"
                            + //
                            "schedules, view patient appointments, provide diagnoses, and prescribe medications. \r\n" + //
                            "Pharmacists can process prescriptions, manage medicine inventory, and track prescription \r\n"
                            + //
                            "status from pending to completion. Whether you're seeking medical care, providing treatment, \r\n"
                            + //
                            "or dispensing medications, our system streamlines healthcare operations with secure login, \r\n"
                            + //
                            "intuitive menus, and real-time database integration. Please select your role below to begin \r\n"
                            + //
                            "your healthcare experience.");
            System.out.println("----Please select your role to register----");
            System.out.println(
                    "1. Register as Patient 🤕\n2. Register as Doctor 🩺\n3. Register as Pharmacist 💊\n4. Already registered? Login\n5. Exit\nEnter your choice: ");
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
        System.out.println("\n------ 🤕 Patient Registration 🤕 ---");
        System.out.print("Enter your full name 🪪: ");
        String fullName = s.next() + s.nextLine();
        System.out.print("Enter your email 📧: ");
        String email = s.next() + s.nextLine();
        System.out.print("Enter your phone number 📞: ");
        String phoneNumber = s.next() + s.nextLine();
        System.out.println("Create a password 🔒: ");
        String password = s.next() + s.nextLine();
        System.out.println("Confirm your password 🔐: ");
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
        System.out.println("\n------ 🧑‍⚕️ Doctor Registration 🧑‍⚕️ ---");
        System.out.print("Enter your full name 🪪: ");
        String fullName = s.next() + s.nextLine();
        System.out.print("Enter your email 📧: ");
        String email = s.next() + s.nextLine();
        System.out.print("Enter your phone number 📞: ");
        String phoneNumber = s.next() + s.nextLine();
        System.out.println("Create a password 🔒: ");
        String password = s.next() + s.nextLine();
        System.out.println("Confirm your password 🔐: ");
        String confirmPassword = s.next() + s.nextLine();

        if (!password.equals(confirmPassword)) {
            System.out.println("❌ Passwords do not match. Registration failed.");
            return;
        }

        Doctor newDoctor = new Doctor(email, password, fullName, phoneNumber, false, confirmPassword);
        newDoctor.register(email, confirmPassword, fullName, phoneNumber);
    }

    public void registerPharmacist() {
        System.out.println("Registering as Pharmacist...");
        System.out.println("\n------ 💊 Pharmacist Registration 💊 ---");
        System.out.print("Enter your full name 🪪: ");
        String fullName = s.next() + s.nextLine();
        System.out.print("Enter your email 📧: ");
        String email = s.next() + s.nextLine();
        System.out.print("Enter your phone number 📞: ");
        String phoneNumber = s.next() + s.nextLine();
        System.out.println("Create a password 🔒: ");
        String password = s.next() + s.nextLine();
        System.out.println("Confirm your password 🔐: ");
        String confirmPassword = s.next() + s.nextLine();

        if (!password.equals(confirmPassword)) {
            System.out.println("❌ Passwords do not match. Registration failed.");
            return;
        }
        Pharmacist newPharmacist = new Pharmacist(email, password, fullName, phoneNumber, false);
        newPharmacist.register(email, confirmPassword, fullName, phoneNumber);
    }

    // logins
    public void loginGeneral() {
        System.out.println("Logging in...");
        System.out.println("What is your role?");
        System.out.println("1. Patient \\uD83E\\uDD15");
        System.out.println("2. Doctor 🩺");
        System.out.println("3. Pharmacist 💊");
        System.out.println("4. Back to Registration Menu ⬅️");
        System.out.print("Enter your choice: ");
        int choice = s.nextInt();
        switch (choice) {
            case 1:
                if (loginPatient()) {
                    patientDashboard();
                }
                break;
            case 2:
                loginDoctor();
                break;
            case 3:
                loginPharmacist();
                break;
            default:
                System.out.println("❌ Invalid choice. Please try again.");
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

    public boolean loginDoctor() {
        System.out.println("Logging in as Doctor...");
        System.out.println("\n--- DOCTOR LOGIN ---");
        System.out.print("Enter email: ");
        String email = s.nextLine();
        System.out.print("Enter password: ");
        String password = s.nextLine();

        Doctor temDoctor = new Doctor(email, password, "", "", false, "");
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT * FROM patient WHERE email = ? AND password = ?";
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
                        rs.getBoolean("on_duty"),
                        rs.getString("specialist"));

                System.out.println("✅ Login successful! Welcome, " + currentDoctor.getFullName());
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
            System.out.println("1. View My Profile");
            System.out.println("2. Update My Profile");
            System.out.println("3. Book an Appointment");
            System.out.println("4. View My Appointments");
            System.out.println("5. View Medical History");
            System.out.println("6. Logout");
            System.out.println("=".repeat(50));

            int choice = s.nextInt();

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
            System.out.println("    DOCTOR DASHBOARD - " + currentDoctor.getFullName());
            System.out.println("=".repeat(50));
            System.out.println("1. View My Profile");
            System.out.println("2. Update My Profile");
            System.out.println("3. View Appointments");
            System.out.println("4. Set Availability");
            System.out.println("5. Logout");
            System.out.println("=".repeat(50));

            int choice = s.nextInt();

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
                    currentDoctor.manageWeeklySchedule();
                case 5:
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
            System.out.println("5. Logout");
            System.out.println("=".repeat(50));

            int choice = s.nextInt();

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
                    currentPharmacist = null;
                    System.out.println("✅ Logged out successfully.");
                    return;
                default:
                    System.out.println("❌ Invalid option. Please try again.");
            }
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
    }
}
