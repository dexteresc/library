package org.library.loan;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.library.media.MediaItem;
import org.library.util.Database;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanManager {
    private static final Logger logger = LogManager.getLogger();

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
        logger.info("Creating loan for media item... (customer: " + customerId + ", media item: " + mediaItem.getId() + ")");
        LocalDate borrowedAt = LocalDate.now();
        LocalDate returnBy = LocalDate.now().plusDays(mediaItem.getMediaType().getLoanPeriod());
        Long loanId = database.insert(CREATE_LOAN_STATEMENT)
                .configure(customerId, mediaItem.getId(), borrowedAt, returnBy)
                .executeQuery();
        return new Loan(loanId, customerId, mediaItem.getId(), borrowedAt, returnBy, null);
    }

    public List<Loan> createLoan(Long customerId, List<MediaItem> mediaItems) throws Exception {
        logger.info("Creating loan for media item(s)...");
        ArrayList<Loan> loans = new ArrayList<>();

        for (MediaItem mediaItem : mediaItems) {
            loans.add(this.createLoan(customerId, mediaItem));
        }

        return loans;
    }

    public Long getNumberOfActiveCustomerLoans(Long customerId) throws Exception {
        logger.info("Getting number of active loans for customer " + customerId + "...");
        return database.select(SELECT_NUMBER_OF_ACTIVE_CUSTOMER_LOANS_STATEMENT, Long.class)
                .configure(customerId)
                .fetch(resultSet -> resultSet.getLong("active_loan_count"));
    }

    public List<Loan> getActiveCustomerLoans(Long customerId) throws Exception {
        logger.info("Getting active loans for customer " + customerId + "...");
        return database.select(SELECT_ACTIVE_CUSTOMER_LOANS_STATEMENT, Loan.class)
                .configure(customerId)
                .fetchAll(Loan::new);
    }

    private Long updateLoan(Loan loan) throws Exception {
        logger.info("Updating loan...");
        return database.update(UPDATE_LOAN_STATEMENT)
                .configure(loan.getCustomerId(), loan.getMediaItemId(), loan.getBorrowedAt(), loan.getReturnBy(), loan.getId())
                .executeQuery();
    }

    public void returnMediaItem(Long mediaItemId) throws Exception {
        logger.info("Returning media item " + mediaItemId + "...");
        database.update(RETURN_MEDIA_ITEM_STATEMENT)
                .configure(LocalDate.now(), mediaItemId)
                .execute();
    }

    public List<Loan> getLateLoans() throws Exception {
        logger.info("Getting loans that are past their return dates...");
        return database.select(SELECT_LATE_LOANS_STATEMENT, Loan.class)
                .configure(LocalDate.now())
                .fetchAll(Loan::new);
    }

}
