package model;
import java.sql.*;

import java.util.ArrayList;

import logic.DatabaseConnect;

public class Prescription {
    protected int prescriptionId;

    protected Patient patient;
    protected Doctor doctor;
    protected String receiptDescription;

    protected ArrayList<Medicine> prescribedMedicines;
    protected String medicine_name;
    protected String dose;
    protected Pharmacist assignedPharmacist;
    protected Appointment relatedAppointment;

    // not in database, managed in memory only
    protected PrescriptionStatus status;

    protected String patientComplaint; // from appointment booking

    protected ArrayList<Prescription> allPrescriptions = new ArrayList<>();
    protected ArrayList<Prescription> pendingPrescriptions = new ArrayList<>();
    protected ArrayList<Prescription> completedPrescriptions = new ArrayList<>();

    public int getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(int prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public String getReceiptDescription() {
        return receiptDescription;
    }

    public void setReceiptDescription(String receiptDescription) {
        this.receiptDescription = receiptDescription;
    }

    public ArrayList<Medicine> getPrescribedMedicines() {
        return prescribedMedicines;
    }

    public void setPrescribedMedicines(ArrayList<Medicine> prescribedMedicines) {
        this.prescribedMedicines = prescribedMedicines;
    }

    public Pharmacist getAssignedPharmacist() {
        return assignedPharmacist;
    }

    public void setAssignedPharmacist(Pharmacist assignedPharmacist) {
        this.assignedPharmacist = assignedPharmacist;
    }

    public Appointment getRelatedAppointment() {
        return relatedAppointment;
    }

    public void setRelatedAppointment(Appointment relatedAppointment) {
        this.relatedAppointment = relatedAppointment;
    }

    public PrescriptionStatus getStatus() {
        return status;
    }

    public void setStatus(PrescriptionStatus status) {
        this.status = status;
    }

    public String getPatientComplaint() {
        return patientComplaint;
    }

    public void setPatientComplaint(String patientComplaint) {
        this.patientComplaint = patientComplaint;
    }

    public ArrayList<Prescription> getAllPrescriptions() {
        return allPrescriptions;
    }

    public void setAllPrescriptions(ArrayList<Prescription> allPrescriptions) {
        this.allPrescriptions = allPrescriptions;
    }

    public ArrayList<Prescription> getPendingPrescriptions() {
        return pendingPrescriptions;
    }

    public void setPendingPrescriptions(ArrayList<Prescription> pendingPrescriptions) {
        this.pendingPrescriptions = pendingPrescriptions;
    }

    public ArrayList<Prescription> getCompletedPrescriptions() {
        return completedPrescriptions;
    }

    public void setCompletedPrescriptions(ArrayList<Prescription> completedPrescriptions) {
        this.completedPrescriptions = completedPrescriptions;
    }

    public enum PrescriptionStatus {
        PENDING("Pending"),
        IN_PROGRESS("In Progress"),
        COMPLETED("Completed"),
        CANCELLED("Cancelled");

        protected final String displayName; // uses 'final', where the keyword applies to each individual enum constant

        // When Java creates the enum, it's essentially doing:
        // PrescriptionStatus.PENDING = new PrescriptionStatus("Pending");
        // PrescriptionStatus.COMPLETED = new PrescriptionStatus("Completed");
        // etc...

        // Each of these objects has a final displayName that NEVER changes:
        // PENDING object always has displayName = "Pending"
        // COMPLETED object always has displayName = "Completed"

        // PrescriptionStatus.PENDING.getDisplayName(); // Always returns "Pending"
        // PrescriptionStatus.COMPLETED.getDisplayName(); // Always returns "Completed"

        PrescriptionStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }

        // for dropdown menu, to get all status options for dropdown
        public PrescriptionStatus[] getAllStatuses() {
            return PrescriptionStatus.values();
        }

        // convert string to enum
        public PrescriptionStatus fromString(String statusString) {
            for (PrescriptionStatus status : PrescriptionStatus.values()) {
                if (status.getDisplayName().equalsIgnoreCase(statusString)
                        || status.name().equalsIgnoreCase(statusString)) {
                    return status;
                }
            }
            return PENDING; // default value
        }

    }

