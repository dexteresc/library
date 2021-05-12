package org.library;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanManager {

    // Statements
    private static final String CREATE_LOAN_STATEMENT = "INSERT INTO loan (customer_id, media_item_id, borrowed_at, return_by) VALUES (?, ?, ?, ?)";
    private static final String SELECT_NUMBER_OF_ACTIVE_CUSTOMER_LOANS_STATEMENT = "SELECT COUNT(*) AS active_loan_count FROM loan WHERE customer_id = ? AND returned_at IS NULL";
    private static final String SELECT_ACTIVE_CUSTOMER_LOANS_STATEMENT = "SELECT * FROM loan WHERE customer_id = ? AND returned_at IS NULL";
    private static final String UPDATE_LOAN_STATEMENT = "UPDATE loan SET customer_id = ?, media_item_id = ?, borrowed_at = ?, return_by = ? WHERE id = ? LIMIT 1";
    private static final String RETURN_MEDIA_ITEM_STATEMENT = "UPDATE loan SET returned_at = ? WHERE media_item_id = ? AND returned_at IS NULL LIMIT 1";
    private static final String SELECT_LATE_LOANS_STATEMENT = "SELECT * FROM loan WHERE returned_at IS NULL AND return_by < ?";


    private final Database database;

    public LoanManager(Database database) {
        this.database = database;
    }

    private Loan createLoan(Long customerId, MediaItem mediaItem) throws Exception {
        LocalDate borrowedAt = LocalDate.now();
        LocalDate returnBy = LocalDate.now().plusDays(mediaItem.getMediaType().getLoanPeriod());
        Long loanId = database.insert(CREATE_LOAN_STATEMENT)
                .configure(customerId, mediaItem.getId(), borrowedAt, returnBy)
                .executeQuery();
        return new Loan(loanId, customerId, mediaItem.getId(), borrowedAt, returnBy, null);
    }

    public List<Loan> createLoan(Long customerId, List<MediaItem> mediaItems) throws Exception {
        ArrayList<Loan> loans = new ArrayList<>();
        for (MediaItem mediaItem : mediaItems) {
            loans.add(this.createLoan(customerId, mediaItem));
        }
        return loans;
    }

    public Long getNumberOfActiveCustomerLoans(Long customerId) throws Exception {
        return database.select(SELECT_NUMBER_OF_ACTIVE_CUSTOMER_LOANS_STATEMENT, Long.class)
                .configure(customerId)
                .fetch(resultSet -> resultSet.getLong("active_loan_count"));
    }

    public List<Loan> getActiveCustomerLoans(Long customerId) throws Exception {
        return database.select(SELECT_ACTIVE_CUSTOMER_LOANS_STATEMENT, Loan.class)
                .configure(customerId)
                .fetchAll(Loan::new);
    }

    private Long updateLoan(Loan loan) throws Exception {
        return database.update(UPDATE_LOAN_STATEMENT)
                .configure(loan.getCustomerId(), loan.getMediaItemId(), loan.getBorrowedAt(), loan.getReturnBy(), loan.getId())
                .executeQuery();
    }

    public void returnMediaItem(Long mediaItemId) throws Exception {
        database.update(RETURN_MEDIA_ITEM_STATEMENT)
                .configure(LocalDate.now(), mediaItemId)
                .execute();
    }

    public List<Loan> getLateLoans() throws Exception {
        return database.select(SELECT_LATE_LOANS_STATEMENT, Loan.class)
                .configure(LocalDate.now())
                .fetchAll(Loan::new);
    }

}
