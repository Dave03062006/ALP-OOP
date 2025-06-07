public class Staff extends User {
    protected boolean onDuty;
    
    public Staff(String email, String password, String fullName, String phoneNumber, boolean onDuty) {
        super(email, password, fullName, phoneNumber);
        this.onDuty = onDuty;
    }
    
    public boolean isOnDuty() { return onDuty; }
    public void setOnDuty(boolean onDuty) { this.onDuty = onDuty; }

    @Override
    public void displayInfo() {
        System.out.println("=== Staff Info: ===");
        System.out.println("Email: " + getEmail());
        System.out.println("Full Name: " + getFullName());
        System.out.println("Phone Number: " + getPhoneNumber());
        System.out.println("On Duty: " + (onDuty ? "Yes" : "No"));

    }


    @Override
    public void login(String email, String password) {
        // Implement login logic here
        System.out.println("Staff logged in with email: " + email);
    }
    @Override
    public void register(String email, String password, String fullName, String phoneNumber) {
        // Implement registration logic here
        System.out.println("Staff registered with email: " + email);
    }

}