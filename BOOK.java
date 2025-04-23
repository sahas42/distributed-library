import java.time.LocalDate;
import java.util.ArrayList;

public class BOOK {
    private int bookId;
    private String name;
    private String BookedStatus;
    private String CurrentPersonal;
    private String Location;
    private int subLibraryID;
    private LocalDate DateOfReturn;
    private String book_type;
    private String Author;
    private String Donatedby;

    static ArrayList<Sublibrary> sublibraries = new ArrayList<>();

    public BOOK() {}

    public BOOK(int bookId, String name, String BookedStatus, String CurrentPersonal,
                int subLibraryID, LocalDate DateOfReturn, String book_type,
                String Author, String Donatedby) {
        this.bookId = bookId;
        this.name = name;
        this.BookedStatus = BookedStatus;
        this.CurrentPersonal = CurrentPersonal;
        this.book_type = book_type;
        this.subLibraryID = subLibraryID;
        this.DateOfReturn = DateOfReturn;
        this.Author = Author;
        this.Donatedby = Donatedby;
    }
    
    public String CheckStatus() {
        return this.BookedStatus;
    }
    public void UpdateStatus(String newStatus) {
        this.BookedStatus = newStatus;
    }
    public String CheckLocations() {
        return this.Location;
    }
    public String getBookDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append("Book ID: ").append(bookId).append("\n")
          .append("Name: ").append(name).append("\n")
          .append("Status: ").append(BookedStatus).append("\n")
          .append("Current Personal: ").append(CurrentPersonal).append("\n")
          .append("Location: ").append(Location).append("\n")
          .append("Sublibrary ID: ").append(subLibraryID).append("\n");
        if (DateOfReturn != null) {
            sb.append("Due Date: ").append(DateOfReturn).append("\n");
        }
        sb.append("Owner: ").append(Donatedby).append("\n");
        return sb.toString();
    }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBookedStatus() { return BookedStatus; }
    public void setBookedStatus(String BookedStatus) { this.BookedStatus = BookedStatus; }

    public String getCurrentPersonal() { return CurrentPersonal; }
    public void setCurrentPersonal(String currentPersonal) { this.CurrentPersonal = currentPersonal; }

    public int getSubLibraryID() { return subLibraryID; }
    public void setSubLibraryID(int subLibraryID) { this.subLibraryID = subLibraryID; }

    public LocalDate getDateOfReturn() { return DateOfReturn; }
    public void setDateOfReturn(LocalDate dateOfReturn) { this.DateOfReturn = dateOfReturn; }

    public String getbook_type() { return book_type; }
    public void setbook_type(String book_type) { this.book_type = book_type; }

    public String getLocation() { return Location; }
    public void setLocation(String location) { this.Location = location; }

    public String getAuthor() { return Author; }
    public void setAuthor(String Author) { this.Author = Author; }

    public String getDonatedby() { return Donatedby; }
    public void setDonatedby(String Donatedby) { this.Donatedby = Donatedby; }
}
