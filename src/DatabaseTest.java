import java.sql.*;

public class DatabaseTest {
    public static void main(String[] args) {
        System.out.println("🔍 Testing Database Connection...");
        System.out.println("=".repeat(40));
        
        try {
            // Test basic connection
            Connection conn = DatabaseConnect.getConnection();
            
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Database connection successful!");
                System.out.println("📊 Connection details:");
                System.out.println("   - URL: " + conn.getMetaData().getURL());
                System.out.println("   - Database: " + conn.getMetaData().getDatabaseProductName());
                System.out.println("   - Version: " + conn.getMetaData().getDatabaseProductVersion());
                
                // Test if tables exist
                testTablesExist(conn);
                
                // Test basic operations
                testBasicOperations(conn);
                
            } else {
                System.out.println("❌ Database connection failed!");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Database connection error:");
            System.out.println("   Error: " + e.getMessage());
            System.out.println("   Type: " + e.getClass().getSimpleName());
            
            // Common solutions
            System.out.println("\n💡 Possible solutions:");
            System.out.println("   1. Make sure XAMPP/MySQL is running");
            System.out.println("   2. Check database name 'alp_oop' exists");
            System.out.println("   3. Verify username/password in DatabaseConnect.java");
            System.out.println("   4. Ensure MySQL JDBC driver is in classpath");
        } finally {
            DatabaseConnect.closeConnection();
        }
    }
    
    private static void testTablesExist(Connection conn) {
        System.out.println("\n📋 Checking tables...");
        String[] tables = {"doctors", "patients", "pharmacists", "appointments", "prescriptions", "medicines"};
        
        try {
            DatabaseMetaData metaData = conn.getMetaData();
            
            for (String table : tables) {
                ResultSet rs = metaData.getTables(null, null, table, new String[]{"TABLE"});
                if (rs.next()) {
                    System.out.println("   ✅ Table '" + table + "' exists");
                } else {
                    System.out.println("   ❌ Table '" + table + "' missing");
                }
                rs.close();
            }
        } catch (SQLException e) {
            System.out.println("   ⚠️ Error checking tables: " + e.getMessage());
        }
    }
    
    private static void testBasicOperations(Connection conn) {
        System.out.println("\n🧪 Testing basic operations...");
        
        try {
            // Test simple query
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT 1 as test");
            
            if (rs.next()) {
                System.out.println("   ✅ Basic query execution works");
            }
            
            rs.close();
            stmt.close();
            
            // Test doctors table if exists
            try {
                PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) as count FROM doctors");
                ResultSet doctorRs = pstmt.executeQuery();
                if (doctorRs.next()) {
                    System.out.println("   ✅ Doctors table accessible - " + doctorRs.getInt("count") + " records");
                }
                doctorRs.close();
                pstmt.close();
            } catch (SQLException e) {
                System.out.println("   ⚠️ Doctors table issue: " + e.getMessage());
            }
            
            // Test patients table if exists
            try {
                PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) as count FROM patients");
                ResultSet patientRs = pstmt.executeQuery();
                if (patientRs.next()) {
                    System.out.println("   ✅ Patients table accessible - " + patientRs.getInt("count") + " records");
                }
                patientRs.close();
                pstmt.close();
            } catch (SQLException e) {
                System.out.println("   ⚠️ Patients table issue: " + e.getMessage());
            }
            
        } catch (SQLException e) {
            System.out.println("   ❌ Basic operations failed: " + e.getMessage());
        }
    }
}