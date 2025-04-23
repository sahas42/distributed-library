import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SublibraryRepository {

    public Sublibrary findById(int id) {
        String sql = "SELECT * FROM sublibraries WHERE id=?";
        Sublibrary sub = null;
        try(Connection conn = DatabaseConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                sub = new Sublibrary();
                sub.setName(rs.getString("name"));
                sub.setLatitude(rs.getDouble("latitude"));
                sub.setLongitude(rs.getDouble("longitude"));
                sub.setLoc_desc(rs.getString("loc_desc"));
            }
            rs.close();
        } catch(SQLException e) {
            System.err.println("Error findById: " + e.getMessage());
        }
        return sub;
    }

    public Sublibrary findByName(String name) {
        String sql = "SELECT * FROM sublibraries WHERE name=?";
        Sublibrary sub = null;
        try(Connection conn = DatabaseConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                sub = new Sublibrary();
                sub.setName(rs.getString("name"));
                sub.setLatitude(rs.getDouble("latitude"));
                sub.setLongitude(rs.getDouble("longitude"));
                sub.setLoc_desc(rs.getString("loc_desc"));
            }
            rs.close();
        } catch(SQLException e) {
            System.err.println("Error findByName: " + e.getMessage());
        }
        return sub;
    }

    public boolean save(Sublibrary sub) {
        String sql = "INSERT INTO sublibraries (name, latitude, longitude, loc_desc) VALUES (?,?,?,?)";
        try(Connection conn = DatabaseConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, sub.getName());
            pstmt.setDouble(2, sub.getLatitude());
            pstmt.setDouble(3, sub.getLongitude());
            pstmt.setString(4, sub.getLoc_desc());
            return pstmt.executeUpdate() > 0;
        } catch(SQLException e) {
            System.err.println("Error saving sublibrary: " + e.getMessage());
            return false;
        }
    }
}
