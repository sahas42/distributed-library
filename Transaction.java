import java.time.LocalDate;
import java.time.LocalDateTime;

public class Transaction {
    private int transactionId;
    private String userId;
    private int bookId;
    private LocalDateTime timestamp;
    private TransactionType type;
    private LocalDate dueDate;

    public Transaction() {
        this.timestamp = LocalDateTime.now();
    }

    public Transaction(int transactionId, String userId, int bookId, TransactionType type, LocalDate dueDate) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.bookId = bookId;
        this.timestamp = LocalDateTime.now();
        this.type = type;
        this.dueDate = dueDate;
    }

    public boolean recordTransaction() {
        String query = 
            "INSERT INTO transactions (transactionId, userId, bookId, timestamp, type, dueDate) " +
            "VALUES (" + transactionId + ", '" + userId + "', " + bookId + ", '" + 
            timestamp + "', '" + type + "', " + 
            (dueDate != null ? "'" + dueDate + "'" : "NULL") + ")";
        int result = DatabaseConnection.executeUpdate(query);
        return result > 0;
    }

    public String getTransactionDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append("Transaction ID: ").append(transactionId).append("\n");
        sb.append("User ID: ").append(userId).append("\n");
        sb.append("Book ID: ").append(bookId).append("\n");
        sb.append("Timestamp: ").append(timestamp).append("\n");
        sb.append("Type: ").append(type).append("\n");
        if (dueDate != null) {
            sb.append("Due Date: ").append(dueDate).append("\n");
        }
        return sb.toString();
    }

    // getters, setters ...
}

enum TransactionType {
    BORROW,
    RETURN,
    DONATE,
    SHARE,
    DEMAND_BACK
}
