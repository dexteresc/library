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
            accountManager.createCustomerAccount(new Customer(new Account(null, "Example", "Customer", "example@example.local", ""), null), "123456");

            Customer customer = (Customer) accountManager.authenticate("example@example.local", "123456");
            System.out.println(customer);
            customer.setPhoneNumber("123456789");
            accountManager.updateAccount(customer);
            System.out.println(customer);

            // Loans
            List<Loan> customerLoans = loanManager.getActiveCustomerLoans(customer.getId());
            System.out.println(customerLoans);

            // Media
            List<Book> matchedBooks = mediaManager.searchBook("Testning");
            System.out.println(matchedBooks);

            // Media item
            List<MediaItem> mediaItems = mediaManager.getMediaItems(matchedBooks.get(0));
            System.out.println(mediaItems);

            // Borrow book
            MediaItem mediaItem = mediaItems.get(0);
            List<Loan> newCustomerLoans = loanManager.createLoan(customer.getId(), List.of(mediaItem));
            System.out.println(newCustomerLoans);

            // Return book
            loanManager.returnMediaItem(mediaItem.getId());

            // Check active customer loans
            customerLoans = loanManager.getActiveCustomerLoans(customer.getId());
            System.out.println(customerLoans);
        } finally {
            accountManager.deleteAccountByEmail("example@example.local");
        }
    }
}
