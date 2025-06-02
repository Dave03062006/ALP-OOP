import java.time.LocalDate;

public class Medicine2 {
    // to get all meds -> put to arraylist
    // to get based on patient id -> to arraylist
    // loop all arraylist to place in a new tree 


    protected int medicineId; // Unique identifier for the medicine
    protected String medicineName;
    protected String usageInstruction;
    protected String dose;
    protected String category;
    protected LocalDate expiredDate;
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

    
}
