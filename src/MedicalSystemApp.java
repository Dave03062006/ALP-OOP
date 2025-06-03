import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.Map;
import java.util.Date;

public class MedicalSystemApp {
        public static void main(String[] args) {
                try {
                        // Set system look and feel
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                        e.printStackTrace();
                }

                JFrame mainFrame = new JFrame("Medical System");
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainFrame.setSize(500, 550);
                mainFrame.setLocationRelativeTo(null); // Center on screen

                MainFrame mainPanel = new MainFrame();
                mainFrame.setContentPane(mainPanel);

                mainFrame.setVisible(true);
        }
}

class MainFrame extends JPanel {
<<<<<<< HEAD
        // Card layout names
        private static final String ROLE_SELECTION_PANEL = "ROLE_SELECTION_PANEL";
        private static final String PATIENT_REGISTER_PANEL = "PATIENT_REGISTER_PANEL";
        private static final String DOCTOR_REGISTER_PANEL = "DOCTOR_REGISTER_PANEL";
        private static final String PHARMACIST_REGISTER_PANEL = "PHARMACIST_REGISTER_PANEL";
        private static final String LOGIN_PANEL = "LOGIN_PANEL";

        // Dashboard panel names
        private static final String PATIENT_DASHBOARD_PANEL = "PATIENT_DASHBOARD_PANEL";
        private static final String DOCTOR_DASHBOARD_PANEL = "DOCTOR_DASHBOARD_PANEL";
        private static final String PHARMACIST_DASHBOARD_PANEL = "PHARMACIST_DASHBOARD_PANEL";

        // Patient-specific panel names
        private static final String PATIENT_PROFILE_PANEL = "PATIENT_PROFILE_PANEL";
        private static final String PATIENT_UPDATE_PROFILE_PANEL = "PATIENT_UPDATE_PROFILE_PANEL";
        private static final String BOOK_APPOINTMENT_PANEL = "BOOK_APPOINTMENT_PANEL";
        private static final String VIEW_APPOINTMENTS_PANEL = "VIEW_APPOINTMENTS_PANEL";
        private static final String PATIENT_MEDICAL_HISTORY_PANEL = "PATIENT_MEDICAL_HISTORY_PANEL";

        // Doctor-specific panel names
        private static final String DOCTOR_PROFILE_PANEL = "DOCTOR_PROFILE_PANEL";
        private static final String DOCTOR_UPDATE_PROFILE_PANEL = "DOCTOR_UPDATE_PROFILE_PANEL";
        private static final String SET_AVAILABILITY_PANEL = "SET_AVAILABILITY_PANEL";
        private static final String VIEW_PATIENTS_PANEL = "VIEW_PATIENTS_PANEL";

        // Pharmacist-specific panel names
        private static final String PHARMACIST_PROFILE_PANEL = "PHARMACIST_PROFILE_PANEL";
        private static final String PHARMACIST_UPDATE_PROFILE_PANEL = "PHARMACIST_UPDATE_PROFILE_PANEL";
        private static final String PRESCRIPTIONS_PANEL = "PRESCRIPTIONS_PANEL";

        // Components
        private JPanel cardPanel;
        private CardLayout cardLayout;
        private String currentRole = "";

