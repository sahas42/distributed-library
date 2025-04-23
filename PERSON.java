import java.util.ArrayList;
import java.util.List;

public class PERSON {
    private String name;
    private long phoneNumber;
    private String email;
    private String ID;
    private String password;
    
    public PERSON() {}

    public PERSON(String name, long phoneNumber, String email, String ID, String password) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.ID = ID;
        this.password = password;
    }

    public List<BOOK> searchBook(String keyword) {
        return new ArrayList<>();
    }

    public String viewAccountDetails() {
        return "User ID: " + ID + "\n"
             + "Name: " + name + "\n"
             + "Email: " + email + "\n"
             + "Phone: " + phoneNumber + "\n";
    }

    public boolean changePassword(String oldPassword, String newPassword) {
        if (!this.password.equals(oldPassword)) {
            return false;
        }
        this.password = newPassword;
        return true;
    }

    public boolean validateCredentials(String id, String password) {
        return this.ID.equals(id) && this.password.equals(password);
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public long getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(long phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getID() { return ID; }
    public void setID(String iD) { ID = iD; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
