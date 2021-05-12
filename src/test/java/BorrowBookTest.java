import org.junit.jupiter.api.Test;
import org.library.*;

import java.util.List;

// NOTE: This test is just meant for testing application logic.
public class BorrowBookTest {
    private final Database database = new Database("jdbc:mysql://ec2-23-20-145-129.compute-1.amazonaws.com:3306/library", "admin", "cbq6LQzci9c");

    @Test
    void testBorrowBook() throws Exception {
        MediaManager mediaManager = new MediaManager(database);
        AccountManager accountManager = new AccountManager(database);
        LoanManager loanManager = new LoanManager(database);

        try {
            // Account
            System.out.println("Create account");
            accountManager.createCustomerAccount(new Customer(new Account(null, "Example", "Customer", "example@example.local", ""), null), "123456");

            System.out.println("Authenticating");
            Customer customer = (Customer) accountManager.authenticate("example@example.local", "123456");

            customer.setPhoneNumber("123456789");
            System.out.println("Updating phone number");
            accountManager.updateAccount(customer);

            // Loans
            System.out.println("Getting active customer loans");
            List<Loan> customerLoans = loanManager.getActiveCustomerLoans(customer.getId());

            // Media
            System.out.println("Searching for book using query ”Praktisk”");
            List<Book> matchedBooks = mediaManager.searchBook("Praktisk").get();

            // Media item
            System.out.println("Getting media items for book");
            List<MediaItem> mediaItems = mediaManager.getAvailableMediaItems(matchedBooks.get(0));

            // Borrow book
            System.out.println("Creating a new loan with the first book media item");
            MediaItem mediaItem = mediaItems.get(0);
            List<Loan> newCustomerLoans = loanManager.createLoan(customer.getId(), List.of(mediaItem));

            // Return book
            System.out.println("Returning loaned media item");
            loanManager.returnMediaItem(mediaItem.getId());

            // Check active customer loans
            System.out.println("Getting active customer loans");
            customerLoans = loanManager.getActiveCustomerLoans(customer.getId());
        } finally {
            accountManager.deleteAccountByEmail("example@example.local");
        }
    }
}
