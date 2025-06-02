import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * COMPREHENSIVE MEDICINE EXPIRATION DATE SEARCH IMPLEMENTATION
 * 
 * This implementation provides various methods to search medicines based on expiration dates
 * using Binary Search Tree for optimal O(log n) performance.
 * 
 * Search Capabilities:
 * 1. Find medicines expiring on specific date
 * 2. Find medicines expiring before/after specific date
 * 3. Find medicines expiring within date range
 * 4. Find medicines expiring in next N days/weeks/months
 * 5. Find expired medicines
 * 6. Find medicines by expiry status (expired, expiring soon, safe)
 * 7. Advanced filtering with multiple criteria
 */

public class Medicine {
    // Existing fields...
    private int medicineId;
    private String medicineName;
    private String usageInstruction;
    private String dose;
    private String category;
    private LocalDate expiredDate;
    
    // Enhanced constructor
    public Medicine(int medicineId, String medicineName, String usageInstruction, 
                   String dose, String category, LocalDate expiredDate) {
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.usageInstruction = usageInstruction;
        this.dose = dose;
        this.category = category;
        this.expiredDate = expiredDate;
    }
    
    // Getters
    public int getMedicineId() { return medicineId; }
    public String getMedicineName() { return medicineName; }
    public String getUsageInstruction() { return usageInstruction; }
    public String getDose() { return dose; }
    public String getCategory() { return category; }
    public LocalDate getExpiredDate() { return expiredDate; }
    
    // Utility method
    public boolean isExpired() {
        return LocalDate.now().isAfter(expiredDate);
    }
    
    /**
     * Enhanced Binary Search Tree Node for Medicine Expiry Management
     * Provides comprehensive expiration date search capabilities
     */
    public static class MedicineExpiryBSTNode {
        private Medicine medicine;                    // Medicine stored in this node
        private MedicineExpiryBSTNode left;          // Medicines expiring earlier
        private MedicineExpiryBSTNode right;         // Medicines expiring later
        
        public MedicineExpiryBSTNode(Medicine medicine) {
            this.medicine = medicine;
            this.left = null;
            this.right = null;
        }
        
