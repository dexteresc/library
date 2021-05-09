package org.library;

import java.time.LocalDate;
import java.util.List;

public class LoanManager {

    // Statements
    private static final String CREATE_LOAN_STATEMENT = "INSERT INTO loan (customer_id, media_item_id, borrowed_at, return_by) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ACTIVE_CUSTOMER_LOANS_STATEMENT = "SELECT * FROM loan WHERE customer_id = ? AND returned_at IS NULL";


    private final Database database;

    public LoanManager(Database database) {
        this.database = database;
    }

    private Long createLoan(Long customerId, MediaItem mediaItem) throws Exception {
        LocalDate borrowedAt = LocalDate.now();
        LocalDate returnBy = LocalDate.now().plusDays(mediaItem.getMediaType().getLoanPeriod());
        return database.insert(CREATE_LOAN_STATEMENT)
                .configure(customerId, mediaItem.getId(), borrowedAt, returnBy)
                .executeQuery();
    }

    public void createLoan(Long customerId, List<MediaItem> mediaItems) throws Exception {
        for (MediaItem mediaItem : mediaItems) {
            this.createLoan(customerId, mediaItem);
        }
    }

    public List<Loan> getActiveCustomerLoans(Long customerId) throws Exception {
        return database.select(SELECT_ACTIVE_CUSTOMER_LOANS_STATEMENT, Loan.class)
                .configure(customerId)
                .fetchAll(Loan::new);
    }
}
