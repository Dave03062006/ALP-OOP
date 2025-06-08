import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Appointment {
    private int appointmentId;
    private Patient patient;
    private Doctor doctor;
    //private int scheduleId; // Foreign key reference to MySQL schedules table
    private String diagnoseResult;
    private Prescription prescription;
    private List<Medicine> medicines;
    private Pharmacist pharmacist;
    private LocalDateTime appointmentDateTime;
    


    public Appointment(int appointmentId, Patient patient, Doctor doctor, int scheduleId, LocalDateTime appointmentDateTime) {
        this.appointmentId = appointmentId;
        this.patient = patient;
        this.doctor = doctor;
        // this.scheduleId = scheduleId;
        this.medicines = new ArrayList<>();
        this.appointmentDateTime = appointmentDateTime;
        this.diagnoseResult = null; // Initially null, to be set after diagnosis
        this.prescription = null; // Initially null, to be set after prescription is created
        this.pharmacist = null; // Initially null, to be set when a pharmacist is assigned
    }
    
    // Getters and Setters
    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }
    
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    
    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }
    
    // public int getScheduleId() { return scheduleId; }
    // public void setScheduleId(int scheduleId) { this.scheduleId = scheduleId; }
    
    public String getDiagnoseResult() { return diagnoseResult; }
    public void setDiagnoseResult(String diagnoseResult) { this.diagnoseResult = diagnoseResult; }
    
    public Prescription getPrescription() { return prescription; }
    public void setPrescription(Prescription prescription) { this.prescription = prescription; }
    
    public List<Medicine> getMedicines() { return medicines; }
    public void setMedicines(List<Medicine> medicines) { this.medicines = medicines; }
    
    public void addMedicine(Medicine medicine) { this.medicines.add(medicine); }
    
    public Pharmacist getPharmacist() { return pharmacist; }
    public void setPharmacist(Pharmacist pharmacist) { this.pharmacist = pharmacist; }
    
    // Utility methods
    public boolean isCompleted() {
        return diagnoseResult != null && prescription != null && !medicines.isEmpty();
    }
    
    public boolean isPrescriptionFulfilled() {
        return !medicines.isEmpty() && pharmacist != null;
    }

        public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }

    public Appointment gAppointment() {
        return this;
    }
}