import java.sql.*;

public class TestConnection {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL JDBC Driver loaded successfully!");
            
            // Test connection (adjust password if needed)
            String url = "jdbc:mysql://localhost:3306/alp_oop";
            Connection conn = DriverManager.getConnection(url, "root", "");
            
            if (conn != null) {
                System.out.println("✅ Database connection successful!");
                conn.close();
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}