import java.time.LocalDate;

public class MedicineExpiryBSTNode {
    protected Medicine medicine;
    protected MedicineExpiryBSTNode left;
    protected MedicineExpiryBSTNode right;

    public MedicineExpiryBSTNode (Medicine medicine) {
        this.medicine = medicine;
        this.left = null;
        this.right = null;
    }

    

    public MedicineExpiryBSTNode insert(Medicine medicineToInsert) {
            if (this == null) {
                System.out.println("Added medicine : " + medicineToInsert.getMedicineName() + ", expired date : " + medicineToInsert.getExpiredDate());
                return new MedicineExpiryBSTNode(medicineToInsert);
            }
            
            // Compare expiry dates for BST property
            LocalDate currentExpiry = this.medicine.getExpiredDate();
            LocalDate newExpiry = medicineToInsert.getExpiredDate();
            
            if (newExpiry.isBefore(currentExpiry)) {
                // Earlier expiry goes to left
                if (this.left == null) {
                    this.left = new MedicineExpiryBSTNode(medicineToInsert);
                } else {
                    this.left = this.left.insert(medicineToInsert);
                }
            } else if (newExpiry.isAfter(currentExpiry)) {
                // Later expiry goes to right
                if (this.right == null) {
                    this.right = new MedicineExpiryBSTNode(medicineToInsert);
                } else {
                    this.right = this.right.insert(medicineToInsert);
                }
            } else {
                // Same expiry date - add to right subtree (allows duplicates)
                if (this.right == null) {
                    this.right = new MedicineExpiryBSTNode(medicineToInsert);
                } else {
                    this.right = this.right.insert(medicineToInsert);
                }
            }
            return this;
        }

        

}
