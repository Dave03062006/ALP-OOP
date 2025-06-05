import java.util.Scanner;

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

    public void loginPatient() {
        System.out.println("Logging in as Patient...");
        // Logic for patient login
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

            int choice = getIntInput();
            
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
    }

    public void updatePatientProfile(){

    }

    public void bookAppointment(){
    }

    public void viewPatientAppointments(){
    }

    public void viewMedicalHistory(){
    }
}