        /**
         * Binary Tree Method: Insert medicine by expiry date
         * @param medicineToInsert Medicine to insert
         * @return Root of updated tree
         */
        public MedicineExpiryBSTNode insert(Medicine medicineToInsert) {
            if (this == null) {
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
        
        /**
         * SEARCH METHOD 1: Find medicines expiring on exact date
         * @param targetDate Specific expiry date to search for
         * @param result List to store medicines expiring on this date
         */
        public void findMedicinesExpiringOnDate(LocalDate targetDate, List<Medicine> result) {
            if (this == null) return;
            
            LocalDate currentExpiry = this.medicine.getExpiredDate();
            
            // If current medicine expires on target date, add it
            if (currentExpiry.equals(targetDate)) {
                result.add(this.medicine);
                
                // Check both subtrees for other medicines with same expiry date
                if (this.left != null) {
                    this.left.findMedicinesExpiringOnDate(targetDate, result);
                }
                if (this.right != null) {
                    this.right.findMedicinesExpiringOnDate(targetDate, result);
                }
            } else if (targetDate.isBefore(currentExpiry)) {
                // Target date is earlier, search left subtree
                if (this.left != null) {
                    this.left.findMedicinesExpiringOnDate(targetDate, result);
                }
            } else {
                // Target date is later, search right subtree
                if (this.right != null) {
                    this.right.findMedicinesExpiringOnDate(targetDate, result);
                }
            }
        }
        
        /**
         * SEARCH METHOD 2: Find medicines expiring before specific date
         * @param cutoffDate Date to check against
         * @param result List to store medicines expiring before this date
         */
        public void findMedicinesExpiringBefore(LocalDate cutoffDate, List<Medicine> result) {
            if (this == null) return;
            
            LocalDate currentExpiry = this.medicine.getExpiredDate();
            
            // If current medicine expires before cutoff, add it
            if (currentExpiry.isBefore(cutoffDate)) {
                result.add(this.medicine);
                
                // All medicines in left subtree also expire before cutoff
                if (this.left != null) {
                    this.left.collectAllMedicines(result);
                }
                
                // Some medicines in right subtree might also expire before cutoff
                if (this.right != null) {
                    this.right.findMedicinesExpiringBefore(cutoffDate, result);
                }
            } else {
                // Current medicine expires on or after cutoff
                // Only check left subtree
                if (this.left != null) {
                    this.left.findMedicinesExpiringBefore(cutoffDate, result);
                }
            }
        }
        
        /**
         * SEARCH METHOD 3: Find medicines expiring after specific date
         * @param cutoffDate Date to check against
         * @param result List to store medicines expiring after this date
         */
        public void findMedicinesExpiringAfter(LocalDate cutoffDate, List<Medicine> result) {
            if (this == null) return;
            
            LocalDate currentExpiry = this.medicine.getExpiredDate();
            
            // If current medicine expires after cutoff, add it
            if (currentExpiry.isAfter(cutoffDate)) {
                result.add(this.medicine);
                
                // All medicines in right subtree also expire after cutoff
                if (this.right != null) {
                    this.right.collectAllMedicines(result);
                }
                
                // Some medicines in left subtree might also expire after cutoff
                if (this.left != null) {
                    this.left.findMedicinesExpiringAfter(cutoffDate, result);
                }
            } else {
                // Current medicine expires on or before cutoff
                // Only check right subtree
                if (this.right != null) {
                    this.right.findMedicinesExpiringAfter(cutoffDate, result);
                }
            }
        }
        
        /**
         * SEARCH METHOD 4: Find medicines expiring within date range
         * @param startDate Start of date range (inclusive)
         * @param endDate End of date range (inclusive)
         * @param result List to store medicines expiring in range
         */
        public void findMedicinesExpiringInRange(LocalDate startDate, LocalDate endDate, List<Medicine> result) {
            if (this == null) return;
            
            LocalDate currentExpiry = this.medicine.getExpiredDate();
            
            // Check if current medicine is in range
            if (!currentExpiry.isBefore(startDate) && !currentExpiry.isAfter(endDate)) {
                result.add(this.medicine);
            }
            
            // Search left subtree if there might be medicines in range
            if (currentExpiry.isAfter(startDate) && this.left != null) {
                this.left.findMedicinesExpiringInRange(startDate, endDate, result);
            }
            
            // Search right subtree if there might be medicines in range
            if (currentExpiry.isBefore(endDate) && this.right != null) {
                this.right.findMedicinesExpiringInRange(startDate, endDate, result);
            }
        }
        
        /**
         * SEARCH METHOD 5: Find medicines expiring within next N days
         * @param days Number of days from today
         * @param result List to store medicines expiring within N days
         */
        public void findMedicinesExpiringWithinDays(int days, List<Medicine> result) {
            LocalDate today = LocalDate.now();
            LocalDate cutoffDate = today.plusDays(days);
            findMedicinesExpiringInRange(today, cutoffDate, result);
        }
        
        /**
         * SEARCH METHOD 6: Find medicines expiring within next N weeks
         * @param weeks Number of weeks from today
         * @param result List to store medicines expiring within N weeks
         */
        public void findMedicinesExpiringWithinWeeks(int weeks, List<Medicine> result) {
            LocalDate today = LocalDate.now();
            LocalDate cutoffDate = today.plusWeeks(weeks);
            findMedicinesExpiringInRange(today, cutoffDate, result);
        }
        
        /**
         * SEARCH METHOD 7: Find medicines expiring within next N months
         * @param months Number of months from today
         * @param result List to store medicines expiring within N months
         */
        public void findMedicinesExpiringWithinMonths(int months, List<Medicine> result) {
            LocalDate today = LocalDate.now();
            LocalDate cutoffDate = today.plusMonths(months);
            findMedicinesExpiringInRange(today, cutoffDate, result);
        }
        
        /**
         * SEARCH METHOD 8: Find already expired medicines
         * @param result List to store expired medicines
         */
        public void findExpiredMedicines(List<Medicine> result) {
            LocalDate today = LocalDate.now();
            findMedicinesExpiringBefore(today, result);
        }
        
        /**
         * SEARCH METHOD 9: Find medicines by expiry status
         * @param status Expiry status to search for
         * @param result List to store medicines with matching status
         */
        public void findMedicinesByExpiryStatus(ExpiryStatus status, List<Medicine> result) {
            if (this == null) return;
            
            // Check if current medicine matches the status
            if (getMedicineExpiryStatus(this.medicine) == status) {
                result.add(this.medicine);
            }
            
            // Recursively search both subtrees
            if (this.left != null) {
                this.left.findMedicinesByExpiryStatus(status, result);
            }
            if (this.right != null) {
                this.right.findMedicinesByExpiryStatus(status, result);
            }
        }
        
        /**
         * SEARCH METHOD 10: Advanced search with multiple criteria
         * @param criteria Search criteria object
         * @param result List to store medicines matching criteria
         */
        public void findMedicinesWithCriteria(MedicineSearchCriteria criteria, List<Medicine> result) {
            if (this == null) return;
            
            // Check if current medicine matches all criteria
            if (criteria.matches(this.medicine)) {
                result.add(this.medicine);
            }
            
            // Search relevant subtrees based on date criteria
            LocalDate currentExpiry = this.medicine.getExpiredDate();
            
            // Search left subtree if needed
            if ((criteria.startDate == null || currentExpiry.isAfter(criteria.startDate)) && this.left != null) {
                this.left.findMedicinesWithCriteria(criteria, result);
            }
            
            // Search right subtree if needed
            if ((criteria.endDate == null || currentExpiry.isBefore(criteria.endDate)) && this.right != null) {
                this.right.findMedicinesWithCriteria(criteria, result);
            }
        }
        
        /**
         * Helper Method: Collect all medicines in subtree
         * @param result List to store all medicines
         */
        private void collectAllMedicines(List<Medicine> result) {
            if (this == null) return;
            
            result.add(this.medicine);
            
            if (this.left != null) {
                this.left.collectAllMedicines(result);
            }
            if (this.right != null) {
                this.right.collectAllMedicines(result);
            }
        }
        
        /**
         * Helper Method: Get medicine expiry status
         * @param medicine Medicine to check
         * @return Expiry status of the medicine
         */
        private ExpiryStatus getMedicineExpiryStatus(Medicine medicine) {
            LocalDate today = LocalDate.now();
            LocalDate expiryDate = medicine.getExpiredDate();
            long daysUntilExpiry = ChronoUnit.DAYS.between(today, expiryDate);
            
            if (daysUntilExpiry < 0) {
                return ExpiryStatus.EXPIRED;
            } else if (daysUntilExpiry <= 7) {
                return ExpiryStatus.EXPIRING_THIS_WEEK;
            } else if (daysUntilExpiry <= 30) {
                return ExpiryStatus.EXPIRING_THIS_MONTH;
            } else if (daysUntilExpiry <= 90) {
                return ExpiryStatus.EXPIRING_SOON;
            } else {
                return ExpiryStatus.SAFE;
            }
        }
        
        /**
         * Binary Tree Method: Find medicine with earliest expiry date
         * @return Medicine expiring soonest
         */
        public Medicine findEarliestExpiry() {
            if (this.left == null) {
                return this.medicine;
            }
            return this.left.findEarliestExpiry();
        }
        
        /**
         * Binary Tree Method: Find medicine with latest expiry date
         * @return Medicine expiring latest
         */
        public Medicine findLatestExpiry() {
            if (this.right == null) {
                return this.medicine;
            }
            return this.right.findLatestExpiry();
        }
        
        /**
         * Binary Tree Method: In-order traversal (medicines by expiry date)
         * @param result List to store medicines in expiry order
         */
        public void inOrderTraversal(List<Medicine> result) {
            if (this != null) {
                // Visit left (earlier expiry)
                if (this.left != null) {
                    this.left.inOrderTraversal(result);
                }
                
                // Visit current
                result.add(this.medicine);
                
                // Visit right (later expiry)
                if (this.right != null) {
                    this.right.inOrderTraversal(result);
                }
            }
        }
    }
    
    /**
     * Enum for Medicine Expiry Status
     * Categorizes medicines based on how soon they expire
     */
    public enum ExpiryStatus {
        EXPIRED("Already Expired"),
        EXPIRING_THIS_WEEK("Expiring This Week"),
        EXPIRING_THIS_MONTH("Expiring This Month"),
        EXPIRING_SOON("Expiring Soon (Within 3 Months)"),
        SAFE("Safe (More than 3 Months)");
        
        private final String description;
        
        ExpiryStatus(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    /**
     * Search Criteria Class for Advanced Medicine Search
     * Allows combining multiple search parameters
     */
    public static class MedicineSearchCriteria {
        private LocalDate startDate;        // Earliest expiry date to include
        private LocalDate endDate;          // Latest expiry date to include
        private String category;            // Medicine category filter
        private String namePattern;         // Medicine name pattern (contains)
        private ExpiryStatus expiryStatus;  // Expiry status filter
        private boolean includeExpired;     // Whether to include expired medicines
        
        public MedicineSearchCriteria() {
            this.includeExpired = false;
        }
        
        // Builder pattern methods for easy criteria construction
        public MedicineSearchCriteria withDateRange(LocalDate start, LocalDate end) {
            this.startDate = start;
            this.endDate = end;
            return this;
        }
        
        public MedicineSearchCriteria withCategory(String category) {
            this.category = category;
            return this;
        }
        
        public MedicineSearchCriteria withNamePattern(String pattern) {
            this.namePattern = pattern;
            return this;
        }
        
        public MedicineSearchCriteria withExpiryStatus(ExpiryStatus status) {
            this.expiryStatus = status;
            return this;
        }
        
        public MedicineSearchCriteria includeExpired(boolean include) {
            this.includeExpired = include;
            return this;
        }
        
        /**
         * Check if medicine matches all specified criteria
         * @param medicine Medicine to check
         * @return true if medicine matches all criteria
         */
        public boolean matches(Medicine medicine) {
            LocalDate expiryDate = medicine.getExpiredDate();
            
            // Check date range
            if (startDate != null && expiryDate.isBefore(startDate)) return false;
            if (endDate != null && expiryDate.isAfter(endDate)) return false;
            
            // Check category
            if (category != null && !medicine.getCategory().toLowerCase().contains(category.toLowerCase())) {
                return false;
            }
            
            // Check name pattern
            if (namePattern != null && !medicine.getMedicineName().toLowerCase().contains(namePattern.toLowerCase())) {
                return false;
            }
            
            // Check expiry status
            if (expiryStatus != null) {
                ExpiryStatus medicineStatus = getMedicineExpiryStatus(medicine);
                if (medicineStatus != expiryStatus) return false;
            }
            
            // Check if expired medicines should be included
            if (!includeExpired && medicine.isExpired()) return false;
            
            return true;
        }
        
        /**
         * Helper method to get medicine expiry status
         */
        private ExpiryStatus getMedicineExpiryStatus(Medicine medicine) {
            LocalDate today = LocalDate.now();
            LocalDate expiryDate = medicine.getExpiredDate();
            long daysUntilExpiry = ChronoUnit.DAYS.between(today, expiryDate);
            
            if (daysUntilExpiry < 0) {
                return ExpiryStatus.EXPIRED;
            } else if (daysUntilExpiry <= 7) {
                return ExpiryStatus.EXPIRING_THIS_WEEK;
            } else if (daysUntilExpiry <= 30) {
                return ExpiryStatus.EXPIRING_THIS_MONTH;
            } else if (daysUntilExpiry <= 90) {
                return ExpiryStatus.EXPIRING_SOON;
            } else {
                return ExpiryStatus.SAFE;
            }
        }
    }
    
    // Static BST root for medicine expiry management
    private static MedicineExpiryBSTNode medicineExpiryBSTRoot = null;
    
    /**
     * Binary Tree Method: Add medicine to expiry BST
     */
    public void addToExpiryBST() {
        if (medicineExpiryBSTRoot == null) {
            medicineExpiryBSTRoot = new MedicineExpiryBSTNode(this);
        } else {
            medicineExpiryBSTRoot = medicineExpiryBSTRoot.insert(this);
        }
        System.out.println("Medicine " + this.getMedicineName() + " added to expiry BST");
    }
    
    // ========================================
    // PUBLIC STATIC SEARCH METHODS
    // ========================================
    
    /**
     * SEARCH API 1: Find medicines expiring on specific date
     * @param targetDate Specific date to search for
     * @return List of medicines expiring on that date
     */
    public static List<Medicine> findMedicinesExpiringOnDate(LocalDate targetDate) {
        List<Medicine> result = new ArrayList<>();
        if (medicineExpiryBSTRoot != null) {
            medicineExpiryBSTRoot.findMedicinesExpiringOnDate(targetDate, result);
        }
        return result;
    }
    
    /**
     * SEARCH API 2: Find medicines expiring before specific date
     * @param cutoffDate Date to check against
     * @return List of medicines expiring before this date
     */
    public static List<Medicine> findMedicinesExpiringBefore(LocalDate cutoffDate) {
        List<Medicine> result = new ArrayList<>();
        if (medicineExpiryBSTRoot != null) {
            medicineExpiryBSTRoot.findMedicinesExpiringBefore(cutoffDate, result);
        }
        return result;
    }
    
    /**
     * SEARCH API 3: Find medicines expiring after specific date
     * @param cutoffDate Date to check against
     * @return List of medicines expiring after this date
     */
    public static List<Medicine> findMedicinesExpiringAfter(LocalDate cutoffDate) {
        List<Medicine> result = new ArrayList<>();
        if (medicineExpiryBSTRoot != null) {
            medicineExpiryBSTRoot.findMedicinesExpiringAfter(cutoffDate, result);
        }
        return result;
    }
    
    /**
     * SEARCH API 4: Find medicines expiring within date range
     * @param startDate Start of range (inclusive)
     * @param endDate End of range (inclusive)
     * @return List of medicines expiring in range
     */
    public static List<Medicine> findMedicinesExpiringInRange(LocalDate startDate, LocalDate endDate) {
        List<Medicine> result = new ArrayList<>();
        if (medicineExpiryBSTRoot != null) {
            medicineExpiryBSTRoot.findMedicinesExpiringInRange(startDate, endDate, result);
        }
        return result;
    }
    
    /**
     * SEARCH API 5: Find medicines expiring within next N days
     * @param days Number of days from today
     * @return List of medicines expiring within N days
     */
    public static List<Medicine> findMedicinesExpiringSoon(int days) {
        List<Medicine> result = new ArrayList<>();
        if (medicineExpiryBSTRoot != null) {
            medicineExpiryBSTRoot.findMedicinesExpiringWithinDays(days, result);
        }
        return result;
    }
    
    /**
     * SEARCH API 6: Find medicines expiring within next N weeks
     * @param weeks Number of weeks from today
     * @return List of medicines expiring within N weeks
     */
    public static List<Medicine> findMedicinesExpiringWithinWeeks(int weeks) {
        List<Medicine> result = new ArrayList<>();
        if (medicineExpiryBSTRoot != null) {
            medicineExpiryBSTRoot.findMedicinesExpiringWithinWeeks(weeks, result);
        }
        return result;
    }
    
    /**
     * SEARCH API 7: Find medicines expiring within next N months
     * @param months Number of months from today
     * @return List of medicines expiring within N months
     */
    public static List<Medicine> findMedicinesExpiringWithinMonths(int months) {
        List<Medicine> result = new ArrayList<>();
        if (medicineExpiryBSTRoot != null) {
            medicineExpiryBSTRoot.findMedicinesExpiringWithinMonths(months, result);
        }
        return result;
    }
    
    /**
     * SEARCH API 8: Find already expired medicines
     * @return List of expired medicines
     */
    public static List<Medicine> findExpiredMedicines() {
        List<Medicine> result = new ArrayList<>();
        if (medicineExpiryBSTRoot != null) {
            medicineExpiryBSTRoot.findExpiredMedicines(result);
        }
        return result;
    }
    
    /**
     * SEARCH API 9: Find medicines by expiry status
     * @param status Expiry status to search for
     * @return List of medicines with matching status
     */
    public static List<Medicine> findMedicinesByExpiryStatus(ExpiryStatus status) {
        List<Medicine> result = new ArrayList<>();
        if (medicineExpiryBSTRoot != null) {
            medicineExpiryBSTRoot.findMedicinesByExpiryStatus(status, result);
        }
        return result;
    }
    
    /**
     * SEARCH API 10: Advanced search with multiple criteria
     * @param criteria Search criteria object
     * @return List of medicines matching all criteria
     */
    public static List<Medicine> findMedicinesWithCriteria(MedicineSearchCriteria criteria) {
        List<Medicine> result = new ArrayList<>();
        if (medicineExpiryBSTRoot != null) {
            medicineExpiryBSTRoot.findMedicinesWithCriteria(criteria, result);
        }
        return result;
    }
    
    /**
     * SEARCH API 11: Find medicine expiring soonest
     * @return Medicine with earliest expiry date
     */
    public static Medicine findMedicineExpiringSoonest() {
        if (medicineExpiryBSTRoot != null) {
            return medicineExpiryBSTRoot.findEarliestExpiry();
        }
        return null;
    }
    
    /**
     * SEARCH API 12: Find medicine expiring latest
     * @return Medicine with latest expiry date
     */
    public static Medicine findMedicineExpiringLatest() {
        if (medicineExpiryBSTRoot != null) {
            return medicineExpiryBSTRoot.findLatestExpiry();
        }
        return null;
    }
    
    /**
     * SEARCH API 13: Get all medicines sorted by expiry date
     * @return List of medicines in expiry order
     */
    public static List<Medicine> getAllMedicinesByExpiry() {
        List<Medicine> result = new ArrayList<>();
        if (medicineExpiryBSTRoot != null) {
            medicineExpiryBSTRoot.inOrderTraversal(result);
        }
        return result;
    }
    
    // ========================================
    // CONVENIENCE SEARCH METHODS
    // ========================================
    
    /**
     * Convenience Method: Find medicines expiring today
     * @return List of medicines expiring today
     */
    public static List<Medicine> findMedicinesExpiringToday() {
        return findMedicinesExpiringOnDate(LocalDate.now());
    }
    
    /**
     * Convenience Method: Find medicines expiring this week
     * @return List of medicines expiring within 7 days
     */
    public static List<Medicine> findMedicinesExpiringThisWeek() {
        return findMedicinesExpiringSoon(7);
    }
    
    /**
     * Convenience Method: Find medicines expiring this month
     * @return List of medicines expiring within 30 days
     */
    public static List<Medicine> findMedicinesExpiringThisMonth() {
        return findMedicinesExpiringSoon(30);
    }
    
    /**
     * Convenience Method: Find medicines expiring next month
     * @return List of medicines expiring in 30-60 days
     */
    public static List<Medicine> findMedicinesExpiringNextMonth() {
        LocalDate startDate = LocalDate.now().plusDays(30);
        LocalDate endDate = LocalDate.now().plusDays(60);
        return findMedicinesExpiringInRange(startDate, endDate);
    }
    
    /**
     * Convenience Method: Find critical medicines (expiring within 3 days)
     * @return List of medicines needing immediate attention
     */
    public static List<Medicine> findCriticalMedicines() {
        return findMedicinesExpiringSoon(3);
    }
}

// ========================================
// USAGE EXAMPLES AND DEMONSTRATIONS
// ========================================

/**
 * Demonstration class showing how to use medicine expiration date search
 */
class MedicineExpirySearchDemo {
    
    public static void main(String[] args) {
        // Create sample medicines with different expiry dates
        setupSampleMedicines();
        
        // Demonstrate various search methods
        demonstrateBasicSearches();
        demonstrateAdvancedSearches();
        demonstrateConvenienceSearches();
        demonstrateCriteriaSearch();
    }
    
    /**
     * Setup sample medicines for testing
     */
    private static void setupSampleMedicines() {
        System.out.println("=== SETTING UP SAMPLE MEDICINES ===");
        
        // Create medicines with various expiry dates
        Medicine med1 = new Medicine(1, "Aspirin", "Take with food", "100mg", "Painkiller", 
                                   LocalDate.now().minusDays(5)); // Already expired
        med1.addToExpiryBST();
        
        Medicine med2 = new Medicine(2, "Vitamin C", "Take daily", "1000mg", "Supplement", 
                                   LocalDate.now().plusDays(2)); // Expiring in 2 days
        med2.addToExpiryBST();
        
        Medicine med3 = new Medicine(3, "Amoxicillin", "Take three times daily", "500mg", "Antibiotic", 
                                   LocalDate.now().plusDays(10)); // Expiring in 10 days
        med3.addToExpiryBST();
        
        Medicine med4 = new Medicine(4, "Lisinopril", "Take once daily", "10mg", "ACE Inhibitor", 
                                   LocalDate.now().plusDays(45)); // Expiring in 45 days
        med4.addToExpiryBST();
        
        Medicine med5 = new Medicine(5, "Metformin", "Take with meals", "500mg", "Antidiabetic", 
                                   LocalDate.now().plusMonths(6)); // Expiring in 6 months
        med5.addToExpiryBST();
        
        Medicine med6 = new Medicine(6, "Ibuprofen", "Take as needed", "200mg", "Painkiller", 
                                   LocalDate.now().plusDays(7)); // Expiring in 1 week
        med6.addToExpiryBST();
        
        Medicine med7 = new Medicine(7, "Omega-3", "Take with dinner", "1000mg", "Supplement", 
                                   LocalDate.now().plusDays(1)); // Expiring tomorrow
        med7.addToExpiryBST();
        
        Medicine med8 = new Medicine(8, "Cephalexin", "Take every 6 hours", "250mg", "Antibiotic", 
                                   LocalDate.now().plusDays(21)); // Expiring in 3 weeks
        med8.addToExpiryBST();
        
        System.out.println("Sample medicines added to BST");
    }
    
    /**
     * Demonstrate basic search methods
     */
    private static void demonstrateBasicSearches() {
        System.out.println("\n=== BASIC SEARCH DEMONSTRATIONS ===");
        
        // 1. Find medicines expiring on specific date
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Medicine> expiringTomorrow = Medicine.findMedicinesExpiringOnDate(tomorrow);
        System.out.println("Medicines expiring on " + tomorrow + ":");
        printMedicineList(expiringTomorrow);
        
        // 2. Find expired medicines
        List<Medicine> expiredMedicines = Medicine.findExpiredMedicines();
        System.out.println("\nExpired medicines:");
        printMedicineList(expiredMedicines);
        
        // 3. Find medicines expiring within 15 days
        List<Medicine> expiringSoon = Medicine.findMedicinesExpiringSoon(15);
        System.out.println("\nMedicines expiring within 15 days:");
        printMedicineList(expiringSoon);
        
        // 4. Find medicines expiring after 30 days
        LocalDate futureDate = LocalDate.now().plusDays(30);
        List<Medicine> expiringLater = Medicine.findMedicinesExpiringAfter(futureDate);
        System.out.println("\nMedicines expiring after " + futureDate + ":");
        printMedicineList(expiringLater);
        
        // 5. Find medicines in date range
        LocalDate startRange = LocalDate.now().plusDays(5);
        LocalDate endRange = LocalDate.now().plusDays(25);
        List<Medicine> inRange = Medicine.findMedicinesExpiringInRange(startRange, endRange);
        System.out.println("\nMedicines expiring between " + startRange + " and " + endRange + ":");
        printMedicineList(inRange);
    }
    
    /**
     * Demonstrate advanced search methods
     */
    private static void demonstrateAdvancedSearches() {
        System.out.println("\n=== ADVANCED SEARCH DEMONSTRATIONS ===");
        
        // 1. Find medicines by expiry status
        List<Medicine> expiringThisWeek = Medicine.findMedicinesByExpiryStatus(Medicine.ExpiryStatus.EXPIRING_THIS_WEEK);
        System.out.println("Medicines expiring this week:");
        printMedicineList(expiringThisWeek);
        
        List<Medicine> safeMedicines = Medicine.findMedicinesByExpiryStatus(Medicine.ExpiryStatus.SAFE);
        System.out.println("\nSafe medicines (expiring in 3+ months):");
        printMedicineList(safeMedicines);
        
        // 2. Find medicines expiring within weeks/months
        List<Medicine> expiringIn2Weeks = Medicine.findMedicinesExpiringWithinWeeks(2);
        System.out.println("\nMedicines expiring within 2 weeks:");
        printMedicineList(expiringIn2Weeks);
        
        List<Medicine> expiringIn3Months = Medicine.findMedicinesExpiringWithinMonths(3);
        System.out.println("\nMedicines expiring within 3 months:");
        printMedicineList(expiringIn3Months);
        
        // 3. Find earliest and latest expiring medicines
        Medicine earliest = Medicine.findMedicineExpiringSoonest();
        Medicine latest = Medicine.findMedicineExpiringLatest();
        
        System.out.println("\nEarliest expiring medicine: " + 
                          (earliest != null ? earliest.getMedicineName() + " on " + earliest.getExpiredDate() : "None"));
        System.out.println("Latest expiring medicine: " + 
                          (latest != null ? latest.getMedicineName() + " on " + latest.getExpiredDate() : "None"));
    }
    
    /**
     * Demonstrate convenience search methods
     */
    private static void demonstrateConvenienceSearches() {
        System.out.println("\n=== CONVENIENCE SEARCH DEMONSTRATIONS ===");
        
        // 1. Find medicines expiring today
        List<Medicine> expiringToday = Medicine.findMedicinesExpiringToday();
        System.out.println("Medicines expiring today:");
        printMedicineList(expiringToday);
        
        // 2. Find medicines expiring this week
        List<Medicine> expiringThisWeek = Medicine.findMedicinesExpiringThisWeek();
        System.out.println("\nMedicines expiring this week:");
        printMedicineList(expiringThisWeek);
        
        // 3. Find medicines expiring this month
        List<Medicine> expiringThisMonth = Medicine.findMedicinesExpiringThisMonth();
        System.out.println("\nMedicines expiring this month:");
        printMedicineList(expiringThisMonth);
        
        // 4. Find medicines expiring next month
        List<Medicine> expiringNextMonth = Medicine.findMedicinesExpiringNextMonth();
        System.out.println("\nMedicines expiring next month:");
        printMedicineList(expiringNextMonth);
        
        // 5. Find critical medicines (expiring within 3 days)
        List<Medicine> criticalMedicines = Medicine.findCriticalMedicines();
        System.out.println("\nCritical medicines (expiring within 3 days):");
        printMedicineList(criticalMedicines);
        
        // 6. Get all medicines sorted by expiry date
        List<Medicine> allSorted = Medicine.getAllMedicinesByExpiry();
        System.out.println("\nAll medicines sorted by expiry date:");
        printMedicineList(allSorted);
    }
    
    /**
     * Demonstrate advanced criteria-based search
     */
    private static void demonstrateCriteriaSearch() {
        System.out.println("\n=== CRITERIA-BASED SEARCH DEMONSTRATIONS ===");
        
        // 1. Search for painkillers expiring within 2 weeks
        Medicine.MedicineSearchCriteria criteria1 = new Medicine.MedicineSearchCriteria()
                .withCategory("Painkiller")
                .withDateRange(LocalDate.now(), LocalDate.now().plusWeeks(2));
        
        List<Medicine> painkillers = Medicine.findMedicinesWithCriteria(criteria1);
        System.out.println("Painkillers expiring within 2 weeks:");
        printMedicineList(painkillers);
        
        // 2. Search for medicines with "Amox" in name expiring within 1 month
        Medicine.MedicineSearchCriteria criteria2 = new Medicine.MedicineSearchCriteria()
                .withNamePattern("Amox")
                .withDateRange(LocalDate.now(), LocalDate.now().plusMonths(1));
        
        List<Medicine> amoxMedicines = Medicine.findMedicinesWithCriteria(criteria2);
        System.out.println("\nMedicines with 'Amox' in name expiring within 1 month:");
        printMedicineList(amoxMedicines);
        
        // 3. Search for expired medicines in specific category
        Medicine.MedicineSearchCriteria criteria3 = new Medicine.MedicineSearchCriteria()
                .withCategory("Painkiller")
                .includeExpired(true)
                .withExpiryStatus(Medicine.ExpiryStatus.EXPIRED);
        
        List<Medicine> expiredPainkillers = Medicine.findMedicinesWithCriteria(criteria3);
        System.out.println("\nExpired painkillers:");
        printMedicineList(expiredPainkillers);
        
        // 4. Complex search: Supplements expiring within 1 week, including expired
        Medicine.MedicineSearchCriteria criteria4 = new Medicine.MedicineSearchCriteria()
                .withCategory("Supplement")
                .withDateRange(LocalDate.now().minusDays(30), LocalDate.now().plusWeeks(1))
                .includeExpired(true);
        
        List<Medicine> supplements = Medicine.findMedicinesWithCriteria(criteria4);
        System.out.println("\nSupplements expiring within 1 week (including expired):");
        printMedicineList(supplements);
        
        // 5. Search by expiry status
        Medicine.MedicineSearchCriteria criteria5 = new Medicine.MedicineSearchCriteria()
                .withExpiryStatus(Medicine.ExpiryStatus.EXPIRING_THIS_WEEK);
        
        List<Medicine> expiringThisWeek = Medicine.findMedicinesWithCriteria(criteria5);
        System.out.println("\nMedicines with 'Expiring This Week' status:");
        printMedicineList(expiringThisWeek);
    }
    
    /**
     * Helper method to print medicine list
     * @param medicines List of medicines to print
     */
    private static void printMedicineList(List<Medicine> medicines) {
        if (medicines.isEmpty()) {
            System.out.println("  No medicines found.");
        } else {
            for (Medicine medicine : medicines) {
                System.out.println("  - " + medicine.getMedicineName() + 
                                 " (" + medicine.getCategory() + ") - Expires: " + medicine.getExpiredDate() +
                                 " - Days until expiry: " + getExpiryStatus(medicine));
            }
        }
    }
    
    /**
     * Helper method to get expiry status description
     * @param medicine Medicine to check
     * @return Expiry status description
     */
    private static String getExpiryStatus(Medicine medicine) {
        LocalDate today = LocalDate.now();
        LocalDate expiryDate = medicine.getExpiredDate();
        long daysUntilExpiry = java.time.temporal.ChronoUnit.DAYS.between(today, expiryDate);
        
        if (daysUntilExpiry < 0) {
            return "EXPIRED (" + Math.abs(daysUntilExpiry) + " days ago)";
        } else if (daysUntilExpiry == 0) {
            return "EXPIRES TODAY";
        } else {
            return daysUntilExpiry + " days";
        }
    }
}

// ========================================
// INTEGRATION WITH PHARMACIST DASHBOARD
// ========================================

/**
 * Example integration with Pharmacist class for medicine management
 */
class PharmacistMedicineManager {
    
    private Pharmacist pharmacist;
    
    public PharmacistMedicineManager(Pharmacist pharmacist) {
        this.pharmacist = pharmacist;
    }
    
    /**
     * Get daily medicine expiry report for pharmacist
     * @return Formatted report string
     */
    public String getDailyExpiryReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== DAILY MEDICINE EXPIRY REPORT ===\n");
        
        // Critical medicines (expiring within 3 days)
        List<Medicine> critical = Medicine.findCriticalMedicines();
        report.append("CRITICAL - Expiring within 3 days: ").append(critical.size()).append(" medicines\n");
        for (Medicine med : critical) {
            report.append("  ⚠️ ").append(med.getMedicineName()).append(" - ").append(med.getExpiredDate()).append("\n");
        }
        
        // Medicines expiring this week
        List<Medicine> thisWeek = Medicine.findMedicinesExpiringThisWeek();
        report.append("\nTHIS WEEK - Expiring within 7 days: ").append(thisWeek.size()).append(" medicines\n");
        for (Medicine med : thisWeek) {
            if (!critical.contains(med)) { // Don't duplicate critical medicines
                report.append("  🔶 ").append(med.getMedicineName()).append(" - ").append(med.getExpiredDate()).append("\n");
            }
        }
        
        // Already expired medicines
        List<Medicine> expired = Medicine.findExpiredMedicines();
        report.append("\nEXPIRED - Need immediate removal: ").append(expired.size()).append(" medicines\n");
        for (Medicine med : expired) {
            report.append("  ❌ ").append(med.getMedicineName()).append(" - ").append(med.getExpiredDate()).append("\n");
        }
        
        return report.toString();
    }
    
    /**
     * Get medicines that need restocking (expiring soon)
     * @param daysAhead Number of days to look ahead for restocking
     * @return List of medicines needing restocking
     */
    public List<Medicine> getMedicinesNeedingRestock(int daysAhead) {
        return Medicine.findMedicinesExpiringSoon(daysAhead);
    }
    
    /**
     * Search medicines by multiple criteria for pharmacist needs
     * @param category Medicine category
     * @param maxDaysUntilExpiry Maximum days until expiry
     * @param includeExpired Whether to include expired medicines
     * @return List of matching medicines
     */
    public List<Medicine> searchMedicinesForPharmacist(String category, int maxDaysUntilExpiry, boolean includeExpired) {
        Medicine.MedicineSearchCriteria criteria = new Medicine.MedicineSearchCriteria()
                .withCategory(category)
                .withDateRange(includeExpired ? LocalDate.now().minusYears(1) : LocalDate.now(), 
                              LocalDate.now().plusDays(maxDaysUntilExpiry))
                .includeExpired(includeExpired);
        
        return Medicine.findMedicinesWithCriteria(criteria);
    }
    
    /**
     * Get inventory summary by expiry status
     * @return Formatted inventory summary
     */
    public String getInventorySummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("=== MEDICINE INVENTORY SUMMARY ===\n");
        
        // Count medicines by expiry status
        List<Medicine> expired = Medicine.findMedicinesByExpiryStatus(Medicine.ExpiryStatus.EXPIRED);
        List<Medicine> expiringThisWeek = Medicine.findMedicinesByExpiryStatus(Medicine.ExpiryStatus.EXPIRING_THIS_WEEK);
        List<Medicine> expiringThisMonth = Medicine.findMedicinesByExpiryStatus(Medicine.ExpiryStatus.EXPIRING_THIS_MONTH);
        List<Medicine> expiringSoon = Medicine.findMedicinesByExpiryStatus(Medicine.ExpiryStatus.EXPIRING_SOON);
        List<Medicine> safe = Medicine.findMedicinesByExpiryStatus(Medicine.ExpiryStatus.SAFE);
        
        summary.append("Expired: ").append(expired.size()).append(" medicines\n");
        summary.append("Expiring This Week: ").append(expiringThisWeek.size()).append(" medicines\n");
        summary.append("Expiring This Month: ").append(expiringThisMonth.size()).append(" medicines\n");
        summary.append("Expiring Soon (3 months): ").append(expiringSoon.size()).append(" medicines\n");
        summary.append("Safe (3+ months): ").append(safe.size()).append(" medicines\n");
        
        // Calculate total
        int total = expired.size() + expiringThisWeek.size() + expiringThisMonth.size() + 
                   expiringSoon.size() + safe.size();
        summary.append("Total Medicines: ").append(total).append("\n");
        
        return summary.toString();
    }
}

// ========================================
// PERFORMANCE ANALYSIS
// ========================================

/**
 * Performance analysis of medicine expiry search operations
 */
class MedicineSearchPerformanceAnalysis {
    
