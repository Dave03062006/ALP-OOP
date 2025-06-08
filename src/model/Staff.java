package model;
public abstract class Staff extends User {
    protected boolean onDuty;
    
    public Staff(String email, String password, String fullName, String phoneNumber, boolean onDuty) {
        super(email, password, fullName, phoneNumber);
        this.onDuty = onDuty;
    }
    
    public boolean isOnDuty() { return onDuty; }
    public void setOnDuty(boolean onDuty) { this.onDuty = onDuty; }

    // Abstract methods inherited from User - must be implemented by Doctor and Pharmacist
    @Override
    public abstract void displayInfo();
    @Override
    public abstract boolean login(String email, String password);
    @Override
    public abstract boolean register(String email, String password, String fullName, String phoneNumber);
}