import java.util.ArrayList;
import java.util.List;

public class User extends PERSON {
    private List<BOOK> BorrowedBooks;
    private List<BOOK> DonatedBooks;
    
    public User() {
        BorrowedBooks = new ArrayList<>();
        DonatedBooks = new ArrayList<>();
    }
    
    public User(String name, long phoneNumber, String email, String ID, String password) {
        super(name, phoneNumber, email, ID, password);
        BorrowedBooks = new ArrayList<>();
        DonatedBooks = new ArrayList<>();
    }

    public boolean donateBook(BOOK book) {
        book.setDonatedby(this.getName());
        book.UpdateStatus("available");
        DonatedBooks.add(book);
        return true; 
    }

    public boolean shareBook(BOOK book) {
        if(!this.getName().equals(book.getDonatedby())) {
            return false;
        }
        book.UpdateStatus("available");
        if(!DonatedBooks.contains(book)) {
            DonatedBooks.add(book);
        }
        return true;
    }

    public boolean demandBookBack(BOOK book) {
        if(!this.getName().equals(book.getDonatedby())) {
            return false;
        }
        String st = book.getBookedStatus().toLowerCase();
        if(st.equals("borrowed")) {
            book.UpdateStatus("demanded");
            return true;
        } else if(st.equals("available")) {
            book.UpdateStatus("withOwner");
            book.setCurrentPersonal(this.getName());
            return true;
        }
        return false;
    }

    public boolean borrowBook(BOOK book) {
        if(book.getBookedStatus().equalsIgnoreCase("available")) {
            BorrowedBooks.add(book);
            book.UpdateStatus("borrowed");
            book.setCurrentPersonal(this.getID());
            return true;
        }
        return false;
    }

    public boolean returnBook(BOOK book) {
        if(BorrowedBooks.contains(book)) {
            BorrowedBooks.remove(book);
            if("demanded".equalsIgnoreCase(book.getBookedStatus())) {
                book.UpdateStatus("withOwner");
                book.setCurrentPersonal(book.getDonatedby());
            } else {
                book.UpdateStatus("available");
                book.setCurrentPersonal(null);
            }
            return true;
        }
        return false;
    }

    public List<BOOK> getBorrowedBooks() {
        return BorrowedBooks;
    }
    public void setBorrowedBooks(List<BOOK> borrowedBooks) {
        this.BorrowedBooks = borrowedBooks;
    }
    public List<BOOK> getDonatedBooks() {
        return DonatedBooks;
    }
    public void setDonatedBooks(List<BOOK> donatedBooks) {
        this.DonatedBooks = donatedBooks;
    }

    public void setPassword(String newPass, String userId) {
        UserRepository repo = new UserRepository();
        repo.updatePassword(newPass, userId);
    }
}