        public MainFrame() {
                setLayout(new BorderLayout());
                setBackground(new Color(236, 240, 241)); // Background color

                // Create the title panel
                JPanel titlePanel = PanelFactory.createTitlePanel();
                add(titlePanel, BorderLayout.NORTH);

                // Create card layout for panels
                cardLayout = new CardLayout();
                cardPanel = new JPanel(cardLayout);
                cardPanel.setBackground(new Color(236, 240, 241));

                // Create role selection panel
                JPanel roleSelectionPanel = PanelFactory.createRoleSelectionPanel(
                                cardLayout, cardPanel,
                                PATIENT_REGISTER_PANEL,
                                DOCTOR_REGISTER_PANEL,
                                PHARMACIST_REGISTER_PANEL,
                                LOGIN_PANEL);

                // Create login panel
                JPanel loginPanel = PanelFactory.createLoginPanel(
                                cardLayout, cardPanel,
                                ROLE_SELECTION_PANEL,
                                this::handleLogin);

                // Create registration panels
                JPanel patientRegisterPanel = PanelFactory.createRegistrationPanel(
                                "Patient", cardLayout, cardPanel,
                                ROLE_SELECTION_PANEL, LOGIN_PANEL,
                                this::handleRegistration);

                JPanel doctorRegisterPanel = PanelFactory.createRegistrationPanel(
                                "Doctor", cardLayout, cardPanel,
                                ROLE_SELECTION_PANEL, LOGIN_PANEL,
                                this::handleRegistration);

                JPanel pharmacistRegisterPanel = PanelFactory.createRegistrationPanel(
                                "Pharmacist", cardLayout, cardPanel,
                                ROLE_SELECTION_PANEL, LOGIN_PANEL,
                                this::handleRegistration);

                // Add panels to card layout
                cardPanel.add(roleSelectionPanel, ROLE_SELECTION_PANEL);
                cardPanel.add(patientRegisterPanel, PATIENT_REGISTER_PANEL);
                cardPanel.add(doctorRegisterPanel, DOCTOR_REGISTER_PANEL);
                cardPanel.add(pharmacistRegisterPanel, PHARMACIST_REGISTER_PANEL);
                cardPanel.add(loginPanel, LOGIN_PANEL);

                add(cardPanel, BorderLayout.CENTER);

                // Show role selection panel by default
                cardLayout.show(cardPanel, ROLE_SELECTION_PANEL);
        }

        private void handleLogin(String role, String username, String password) {
                // Simple validation
                if (username.isEmpty() || password.isEmpty()) {
                        JOptionPane.showMessageDialog(this,
                                        "Please enter both username and password",
                                        "Login Error",
                                        JOptionPane.ERROR_MESSAGE);
                        return;
                }
                // Authentication logic
                currentRole = role;

                // Create and add dashboard panels based on role
                if (role.equals("Patient")) {
                        // Create all patient-related panels if they don't exist
                        if (!panelExists(PATIENT_DASHBOARD_PANEL)) {
                                createPatientPanels(username);
                        }
                        cardLayout.show(cardPanel, PATIENT_DASHBOARD_PANEL);

                } else if (role.equals("Doctor")) {
                        // Create all doctor-related panels if they don't exist
                        if (!panelExists(DOCTOR_DASHBOARD_PANEL)) {
                                createDoctorPanels(username);
                        }
                        cardLayout.show(cardPanel, DOCTOR_DASHBOARD_PANEL);

                } else if (role.equals("Pharmacist")) {
                        // Create all pharmacist-related panels if they don't exist
                        if (!panelExists(PHARMACIST_DASHBOARD_PANEL)) {
                                createPharmacistPanels(username);
                        }
                        cardLayout.show(cardPanel, PHARMACIST_DASHBOARD_PANEL);
                }
        }

