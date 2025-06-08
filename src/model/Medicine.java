package model;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import logic.DatabaseConnect;

public class Medicine {
    protected int medicineId;
    protected String medicineName;
    protected String usageInstruction;
    protected String dose;
    protected String category;
    protected LocalDate expiredDate;

    // Getters and Setters
    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getUsageInstruction() {
        return usageInstruction;
    }

    public void setUsageInstruction(String usageInstruction) {
        this.usageInstruction = usageInstruction;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(LocalDate expiredDate) {
        this.expiredDate = expiredDate;
    }

    // Utility method to check if medicine is expired
    public boolean isExpired() {
        return LocalDate.now().isAfter(expiredDate);
    }

    // Constructor when there is no medicineId (e.g., when creating a new medicine)
    public Medicine(String medicineName, String usageInstruction, String dose,
            String category, LocalDate expiredDate) {
        this.medicineName = medicineName;
        this.usageInstruction = usageInstruction;
        this.dose = dose;
        this.category = category;
        this.expiredDate = expiredDate;
        this.medicineId = 0;
    }

    // Constructor when there is a medicineId (e.g., when loading from database)
    public Medicine(int medicineId, String medicineName, String usageInstruction, String dose,
            String category, LocalDate expiredDate) {
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.usageInstruction = usageInstruction;
        this.dose = dose;
        this.category = category;
        this.expiredDate = expiredDate;
    }

    // Method to check how many days until expiry
    public long getDaysUntilExpiry() {
        return LocalDate.now().until(expiredDate).getDays();
    }

    // Display medicine information
    public void displayInfo() {
        System.out.println("=================== MEDICINE INFO ===================");
        System.out.println("Medicine ID: " + medicineId);
        System.out.println("Medicine Name: " + medicineName);
        System.out.println("Usage Instruction: " + usageInstruction);
        System.out.println("Dose: " + dose);
        System.out.println("Category: " + category);
        System.out.println("Expired Date: " + expiredDate);
        System.out.println("Current Status: " + (isExpired() ? "Expired" : "Valid"));
        
        if (!isExpired()) {
            System.out.println("Days until expiry: " + getDaysUntilExpiry());
        } else {
            System.out.println("Expired " + Math.abs(getDaysUntilExpiry()) + " days ago");
        }
        System.out.println("==================================================");
    }

    // Save medicine to database
    public void saveToDatabase() {
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "INSERT INTO medicines (medicine_name, usage_instruction, dose, category, expired_date) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, medicineName);
            pstmt.setString(2, usageInstruction);
            pstmt.setString(3, dose);
            pstmt.setString(4, category);
            pstmt.setString(5, expiredDate.toString());

            int result = pstmt.executeUpdate();
            if (result > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    this.medicineId = generatedKeys.getInt(1);
                }
                System.out.println("Medicine saved to database with ID: " + this.medicineId);
            }
        } catch (SQLException e) {
            System.out.println("Error saving medicine: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Save medicine to database with foreign keys
    public void saveToDatabase(int prescriptionId, int patientId, int pharmacistId) {
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "INSERT INTO medicines (medicine_name, usage_instruction, dose, category, expired_date, prescription_id, patient_id, pharmacist_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, medicineName);
            pstmt.setString(2, usageInstruction);
            pstmt.setString(3, dose);
            pstmt.setString(4, category);
            pstmt.setString(5, expiredDate.toString());
            pstmt.setInt(6, prescriptionId > 0 ? prescriptionId : null);
            pstmt.setInt(7, patientId > 0 ? patientId : null);
            pstmt.setInt(8, pharmacistId > 0 ? pharmacistId : null);

            int result = pstmt.executeUpdate();
            if (result > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    this.medicineId = generatedKeys.getInt(1);
                }
                System.out.println("Medicine saved to database with ID: " + this.medicineId);
            }
        } catch (SQLException e) {
            System.out.println("Error saving medicine: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Static methods for medicine management
    public static ArrayList<Medicine> getAllMedicines() {
        ArrayList<Medicine> medicines = new ArrayList<>();
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT * FROM medicines ORDER BY medicine_name";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Medicine medicine = new Medicine(
                    rs.getInt("medicine_id"),
                    rs.getString("medicine_name"),
                    rs.getString("usage_instruction"),
                    rs.getString("dose"),
                    rs.getString("category"),
                    LocalDate.parse(rs.getString("expired_date"))
                );
                medicines.add(medicine);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medicines;
    }

    public static ArrayList<Medicine> getExpiredMedicines() {
        ArrayList<Medicine> expiredMedicines = new ArrayList<>();
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT * FROM medicines WHERE expired_date < CURRENT_DATE ORDER BY expired_date";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Medicine medicine = new Medicine(
                    rs.getInt("medicine_id"),
                    rs.getString("medicine_name"),
                    rs.getString("usage_instruction"),
                    rs.getString("dose"),
                    rs.getString("category"),
                    LocalDate.parse(rs.getString("expired_date"))
                );
                expiredMedicines.add(medicine);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expiredMedicines;
    }

    public static ArrayList<Medicine> getValidMedicines() {
        ArrayList<Medicine> validMedicines = new ArrayList<>();
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT * FROM medicines WHERE expired_date >= CURRENT_DATE ORDER BY expired_date";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Medicine medicine = new Medicine(
                    rs.getInt("medicine_id"),
                    rs.getString("medicine_name"),
                    rs.getString("usage_instruction"),
                    rs.getString("dose"),
                    rs.getString("category"),
                    LocalDate.parse(rs.getString("expired_date"))
                );
                validMedicines.add(medicine);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return validMedicines;
    }

