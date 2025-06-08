package model;
import java.sql.*;
import java.util.*;
import logic.DatabaseConnect;
import logic.Queue;
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

    // Constructors
    public Pharmacist(String email, String password, String fullName, String phoneNumber, boolean onDuty) {
        super(email, password, fullName, phoneNumber, onDuty);
        this.queue = new Queue();
        this.pharmacistId = 0;
    }

    public Pharmacist(int pharmacistId, String email, String password, String fullName, String phoneNumber,
            boolean onDuty) {
        super(email, password, fullName, phoneNumber, onDuty);
        this.pharmacistId = pharmacistId;
        this.queue = new Queue();
    }

    @Override
    public void displayInfo() {
        System.out.println("\n=== PHARMACIST PROFILE ===");
        System.out.println("Pharmacist ID: " + this.getPharmacistId());
        System.out.println("Name: " + this.getFullName());
        System.out.println("Email: " + this.getEmail());
        System.out.println("Phone: " + this.getPhoneNumber());
        System.out.println("On Duty: " + (this.isOnDuty() ? "Yes" : "No"));
        System.out.println("Queue size: " + queue.size());
        System.out.println("=========================");
    }

    public void updatePharmacistInfo() {
        System.out.println("\n--- UPDATE PHARMACIST PROFILE ---");
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

        System.out.println("Current On Duty Status: " + this.isOnDuty());
        System.out.print("Are you on duty? (true/false, or press Enter to keep current): ");
        String onDutyStr = s.nextLine();
        boolean onDuty = this.isOnDuty();
        if (!onDutyStr.trim().isEmpty()) {
            onDuty = Boolean.parseBoolean(onDutyStr);
        }

        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "UPDATE pharmacists SET email = ?, full_name = ?, phone_number = ? WHERE pharmacist_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, fullName);
            pstmt.setString(3, phoneNumber);
            pstmt.setInt(4, this.pharmacistId);

            int result = pstmt.executeUpdate();
            if (result > 0) {
                this.setEmail(email);
                this.setFullName(fullName);
                this.setPhoneNumber(phoneNumber);
                this.setOnDuty(onDuty);
                System.out.println("✅ Profile updated successfully!");
            } else {
                System.out.println("❌ No pharmacist found with ID: " + this.pharmacistId);
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
            String sql = "SELECT * FROM pharmacists WHERE email = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                this.pharmacistId = rs.getInt("pharmacist_id");
                this.email = rs.getString("email");
                this.password = rs.getString("password");
                this.fullName = rs.getString("full_name");
                this.phoneNumber = rs.getString("phone_number");
                System.out.println("Pharmacist login successful!");
            } else {
                System.out.println("Invalid credentials!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public void processPrescriptions() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("     PENDING PRESCRIPTIONS");
        System.out.println("=".repeat(50));

        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT p.prescription_id, p.recipe_description, p.status, " +
                        "pt.patient_id, pt.full_name as patient_name, " +
                        "d.doctor_id, d.full_name as doctor_name, d.specialist " +
                        "FROM prescriptions p " +
                        "JOIN patients pt ON p.patient_id = pt.patient_id " +
                        "JOIN doctors d ON p.doctor_id = d.doctor_id " +
                        "WHERE p.status IN ('PENDING', 'IN_PROGRESS') " +
                        "ORDER BY p.prescription_id";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // Check if prescriptions exist
            boolean hasPrescriptions = false;
            ArrayList<Integer> prescriptionIds = new ArrayList<>();
            
            System.out.printf("%-5s %-20s %-20s %-15s %-10s\n",
                    "ID", "Patient", "Doctor", "Specialist", "Status");
            System.out.println("-".repeat(80));

            while (rs.next()) {
                hasPrescriptions = true;
                int prescId = rs.getInt("prescription_id");
                prescriptionIds.add(prescId);
                
                System.out.printf("%-5d %-20s %-20s %-15s %-10s\n",
                        prescId,
                        rs.getString("patient_name"),
                        rs.getString("doctor_name"),
                        rs.getString("specialist"),
                        rs.getString("status"));
            }

            if (!hasPrescriptions) {
                System.out.println("No pending prescriptions found.");
                return;
            }

            // Ask pharmacist to select a prescription to process
            System.out.print("\nEnter prescription ID to process (or 0 to cancel): ");
            int selectedId = 0;
            try {
                selectedId = s.nextInt();
                s.nextLine(); // Consume newline
            } catch (Exception e) {
                System.out.println("❌ Invalid input.");
                s.nextLine();
                return;
            }

            if (selectedId == 0) {
                System.out.println("Operation cancelled.");
                return;
            }

            if (!prescriptionIds.contains(selectedId)) {
                System.out.println("❌ Invalid prescription ID.");
                return;
            }

            // Process the selected prescription
            processSelectedPrescription(selectedId);

        } catch (SQLException e) {
            System.out.println("❌ Error processing prescriptions: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void processSelectedPrescription(int prescriptionId) {
        try {
            Connection conn = DatabaseConnect.getConnection();
            
            // Get prescription details
            String sql = "SELECT p.*, pt.full_name as patient_name, d.full_name as doctor_name " +
                        "FROM prescriptions p " +
                        "JOIN patients pt ON p.patient_id = pt.patient_id " +
                        "JOIN doctors d ON p.doctor_id = d.doctor_id " +
                        "WHERE p.prescription_id = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, prescriptionId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int patientId = rs.getInt("patient_id");
                int doctorId = rs.getInt("doctor_id");
                String recipeDescription = rs.getString("recipe_description");
                String patientName = rs.getString("patient_name");
                String doctorName = rs.getString("doctor_name");

                System.out.println("\n" + "=".repeat(50));
                System.out.println("PROCESSING PRESCRIPTION #" + prescriptionId);
                System.out.println("=".repeat(50));
                System.out.println("Patient: " + patientName);
                System.out.println("Doctor: " + doctorName);
                System.out.println("Details: " + recipeDescription);

                // Get medicines for this prescription
                String medicinesSql = "SELECT * FROM medicines WHERE prescription_id = ?";
                PreparedStatement medicinesStmt = conn.prepareStatement(medicinesSql);
                medicinesStmt.setInt(1, prescriptionId);
                ResultSet medicineRs = medicinesStmt.executeQuery();

                List<Medicine> medicines = new ArrayList<>();
                System.out.println("\nRequired Medicines:");
                int count = 1;

                boolean hasMedicines = false;
                while (medicineRs.next()) {
                    hasMedicines = true;
                    int medicineId = medicineRs.getInt("medicine_id");
                    String medicineName = medicineRs.getString("medicine_name");
                    String dosage = medicineRs.getString("dose");
                    String category = medicineRs.getString("category");
                    String usage = medicineRs.getString("usage_instruction");
                    LocalDate expiry = LocalDate.parse(medicineRs.getString("expired_date"));

                    Medicine medicine = new Medicine(medicineId, medicineName, usage, dosage, category, expiry);
                    medicines.add(medicine);

                    System.out.println(count + ". " + medicineName + " - " + dosage +
                            " (" + category + ") Expires: " + expiry);
                    count++;
                }

                if (!hasMedicines) {
                    System.out.println("No specific medicines assigned to this prescription.");
                    System.out.println("Prescription contains general instructions: " + recipeDescription);
                }

                System.out.print("\nDo you want to process this prescription? (Y/N): ");
                String confirm = s.nextLine();

                if (confirm.equalsIgnoreCase("Y")) {
                    // Update prescription status
                    String updateSql = "UPDATE prescriptions SET status = 'COMPLETED', pharmacist_id = ? WHERE prescription_id = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                    updateStmt.setInt(1, this.getPharmacistId());
                    updateStmt.setInt(2, prescriptionId);
                    int result = updateStmt.executeUpdate();

                    if (result > 0) {
                        System.out.println("✅ Prescription processed successfully!");

                        // Add medicines to queue and process them
                        if (!medicines.isEmpty()) {
                            addMedicinesToQueue(medicines);
                            processMedicines();
                            
                            // Update medicines with pharmacist ID
                            for (Medicine medicine : medicines) {
                                String updateMedSql = "UPDATE medicines SET pharmacist_id = ? WHERE medicine_id = ?";
                                PreparedStatement updateMedStmt = conn.prepareStatement(updateMedSql);
                                updateMedStmt.setInt(1, this.getPharmacistId());
                                updateMedStmt.setInt(2, medicine.getMedicineId());
                                updateMedStmt.executeUpdate();
                            }
                        }
                    } else {
                        System.out.println("❌ Failed to process prescription.");
                    }
                } else {
                    System.out.println("Operation cancelled.");
                }
            } else {
                System.out.println("❌ Prescription not found.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Error processing prescription: " + e.getMessage());
            e.printStackTrace();
        }
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
            String categorySql = "SELECT DISTINCT category FROM medicines WHERE category IS NOT NULL ORDER BY category";
            Statement categoryStmt = conn.createStatement();
            ResultSet categoryRs = categoryStmt.executeQuery(categorySql);

            List<String> categories = new ArrayList<>();
            while (categoryRs.next()) {
                categories.add(categoryRs.getString("category"));
            }

            // Display inventory by category
            System.out.println("\n" + "=".repeat(50));
            System.out.println("INVENTORY BY CATEGORY");

            for (String category : categories) {
                System.out.println("\nCategory: " + category);
                System.out.println("-".repeat(80));
                System.out.printf("%-25s %-15s %-15s %-10s %-15s\n", 
                    "Medicine Name", "Dosage", "Expiry Date", "Status", "Assigned To");
                System.out.println("-".repeat(80));

                String sql = "SELECT m.*, p.full_name as patient_name " +
                           "FROM medicines m " +
                           "LEFT JOIN patients p ON m.patient_id = p.patient_id " +
                           "WHERE m.category = ? " +
                           "ORDER BY m.expired_date";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, category);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    String medicineName = rs.getString("medicine_name");
                    String dosage = rs.getString("dose");
                    LocalDate expiry = LocalDate.parse(rs.getString("expired_date"));
                    boolean isExpired = expiry.isBefore(LocalDate.now());
                    String patientName = rs.getString("patient_name");
                    String assignedTo = (patientName != null) ? patientName : "Available";

                    System.out.printf("%-25s %-15s %-15s %-10s %-15s\n",
                            medicineName.length() > 24 ? medicineName.substring(0, 24) : medicineName,
                            dosage.length() > 14 ? dosage.substring(0, 14) : dosage,
                            expiry.toString(),
                            isExpired ? "EXPIRED" : "VALID",
                            assignedTo.length() > 14 ? assignedTo.substring(0, 14) : assignedTo);
                }
            }

            // Option to view details of a specific medicine
            System.out.print("\nEnter medicine ID to view details (or 0 to return): ");
            int selectedId = 0;
            try {
                selectedId = s.nextInt();
                s.nextLine(); // Consume newline
            } catch (Exception e) {
                System.out.println("❌ Invalid input.");
                s.nextLine();
                return;
            }

            if (selectedId > 0) {
                viewMedicineDetails(selectedId);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error viewing medicine inventory: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void viewMedicineDetails(int medicineId) {
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT m.*, p.full_name as patient_name, pr.full_name as pharmacist_name " +
                        "FROM medicines m " +
                        "LEFT JOIN patients p ON m.patient_id = p.patient_id " +
                        "LEFT JOIN pharmacists pr ON m.pharmacist_id = pr.pharmacist_id " +
                        "WHERE m.medicine_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, medicineId);
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

                String patientName = rs.getString("patient_name");
                String pharmacistName = rs.getString("pharmacist_name");

                if (patientName != null) {
                    System.out.println("Assigned to Patient: " + patientName);
                }
                if (pharmacistName != null) {
                    System.out.println("Processed by Pharmacist: " + pharmacistName);
                }

                System.out.println("=".repeat(50));
            } else {
                System.out.println("❌ Medicine not found.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error retrieving medicine details: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Static method to load all pharmacists
    public static ArrayList<Pharmacist> loadAllFromDatabase() {
        ArrayList<Pharmacist> pharmacists = new ArrayList<>();
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT * FROM pharmacists ORDER BY full_name";
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

    public void manageQueue() {
        System.out.println("\n--- MEDICINE QUEUE MANAGEMENT ---");
        System.out.println("Current queue size: " + queue.size());
        
        if (queue.isEmpty()) {
            System.out.println("Queue is empty. Load medicines from prescriptions first.");
            return;
        }
        
        System.out.println("1. View queue contents");
        System.out.println("2. Process next medicine");
        System.out.println("3. Process all medicines");
        System.out.println("4. Clear queue");
        System.out.println("5. Back to dashboard");
        System.out.print("Enter your choice: ");
        
        try {
            int choice = s.nextInt();
            s.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    viewQueueContents();
                    break;
                case 2:
                    processNextMedicine();
                    break;
                case 3:
                    processMedicines();
                    break;
                case 4:
                    clearQueue();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("❌ Invalid choice.");
            }
        } catch (Exception e) {
            System.out.println("❌ Invalid input.");
            s.nextLine();
        }
    }
    
    private void viewQueueContents() {
        System.out.println("\n--- QUEUE CONTENTS ---");
        if (queue.isEmpty()) {
            System.out.println("Queue is empty.");
            return;
        }
        
        // Since we can't peek into all elements without dequeuing, 
        // we'll show the queue size and next medicine
        Medicine nextMedicine = queue.peek();
        if (nextMedicine != null) {
            System.out.println("Next medicine to process: " + nextMedicine.getMedicineName() + 
                             " (" + nextMedicine.getDose() + ")");
            System.out.println("Total medicines in queue: " + queue.size());
        }
    }
    
    private void processNextMedicine() {
        if (queue.isEmpty()) {
            System.out.println("❌ Queue is empty.");
            return;
        }
        
        Medicine medicine = queue.dequeue();
        System.out.println("✅ Processed: " + medicine.getMedicineName() + " (" + medicine.getDose() + ")");
        System.out.println("Remaining in queue: " + queue.size());
    }
    
    private void clearQueue() {
        while (!queue.isEmpty()) {
            queue.dequeue();
        }
        System.out.println("✅ Queue cleared.");
    }
}