        private boolean handleRegistration(String role, String fullName, String email,
                        String specificId, String username,
                        String password, String confirmPassword) {
                // Initial check
                if (fullName.isEmpty() || email.isEmpty() || specificId.isEmpty() ||
                                username.isEmpty() || password.isEmpty()) {
                        JOptionPane.showMessageDialog(this,
                                        "Please fill in all required fields",
                                        "Registration Error",
                                        JOptionPane.ERROR_MESSAGE);
                        return false;
                }

                // Password confirmation
                if (!password.equals(confirmPassword)) {
                        JOptionPane.showMessageDialog(this,
                                        "Passwords do not match",
                                        "Registration Error",
                                        JOptionPane.ERROR_MESSAGE);
                        return false;
                }

                // Registration logic here

                String fieldName = role.equals("Patient") ? "Medical Record"
                                : (role.equals("Doctor") ? "License" : "Pharmacy ID");

=======

        @FunctionalInterface
        public interface AppointmentBookingHandler {
                boolean bookAppointment(String day, String time, String specialistType, String reason);
        }

        // Card layout names
        private static final String ROLE_SELECTION_PANEL = "ROLE_SELECTION_PANEL";
        private static final String PATIENT_REGISTER_PANEL = "PATIENT_REGISTER_PANEL";
        private static final String DOCTOR_REGISTER_PANEL = "DOCTOR_REGISTER_PANEL";
        private static final String PHARMACIST_REGISTER_PANEL = "PHARMACIST_REGISTER_PANEL";
        private static final String LOGIN_PANEL = "LOGIN_PANEL";

        // Dashboard panel names
        private static final String PATIENT_DASHBOARD_PANEL = "PATIENT_DASHBOARD_PANEL";
        private static final String DOCTOR_DASHBOARD_PANEL = "DOCTOR_DASHBOARD_PANEL";
        private static final String PHARMACIST_DASHBOARD_PANEL = "PHARMACIST_DASHBOARD_PANEL";

        // Patient-specific panel names
        private static final String PATIENT_PROFILE_PANEL = "PATIENT_PROFILE_PANEL";
        private static final String PATIENT_UPDATE_PROFILE_PANEL = "PATIENT_UPDATE_PROFILE_PANEL";
        private static final String BOOK_APPOINTMENT_PANEL = "BOOK_APPOINTMENT_PANEL";
        private static final String VIEW_APPOINTMENTS_PANEL = "VIEW_APPOINTMENTS_PANEL";
        private static final String PATIENT_MEDICAL_HISTORY_PANEL = "PATIENT_MEDICAL_HISTORY_PANEL";

        // Doctor-specific panel names
        private static final String DOCTOR_PROFILE_PANEL = "DOCTOR_PROFILE_PANEL";
        private static final String DOCTOR_UPDATE_PROFILE_PANEL = "DOCTOR_UPDATE_PROFILE_PANEL";
        private static final String SET_AVAILABILITY_PANEL = "SET_AVAILABILITY_PANEL";
        private static final String VIEW_PATIENTS_PANEL = "VIEW_PATIENTS_PANEL";

        // Pharmacist-specific panel names
        private static final String PHARMACIST_PROFILE_PANEL = "PHARMACIST_PROFILE_PANEL";
        private static final String PHARMACIST_UPDATE_PROFILE_PANEL = "PHARMACIST_UPDATE_PROFILE_PANEL";
        private static final String PRESCRIPTIONS_PANEL = "PRESCRIPTIONS_PANEL";

        // Components
        private JPanel cardPanel;
        private CardLayout cardLayout;
        private String currentRole = "";

        public MainFrame() {
                setLayout(new BorderLayout());
                setBackground(new Color(236, 240, 241)); // Background color

                // Create the title panel
                JPanel titlePanel = PanelFactory.createTitlePanel();
                add(titlePanel, BorderLayout.NORTH);

                // Create card layout for panels
                cardLayout = new CardLayout();
                cardPanel = new JPanel(cardLayout);
                cardPanel.setBackground(new Color(236, 240, 241));

                // Create role selection panel
                JPanel roleSelectionPanel = PanelFactory.createRoleSelectionPanel(
                                cardLayout, cardPanel,
                                PATIENT_REGISTER_PANEL,
                                DOCTOR_REGISTER_PANEL,
                                PHARMACIST_REGISTER_PANEL,
                                LOGIN_PANEL);

                // Create login panel
                JPanel loginPanel = PanelFactory.createLoginPanel(
                                cardLayout, cardPanel,
                                ROLE_SELECTION_PANEL,
                                this::handleLogin);

                // Create registration panels
                JPanel patientRegisterPanel = PanelFactory.createRegistrationPanel(
                                "Patient", cardLayout, cardPanel,
                                ROLE_SELECTION_PANEL, LOGIN_PANEL,
                                this::handleRegistration);

                JPanel doctorRegisterPanel = PanelFactory.createRegistrationPanel(
                                "Doctor", cardLayout, cardPanel,
                                ROLE_SELECTION_PANEL, LOGIN_PANEL,
                                this::handleRegistration);

                JPanel pharmacistRegisterPanel = PanelFactory.createRegistrationPanel(
                                "Pharmacist", cardLayout, cardPanel,
                                ROLE_SELECTION_PANEL, LOGIN_PANEL,
                                this::handleRegistration);

                // Add panels to card layout
                cardPanel.add(roleSelectionPanel, ROLE_SELECTION_PANEL);
                cardPanel.add(patientRegisterPanel, PATIENT_REGISTER_PANEL);
                cardPanel.add(doctorRegisterPanel, DOCTOR_REGISTER_PANEL);
                cardPanel.add(pharmacistRegisterPanel, PHARMACIST_REGISTER_PANEL);
                cardPanel.add(loginPanel, LOGIN_PANEL);

                add(cardPanel, BorderLayout.CENTER);

                // Show role selection panel by default
                cardLayout.show(cardPanel, ROLE_SELECTION_PANEL);
        }

        private void handleLogin(String role, String username, String password) {
                // Simple validation
                if (username.isEmpty() || password.isEmpty()) {
                        JOptionPane.showMessageDialog(this,
                                        "Please enter both username and password",
                                        "Login Error",
                                        JOptionPane.ERROR_MESSAGE);
                        return;
                }
                // Authentication logic
                currentRole = role;

                // Create and add dashboard panels based on role
                if (role.equals("Patient")) {
                        // Create all patient-related panels if they don't exist
                        if (!panelExists(PATIENT_DASHBOARD_PANEL)) {
                                createPatientPanels(username);
                        }
                        cardLayout.show(cardPanel, PATIENT_DASHBOARD_PANEL);

                } else if (role.equals("Doctor")) {
                        // Create all doctor-related panels if they don't exist
                        if (!panelExists(DOCTOR_DASHBOARD_PANEL)) {
                                createDoctorPanels(username);
                        }
                        cardLayout.show(cardPanel, DOCTOR_DASHBOARD_PANEL);

                } else if (role.equals("Pharmacist")) {
                        // Create all pharmacist-related panels if they don't exist
                        if (!panelExists(PHARMACIST_DASHBOARD_PANEL)) {
                                createPharmacistPanels(username);
                        }
                        cardLayout.show(cardPanel, PHARMACIST_DASHBOARD_PANEL);
                }
        }

        private boolean handleRegistration(String role, String fullName, String email,
                        String specificId, String username,
                        String password, String confirmPassword) {
                // Initial check
                if (fullName.isEmpty() || email.isEmpty() || specificId.isEmpty() ||
                                username.isEmpty() || password.isEmpty()) {
                        JOptionPane.showMessageDialog(this,
                                        "Please fill in all required fields",
                                        "Registration Error",
                                        JOptionPane.ERROR_MESSAGE);
                        return false;
                }

                // Password confirmation
                if (!password.equals(confirmPassword)) {
                        JOptionPane.showMessageDialog(this,
                                        "Passwords do not match",
                                        "Registration Error",
                                        JOptionPane.ERROR_MESSAGE);
                        return false;
                }

                // Registration logic here

                String fieldName = role.equals("Patient") ? "Medical Record"
                                : (role.equals("Doctor") ? "License" : "Pharmacy ID");

>>>>>>> Dave
                JOptionPane.showMessageDialog(this,
                                "Registration as " + role + " for: " + fullName + "\n" +
                                                fieldName + ": " + specificId,
                                "Registration Information",
                                JOptionPane.INFORMATION_MESSAGE);

                // Return true to indicate successful registration
                return true;
        }

        // Helper method to check if a panel exists
        private boolean panelExists(String panelName) {
                for (Component comp : cardPanel.getComponents()) {
                        if (comp.getName() != null && comp.getName().equals(panelName)) {
                                return true;
                        }
                }
                return false;
        }

        // Create all patient-related panels
        private void createPatientPanels(String username) {
                String patientId = "P" + (10000 + new Random().nextInt(90000)); // Random ID
                int patientAge = 30; // Default age
                String patientEmail = username + "@email.com";

                // Create patient dashboard
                JPanel patientDashboard = DashboardPanels.createPatientDashboard(
                                username, cardLayout, cardPanel,
                                PATIENT_PROFILE_PANEL, PATIENT_UPDATE_PROFILE_PANEL,
                                BOOK_APPOINTMENT_PANEL, VIEW_APPOINTMENTS_PANEL);
                patientDashboard.setName(PATIENT_DASHBOARD_PANEL);

                // Create patient profile view
                JPanel patientProfile = DashboardPanels.createProfileViewPanel(
                                "Patient", username, patientId, patientAge, patientEmail,
                                cardLayout, cardPanel, PATIENT_DASHBOARD_PANEL, PATIENT_MEDICAL_HISTORY_PANEL);
                patientProfile.setName(PATIENT_PROFILE_PANEL);

                // Create patient profile update panel
                JPanel patientUpdateProfile = DashboardPanels.createProfileUpdatePanel(
                                "Patient", username, patientAge, patientEmail,
                                cardLayout, cardPanel, PATIENT_DASHBOARD_PANEL,
                                this::handleProfileUpdate);
                patientUpdateProfile.setName(PATIENT_UPDATE_PROFILE_PANEL);

                // Create appointment booking panel
                JPanel bookAppointment = DashboardPanels.createBookAppointmentPanel(
                                cardLayout, cardPanel, PATIENT_DASHBOARD_PANEL,
<<<<<<< HEAD
                                (day, time, specialistType, reason) -> handleAppointmentBooking(day, time,
                                                specialistType, reason));
                bookAppointment.setName(BOOK_APPOINTMENT_PANEL);

=======
                                new DashboardPanels.AppointmentBookingHandler() {
                                        @Override
                                        public boolean checkAvailability(String day, String time,
                                                        String specialistType) {
                                                // Implement availability check
                                                return true;
                                        }

                                        @Override
                                        public boolean handleAppointmentBooking(String day, String time,
                                                        String specialistType, String reason) {
                                                return MainFrame.this.handleAppointmentBooking(day, time,
                                                                specialistType, reason);
                                        }
                                });
                bookAppointment.setName(BOOK_APPOINTMENT_PANEL);
>>>>>>> Dave
                // Create appointments viewing panel
                JPanel viewAppointments = DashboardPanels.createViewAppointmentsPanel(
                                cardLayout, cardPanel, PATIENT_DASHBOARD_PANEL);
                viewAppointments.setName(VIEW_APPOINTMENTS_PANEL);

                // Create a simple medical history panel (placeholder)
                JPanel medicalHistory = createSimplePlaceholderPanel(
<<<<<<< HEAD
                                "Medical History", "Patient", PATIENT_PROFILE_PANEL);
=======
                                "Medical History", "Patient",
                                PATIENT_PROFILE_PANEL);
>>>>>>> Dave
                medicalHistory.setName(PATIENT_MEDICAL_HISTORY_PANEL);

