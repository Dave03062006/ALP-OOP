import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PanelFactory {
    // Colorse
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185); // Medical blue
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241); // Light gray
    private static final Color TEXT_COLOR = new Color(44, 62, 80); // Dark blue/gray
    private static final Color BUTTON_COLOR = new Color(52, 152, 219); // Lighter blue
    private static final Color PATIENT_COLOR = new Color(46, 204, 113); // Green
    private static final Color DOCTOR_COLOR = new Color(52, 152, 219); // Blue
    private static final Color PHARMACIST_COLOR = new Color(155, 89, 182); // Purple

    /**
     * Creates the title panel shown at the top of the application.
     */
    public static JPanel createTitlePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(PRIMARY_COLOR);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("Medical System");
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        panel.add(titleLabel, BorderLayout.CENTER);

        return panel;
    }

    public static JPanel createRoleSelectionPanel(CardLayout cardLayout, JPanel cardPanel,
            String patientPanelName, String doctorPanelName, String pharmacistPanelName, String loginPanelName) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);

        // Heading
        JLabel headingLabel = new JLabel("Welcome to the Medical System");
        headingLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        headingLabel.setForeground(TEXT_COLOR);
        headingLabel.setHorizontalAlignment(JLabel.CENTER);
        headingLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.add(headingLabel, BorderLayout.CENTER);

        // Instructions
        JLabel instructionLabel = new JLabel("Please select your role to register:");
        instructionLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        instructionLabel.setForeground(TEXT_COLOR);
        instructionLabel.setHorizontalAlignment(JLabel.CENTER);
        instructionLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        headerPanel.add(instructionLabel, BorderLayout.SOUTH);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Role buttons panel
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 0, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        // Patient button
        JButton patientButton = createRoleButton("Patient", PATIENT_COLOR, e -> {
            cardLayout.show(cardPanel, patientPanelName);
        });

        // Doctor button
        JButton doctorButton = createRoleButton("Doctor", DOCTOR_COLOR, e -> {
            cardLayout.show(cardPanel, doctorPanelName);
        });

        // Pharmacist button
        JButton pharmacistButton = createRoleButton("Pharmacist", PHARMACIST_COLOR, e -> {
            cardLayout.show(cardPanel, pharmacistPanelName);
        });

        buttonPanel.add(patientButton);
        buttonPanel.add(doctorButton);
        buttonPanel.add(pharmacistButton);

        panel.add(buttonPanel, BorderLayout.CENTER);

        // Already have an account link
        JPanel loginLinkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginLinkPanel.setBackground(BACKGROUND_COLOR);
        loginLinkPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));

        JLabel loginLink = new JLabel("Already have an account? Login");
        loginLink.setForeground(PRIMARY_COLOR);
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(cardPanel, loginPanelName);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                loginLink.setText("<html><u>Already have an account? Login</u></html>");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginLink.setText("Already have an account? Login");
            }
        });

        loginLinkPanel.add(loginLink);
        panel.add(loginLinkPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Helper method to create a styled role button.
     */
    public static JButton createRoleButton(String role, Color color, ActionListener actionListener) {
        JButton button = new JButton("Register as " + role);
        button.setBackground(color);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Times New Roman", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 60));
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.addActionListener(actionListener);
        return button;
    }

    public static JPanel createLoginPanel(CardLayout cardLayout, JPanel cardPanel,
            String roleSelectionPanelName, LoginHandler loginHandler) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Login label
        JLabel loginLabel = new JLabel("Login to your account");
        loginLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));
        loginLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(loginLabel, gbc);

        // Role selector
        JLabel roleLabel = new JLabel("Login as:");
        roleLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(roleLabel, gbc);

        String[] roles = { "Patient", "Doctor", "Pharmacist" };
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(roleComboBox, gbc);

        // Username
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panel.add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(usernameField, gbc);

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(passwordField, gbc);

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(BUTTON_COLOR);
        loginButton.setForeground(Color.BLACK);
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(e -> {
            String selectedRole = (String) roleComboBox.getSelectedItem();
            loginHandler.handleLogin(selectedRole, usernameField.getText(),
                    new String(passwordField.getPassword()));
        });
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.insets = new Insets(15, 5, 5, 5);
        panel.add(loginButton, gbc);

        // Register link
        JPanel linkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        linkPanel.setBackground(BACKGROUND_COLOR);
        JLabel registerLink = new JLabel("Don't have an account? Register");
        registerLink.setForeground(BUTTON_COLOR);
        registerLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(cardPanel, roleSelectionPanelName);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                registerLink.setText("<html><u>Don't have an account? Register</u></html>");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                registerLink.setText("Don't have an account? Register");
            }
        });
        linkPanel.add(registerLink);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.insets = new Insets(10, 5, 5, 5);
        panel.add(linkPanel, gbc);

        return panel;
    }

    public static JPanel createRegistrationPanel(String role, CardLayout cardLayout, JPanel cardPanel,
            String roleSelectionPanelName, String loginPanelName, RegistrationHandler registrationHandler) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Register label
        JLabel registerLabel = new JLabel("Register as " + role);
        registerLabel.setFont(new Font("Times New", Font.BOLD, 16));
        registerLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(registerLabel, gbc);

        // Full Name
        JLabel fullNameLabel = new JLabel("Full Name:");
        fullNameLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(fullNameLabel, gbc);

        JTextField fullNameField = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(fullNameField, gbc);

        // Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(emailLabel, gbc);

        JTextField emailField = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(emailField, gbc);

        // Removed ID Number field since it will be auto-generated by the database

        // Username
        JLabel usernameLabel = new JLabel("Create Username:");
        usernameLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 5; // Adjusted position
        panel.add(usernameLabel, gbc);

        JTextField newUsernameField = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 6; // Adjusted position
        panel.add(newUsernameField, gbc);

        // Password
        JLabel passwordLabel = new JLabel("Create Password:");
        passwordLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 7; // Adjusted position
        panel.add(passwordLabel, gbc);

        JPasswordField newPasswordField = new JPasswordField(20);
        gbc.gridx = 0;
        gbc.gridy = 8; // Adjusted position
        panel.add(newPasswordField, gbc);

        // Confirm Password
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 9; // Adjusted position
        panel.add(confirmPasswordLabel, gbc);

        JPasswordField confirmPasswordField = new JPasswordField(20);
        gbc.gridx = 0;
        gbc.gridy = 10; // Adjusted position
        panel.add(confirmPasswordField, gbc);

        // Button Panel for two buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        // Back button
        JButton backButton = new JButton("Back");
        backButton.setBackground(new Color(189, 195, 199));
        backButton.setForeground(TEXT_COLOR);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> cardLayout.show(cardPanel, roleSelectionPanelName));

        // Register button
        JButton registerButton = new JButton("Register");

        // Set button color based on role
        Color roleColor;
        if (role.equals("Patient")) {
            roleColor = PATIENT_COLOR;
        } else if (role.equals("Doctor")) {
            roleColor = DOCTOR_COLOR;
        } else {
            roleColor = PHARMACIST_COLOR;
        }

        registerButton.setBackground(roleColor);
        registerButton.setForeground(Color.BLACK);
        registerButton.setFocusPainted(false);
        registerButton.addActionListener(e -> {
            boolean success = registrationHandler.handleRegistration(
                    role,
                    fullNameField.getText(),
                    emailField.getText(),
                    "", // Empty string for ID as it will be auto-generated
                    newUsernameField.getText(),
                    new String(newPasswordField.getPassword()),
                    new String(confirmPasswordField.getPassword()));

            if (success) {
                cardLayout.show(cardPanel, loginPanelName);
            }
        });

        buttonPanel.add(backButton);
        buttonPanel.add(registerButton);

        gbc.gridx = 0;
        gbc.gridy = 11; // Adjusted position
        gbc.insets = new Insets(15, 5, 5, 5);
        panel.add(buttonPanel, gbc);

        // Login link
        JPanel linkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        linkPanel.setBackground(BACKGROUND_COLOR);
        JLabel loginLink = new JLabel("Already have an account? Login");
        loginLink.setForeground(PRIMARY_COLOR);
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(cardPanel, loginPanelName);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                loginLink.setText("<html><u>Already have an account? Login</u></html>");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginLink.setText("Already have an account? Login");
            }
        });
        linkPanel.add(loginLink);

        gbc.gridx = 0;
        gbc.gridy = 12; // Adjusted position
        gbc.insets = new Insets(10, 5, 5, 5);
        panel.add(linkPanel, gbc);

        return panel;
    }

    /**
     * Interface for handling login actions
     */
    public interface LoginHandler {
        void handleLogin(String role, String username, String password);
    }

    /**
     * Interface for handling registration actions
     */
    public interface RegistrationHandler {
        boolean handleRegistration(String role, String fullName, String email,
                String specificId, String username, String password, String confirmPassword);
    }
}
