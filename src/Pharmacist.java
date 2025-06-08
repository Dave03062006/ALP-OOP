import java.sql.*;
import java.util.*;
import java.time.*;
import java.time.temporal.ChronoUnit;

public class Pharmacist extends Staff {
    protected Queue queue;
    protected int pharmacistId;
    Scanner s = new Scanner(System.in);

    public int getPharmacistId() {
        return pharmacistId;
    }

    public void setPharmacistId(int pharmacistId) {
        this.pharmacistId = pharmacistId;
    }

    public Queue getQueue() {
        return queue;
    }

    public Pharmacist(String email, String password, String fullName, String phoneNumber, boolean onDuty) {
        super(email, password, fullName, phoneNumber, onDuty);
        this.queue = new Queue();
        this.pharmacistId = 0; // Default ID, will be set when registered
    }

    // Add this constructor if you want to create from DB with ID
    public Pharmacist(int pharmacistId, String email, String password, String fullName, String phoneNumber,
            boolean onDuty) {
        super(email, password, fullName, phoneNumber, onDuty);
        this.pharmacistId = pharmacistId;
        this.queue = new Queue();
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Role: Pharmacist");
        System.out.println("ID: " + pharmacistId);
        System.out.println("Queue size: " + queue.size());
    }

    public void updatePharmacistInfo() {
        System.out.println("Updating pharmacist information...");
        System.out.println("Current email: " + this.email);
        System.out.println("Entering new email: " + email);
        String email = s.next() + s.nextLine();
        if (email.trim().isEmpty()) {
            email = this.email;
        }
        System.out.println("Current password: " + this.password);
        System.out.println("Entering new password: " + password);
        String password = s.next() + s.nextLine();
        if (password.trim().isEmpty()) {
            password = this.password;
        }
        System.out.println("Current full name: " + this.fullName);
        System.out.println("Entering new full name: " + fullName);
        String fullName = s.next() + s.nextLine();
        if (fullName.trim().isEmpty()) {
            fullName = this.fullName;
        }
        System.out.println("Current phone number: " + this.phoneNumber);
        System.out.println("Entering new phone number: " + phoneNumber);
        String phoneNumber = s.next() + s.nextLine();
        if (phoneNumber.trim().isEmpty()) {
            phoneNumber = this.phoneNumber;
        }

        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "UPDATE pharmacists SET email = ?, password = ?, full_name = ?, phone_number = ? WHERE pharmacist_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            pstmt.setString(3, fullName);
            pstmt.setString(4, phoneNumber);
            pstmt.setInt(5, this.pharmacistId);

            int result = pstmt.executeUpdate();
            if (result > 0) {
                System.out.println("Pharmacist information updated successfully in database.");
            } else {
                System.out.println("No pharmacist found with ID: " + this.pharmacistId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void login(String email, String password) {
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT * FROM pharmacists WHERE email = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                this.pharmacistId = rs.getInt("pharmacist_id");
                this.email = rs.getString("email");
                this.fullName = rs.getString("full_name");
                this.phoneNumber = rs.getString("phone_number");
                System.out.println("Pharmacist login successful!");
            } else {
                System.out.println("Invalid credentials!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Pharmacist logged in with email: " + email);
    }

    @Override
    public void register(String email, String password, String fullName, String phoneNumber) {
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "INSERT INTO pharmacists (email, password, full_name, phone_number) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            pstmt.setString(3, fullName);
            pstmt.setString(4, phoneNumber);

            int result = pstmt.executeUpdate();
            if (result > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    this.pharmacistId = generatedKeys.getInt(1);
                }
                this.email = email;
                this.password = password;
                this.fullName = fullName;
                this.phoneNumber = phoneNumber;
                System.out.println("Pharmacist registered successfully with ID: " + this.pharmacistId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Pharmacist registered with email: " + email);
    }

    public void addMedicinesToQueue(List<Medicine> medicines) {
        for (Medicine medicine : medicines) {
            queue.enqueue(medicine);
        }
        System.out.println("Medicines added to queue. Current queue size: " + queue.size());
    }

    public void processMedicines() {
        System.out.println("Pharmacist " + getFullName() + " processing medicines:");
        while (!queue.isEmpty()) {
            Medicine medicine = queue.dequeue();
            System.out.println("- Processed: " + medicine.getMedicineName() + " (" + medicine.getDose() + ")");
        }
        System.out.println("All medicines processed!");
    }

    public void fulfillPrescription(Appointment appointment) {
        if (appointment.getPrescription() != null) {
            appointment.setPharmacist(this);
            addMedicinesToQueue(appointment.getMedicines());
        }
    }

    public void processPrescriptions() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("     PENDING PRESCRIPTIONS");
        System.out.println("=".repeat(50));

        try {
            // Get all pending prescriptions
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT p.prescription_id, p.recipe_description, " +
                    "pt.patient_id, pt.full_name as patient_name, " +
                    "d.doctor_id, d.full_name as doctor_name, d.specialist " +
                    "FROM prescriptions p " +
                    "JOIN patients pt ON p.patient_id = pt.patient_id " +
                    "JOIN doctors d ON p.doctor_id = d.doctor_id " +
                    "WHERE p.status = 'PENDING' OR p.status = 'IN_PROGRESS'";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // Check if prescriptions exist
            if (!rs.isBeforeFirst()) {
                System.out.println("No pending prescriptions found.");
                return;
            }

            // Display all pending prescriptions
            System.out.printf("%-5s %-20s %-20s %-15s %-30s\n",
                    "ID", "Patient", "Doctor", "Specialist", "Description");
            System.out.println("-".repeat(90));

            while (rs.next()) {
                System.out.printf("%-5d %-20s %-20s %-15s %-30s\n",
                        rs.getInt("prescription_id"),
                        rs.getString("patient_name"),
                        rs.getString("doctor_name"),
                        rs.getString("specialist"),
                        rs.getString("recipe_description").substring(0,
                                Math.min(rs.getString("recipe_description").length(), 30)));
            }

            // Ask pharmacist to select a prescription to process
            System.out.println("\nEnter prescription ID to process or 0 to cancel: ");
            int selectedId = s.nextInt();

            if (selectedId == 0) {
                System.out.println("Operation cancelled.");
                return;
            }

            // Get the selected prescription details
            sql = "SELECT p.*, pt.full_name as patient_name, d.full_name as doctor_name " +
                    "FROM prescriptions p " +
                    "JOIN patients pt ON p.patient_id = pt.patient_id " +
                    "JOIN doctors d ON p.doctor_id = d.doctor_id " +
                    "WHERE p.prescription_id = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, selectedId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                int patientId = rs.getInt("patient_id");
                int doctorId = rs.getInt("doctor_id");
                String recipeDescription = rs.getString("recipe_description");
                String patientName = rs.getString("patient_name");
                String doctorName = rs.getString("doctor_name");

                System.out.println("\n" + "=".repeat(50));
                System.out.println("PROCESSING PRESCRIPTION #" + selectedId);
                System.out.println("=".repeat(50));
                System.out.println("Patient: " + patientName);
                System.out.println("Doctor: " + doctorName);
                System.out.println("Details: " + recipeDescription);

                // Get medicines for this prescription
                sql = "SELECT m.* FROM medicines m WHERE m.prescription_id = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, selectedId);
                ResultSet medicineRs = pstmt.executeQuery();

                List<Medicine> medicines = new ArrayList<>();

                System.out.println("\nRequired Medicines:");
                int count = 1;

                // Use a list to track medicines sorted by expiry date
                List<Medicine> sortedMedicines = new ArrayList<>();

                while (medicineRs.next()) {
                    int medicineId = medicineRs.getInt("medicine_id");
                    String medicineName = medicineRs.getString("medicine_name");
                    String dosage = medicineRs.getString("dose");
                    String category = medicineRs.getString("category");
                    String usage = medicineRs.getString("usage_instruction");
                    LocalDate expiry = LocalDate.parse(medicineRs.getString("expired_date"));

                    // Create medicine object
                    Medicine medicine = new Medicine(medicineId, medicineName, usage, dosage, category, expiry);
                    medicines.add(medicine);
                    sortedMedicines.add(medicine);

                    System.out.println(count + ". " + medicineName + " - " + dosage +
                            " (" + category + ") Expires: " + expiry);
                    count++;
                }

                // Sort medicines by expiry date (earliest first)
                Collections.sort(sortedMedicines, (m1, m2) -> m1.getExpiredDate().compareTo(m2.getExpiredDate()));

                // Display medicines sorted by expiry date
                System.out.println("\nMedicines organized by expiry date (earliest first):");
                for (int i = 0; i < sortedMedicines.size(); i++) {
                    Medicine medicine = sortedMedicines.get(i);
                    System.out.println((i + 1) + ". " + medicine.getMedicineName() +
                            " - Expires: " + medicine.getExpiredDate());
                }

                System.out.println("\nDo you want to process this prescription? (Y/N)");
                String confirm = s.next();

                if (confirm.equalsIgnoreCase("Y")) {
                    // Update prescription status
                    sql = "UPDATE prescriptions SET status = 'COMPLETED', pharmacist_id = ? WHERE prescription_id = ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, this.getPharmacistId());
                    pstmt.setInt(2, selectedId);
                    int result = pstmt.executeUpdate();

                    if (result > 0) {
                        System.out.println("Prescription processed successfully!");

                        // Add medicines to queue
                        addMedicinesToQueue(medicines);

                        // Process medicines
                        processMedicines();
                    } else {
                        System.out.println("Failed to process prescription.");
                    }
                } else {
                    System.out.println("Operation cancelled.");
                }
            } else {
                System.out.println("Prescription not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error processing prescriptions: " + e.getMessage());
        }
    }

    // Load all pharmacists from database into ArrayList
    public static ArrayList<Pharmacist> loadAllFromDatabase() {
        ArrayList<Pharmacist> pharmacists = new ArrayList<>();
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT * FROM pharmacists";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Pharmacist pharmacist = new Pharmacist(
                        rs.getInt("pharmacist_id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getString("phone_number"),
                        true);
                pharmacists.add(pharmacist);
            }
            System.out.println("Loaded " + pharmacists.size() + " pharmacists from database");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pharmacists;
    }

    public void viewMedicineInventory() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("     MEDICINE INVENTORY");
        System.out.println("=".repeat(50));

        try {
            Connection conn = DatabaseConnect.getConnection();

            // Get summary counts
            String countSql = "SELECT " +
                    "COUNT(*) as total, " +
                    "SUM(CASE WHEN expired_date >= CURRENT_DATE THEN 1 ELSE 0 END) as valid, " +
                    "SUM(CASE WHEN expired_date < CURRENT_DATE THEN 1 ELSE 0 END) as expired " +
                    "FROM medicines";

            Statement countStmt = conn.createStatement();
            ResultSet countRs = countStmt.executeQuery(countSql);

            if (countRs.next()) {
                System.out.println("Total Medicines: " + countRs.getInt("total"));
                System.out.println("Valid Medicines: " + countRs.getInt("valid"));
                System.out.println("Expired Medicines: " + countRs.getInt("expired"));
            }

            // Get all categories
            String categorySql = "SELECT DISTINCT category FROM medicines ORDER BY category";
            Statement categoryStmt = conn.createStatement();
            ResultSet categoryRs = categoryStmt.executeQuery(categorySql);

            // Store categories in a list
            List<String> categories = new ArrayList<>();
            while (categoryRs.next()) {
                categories.add(categoryRs.getString("category"));
            }

            // Display inventory by category
            System.out.println("\n" + "=".repeat(50));
            System.out.println("INVENTORY BY CATEGORY");

            for (String category : categories) {
                System.out.println("\nCategory: " + category);
                System.out.println("-".repeat(30));

                // Select medicines for this category, sorted by expiry date
                String sql = "SELECT * FROM medicines WHERE category = ? ORDER BY expired_date";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, category);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    String medicineName = rs.getString("medicine_name");
                    String dosage = rs.getString("dose");
                    LocalDate expiry = LocalDate.parse(rs.getString("expired_date"));
                    boolean isExpired = expiry.isBefore(LocalDate.now());

                    System.out.printf("%-25s %-15s %-15s %s\n",
                            medicineName,
                            dosage,
                            expiry,
                            isExpired ? "EXPIRED" : "VALID");
                }
            }

            // Option to view details of a specific medicine
            System.out.println("\nEnter medicine ID to view details or 0 to return: ");
            int selectedId = s.nextInt();

            if (selectedId > 0) {
                String sql = "SELECT * FROM medicines WHERE medicine_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, selectedId);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    System.out.println("\n" + "=".repeat(50));
                    System.out.println("MEDICINE DETAILS");
                    System.out.println("=".repeat(50));
                    System.out.println("ID: " + rs.getInt("medicine_id"));
                    System.out.println("Name: " + rs.getString("medicine_name"));
                    System.out.println("Dosage: " + rs.getString("dose"));
                    System.out.println("Category: " + rs.getString("category"));
                    System.out.println("Usage: " + rs.getString("usage_instruction"));
                    System.out.println("Expiry Date: " + rs.getString("expired_date"));

                    LocalDate expiry = LocalDate.parse(rs.getString("expired_date"));
                    long daysToExpiry = ChronoUnit.DAYS.between(LocalDate.now(), expiry);

                    if (daysToExpiry < 0) {
                        System.out.println("Status: EXPIRED " + Math.abs(daysToExpiry) + " days ago");
                    } else {
                        System.out.println("Status: VALID (Expires in " + daysToExpiry + " days)");
                    }
                } else {
                    System.out.println("Medicine not found.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error viewing medicine inventory: " + e.getMessage());
        }
    }
}