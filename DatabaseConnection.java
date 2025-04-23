import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static Connection connection;

    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/liabrary_admin";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "mypass99";

    static Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Database connection error: " + e.getMessage());
            return null;
        }
    }

    public static void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }

    public static ResultSet executeQuery(String sql) {
        try {
            Statement st = connect().createStatement();
            return st.executeQuery(sql);
        } catch (SQLException e) {
            System.err.println("Query execution error: " + e.getMessage());
            return null;
        }
    }

    public static int executeUpdate(String sql) {
        try {
            Statement st = connect().createStatement();
            return st.executeUpdate(sql);
        } catch (SQLException e) {
            System.err.println("Update execution error: " + e.getMessage());
            return -1;
        }
    }
}