    /**
     * Analyze search performance with different tree sizes
     */
    public static void analyzeSearchPerformance() {
        System.out.println("\n=== MEDICINE SEARCH PERFORMANCE ANALYSIS ===");
        
        // Test with different numbers of medicines
        int[] treeSizes = {100, 1000, 10000};
        
        for (int size : treeSizes) {
            System.out.println("\nTesting with " + size + " medicines:");
            
            // Clear existing tree and create new test data
            createTestMedicines(size);
            
            // Test various search operations
            long startTime, endTime;
            
            // Test 1: Find medicines expiring within 30 days
            startTime = System.nanoTime();
            List<Medicine> expiringSoon = Medicine.findMedicinesExpiringSoon(30);
            endTime = System.nanoTime();
            System.out.println("  Find expiring within 30 days: " + 
                             ((endTime - startTime) / 1000000.0) + " ms, Found: " + expiringSoon.size());
            
            // Test 2: Find expired medicines
            startTime = System.nanoTime();
            List<Medicine> expired = Medicine.findExpiredMedicines();
            endTime = System.nanoTime();
            System.out.println("  Find expired medicines: " + 
                             ((endTime - startTime) / 1000000.0) + " ms, Found: " + expired.size());
            
            // Test 3: Find medicines in date range
            startTime = System.nanoTime();
            LocalDate start = LocalDate.now().plusDays(10);
            LocalDate end = LocalDate.now().plusDays(50);
            List<Medicine> inRange = Medicine.findMedicinesExpiringInRange(start, end);
            endTime = System.nanoTime();
            System.out.println("  Find in date range: " + 
                             ((endTime - startTime) / 1000000.0) + " ms, Found: " + inRange.size());
            
            // Test 4: Get all medicines sorted
            startTime = System.nanoTime();
            List<Medicine> allSorted = Medicine.getAllMedicinesByExpiry();
            endTime = System.nanoTime();
            System.out.println("  Get all sorted: " + 
                             ((endTime - startTime) / 1000000.0) + " ms, Total: " + allSorted.size());
        }
    }
    
