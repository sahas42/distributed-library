import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BorrowedBookRepository {

    /* saveTransaction now sets ExpectedDateOfReturn = today + 14 */
    public boolean saveTransaction(String userId, String bookId, String bookType) {
        String sql = "INSERT INTO borrowedBooks " +
                     "(UserId, BookId, DateOfBorrowal, ExpectedDateOfReturn, BookType) " +
                     "VALUES (?,?,?,?,?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            LocalDate today = LocalDate.now();
            ps.setString(1, userId);
            ps.setString(2, bookId);
            ps.setDate  (3, java.sql.Date.valueOf(today));
            ps.setDate  (4, java.sql.Date.valueOf(today.plusDays(14)));   // 14‑day window
            ps.setString(5, bookType);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("saveTransaction error: " + e.getMessage());
            return false;
        }
    }

    public boolean removeTransaction(String userId, String bookId) {
        String sql = "DELETE FROM borrowedBooks WHERE UserId=? AND BookId=?";
        try (Connection c = DatabaseConnection.connect();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, userId);
            p.setString(2, bookId);
            return p.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("removeTransaction error: " + e.getMessage());
            return false;
        }
    }

    public boolean removeTransactionByBook(int bookId) {
        String sql = "DELETE FROM borrowedBooks WHERE BookId=?";
        try (Connection c = DatabaseConnection.connect();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, bookId);
            return p.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public List<Integer> findOverdueBookIds(LocalDate dateLimit) {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT BookId FROM borrowedBooks WHERE DateOfBorrowal < ?";
        try (Connection c = DatabaseConnection.connect();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setDate(1, java.sql.Date.valueOf(dateLimit));
            ResultSet rs = p.executeQuery();
            while (rs.next()) ids.add(rs.getInt(1));
            rs.close();
        } catch (SQLException e) {
            System.err.println("findOverdueBookIds error: " + e.getMessage());
        }
        return ids;
    }
}