                // Add all panels to the card layout
                cardPanel.add(patientDashboard, PATIENT_DASHBOARD_PANEL);
                cardPanel.add(patientProfile, PATIENT_PROFILE_PANEL);
                cardPanel.add(patientUpdateProfile, PATIENT_UPDATE_PROFILE_PANEL);
                cardPanel.add(bookAppointment, BOOK_APPOINTMENT_PANEL);
                cardPanel.add(viewAppointments, VIEW_APPOINTMENTS_PANEL);
                cardPanel.add(medicalHistory, PATIENT_MEDICAL_HISTORY_PANEL);
        }

        // Create all doctor-related panels
        private void createDoctorPanels(String username) {
                // For demo, we'll use sample data
                String doctorId = "D" + (10000 + new Random().nextInt(90000)); // Random ID
                int doctorAge = 45; // Default age
                String doctorEmail = "dr." + username + "@hospital.com";

                // Create doctor dashboard
                JPanel doctorDashboard = DashboardPanels.createDoctorDashboard(
                                username, cardLayout, cardPanel,
                                DOCTOR_PROFILE_PANEL, DOCTOR_UPDATE_PROFILE_PANEL,
                                SET_AVAILABILITY_PANEL, VIEW_PATIENTS_PANEL);
                doctorDashboard.setName(DOCTOR_DASHBOARD_PANEL);

                // Create doctor profile view
                JPanel doctorProfile = DashboardPanels.createProfileViewPanel(
                                "Doctor", "Dr. " + username, doctorId, doctorAge, doctorEmail,
                                cardLayout, cardPanel, DOCTOR_DASHBOARD_PANEL, null);
                doctorProfile.setName(DOCTOR_PROFILE_PANEL);

                // Create doctor profile update panel
                JPanel doctorUpdateProfile = DashboardPanels.createProfileUpdatePanel(
                                "Doctor", "Dr. " + username, doctorAge, doctorEmail,
                                cardLayout, cardPanel, DOCTOR_DASHBOARD_PANEL,
                                this::handleProfileUpdate);
                doctorUpdateProfile.setName(DOCTOR_UPDATE_PROFILE_PANEL);

                // Create availability setting panel
                JPanel setAvailability = DashboardPanels.createSetAvailabilityPanel(
                                cardLayout, cardPanel, DOCTOR_DASHBOARD_PANEL,
                                this::handleAvailabilityUpdate);
                setAvailability.setName(SET_AVAILABILITY_PANEL);

                // Create patient viewing panel
                JPanel viewPatients = DashboardPanels.createViewPatientsPanel(
                                cardLayout, cardPanel, DOCTOR_DASHBOARD_PANEL);
                viewPatients.setName(VIEW_PATIENTS_PANEL);

                // Add all panels to the card layout
                cardPanel.add(doctorDashboard, DOCTOR_DASHBOARD_PANEL);
                cardPanel.add(doctorProfile, DOCTOR_PROFILE_PANEL);
                cardPanel.add(doctorUpdateProfile, DOCTOR_UPDATE_PROFILE_PANEL);
                cardPanel.add(setAvailability, SET_AVAILABILITY_PANEL);
                cardPanel.add(viewPatients, VIEW_PATIENTS_PANEL);
        }

        // Create all pharmacist-related panels
        private void createPharmacistPanels(String username) {
                String pharmacistId = "PH" + (10000 + new Random().nextInt(90000)); // Random ID
                int pharmacistAge = 35; // Default age
                String pharmacistEmail = username + "@pharmacy.com";

                // Create pharmacist dashboard
                JPanel pharmacistDashboard = DashboardPanels.createPharmacistDashboard(
                                username, cardLayout, cardPanel,
                                PHARMACIST_PROFILE_PANEL, PHARMACIST_UPDATE_PROFILE_PANEL,
                                PRESCRIPTIONS_PANEL);
                pharmacistDashboard.setName(PHARMACIST_DASHBOARD_PANEL);

                // Create pharmacist profile view
                JPanel pharmacistProfile = DashboardPanels.createProfileViewPanel(
                                "Pharmacist", username, pharmacistId, pharmacistAge, pharmacistEmail,
                                cardLayout, cardPanel, PHARMACIST_DASHBOARD_PANEL, null);
                pharmacistProfile.setName(PHARMACIST_PROFILE_PANEL);

                // Create pharmacist profile update panel
                JPanel pharmacistUpdateProfile = DashboardPanels.createProfileUpdatePanel(
                                "Pharmacist", username, pharmacistAge, pharmacistEmail,
                                cardLayout, cardPanel, PHARMACIST_DASHBOARD_PANEL,
                                this::handleProfileUpdate);
                pharmacistUpdateProfile.setName(PHARMACIST_UPDATE_PROFILE_PANEL);

                // Create prescriptions panel
                JPanel prescriptions = DashboardPanels.createPrescriptionsPanel(
                                cardLayout, cardPanel, PHARMACIST_DASHBOARD_PANEL,
                                this::handlePrescriptionStatusChange);
                prescriptions.setName(PRESCRIPTIONS_PANEL);

                // Add all panels to the card layout
                cardPanel.add(pharmacistDashboard, PHARMACIST_DASHBOARD_PANEL);
                cardPanel.add(pharmacistProfile, PHARMACIST_PROFILE_PANEL);
                cardPanel.add(pharmacistUpdateProfile, PHARMACIST_UPDATE_PROFILE_PANEL);
                cardPanel.add(prescriptions, PRESCRIPTIONS_PANEL);
        }

