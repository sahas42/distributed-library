import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class MAIN {
    private BookRepository bookRepository = new BookRepository();
    private UserRepository userRepository = new UserRepository();
    private SublibraryRepository sublibraryRepository = new SublibraryRepository();
    private List<PERSON> loggedInUsers = new ArrayList<>();
    
    public void sendNotifications() {
        LocalDate soon = LocalDate.now().plusDays(3);
        System.out.println("Notifications: any books due around " + soon);
    }

    public boolean login(String userId, String password) {
        PERSON person = userRepository.findById(userId);
        if (person != null && person.validateCredentials(userId, password)) {
            loggedInUsers.add(person);
            return true;
        }
        return false;
    }

    public boolean createAccount(String name, long phoneNumber, String email, 
                                 String id, String password, boolean isAdmin) {
        if (userRepository.findById(id) != null) {
            System.out.println("User ID already exists: " + id);
            return false;
        }
        PERSON newPerson = isAdmin 
            ? new ADMIN(name, phoneNumber, email, id, password)
            : new User(name, phoneNumber, email, id, password);
        return userRepository.save(newPerson);
    }

    public static void initializeDatabase() {
        try {
            String createPersonsTable = 
                "CREATE TABLE IF NOT EXISTS persons (" +
                "  ID VARCHAR(50) PRIMARY KEY, " +
                "  name VARCHAR(100) NOT NULL, " +
                "  phoneNumber BIGINT NOT NULL, " +
                "  email VARCHAR(100) NOT NULL, " +
                "  password VARCHAR(255) NOT NULL, " +
                "  person_type VARCHAR(10) NOT NULL" +
                ")";
            
            String createSublibrariesTable =
                "CREATE TABLE IF NOT EXISTS sublibraries (" +
                "  id INT AUTO_INCREMENT PRIMARY KEY," +
                "  name VARCHAR(100) NOT NULL," +
                "  latitude DOUBLE NOT NULL," +
                "  longitude DOUBLE NOT NULL," +
                "  loc_desc VARCHAR(255)" +
                ")";

            String createBooksTable = 
                "CREATE TABLE IF NOT EXISTS books (" +
                "  bookId INT AUTO_INCREMENT PRIMARY KEY," +
                "  name VARCHAR(100) NOT NULL," +
                "  BookedStatus VARCHAR(20) NOT NULL," +
                "  CurrentPersonal VARCHAR(50)," +
                "  Location VARCHAR(100)," +
                "  subLibraryID INT," +
                "  DateOfReturn DATE," +
                "  book_type VARCHAR(20) NOT NULL," +
                "  Author VARCHAR(100)," +
                "  DonatedBy VARCHAR(100)," +
                "  FOREIGN KEY (subLibraryID) REFERENCES sublibraries(id)" +
                ")";

            String createBorrowedBooksTable =
                "CREATE TABLE IF NOT EXISTS borrowedBooks (" +
                "  transactionNumber INT AUTO_INCREMENT PRIMARY KEY," +
                "  UserId VARCHAR(50) NOT NULL," +
                "  BookId INT NOT NULL," +
                "  DateOfBorrowal DATE NOT NULL," +
                "  ExpectedDateOfReturn DATE NOT NULL," +
                "  BookType ENUM('LibraryBook','SharedBook') NOT NULL," +
                "  FOREIGN KEY (UserId) REFERENCES persons(ID)," +
                "  FOREIGN KEY (BookId) REFERENCES books(bookId)" +
                ")";

            String createSharedBooksTable =
                "CREATE TABLE IF NOT EXISTS sharedBooks (" +
                "  SharedBookNumber INT AUTO_INCREMENT PRIMARY KEY," +
                "  BookId INT NOT NULL," +
                "  UserId VARCHAR(50)," +
                "  DateOfShared DATE NOT NULL," +
                "  DateOfDemand DATE NOT NULL," +
                "  FOREIGN KEY (BookId) REFERENCES books(bookId)," +
                "  FOREIGN KEY (UserId) REFERENCES persons(ID)" +
                ")";

            DatabaseConnection.executeUpdate(createPersonsTable);
            DatabaseConnection.executeUpdate(createSublibrariesTable);
            DatabaseConnection.executeUpdate(createBooksTable);
            DatabaseConnection.executeUpdate(createBorrowedBooksTable);
            DatabaseConnection.executeUpdate(createSharedBooksTable);

            System.out.println("Database initialized successfully");
        } catch(Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    
        
        public static void main(String[] args) {
            initializeDatabase();
            new AutoReturnService().processOverdues();
    
            MainGUI gui = new MainGUI();
            gui.setVisible(true);
        }
    
}