    // constructor for new prescriptions (before database insertionss)
    public Prescription(Patient patient, Doctor doctor, String receiptDescription, String medicineNname, String dose) {
        this.patient = patient;
        this.doctor = doctor;
        this.receiptDescription = receiptDescription;
        this.status = PrescriptionStatus.PENDING;
        this.prescribedMedicines = new ArrayList<>();
        this.medicine_name = medicineNname;
        this.dose = dose;
        this.prescriptionId = prescriptionId;

    }

    // constructor with patient complaint taken from appointment booking

    public Prescription(Patient patient, Doctor doctor, String receiptDescription, String patientComplaint) {
        this(patient, doctor, receiptDescription, receiptDescription, patientComplaint);
        this.patientComplaint = patientComplaint;
    }

    // constructor for existing prescriptions from database
    public Prescription(int prescriptionId, Patient patient, Doctor doctor, String receiptDescription) {
        this.prescriptionId = prescriptionId;
        this.patient = patient;
        this.doctor = doctor;
        this.receiptDescription = receiptDescription;
        this.status = PrescriptionStatus.PENDING;
        this.prescribedMedicines = new ArrayList<>();

        loadPrescribedMedicinesFromDatabase();
    }

    public void addMedicine(Medicine medicine) {
        if (!prescribedMedicines.contains(medicine)) {
            prescribedMedicines.add(medicine);
            System.out.println("Medicine " + medicine.getMedicineName() + " added to prescription");
        }
    }

    public void removeMedicine(Medicine medicine) {
        if (prescribedMedicines.remove(medicine)) {
            System.out.println("Medicine " + medicine.getMedicineName() + " removed from prescription");
        }
    }

    public boolean hasMedicine(Medicine medicine) {
        return prescribedMedicines.contains(medicine);
    }

    public int getMedicineCount() {
        return prescribedMedicines.size();
    }

    // Getters and Setters

    // database stuff

