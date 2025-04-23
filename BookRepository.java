import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookRepository {

    public BOOK findById(int id) {
        String sql = "SELECT * FROM books WHERE bookId = ?";
        BOOK book = null;
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                book = buildBookFromResultSet(rs);
            }
            rs.close();
        } catch(SQLException e) {
            System.err.println("Error findById: " + e.getMessage());
        }
        return book;
    }

    public List<BOOK> findAvailableBooksByKeyword(String keyword) {
        String sql = "SELECT * FROM books WHERE BookedStatus='available' AND name LIKE ?";
        List<BOOK> results = new ArrayList<>();
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                results.add(buildBookFromResultSet(rs));
            }
            rs.close();
        } catch(SQLException e) {
            System.err.println("Error in findAvailableBooksByKeyword: " + e.getMessage());
        }
        return results;
    }

    public List<BOOK> findAvailableBooks() {
        return findAvailableBooksByKeyword(""); // empty => no filter
    }

    public List<BOOK> findBorrowedByUser(String userId) {
        String sql = "SELECT * FROM books WHERE BookedStatus='borrowed' AND CurrentPersonal=?";
        List<BOOK> borrowed = new ArrayList<>();
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                borrowed.add(buildBookFromResultSet(rs));
            }
            rs.close();
        } catch(SQLException e) {
            System.err.println("Error in findBorrowedByUser: " + e.getMessage());
        }
        return borrowed;
    }

    public List<BOOK> findOwnedBooksByKeyword(String owner, String keyword) {
        String sql = "SELECT * FROM books WHERE DonatedBy=? AND name LIKE ?";
        List<BOOK> results = new ArrayList<>();
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, owner);
            pstmt.setString(2, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                results.add(buildBookFromResultSet(rs));
            }
            rs.close();
        } catch(SQLException e) {
            System.err.println("Error in findOwnedBooksByKeyword: " + e.getMessage());
        }
        return results;
    }

    public ArrayList<BOOK> findByDonar(String donor) {
        String sql = "SELECT * FROM books WHERE DonatedBy=?";
        ArrayList<BOOK> results = new ArrayList<>();
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, donor);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                results.add(buildBookFromResultSet(rs));
            }
            rs.close();
        } catch(SQLException e) {
            System.err.println("Error in findByDonar: " + e.getMessage());
        }
        return results;
    }

    public boolean save(BOOK book) {
        String sql = "INSERT INTO books " +
            "(name, BookedStatus, CurrentPersonal, Location, subLibraryID, DateOfReturn, book_type, Author, DonatedBy) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection conn = DatabaseConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getName());
            pstmt.setString(2, book.getBookedStatus());
            pstmt.setString(3, book.getCurrentPersonal());
            pstmt.setString(4, book.getLocation());
            pstmt.setInt(5, book.getSubLibraryID());

            if (book.getDateOfReturn() != null) {
                pstmt.setDate(6, java.sql.Date.valueOf(book.getDateOfReturn()));
            } else {
                pstmt.setNull(6, java.sql.Types.DATE);
            }
            pstmt.setString(7, book.getbook_type());
            pstmt.setString(8, book.getAuthor());
            pstmt.setString(9, book.getDonatedby());

            return pstmt.executeUpdate() > 0;
        } catch(SQLException e) {
            System.err.println("Error saving book: " + e.getMessage());
            return false;
        }
    }

    public boolean update(BOOK book) {
        String sql = "UPDATE books SET " +
            "name=?, BookedStatus=?, CurrentPersonal=?, Location=?, subLibraryID=?, DateOfReturn=?, book_type=?, Author=?, DonatedBy=? " +
            "WHERE bookId=?";

        try(Connection conn = DatabaseConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getName());
            pstmt.setString(2, book.getBookedStatus());
            pstmt.setString(3, book.getCurrentPersonal());
            pstmt.setString(4, book.getLocation());
            pstmt.setInt(5, book.getSubLibraryID());

            if (book.getDateOfReturn() != null) {
                pstmt.setDate(6, java.sql.Date.valueOf(book.getDateOfReturn()));
            } else {
                pstmt.setNull(6, java.sql.Types.DATE);
            }
            pstmt.setString(7, book.getbook_type());
            pstmt.setString(8, book.getAuthor());
            pstmt.setString(9, book.getDonatedby());
            pstmt.setInt(10, book.getBookId());

            return pstmt.executeUpdate() > 0;
        } catch(SQLException e) {
            System.err.println("Error updating book: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int bookId) {
        String sql = "DELETE FROM books WHERE bookId=?";
        try(Connection conn = DatabaseConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bookId);
            return pstmt.executeUpdate() > 0;
        } catch(SQLException e) {
            System.err.println("Error deleting book: " + e.getMessage());
            return false;
        }
    }

    private BOOK buildBookFromResultSet(ResultSet rs) throws SQLException {
        BOOK b = new BOOK();
        b.setBookId(rs.getInt("bookId"));
        b.setName(rs.getString("name"));
        b.setBookedStatus(rs.getString("BookedStatus"));
        b.setCurrentPersonal(rs.getString("CurrentPersonal"));
        b.setLocation(rs.getString("Location"));
        b.setSubLibraryID(rs.getInt("subLibraryID"));
        b.setbook_type(rs.getString("book_type"));
        b.setAuthor(rs.getString("Author"));
        b.setDonatedby(rs.getString("DonatedBy"));

        java.sql.Date dd = rs.getDate("DateOfReturn");
        if (dd != null) {
            b.setDateOfReturn(dd.toLocalDate());
        }
        return b;
    }
}