<<<<<<< HEAD
        // DUMMY
=======
>>>>>>> Dave
        // Create a simple placeholder panel for features not fully implemented
        private JPanel createSimplePlaceholderPanel(String title, String role, String backPanelName) {
                JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout());
                panel.setBackground(new Color(236, 240, 241));

                // Get role-specific color
                Color roleColor;
                if (role.equals("Patient")) {
                        roleColor = new Color(46, 204, 113);
                } else if (role.equals("Doctor")) {
                        roleColor = new Color(52, 152, 219);
                } else {
                        roleColor = new Color(155, 89, 182);
                }

                // Header
                JPanel headerPanel = new JPanel();
                headerPanel.setBackground(roleColor);
                headerPanel.setPreferredSize(new Dimension(10, 60));
                headerPanel.setLayout(new BorderLayout());

                JLabel titleLabel = new JLabel(title + " - " + role);
                titleLabel.setForeground(Color.WHITE);
                titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
                headerPanel.add(titleLabel, BorderLayout.CENTER);

                JButton backButton = new JButton("Back");
                backButton.setBackground(Color.WHITE);
                backButton.setForeground(Color.BLACK);
                backButton.setFont(new Font("Times New Roman", Font.PLAIN, 14));
                backButton.setFocusPainted(false);
                backButton.setBorderPainted(false);
                backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                backButton.addActionListener(e -> cardLayout.show(cardPanel, backPanelName));
                headerPanel.add(backButton, BorderLayout.WEST);

                panel.add(headerPanel, BorderLayout.NORTH);

                // Placeholder content
                JLabel contentLabel = new JLabel("Content for " + title + " not available yet.");
                contentLabel.setHorizontalAlignment(SwingConstants.CENTER);
                contentLabel.setFont(new Font("Times New Roman", Font.ITALIC, 14));
                panel.add(contentLabel, BorderLayout.CENTER);

                return panel;
        }

        private boolean handleProfileUpdate(String newName, int newAge, String newEmail) {
                // Simple validation
                if (newName.isEmpty() || newEmail.isEmpty()) {
                        JOptionPane.showMessageDialog(this,
                                        "Please fill in all required fields",
                                        "Update Error",
                                        JOptionPane.ERROR_MESSAGE);
                        return false;
                }

                // Database update logic

                JOptionPane.showMessageDialog(this,
                                "Profile updated for " + currentRole + ":\n" +
                                                "Name: " + newName + "\n" +
                                                "Age: " + newAge + "\n" +
                                                "Email: " + newEmail,
                                "Profile Update",
                                JOptionPane.INFORMATION_MESSAGE);

                return true;
        }

        // Handler for appointment bookings
        private boolean handleAppointmentBooking(String day, String time, String specialistType, String reason) {
                // Simple validation
                if (reason.isEmpty()) {
                        JOptionPane.showMessageDialog(this,
                                        "Please provide a reason for your appointment",
                                        "Booking Error",
                                        JOptionPane.ERROR_MESSAGE);
                        return false;
                }

                // Here you would add actual database insertion logic
                JOptionPane.showMessageDialog(this,
                                "Appointment booked:\n" +
                                                "Day: " + day + "\n" +
                                                "Time: " + time + "\n" +
                                                "Specialist: " + specialistType + "\n" +
                                                "Reason: " + reason,
                                "Appointment Booking",
                                JOptionPane.INFORMATION_MESSAGE);

                return true;
        }

<<<<<<< HEAD
        // what is this, ho to do this, i dont understand 
=======
>>>>>>> Dave
        // Handler for doctor availability updates
        private boolean handleAvailabilityUpdate(Map<String, String> availability) {
                StringBuilder availabilityText = new StringBuilder("Availability updated:\n");

                // Build a readable representation of the availability
                for (Map.Entry<String, String> entry : availability.entrySet()) {
                        String day = entry.getKey();
                        String hours = entry.getValue();
                        availabilityText.append(day).append(": ").append(hours).append("\n");
                }

                // Here you would add actual database update logic
                JOptionPane.showMessageDialog(this,
                                availabilityText.toString(),
                                "Availability Update",
                                JOptionPane.INFORMATION_MESSAGE);

                return true;
        }

        // Handler for prescription status changes
        private boolean handlePrescriptionStatusChange(String patientId, String newStatus) {
                // Here you would add actual database update logic
                JOptionPane.showMessageDialog(this,
                                "Prescription status for patient " + patientId +
                                                " updated to: " + newStatus,
                                "Prescription Status Update",
                                JOptionPane.INFORMATION_MESSAGE);

                return true;
        }
}