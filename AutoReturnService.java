import java.time.LocalDate;
import java.util.List;

public class AutoReturnService {
    private final BookRepository bookRepo = new BookRepository();
    private final BorrowedBookRepository borrowRepo = new BorrowedBookRepository();

    public void processOverdues() {
        LocalDate limit = LocalDate.now().minusDays(14);
        List<Integer> overdueBookIds = borrowRepo.findOverdueBookIds(limit);

        for (Integer bookId : overdueBookIds) {
            BOOK book = bookRepo.findById(bookId);
            if (book == null) continue;

            borrowRepo.removeTransactionByBook(bookId);

            if ("demanded".equalsIgnoreCase(book.getBookedStatus())) {
                bookRepo.delete(bookId);
            } else {
                book.UpdateStatus("available");
                book.setCurrentPersonal(null);
                bookRepo.update(book);
            }
        }
    }
}
