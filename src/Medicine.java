import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Medicine {
    protected int medicineId; // Unique identifier for the medicine
    protected String medicineName;
    protected String usageInstruction;
    protected String dose;
    protected String category;
    protected LocalDate expiredDate;

    // foreign keys as arrayLists
    protected ArrayList<Patient> assignedPatients; // List of patients who have this medicine
    protected ArrayList<Pharmacist> handlingPharmacists; // List of pharmacists who handle this medicine
    protected ArrayList<Prescription> prescriptions; // List of prescriptions associated with this medicine
    protected ArrayList<Appointment> appointments; // List of appointments associated with this medicine

    protected ArrayList<Medicine> allMedicines; // List of medicines in the queue
    protected ArrayList<Medicine> expiredMedicines;
    protected ArrayList<Medicine> notExpiredMedicines;
    
    public ArrayList<Patient> getAssignedPatients() {
        return assignedPatients;
    }

    public void setAssignedPatients(ArrayList<Patient> assignedPatients) {
        this.assignedPatients = assignedPatients;
    }

    public ArrayList<Pharmacist> getHandlingPharmacists() {
        return handlingPharmacists;
    }

    public void setHandlingPharmacists(ArrayList<Pharmacist> handlingPharmacists) {
        this.handlingPharmacists = handlingPharmacists;
    }

    public ArrayList<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(ArrayList<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(ArrayList<Appointment> appointments) {
        this.appointments = appointments;
    }

    public ArrayList<Medicine> getAllMedicines() {
        return allMedicines;
    }

    public void setAllMedicines(ArrayList<Medicine> allMedicines) {
        this.allMedicines = allMedicines;
    }

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

        // Initialize ArrayList attributes (OOP approach)
        this.assignedPatients = new ArrayList<>();
        this.prescriptions = new ArrayList<>();
        this.handlingPharmacists = new ArrayList<>();
        this.appointments = new ArrayList<>();
        
        // Initialize collection ArrayLists
        this.allMedicines = new ArrayList<>();
        this.expiredMedicines = new ArrayList<>();
        this.notExpiredMedicines = new ArrayList<>();
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

        // Initialize ArrayList attributes (OOP approach)
        this.assignedPatients = new ArrayList<>();
        this.prescriptions = new ArrayList<>();
        this.handlingPharmacists = new ArrayList<>();
        this.appointments = new ArrayList<>();
        
        // Initialize collection ArrayLists
        this.allMedicines = new ArrayList<>();
        this.expiredMedicines = new ArrayList<>();
        this.notExpiredMedicines = new ArrayList<>();

        loadRelatedObjectsFromDatabase();
    }

    // to load foreign keys from database
    public void loadRelatedObjectsFromDatabase() {
        try {
            Connection conn = DatabaseConnect.getConnection();
            String patientSql = "SELECT DISTINCT p.* FROM patients p "
                    + "JOIN medicines m ON p.patient_id = m.patient_id "
                    + "WHERE m.medicine_id = ? AND m.patient_id > 0";
            PreparedStatement pstmt = conn.prepareStatement(patientSql);
            pstmt.setInt(1, this.medicineId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Patient patient = new Patient(
                        rs.getInt("patient_id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getString("phone_number"));
                this.assignedPatients.add(patient);
            }

            String pharmacistSql = "SELECT DISTINCT ph.* FROM pharmacists ph "
                    + "JOIN medicines m ON ph.pharmacist_id = m.pharmacist_id "
                    + "WHERE m.medicine_id = ? AND m.pharmacist_id > 0";
            pstmt = conn.prepareStatement(pharmacistSql);
            pstmt.setInt(1, this.medicineId); // Fixed: added missing parameter
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Pharmacist pharmacist = new Pharmacist(
                        rs.getInt("pharmacist_id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getString("phone_number"),
                        true);
                this.handlingPharmacists.add(pharmacist);
            }
            System.out.println("Loaded relationships for medicine " + medicineName + ": " + assignedPatients.size()
                    + " patients, " + handlingPharmacists.size() + " pharmacists.");

            // Load related objects (patients, pharmacists, prescriptions, appointments)
            // This part can be expanded based on your database schema and relationships
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // oop methods
    public ArrayList<Medicine> getExpiredMedicines() {
        ArrayList<Medicine> expiredMedicines = new ArrayList<>();
        for (Medicine medicine : allMedicines) {
            if (medicine.isExpired()) {
                expiredMedicines.add(medicine);
            }
        }
        return expiredMedicines;
    }

    // get available medicines
    public ArrayList<Medicine> getAvailableMedicines() {
        ArrayList<Medicine> availableMedicines = new ArrayList<>();
        for (Medicine medicine : allMedicines) {
            if (!medicine.isExpired()) {
                availableMedicines.add(medicine);
            }
        }
        return availableMedicines;
    }

    // get assigned medicines (computed from single arraylist)
    public ArrayList<Medicine> getProcessedMedicines() {
        ArrayList<Medicine> processedMedicines = new ArrayList<>();
        for (Medicine medicine : allMedicines) {
            if (medicine.getAssignedPatients().size() > 0 || medicine.getHandlingPharmacists().size() > 0) {
                processedMedicines.add(medicine);
            }
        }
        return processedMedicines;
    }


    public ArrayList<Medicine> findMedicinesForPatient(Patient targetPatient) {
        ArrayList<Medicine> patientMedicines = new ArrayList<>();
        for (Medicine medicine : allMedicines) {
            if (medicine.getAssignedPatients().contains(targetPatient)) {
                patientMedicines.add(medicine);
            }
        }
        return patientMedicines;
    }

    // find medicines processed by pharmacist
    public ArrayList<Medicine> findMedicinesProcessedByPharmacist(Pharmacist targetPharmacist) {
        ArrayList<Medicine> pharmacistMedicines = new ArrayList<>();
        for (Medicine medicine : allMedicines) {
            if (medicine.getHandlingPharmacists().contains(targetPharmacist)) {
                pharmacistMedicines.add(medicine);
            }
        }
        return pharmacistMedicines;
    }

    // find medicines by category
    public ArrayList<Medicine> findMedicinesByCategory(String category) {
        ArrayList<Medicine> result = new ArrayList<>();
        for (Medicine medicine : allMedicines) {
            if (medicine.getCategory().equalsIgnoreCase(category)) {
                result.add(medicine);
            }
        }
        return result;
    }

    public void addToAppointmentHistory(Appointment appointment) {
        if (!appointments.contains(appointment)) {
            appointments.add(appointment);
            System.out.println("Medicine " + medicineName + " added to appointment history");
        }
    }

    public void removePatientAssignment(Patient patient) { // Fixed typo: was "removedPatientAssignment"
        if (assignedPatients.remove(patient)) {
            System.out.println("Patient " + patient.getFullName() + " unassigned from medicine " + medicineName);
        }
    }

    // to check if medicine was assigned to specific patient
    public boolean isAssignedToPatient(Patient patient) {
        return assignedPatients.contains(patient);
    }

    // check if medicine was processed by specific pharmacist
    public boolean wasProcessedBy(Pharmacist pharmacist) {
        return handlingPharmacists.contains(pharmacist);
    }

    public ArrayList<String> getPatientNamesAssignedWithMedicine() {
        ArrayList<String> names = new ArrayList<>();
        for (Patient patient : assignedPatients) {
            names.add(patient.getFullName());
        }
        return names;
    }

    // to get all pharmacist names
    public ArrayList<String> getPharmacistNames() {
        ArrayList<String> names = new ArrayList<>();
        for (Pharmacist pharmacist : handlingPharmacists) {
            names.add(pharmacist.getFullName());
        }
        return names;
    }

    public Prescription getLatestPrescription() {
        if (prescriptions.isEmpty()) return null; // Fixed: added null check
        return prescriptions.get(prescriptions.size() - 1);
    }

    public Appointment getLatestAppointment() {
        if (appointments.isEmpty()) return null; // Fixed: added null check
        return appointments.get(appointments.size() - 1);
    }

    public long getDaysUntilExpiry() {
        return LocalDate.now().until(expiredDate).getDays();
    }

    public boolean isProcessed() {
        return !handlingPharmacists.isEmpty();
    }

    // to get the number of pharmacists who handled this medicine
    public int getPharmacistCount() {
        return handlingPharmacists.size();
    }

    // THE WHOLE medicine info
    public void displayInfo() {
        System.out.println("=================== DETAILED MEDICINE INFO ===================");
        System.out.println("Medicine ID: " + medicineId);
        System.out.println("Medicine Name: " + medicineName);
        System.out.println("Usage Instruction: " + usageInstruction);
        System.out.println("Dose: " + dose);
        System.out.println("Category: " + category);
        System.out.println("Expired Date: " + expiredDate);
        System.out.println("Current Expiry Status: " + (isExpired() ? "Expired" : "Not Expired"));
        System.out.println("Days until expiry: " + getDaysUntilExpiry());

        System.out.println("\n--- RELATIONSHIPS ---");
        System.out.println("Assigned Patients (" + assignedPatients.size() + "):");
        for (Patient patient : assignedPatients) {
            System.out.println("  - " + patient.getFullName() + " (ID: " + patient.getPatientId() + ")");
        }

        System.out.println("Handling Pharmacists (" + handlingPharmacists.size() + "):");
        for (Pharmacist pharmacist : handlingPharmacists) {
            System.out.println("  - " + pharmacist.getFullName() + " (ID: " + pharmacist.getPharmacistId() + ")");
        }

        System.out.println();

        System.out.println("Appointment History (" + appointments.size() + "):");
        for (Appointment appointment : appointments) {
            System.out.println("  - Appointment Diagnose Result: " + appointment.getDiagnoseResult() +
                    " on " + appointment.getAppointmentDateTime() + " prescribed by " + appointment.getDoctor().getFullName());
        }

        System.out.println("===========================================================");
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
            pstmt.setInt(6, prescriptionId);
            pstmt.setInt(7, patientId);
            pstmt.setInt(8, pharmacistId);

            int result = pstmt.executeUpdate();
            if (result > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    this.medicineId = generatedKeys.getInt(1);
                }
                System.out.println("Medicine saved to database with ID: " + this.medicineId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // to load all meds from db and add on to arraylists
    public void loadAllMedicinesFromDatabase() {
        allMedicines.clear();
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
                allMedicines.add(medicine);
            }

            organizeMedicines();
            System.out.println("Loaded " + allMedicines.size() + " with their relations");
            
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void organizeMedicines() {
        expiredMedicines.clear();
        notExpiredMedicines.clear();
        
        for (Medicine medicine : allMedicines) {
            if(medicine.isExpired()) {
                expiredMedicines.add(medicine);
            } else {
                notExpiredMedicines.add(medicine);
            }
        }

        System.out.println("Medicines Organized: ");
        System.out.println("- Expired Medicines: " + expiredMedicines.size());
        System.out.println("- Not Expired Medicines: " + notExpiredMedicines.size());

        System.out.println("\nExpired Medicines: ");
        for (Medicine medicine : expiredMedicines) {
            System.out.println("  - " + medicine.getMedicineName() + " (" + medicine.getDose() + ") - Expired: " + medicine.getExpiredDate());
        }

        System.out.println("\nNot Expired Medicines: ");
        for (Medicine medicine : notExpiredMedicines) {
            System.out.println("  - " + medicine.getMedicineName() + " (" + medicine.getDose() + ") - Expires: " + medicine.getExpiredDate());
        }
    }

    // Find medicines by patient (using ArrayList relationships)
    public ArrayList<Medicine> findMedicinesForPatientByRelationship(Patient targetPatient) {
        ArrayList<Medicine> result = new ArrayList<>();
        for (Medicine medicine : allMedicines) {
            if (medicine.isAssignedToPatient(targetPatient)) {
                result.add(medicine);
            }
        }
        System.out.println("Found " + result.size() + " medicines for patient: " + targetPatient.getFullName());
        return result;
    }
    
    // Find medicines processed by pharmacist (using ArrayList relationships)
    public ArrayList<Medicine> findMedicinesProcessedBy(Pharmacist targetPharmacist) {
        ArrayList<Medicine> result = new ArrayList<>();
        for (Medicine medicine : allMedicines) {
            if (medicine.wasProcessedBy(targetPharmacist)) {
                result.add(medicine);
            }
        }
        System.out.println("Found " + result.size() + " medicines processed by: " + targetPharmacist.getFullName());
        return result;
    }
    
    // Find medicines by category
    public ArrayList<Medicine> findByCategory(String category) {
        ArrayList<Medicine> result = new ArrayList<>();
        for (Medicine medicine : allMedicines) {
            if (medicine.getCategory().equalsIgnoreCase(category)) {
                result.add(medicine);
            }
        }
        return result;
    }
}