import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This class contains all panels related to user dashboards after login
 * It includes role-specific panels for Patient, Doctor, and Pharmacist
 */
public class DashboardPanels {
    // Colors for UI consistency
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private static final Color HIGHLIGHT_COLOR = new Color(26, 188, 156);
    private static final Color TEXT_COLOR = new Color(44, 62, 80);
    private static final Color PATIENT_COLOR = new Color(46, 204, 113);
    private static final Color DOCTOR_COLOR = new Color(52, 152, 219);
    private static final Color PHARMACIST_COLOR = new Color(155, 89, 182);

    // Shared method to create a standardized dashboard button
    private static JButton createDashboardButton(String text, Color color, ActionListener listener) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Times New Roman", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(listener);
        return button;
    }

    /**
     * Creates a patient dashboard with their specific features
     */
    public static JPanel createPatientDashboard(String username, CardLayout mainCardLayout, JPanel mainCardPanel,
            String profilePanelName, String updateProfilePanelName,
            String bookAppointmentPanelName, String viewAppointmentsPanelName) {

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);

        // Welcome header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PATIENT_COLOR);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel welcomeLabel = new JLabel("Welcome, " + username + " | Patient Dashboard");
        welcomeLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(231, 76, 60));
        logoutButton.setForeground(Color.BLACK);
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(panel,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                // Return to login screen
                mainCardLayout.show(mainCardPanel, "LOGIN_PANEL");
            }
        });
        headerPanel.add(logoutButton, BorderLayout.EAST);

        panel.add(headerPanel, BorderLayout.NORTH);

        // Main content with dashboard options
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 50, 10, 50);

        // Description label
        JLabel descLabel = new JLabel("What would you like to do today?");
        descLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        descLabel.setForeground(TEXT_COLOR);
        descLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.insets = new Insets(30, 50, 30, 50);
        contentPanel.add(descLabel, gbc);
        gbc.insets = new Insets(10, 50, 10, 50);

        // View Profile Button
        JButton viewProfileBtn = createDashboardButton("View My Profile", PATIENT_COLOR, e -> {
            mainCardLayout.show(mainCardPanel, profilePanelName);
        });
        contentPanel.add(viewProfileBtn, gbc);

        // Update Profile Button
        JButton updateProfileBtn = createDashboardButton("Update My Profile", PATIENT_COLOR, e -> {
            mainCardLayout.show(mainCardPanel, updateProfilePanelName);
        });
        contentPanel.add(updateProfileBtn, gbc);

        // Book Appointment Button
        JButton bookAppointmentBtn = createDashboardButton("Book an Appointment", PATIENT_COLOR, e -> {
            mainCardLayout.show(mainCardPanel, bookAppointmentPanelName);
        });
        contentPanel.add(bookAppointmentBtn, gbc);

        // View Appointments Button
        JButton viewAppointmentsBtn = createDashboardButton("View My Appointments", PATIENT_COLOR, e -> {
            mainCardLayout.show(mainCardPanel, viewAppointmentsPanelName);
        });
        contentPanel.add(viewAppointmentsBtn, gbc);

        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates a doctor dashboard with their specific features
     */
    public static JPanel createDoctorDashboard(String username, CardLayout mainCardLayout, JPanel mainCardPanel,
            String profilePanelName, String updateProfilePanelName,
            String setAvailabilityPanelName, String viewPatientsPanelName) {

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);

        // Welcome header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DOCTOR_COLOR);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel welcomeLabel = new JLabel("Welcome, Dr. " + username + " | Doctor Dashboard");
        welcomeLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(231, 76, 60));
        logoutButton.setForeground(Color.BLACK);
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(panel,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                // Return to login screen
                mainCardLayout.show(mainCardPanel, "LOGIN_PANEL");
            }
        });
        headerPanel.add(logoutButton, BorderLayout.EAST);

        panel.add(headerPanel, BorderLayout.NORTH);

        // Main content with dashboard options
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 50, 10, 50);

        // Description label
        JLabel descLabel = new JLabel("What would you like to do today?");
        descLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        descLabel.setForeground(TEXT_COLOR);
        descLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.insets = new Insets(30, 50, 30, 50);
        contentPanel.add(descLabel, gbc);
        gbc.insets = new Insets(10, 50, 10, 50);

        // View Profile Button
        JButton viewProfileBtn = createDashboardButton("View My Profile", DOCTOR_COLOR, e -> {
            mainCardLayout.show(mainCardPanel, profilePanelName);
        });
        contentPanel.add(viewProfileBtn, gbc);

        // Update Profile Button
        JButton updateProfileBtn = createDashboardButton("Update My Profile", DOCTOR_COLOR, e -> {
            mainCardLayout.show(mainCardPanel, updateProfilePanelName);
        });
        contentPanel.add(updateProfileBtn, gbc);

        // Set Availability Button
        JButton setAvailabilityBtn = createDashboardButton("Set My Availability", DOCTOR_COLOR, e -> {
            mainCardLayout.show(mainCardPanel, setAvailabilityPanelName);
        });
        contentPanel.add(setAvailabilityBtn, gbc);

        // View Patients Button
        JButton viewPatientsBtn = createDashboardButton("View Patient Appointments", DOCTOR_COLOR, e -> {
            mainCardLayout.show(mainCardPanel, viewPatientsPanelName);
        });
        contentPanel.add(viewPatientsBtn, gbc);

        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates a pharmacist dashboard with their specific features
     */
    public static JPanel createPharmacistDashboard(String username, CardLayout mainCardLayout, JPanel mainCardPanel,
            String profilePanelName, String updateProfilePanelName, String prescriptionsPanelName) {

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);

        // Welcome header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PHARMACIST_COLOR);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel welcomeLabel = new JLabel("Welcome, " + username + " | Pharmacist Dashboard");
        welcomeLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.white);
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(231, 76, 60));
        logoutButton.setForeground(Color.black);
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(panel,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                // Return to login screen
                mainCardLayout.show(mainCardPanel, "LOGIN_PANEL");
            }
        });
        headerPanel.add(logoutButton, BorderLayout.EAST);

        panel.add(headerPanel, BorderLayout.NORTH);

        // Main content with dashboard options
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 50, 10, 50);

        // Description label
        JLabel descLabel = new JLabel("What would you like to do today?");
        descLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        descLabel.setForeground(TEXT_COLOR);
        descLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.insets = new Insets(30, 50, 30, 50);
        contentPanel.add(descLabel, gbc);
        gbc.insets = new Insets(10, 50, 10, 50);

        // View Profile Button
        JButton viewProfileBtn = createDashboardButton("View My Profile", PHARMACIST_COLOR, e -> {
            mainCardLayout.show(mainCardPanel, profilePanelName);
        });
        contentPanel.add(viewProfileBtn, gbc);

        // Update Profile Button
        JButton updateProfileBtn = createDashboardButton("Update My Profile", PHARMACIST_COLOR, e -> {
            mainCardLayout.show(mainCardPanel, updateProfilePanelName);
        });
        contentPanel.add(updateProfileBtn, gbc);

        // Check Prescriptions Button
        JButton checkPrescriptionsBtn = createDashboardButton("Check Prescription List", PHARMACIST_COLOR, e -> {
            mainCardLayout.show(mainCardPanel, prescriptionsPanelName);
        });
        contentPanel.add(checkPrescriptionsBtn, gbc);

        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates a profile view panel for any user type
     */
    public static JPanel createProfileViewPanel(String role, String name, String id, int age,
            String email, CardLayout mainCardLayout, JPanel mainCardPanel,
            String dashboardPanelName, String medicalHistoryPanelName) {

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);

        // Get role-specific color
        Color roleColor;
        if (role.equals("Patient")) {
            roleColor = PATIENT_COLOR;
        } else if (role.equals("Doctor")) {
            roleColor = DOCTOR_COLOR;
        } else {
            roleColor = PHARMACIST_COLOR;
        }

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(roleColor);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("My Profile | " + role);
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton backButton = new JButton("Back to Dashboard");
        backButton.setBackground(new Color(189, 195, 199));
        backButton.setForeground(TEXT_COLOR);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> mainCardLayout.show(mainCardPanel, dashboardPanelName));
        headerPanel.add(backButton, BorderLayout.EAST);

        panel.add(headerPanel, BorderLayout.NORTH);

        // Profile content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Add profile information
        addProfileField(contentPanel, gbc, "Name:", name);
        gbc.gridy++;
        addProfileField(contentPanel, gbc, "ID:", id);
        gbc.gridy++;
        addProfileField(contentPanel, gbc, "Age:", String.valueOf(age));
        gbc.gridy++;
        addProfileField(contentPanel, gbc, "Email:", email);
        gbc.gridy++;

        // Add medical history button for patients only
        if (role.equals("Patient")) {
            gbc.gridy++;
            gbc.gridx = 0;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(20, 10, 10, 10);

            JButton medicalHistoryButton = new JButton("View Medical History");
            medicalHistoryButton.setBackground(PATIENT_COLOR);
            medicalHistoryButton.setForeground(Color.black);
            medicalHistoryButton.setFont(new Font("Times New Roman", Font.BOLD, 14));
            medicalHistoryButton.setFocusPainted(false);
            medicalHistoryButton.addActionListener(e -> {
                mainCardLayout.show(mainCardPanel, medicalHistoryPanelName);
            });
            contentPanel.add(medicalHistoryButton, gbc);
        }

        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    // Helper method to add profile field with label and value
    private static void addProfileField(JPanel panel, GridBagConstraints gbc, String label, String value) {
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
        fieldLabel.setForeground(TEXT_COLOR);
        panel.add(fieldLabel, gbc);

        gbc.gridx = 1;
        JLabel fieldValue = new JLabel(value);
        fieldValue.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        panel.add(fieldValue, gbc);

        gbc.gridx = 0;
    }

    /**
     * Creates a profile update panel for any user type
     */
    public static JPanel createProfileUpdatePanel(String role, String currentName, int currentAge,
            String currentEmail, CardLayout mainCardLayout, JPanel mainCardPanel,
            String dashboardPanelName, ProfileUpdateHandler updateHandler) {

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);

        // Get role-specific color
        Color roleColor;
        if (role.equals("Patient")) {
            roleColor = PATIENT_COLOR;
        } else if (role.equals("Doctor")) {
            roleColor = DOCTOR_COLOR;
        } else {
            roleColor = PHARMACIST_COLOR;
        }

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(roleColor);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Update My Profile | " + role);
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton backButton = new JButton("Back to Dashboard");
        backButton.setBackground(new Color(189, 195, 199));
        backButton.setForeground(TEXT_COLOR);
        backButton.setFont(new Font("Times New Roman", Font.BOLD, 14));
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> mainCardLayout.show(mainCardPanel, dashboardPanelName));
        headerPanel.add(backButton, BorderLayout.EAST);

        panel.add(headerPanel, BorderLayout.NORTH);

        // Form content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridwidth = 1;

        // Name field
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
        contentPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        JTextField nameField = new JTextField(currentName, 20);
        contentPanel.add(nameField, gbc);

        // Age field
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
        contentPanel.add(ageLabel, gbc);

        gbc.gridx = 1;
        SpinnerNumberModel ageModel = new SpinnerNumberModel(currentAge, 1, 120, 1);
        JSpinner ageSpinner = new JSpinner(ageModel);
        contentPanel.add(ageSpinner, gbc);

        // Email field
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
        contentPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        JTextField emailField = new JTextField(currentEmail, 20);
        contentPanel.add(emailField, gbc);

        // Update button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 10, 10, 10);
        JButton updateButton = new JButton("Save Changes");
        updateButton.setBackground(roleColor);
        updateButton.setFont(new Font("Times New Roman", Font.BOLD, 14));
        updateButton.setForeground(Color.black);
        updateButton.setFocusPainted(false);
        updateButton.addActionListener(e -> {
            // Validate inputs
            String newName = nameField.getText().trim();
            String newEmail = emailField.getText().trim();
            int newAge = (Integer) ageSpinner.getValue();

            if (newName.isEmpty() || newEmail.isEmpty()) {
                JOptionPane.showMessageDialog(panel,
                        "Please fill in all required fields",
                        "Update Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Call update handler
            boolean success = updateHandler.handleProfileUpdate(newName, newAge, newEmail);

            if (success) {
                JOptionPane.showMessageDialog(panel,
                        "Profile updated successfully!",
                        "Update Success",
                        JOptionPane.INFORMATION_MESSAGE);

                // Return to dashboard
                mainCardLayout.show(mainCardPanel, dashboardPanelName);
            }
        });
        contentPanel.add(updateButton, gbc);

        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates a panel for patients to book appointments
     */
    public static JPanel createBookAppointmentPanel(CardLayout mainCardLayout, JPanel mainCardPanel,
            String patientDashboardPanelName, AppointmentBookingHandler bookingHandler) {

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PATIENT_COLOR);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Book an Appointment");
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton backButton = new JButton("Back to Dashboard");
        backButton.setBackground(new Color(189, 195, 199));
        backButton.setForeground(TEXT_COLOR);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> mainCardLayout.show(mainCardPanel, patientDashboardPanelName));
        headerPanel.add(backButton, BorderLayout.EAST);

        panel.add(headerPanel, BorderLayout.NORTH);

        // Form content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridwidth = 1;

        // Time selector
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel timeLabel = new JLabel("Preferred Time:");
        timeLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
        contentPanel.add(timeLabel, gbc);

        gbc.gridx = 1;
        // Create 24-hour time options with 15-minute intervals
        JComboBox<String> timeDropdown = new JComboBox<>(createTimeOptions());
        timeDropdown.setSelectedIndex(32); // Default to 8:00 AM (32nd item)
        contentPanel.add(timeDropdown, gbc);

        // Day selector
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel dayLabel = new JLabel("Day of Week:");
        dayLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
        contentPanel.add(dayLabel, gbc);

        gbc.gridx = 1;
        String[] days = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
        JComboBox<String> dayDropdown = new JComboBox<>(days);
        contentPanel.add(dayDropdown, gbc);

        // Specialist type dropdown
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel specialistLabel = new JLabel("Specialist Type:");
        specialistLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
        contentPanel.add(specialistLabel, gbc);

        gbc.gridx = 1;
        String[] specialistTypes = {
                "General Practitioner",
                "Cardiologist",
                "Dermatologist",
                "Neurologist",
                "Orthopedist",
                "Pediatrician",
                "Psychiatrist",
                "Ophthalmologist"
        };
        JComboBox<String> specialistDropdown = new JComboBox<>(specialistTypes);
        contentPanel.add(specialistDropdown, gbc);

        // Reason text area
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel reasonLabel = new JLabel("Reason/Health Issue:");
        reasonLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
        contentPanel.add(reasonLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JTextArea reasonTextArea = new JTextArea(5, 20);
        reasonTextArea.setLineWrap(true);
        reasonTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(reasonTextArea);
        contentPanel.add(scrollPane, gbc);

        // Book button
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 10, 10, 10);
        JButton bookButton = new JButton("Book Appointment");
        bookButton.setBackground(PATIENT_COLOR);
        bookButton.setForeground(Color.black);
        bookButton.setFocusPainted(false);
        bookButton.addActionListener(e -> {
            // Validate inputs
            String selectedTime = (String) timeDropdown.getSelectedItem();
            String selectedDay = (String) dayDropdown.getSelectedItem();
            String selectedSpecialist = (String) specialistDropdown.getSelectedItem();
            String reason = reasonTextArea.getText().trim();

            if (reason.isEmpty()) {
                JOptionPane.showMessageDialog(panel,
                        "Please describe your health issue or reason for the appointment",
                        "Booking Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Call booking handler with the selected time and day
            boolean success = bookingHandler.handleAppointmentBooking(
                    selectedDay, selectedTime, selectedSpecialist, reason);

            if (success) {
                JOptionPane.showMessageDialog(panel,
                        "Appointment booked successfully for " + selectedDay +
                                " at " + selectedTime + " with " + selectedSpecialist,
                        "Booking Success",
                        JOptionPane.INFORMATION_MESSAGE);

                // Return to dashboard
                mainCardLayout.show(mainCardPanel, patientDashboardPanelName);
            }
        });
        contentPanel.add(bookButton, gbc);

        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Helper method to create time options with 15-minute intervals in 24-hour
     * format
     */
    private static String[] createTimeOptions() {
        String[] timeOptions = new String[96]; // 24 hours * 4 intervals = 96 options
        int index = 0;

        for (int hour = 0; hour < 24; hour++) {
            for (int minute = 0; minute < 60; minute += 15) {
                String hourStr = String.format("%02d", hour);
                String minuteStr = String.format("%02d", minute);
                timeOptions[index++] = hourStr + ":" + minuteStr;
            }
        }

        return timeOptions;
    }

    /**
     * Creates a panel for patients to view their appointments
     */
    public static JPanel createViewAppointmentsPanel(CardLayout mainCardLayout, JPanel mainCardPanel,
            String patientDashboardPanelName) {

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PATIENT_COLOR);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("My Appointments");
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton backButton = new JButton("Back to Dashboard");
        backButton.setBackground(new Color(189, 195, 199));
        backButton.setForeground(TEXT_COLOR);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> mainCardLayout.show(mainCardPanel, patientDashboardPanelName));
        headerPanel.add(backButton, BorderLayout.EAST);

        panel.add(headerPanel, BorderLayout.NORTH);

        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // For demo purposes, we'll add some sample appointments
        // In a real application, these would come from a database
        String[] columnNames = { "Date", "Specialist", "Status", "Payment Status" };
        Object[][] data = {
                { "10/06/2023", "General Practitioner", "Completed", "Paid" },
                { "15/07/2023", "Dermatologist", "Scheduled", "Pending" },
                { "22/07/2023", "Cardiologist", "Scheduled", "Not paid" }
        };

        JTable appointmentsTable = new JTable(data, columnNames);
        appointmentsTable.setFillsViewportHeight(true);
        appointmentsTable.setRowHeight(30);

        // Center text in cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < appointmentsTable.getColumnCount(); i++) {
            appointmentsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(appointmentsTable);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Add a note about payment
        JLabel noteLabel = new JLabel(
                "<html><body>Note: Please complete payment for pending appointments.<br>" +
                        "For 'Not paid' appointments, please visit the facility for payment processing.</body></html>");
        noteLabel.setFont(new Font("Times New Roman", Font.ITALIC, 12));
        noteLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        contentPanel.add(noteLabel, BorderLayout.SOUTH);

        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates a panel for doctors to set their availability
     */
    public static JPanel createSetAvailabilityPanel(CardLayout mainCardLayout, JPanel mainCardPanel,
            String doctorDashboardPanelName, AvailabilityHandler availabilityHandler) {

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DOCTOR_COLOR);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Set Your Availability");
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton backButton = new JButton("Back to Dashboard");
        backButton.setBackground(new Color(189, 195, 199));
        backButton.setForeground(TEXT_COLOR);
        backButton.setFont(new Font("Times New Roman", Font.BOLD, 14));
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> mainCardLayout.show(mainCardPanel, doctorDashboardPanelName));
        headerPanel.add(backButton, BorderLayout.EAST);

        panel.add(headerPanel, BorderLayout.NORTH);

        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);

        // Weekly schedule panel
        JPanel schedulePanel = new JPanel(new GridLayout(8, 2, 10, 15));
        schedulePanel.setBackground(BACKGROUND_COLOR);
        schedulePanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Header row
        JLabel dayLabel = new JLabel("Day");
        dayLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));
        schedulePanel.add(dayLabel);

        JLabel hoursLabel = new JLabel("Available Hours");
        hoursLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));
        schedulePanel.add(hoursLabel);

        // Days of week with time selectors
        String[] days = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
        Map<String, JComboBox<String>> timeSelections = new HashMap<>();

        for (String day : days) {
            JLabel dayNameLabel = new JLabel(day);
            dayNameLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
            schedulePanel.add(dayNameLabel);

            String[] availabilityOptions = {
                    "Not Available",
                    "8:00 AM - 12:00 PM",
                    "1:00 PM - 5:00 PM",
                    "8:00 AM - 5:00 PM"
            };
            JComboBox<String> timeDropdown = new JComboBox<>(availabilityOptions);
            schedulePanel.add(timeDropdown);
            timeSelections.put(day, timeDropdown);
        }

        // Create a scroll pane for the schedule
        JScrollPane scrollPane = new JScrollPane(schedulePanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Save button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JButton saveButton = new JButton("Save Availability");
        saveButton.setBackground(DOCTOR_COLOR);
        saveButton.setForeground(Color.black);
        saveButton.setFont(new Font("Times New Roman", Font.BOLD, 14));
        saveButton.setFocusPainted(false);
        saveButton.addActionListener(e -> {
            // Collect all availability settings
            Map<String, String> availability = new HashMap<>();
            for (String day : days) {
                availability.put(day, (String) timeSelections.get(day).getSelectedItem());
            }

            boolean success = availabilityHandler.handleAvailabilityUpdate(availability);

            if (success) {
                JOptionPane.showMessageDialog(panel,
                        "Your availability has been updated successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                mainCardLayout.show(mainCardPanel, doctorDashboardPanelName);
            }
        });

        buttonPanel.add(saveButton);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates a panel for doctors to view patient details
     */
    public static JPanel createViewPatientsPanel(CardLayout mainCardLayout, JPanel mainCardPanel,
            String doctorDashboardPanelName) {

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DOCTOR_COLOR);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Patient Appointments");
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton backButton = new JButton("Back to Dashboard");
        backButton.setBackground(new Color(189, 195, 199));
        backButton.setForeground(TEXT_COLOR);
        backButton.setFont(new Font("Times New Roman", Font.BOLD, 14));
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> mainCardLayout.show(mainCardPanel, doctorDashboardPanelName));
        headerPanel.add(backButton, BorderLayout.EAST);

        panel.add(headerPanel, BorderLayout.NORTH);

        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // For demo purposes, we'll add some sample patient appointments
        // In a real application, these would come from a database
        String[] columnNames = { "Date", "Patient Name", "Patient ID", "Reason", "Status" };
        Object[][] data = {
                { "10/06/2023", "John Doe", "P12345", "Regular checkup", "Completed" },
                { "15/07/2023", "Jane Smith", "P23456", "Skin rash", "Scheduled" },
                { "22/07/2023", "Bob Johnson", "P34567", "Chest pain", "Scheduled" }
        };

        JTable patientsTable = new JTable(data, columnNames);
        patientsTable.setFillsViewportHeight(true);
        patientsTable.setRowHeight(30);

        // Center text in cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < patientsTable.getColumnCount(); i++) {
            patientsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Add action on double click to view patient details
        patientsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = patientsTable.getSelectedRow();
                    if (row >= 0) {
                        String patientName = (String) patientsTable.getValueAt(row, 1);
                        String patientId = (String) patientsTable.getValueAt(row, 2);
                        String reason = (String) patientsTable.getValueAt(row, 3);

                        JOptionPane.showMessageDialog(panel,
                                "Patient: " + patientName + " (ID: " + patientId + ")\n" +
                                        "Reason for visit: " + reason + "\n\n" +
                                        "Medical History: Patient has a history of hypertension.\n" +
                                        "Allergies: None reported\n" +
                                        "Current Medications: Lisinopril 10mg daily",
                                "Patient Details",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(patientsTable);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        JLabel noteLabel = new JLabel("Double-click on a patient to view more details");
        noteLabel.setFont(new Font("Times New Roman", Font.ITALIC, 12));
        noteLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        contentPanel.add(noteLabel, BorderLayout.SOUTH);

        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates a panel for pharmacists to check prescription list
     */
    public static JPanel createPrescriptionsPanel(CardLayout mainCardLayout, JPanel mainCardPanel,
            String pharmacistDashboardPanelName, PrescriptionHandler prescriptionHandler) {

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PHARMACIST_COLOR);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Prescription List");
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton backButton = new JButton("Back to Dashboard");
        backButton.setBackground(new Color(189, 195, 199));
        backButton.setForeground(TEXT_COLOR);
        backButton.setFont(new Font("Times New Roman", Font.BOLD, 14));
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> mainCardLayout.show(mainCardPanel, pharmacistDashboardPanelName));
        headerPanel.add(backButton, BorderLayout.EAST);

        panel.add(headerPanel, BorderLayout.NORTH);

        // Content panel with prescriptions
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // For demo purposes, we'll add some sample prescriptions
        // In a real application, these would come from a database
        JPanel prescriptionsPanel = new JPanel();
        prescriptionsPanel.setLayout(new BoxLayout(prescriptionsPanel, BoxLayout.Y_AXIS));
        prescriptionsPanel.setBackground(BACKGROUND_COLOR);

        // Sample prescription data
        String[][] prescriptions = {
                { "John Doe", "P12345", "Dr. Smith", "Amoxicillin 500mg, 3 times daily for 7 days", "Pending" },
                { "Jane Smith", "P23456", "Dr. Johnson", "Ibuprofen 400mg, as needed for pain", "Completed" },
                { "Bob Johnson", "P34567", "Dr. Williams", "Lisinopril 10mg, once daily", "Pending" },
                { "Mary Brown", "P45678", "Dr. Smith", "Metformin 500mg, twice daily with meals", "Pending" }
        };

        // Create collapsible panels for each prescription
        for (String[] prescription : prescriptions) {
            JPanel prescriptionPanel = createPrescriptionPanel(
                    prescription[0], prescription[1], prescription[2],
                    prescription[3], prescription[4], prescriptionHandler);
            prescriptionsPanel.add(prescriptionPanel);
            prescriptionsPanel.add(Box.createVerticalStrut(10)); // Add some space between panels
        }

        JScrollPane scrollPane = new JScrollPane(prescriptionsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    // Helper method to create a prescription panel with dropdown
    private static JPanel createPrescriptionPanel(String patientName, String patientId,
            String doctorName, String medicationDetails, String currentStatus,
            PrescriptionHandler prescriptionHandler) {

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        // Header panel with patient name and doctor
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        JLabel patientLabel = new JLabel("<html><b>" + patientName + "</b> (ID: " + patientId + ")</html>");
        patientLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        headerPanel.add(patientLabel, BorderLayout.WEST);

        JLabel doctorLabel = new JLabel("Prescribed by: " + doctorName);
        doctorLabel.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        headerPanel.add(doctorLabel, BorderLayout.EAST);

        panel.add(headerPanel, BorderLayout.NORTH);

        // Medication details
        JLabel medicationLabel = new JLabel("<html>" + medicationDetails + "</html>");
        medicationLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        medicationLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panel.add(medicationLabel, BorderLayout.CENTER);

        // Status panel with dropdown
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(Color.WHITE);

        JLabel statusLabel = new JLabel("Status: ");
        statusLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        statusPanel.add(statusLabel, BorderLayout.WEST);

        String[] statuses = { "Pending", "In Progress", "Completed" };
        JComboBox<String> statusDropdown = new JComboBox<>(statuses);
        statusDropdown.setSelectedItem(currentStatus);
        statusDropdown.addActionListener(e -> {
            String newStatus = (String) statusDropdown.getSelectedItem();
            prescriptionHandler.handleStatusChange(patientId, newStatus);
        });
        statusPanel.add(statusDropdown, BorderLayout.CENTER);

        panel.add(statusPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Interface for handling profile updates
     */
    public interface ProfileUpdateHandler {
        boolean handleProfileUpdate(String newName, int newAge, String newEmail);
    }

    /**
     * Interface for handling appointment bookings
     */
    public interface AppointmentBookingHandler {
        boolean handleAppointmentBooking(String day, String time, String specialistType, String reason);
    }

    /**
     * Interface for handling doctor availability updates
     */
    public interface AvailabilityHandler {
        boolean handleAvailabilityUpdate(Map<String, String> availability);
    }

    /**
     * Interface for handling prescription status changes
     */
    public interface PrescriptionHandler {
        boolean handleStatusChange(String patientId, String newStatus);
    }
}