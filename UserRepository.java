import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {

    public PERSON findById(String id) {
        String sql = "SELECT * FROM persons WHERE ID=?";
        PERSON person = null;
        try(Connection conn = DatabaseConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                String pType = rs.getString("person_type");
                if("admin".equalsIgnoreCase(pType)) {
                    person = new ADMIN();
                } else {
                    person = new User();
                }
                person.setID(rs.getString("ID"));
                person.setName(rs.getString("name"));
                person.setPhoneNumber(rs.getLong("phoneNumber"));
                person.setEmail(rs.getString("email"));
                person.setPassword(rs.getString("password"));
            }
            rs.close();
        } catch(SQLException e) {
            System.err.println("Error findById: " + e.getMessage());
        }
        return person;
    }

    public boolean save(PERSON person) {
        String sql = 
            "INSERT INTO persons (ID, name, phoneNumber, email, password, person_type) " +
            "VALUES (?,?,?,?,?,?)";
        try(Connection conn = DatabaseConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, person.getID());
            pstmt.setString(2, person.getName());
            pstmt.setLong(3, person.getPhoneNumber());
            pstmt.setString(4, person.getEmail());
            pstmt.setString(5, person.getPassword());
            String t = (person instanceof ADMIN) ? "admin" : "user";
            pstmt.setString(6, t);

            return pstmt.executeUpdate() > 0;
        } catch(SQLException e) {
            System.err.println("Error saving person: " + e.getMessage());
            return false;
        }
    }

    public boolean update(PERSON person) {
        String sql = "UPDATE persons SET name=?, phoneNumber=?, email=?, password=? WHERE ID=?";
        try(Connection conn = DatabaseConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, person.getName());
            pstmt.setLong(2, person.getPhoneNumber());
            pstmt.setString(3, person.getEmail());
            pstmt.setString(4, person.getPassword());
            pstmt.setString(5, person.getID());
            return pstmt.executeUpdate() > 0;
        } catch(SQLException e) {
            System.err.println("Error updating person: " + e.getMessage());
            return false;
        }
    }

    public boolean updatePassword(String newPassword, String userId) {
        String sql = "UPDATE persons SET password=? WHERE ID=?";
        try(Connection conn = DatabaseConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newPassword);
            pstmt.setString(2, userId);
            return pstmt.executeUpdate() > 0;
        } catch(SQLException e) {
            System.err.println("Error updating password: " + e.getMessage());
            return false;
        }
    }
}