    // to load related medicines from database
    public void loadPrescribedMedicinesFromDatabase() {
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT * FROM medicines WHERE prescription_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, prescriptionId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Medicine medicine = new Medicine(
                        rs.getInt("medicine_id"),
                        rs.getString("medicine_name"),
                        rs.getString("usage_instruction"),
                        rs.getString("dose"),
                        rs.getString("category"),
                        rs.getDate("expired_date").toLocalDate());
                prescribedMedicines.add(medicine);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveToDatabase() {
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "INSERT INTO prescriptions (recipe_description, doctor_id, patient_id) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, receiptDescription);
            pstmt.setInt(2, doctor.getDoctorId());
            pstmt.setInt(3, patient.getPatientId());

            int result = pstmt.executeUpdate();
            if (result > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    this.prescriptionId = generatedKeys.getInt(1);
                }
                System.out.println("Prescription saved to database with ID: " + this.prescriptionId);
                saveMedicinesToDatabase();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveMedicinesToDatabase() {
        for (Medicine medicine : prescribedMedicines) {
            medicine.saveToDatabase(this.prescriptionId, patient.getPatientId(),
                    assignedPharmacist != null ? assignedPharmacist.getPharmacistId() : 0);
        }
    }

    public boolean isPending() {
        return status == PrescriptionStatus.PENDING || status == PrescriptionStatus.IN_PROGRESS;
    }

    public boolean isCompleted() {
        return status == PrescriptionStatus.COMPLETED;
    }

    public void assignToPharmacist(Pharmacist pharmacist) {
        this.assignedPharmacist = pharmacist;
        if (status == PrescriptionStatus.PENDING) {
            setStatus(PrescriptionStatus.IN_PROGRESS);
        }
        System.out.println("Prescription " + prescriptionId + " assigned to pharmacist " + pharmacist.getFullName());
    }

    public void markAsCompleted() {
        if (assignedPharmacist != null) {
            setStatus(PrescriptionStatus.COMPLETED);
        } else {
            System.out.println("Cannot complete prescription without assigned pharmacist");
        }
    }

    // call this when you know the specific doctor and patient
    public void displayInfo() {
        System.out.println("=================== PRESCRIPTION INFO ===================");
        System.out.println("Prescription ID: " + prescriptionId);
        System.out.println("Patient: " + patient.getFullName() + " (" + patient.getEmail());
        System.out.println("Doctor: " + doctor.getFullName() + " (" + doctor.getSpecialist() + ")");
        System.out.println("Status: " + status.getDisplayName());

        if (patientComplaint != null) {
            System.out.println("Patient Complaint: " + patientComplaint);
        }
        System.out.println("Prescription Details: " + receiptDescription);

        if (assignedPharmacist != null) {
            System.out.println("Assigned Pharmacist: " + assignedPharmacist.getFullName());
        }
        System.out.println("Prescribed Medicines (" + prescribedMedicines.size() + "):");
        for (Medicine medicine : prescribedMedicines) {
            System.out.println(" -" + medicine.getMedicineName() + " (" + medicine.getDose() + ") - "
                    + medicine.getUsageInstruction());
        }
        System.out.println("=====================================================");
    }

    // method to get prescription summary for UI display
    public String getPrescriptionSummary() {
        return String.format("Prescription #%d - Patient: %s, Doctor: %s (%s), Status: %s", prescriptionId,
                patient.getFullName(), doctor.getFullName(), doctor.getSpecialist(), status.getDisplayName());
    }

    // database
    public ArrayList<Prescription> getAllPrescriptionsFromDatabase() {
        ArrayList<Prescription> prescriptions = new ArrayList<>();
        try {
            Connection conn = DatabaseConnect.getConnection();
            // patients pt
            // doctors d
            // prescriptions p
            String sql = "SELECT p.*pt.full_name as patient_name, pt.email as patient_email, pt.phone_number as patient_phone_number, pt.password as patient_password"
                    + "d.specialist as doctor_specialist, d.email as doctor_email, d.phone_number as doctor_phone_number, d.password as doctor_password"
                    + "FROM prescriptions p" + "JOIN patients pt ON p.patient_id = pt.patient_id"
                    + "JOIN doctors d ON p.doctor_id = d.doctor_id" + "ORDER BY p.prescription_id DESC";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Patient patient = new Patient(
                        rs.getInt("patient_id"),
                        rs.getString("patient_email"),
                        rs.getString("patient_password"),
                        rs.getString("patient_name"),
                        rs.getString("patient_phone_number"));
                Doctor doctor = new Doctor(
                        rs.getInt("doctor_id"),
                        rs.getString("doctor_email"),
                        rs.getString("doctor_password"),
                        rs.getString("doctor_name"),
                        rs.getString("doctor_phone"),
                        true,
                        rs.getString("specialist"));

                Prescription prescription = new Prescription(rs.getInt("prescription_id"), patient, doctor,
                        rs.getString("recipe_description"));

                prescriptions.add(prescription);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prescriptions;
    }

    public ArrayList<Prescription> getPendingPrescriptionsFromDatabase() {
        ArrayList<Prescription> allPrescriptions = getAllPrescriptions();
        ArrayList<Prescription> pending = new ArrayList<>();

        for (Prescription prescription : allPrescriptions) {
            if (prescription.isPending()) {
                pending.add(prescription);
            }
        }

        return pending;
    }

    // prescription from doctor who to patients who
    public ArrayList<Prescription> getPrescriptionsByDoctorFromDatabase() {
        ArrayList<Prescription> result = new ArrayList<>();
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT p.*, pt.full_name as patient_name, pt.email as patient_email, pt.phone_number as patient_phone_number, pt.password as patient_password"
                    + "FROM prescriptions p" + "JOIN patients pt ON p.patient_id = pt.patient_id"
                    + "WHERE p.doctor_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, doctor.getDoctorId());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Patient patient = new Patient(
                        rs.getInt("patient_id"),
                        rs.getString("patient_email"),
                        rs.getString("patient_password"),
                        rs.getString("patient_name"),
                        rs.getString("patient_phone"));

                Prescription prescription = new Prescription(
                        rs.getInt("prescription_id"),
                        patient,
                        doctor,
                        rs.getString("recipe_description"));

                result.add(prescription);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}