import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class Pharmacist extends Staff {
    protected Queue queue;
    protected int pharmacistId;

    public int getPharmacistId() {
        return pharmacistId;
    }

    public void setPharmacistId(int pharmacistId) {
        this.pharmacistId = pharmacistId;
    }

    public Queue getQueue() {
        return queue;
    }

    public Pharmacist(String email, String password, String fullName, String phoneNumber, boolean onDuty) {
        super(email, password, fullName, phoneNumber, onDuty);
        this.queue = new Queue();
        this.pharmacistId = 0; // Default ID, will be set when registered
    }

    // Add this constructor if you want to create from DB with ID
    public Pharmacist(int pharmacistId, String email, String password, String fullName, String phoneNumber, boolean onDuty) {
        super(email, password, fullName, phoneNumber, onDuty);
        this.pharmacistId = pharmacistId;
        this.queue = new Queue();
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Role: Pharmacist");
        System.out.println("ID: " + pharmacistId);
        System.out.println("Queue size: " + queue.size());
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
                this.fullName = rs.getString("full_name");
                this.phoneNumber = rs.getString("phone_number");
                System.out.println("Pharmacist login successful!");
            } else {
                System.out.println("Invalid credentials!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Pharmacist logged in with email: " + email);
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
        System.out.println("Pharmacist registered with email: " + email);
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

    public void fulfillPrescription(Appointment appointment) {
        if (appointment.getPrescription() != null) {
            appointment.setPharmacist(this);
            addMedicinesToQueue(appointment.getMedicines());
        }
    }

    // Load all pharmacists from database into ArrayList
    public static ArrayList<Pharmacist> loadAllFromDatabase() {
        ArrayList<Pharmacist> pharmacists = new ArrayList<>();
        try {
            Connection conn = DatabaseConnect.getConnection();
            String sql = "SELECT * FROM pharmacists";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Pharmacist pharmacist = new Pharmacist(
                    rs.getInt("pharmacist_id"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("full_name"),
                    rs.getString("phone_number"),
                    true
                );
                pharmacists.add(pharmacist);
            }
            System.out.println("Loaded " + pharmacists.size() + " pharmacists from database");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pharmacists;
    }
}