    public static ArrayList<Medicine> getMedicinesByCategory(String category) {
        ArrayList<Medicine> categoryMedicines = new ArrayList<>();
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT * FROM medicines WHERE category = ? ORDER BY medicine_name";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Medicine medicine = new Medicine(
                    rs.getInt("medicine_id"),
                    rs.getString("medicine_name"),
                    rs.getString("usage_instruction"),
                    rs.getString("dose"),
                    rs.getString("category"),
                    LocalDate.parse(rs.getString("expired_date"))
                );
                categoryMedicines.add(medicine);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoryMedicines;
    }

    public static ArrayList<Medicine> getAvailableMedicines() {
        ArrayList<Medicine> availableMedicines = new ArrayList<>();
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT * FROM medicines WHERE expired_date >= CURRENT_DATE AND (prescription_id IS NULL OR prescription_id = 0) ORDER BY medicine_name";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Medicine medicine = new Medicine(
                    rs.getInt("medicine_id"),
                    rs.getString("medicine_name"),
                    rs.getString("usage_instruction"),
                    rs.getString("dose"),
                    rs.getString("category"),
                    LocalDate.parse(rs.getString("expired_date"))
                );
                availableMedicines.add(medicine);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return availableMedicines;
    }

    public static ArrayList<Medicine> getMedicinesForPatient(int patientId) {
        ArrayList<Medicine> patientMedicines = new ArrayList<>();
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT * FROM medicines WHERE patient_id = ? ORDER BY medicine_name";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Medicine medicine = new Medicine(
                    rs.getInt("medicine_id"),
                    rs.getString("medicine_name"),
                    rs.getString("usage_instruction"),
                    rs.getString("dose"),
                    rs.getString("category"),
                    LocalDate.parse(rs.getString("expired_date"))
                );
                patientMedicines.add(medicine);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patientMedicines;
    }

    public static ArrayList<Medicine> getMedicinesProcessedBy(int pharmacistId) {
        ArrayList<Medicine> pharmacistMedicines = new ArrayList<>();
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT * FROM medicines WHERE pharmacist_id = ? ORDER BY medicine_name";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, pharmacistId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Medicine medicine = new Medicine(
                    rs.getInt("medicine_id"),
                    rs.getString("medicine_name"),
                    rs.getString("usage_instruction"),
                    rs.getString("dose"),
                    rs.getString("category"),
                    LocalDate.parse(rs.getString("expired_date"))
                );
                pharmacistMedicines.add(medicine);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pharmacistMedicines;
    }

    // Method to add sample medicines to database (for testing purposes)
    public static void addSampleMedicines() {
        ArrayList<Medicine> sampleMedicines = new ArrayList<>();
        
        // Add some sample medicines
        sampleMedicines.add(new Medicine("Paracetamol", "Take 1 tablet every 6 hours", "500mg", "Pain Relief", LocalDate.of(2025, 12, 31)));
        sampleMedicines.add(new Medicine("Amoxicillin", "Take 1 capsule 3 times daily", "250mg", "Antibiotic", LocalDate.of(2025, 8, 15)));
        sampleMedicines.add(new Medicine("Lisinopril", "Take 1 tablet daily", "10mg", "Cardiovascular", LocalDate.of(2025, 10, 20)));
        sampleMedicines.add(new Medicine("Aspirin", "Take 1 tablet daily with food", "81mg", "Cardiovascular", LocalDate.of(2025, 6, 30)));
        sampleMedicines.add(new Medicine("Ibuprofen", "Take 1-2 tablets every 4-6 hours as needed", "200mg", "Pain Relief", LocalDate.of(2024, 3, 15))); // Expired
        sampleMedicines.add(new Medicine("Metformin", "Take 1 tablet twice daily with meals", "500mg", "Diabetes", LocalDate.of(2025, 11, 25)));
        sampleMedicines.add(new Medicine("Omeprazole", "Take 1 capsule daily before breakfast", "20mg", "Gastric", LocalDate.of(2025, 9, 10)));
        sampleMedicines.add(new Medicine("Cetirizine", "Take 1 tablet daily for allergies", "10mg", "Antihistamine", LocalDate.of(2025, 7, 22)));
        
        for (Medicine medicine : sampleMedicines) {
            medicine.saveToDatabase();
        }
        
        System.out.println("✅ Sample medicines added to database!");
    }

    @Override
    public String toString() {
        return medicineName + " (" + dose + ") - " + category;
    }
}