    /**
     * Create test medicines with random expiry dates
     * @param count Number of medicines to create
     */
    private static void createTestMedicines(int count) {
        // Note: In a real implementation, you would clear the existing BST first
        System.out.println("Creating " + count + " test medicines...");
        
        for (int i = 1; i <= count; i++) {
            // Create medicines with random expiry dates (within 2 years)
            LocalDate randomExpiry = LocalDate.now().plusDays((int)(Math.random() * 730) - 365);
            
            Medicine testMedicine = new Medicine(
                i,
                "TestMedicine" + i,
                "Test instruction",
                "100mg",
                "TestCategory",
                randomExpiry
            );
            
            testMedicine.addToExpiryBST();
        }
    }
}

/**
 * SUMMARY OF MEDICINE EXPIRATION DATE SEARCH IMPLEMENTATION
 * 
 * This comprehensive implementation provides:
 * 
 * 1. BASIC SEARCH OPERATIONS:
 *    - Find by exact date
 *    - Find before/after specific date
 *    - Find within date range
 *    - Find expired medicines
 * 
 * 2. TIME-BASED SEARCHES:
 *    - Find expiring within N days/weeks/months
 *    - Convenience methods for today/this week/this month
 *    - Critical medicines (expiring within 3 days)
 * 
 * 3. STATUS-BASED SEARCHES:
 *    - Search by expiry status (expired, expiring soon, safe)
 *    - Categorization by urgency level
 * 
 * 4. ADVANCED CRITERIA SEARCH:
 *    - Multiple filter combination
 *    - Name pattern matching
 *    - Category filtering
 *    - Builder pattern for easy criteria construction
 * 
 * 5. UTILITY OPERATIONS:
 *    - Find earliest/latest expiring medicine
 *    - Get all medicines sorted by expiry
 *    - Inventory management features
 * 
 * 6. INTEGRATION FEATURES:
 *    - Pharmacist dashboard integration
 *    - Daily expiry reports
 *    - Inventory summaries
 *    - Restocking recommendations
 * 
 * PERFORMANCE CHARACTERISTICS:
 * - Search operations: O(log n) average case
 * - Range queries: O(log n + k) where k is result size
 * - In-order traversal: O(n) for complete sorted list
 * - Memory usage: O(n) for tree structure
 * 
 * BINARY SEARCH TREE ADVANTAGES:
 * - Efficient searching compared to linear scan O(n)
 * - Automatic sorting maintenance
 * - Range queries are naturally efficient
 * - Easy to add/remove medicines while maintaining order
 * 
 * This implementation demonstrates how binary search trees can be used
 * to solve real-world problems in medical inventory management with
 * optimal performance and clean, maintainable code structure.
 */