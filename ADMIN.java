import java.util.*;
import java.sql.ResultSet;

public class ADMIN extends PERSON {
    
    public ADMIN() {}

    public ADMIN(String name, long phoneNumber, String email, String ID, String password) {
        super(name, phoneNumber, email, ID, password);
    }
    
    public String viewUserDetails(String userId) {
        String query = "SELECT * FROM persons WHERE ID='" + userId + "'";
        try {
            ResultSet rs = DatabaseConnection.executeQuery(query);
            if (rs != null && rs.next()) {
                return "User ID: " + rs.getString("ID") + "\n"
                     + "Name: " + rs.getString("name") + "\n"
                     + "Email: " + rs.getString("email") + "\n"
                     + "Phone: " + rs.getLong("phoneNumber") + "\n";
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return "User details not found for " + userId;
    }
    
    public boolean addBook(BOOK book) {
        String query = 
            "INSERT INTO books (bookId, name, BookedStatus, Location, subLibraryID, book_type, Author, DonatedBy) " +
            "VALUES (" + book.getBookId() + ", '" + book.getName() + "', '" +
            book.getBookedStatus() + "', '" + book.getLocation() + "', " +
            book.getSubLibraryID() + ", '" + book.getbook_type() + "', '" +
            book.getAuthor() + "', '" + book.getDonatedby() + "')";
        int result = DatabaseConnection.executeUpdate(query);
        return result > 0;
    }

    public boolean removeBook(int bookId) {
        String query = "DELETE FROM books WHERE bookId=" + bookId;
        int result = DatabaseConnection.executeUpdate(query);
        return result > 0;
    }

    public List<BOOK> ViewBooks() {
        List<BOOK> books = new ArrayList<>();
        String query = "SELECT * FROM books";
        try {
            ResultSet rs = DatabaseConnection.executeQuery(query);
            while (rs != null && rs.next()) {
                BOOK b = new BOOK();
                b.setBookId(rs.getInt("bookId"));
                b.setName(rs.getString("name"));
                b.setBookedStatus(rs.getString("BookedStatus"));
                b.setSubLibraryID(rs.getInt("subLibraryID"));
                b.setAuthor(rs.getString("Author"));
                b.setDonatedby(rs.getString("DonatedBy"));
                books.add(b);
            }
        } catch(Exception e) {
            System.err.println("Error retrieving books: " + e.getMessage());
        }
        return books;
    }

    public boolean addSubLibrary(Sublibrary sub) {
        String query = 
            "INSERT INTO sublibraries (name, latitude, longitude, loc_desc) VALUES ('" +
            sub.getName() + "', " + sub.getLatitude() + ", " + sub.getLongitude() + ", '" +
            sub.getLoc_desc() + "')";
        int result = DatabaseConnection.executeUpdate(query);
        return result > 0;
    }
}
