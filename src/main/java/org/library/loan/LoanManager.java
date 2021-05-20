package org.library.loan;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.library.account.Customer;
import org.library.media.MediaItem;
import org.library.util.Database;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Manager for loan type.
 *
 * @see Loan
 */
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

    /**
     * Creates a new loan manager instance.
     *
     * @param database A database instance.
     */
    public LoanManager(Database database) {
        this.database = database;
    }

    /**
     * Creates a loan.
     *
     * @param customer  A customer instance (loan holder).
     * @param mediaItem Media item instance to loan.
     * @return A loan instance referencing the customer and loaned media item.
     * @throws Exception if the customer exceeds the maximum number of loans for their customer type, or if a general database error occurs.
     * @implNote This is a blocking operation.
     * @implNote Modifies media instance by decrementing number of available items field.
     */
    private Loan createLoan(Customer customer, MediaItem mediaItem) throws Exception {
        logger.info("Creating loan for media item... (customer: " + customer.getId() + ", media item: " + mediaItem.getId() + ")");
        LocalDate borrowedAt = LocalDate.now();
        LocalDate returnBy = LocalDate.now().plusDays(mediaItem.getMediaType().getLoanPeriod());
        Long loanId = database.insert(CREATE_LOAN_STATEMENT)
                .configure(customer.getId(), mediaItem.getId(), borrowedAt, returnBy)
                .executeQuery();

        // Update computed values
        mediaItem.getMedia().setNumberOfLoanableItems(mediaItem.getMedia().getNumberOfLoanableItems() - 1);
        return new Loan(loanId, customer.getId(), mediaItem.getId(), borrowedAt, returnBy, null);
    }

    /**
     * Creates loans for a list of media items.
     *
     * @param customer   A customer instance (loan holder).
     * @param mediaItems A list of media items to loan.
     * @return A list of loans registered to the specified customer.
     * @throws Exception if the customer exceeds the maximum number of loans for their customer type, or if a general database error occurs.
     * @implNote This is a blocking operation.
     * @implNote Modifies customer instance by incrementing the active loans field.
     */
    public List<Loan> createLoan(Customer customer, List<MediaItem> mediaItems) throws Exception {
        logger.info("Creating loan for media item(s)...");
        ArrayList<Loan> loans = new ArrayList<>();

        for (MediaItem mediaItem : mediaItems) {
            loans.add(this.createLoan(customer, mediaItem));
        }

        return loans;
    }

    /**
     * @param customerId Id of the customer to get number of active loans for.
     * @return Number of active loans for a customer.
     * @throws Exception if a general database error occurs.
     * @implNote This is a blocking operation.
     */
    public Long getNumberOfActiveCustomerLoans(Long customerId) throws Exception {
        logger.info("Getting number of active loans for customer " + customerId + "...");
        return database.select(SELECT_NUMBER_OF_ACTIVE_CUSTOMER_LOANS_STATEMENT, Long.class)
                .configure(customerId)
                .fetch(resultSet -> resultSet.getLong("active_loan_count"));
    }

    /**
     * @param customerId Id of the customer to get active loans for.
     * @return A list of active loans for a customer.
     * @throws Exception if a general database error occurs.
     * @implNote This is a blocking operation.
     */
    public List<Loan> getActiveCustomerLoans(Long customerId) throws Exception {
        logger.info("Getting active loans for customer " + customerId + "...");
        return database.select(SELECT_ACTIVE_CUSTOMER_LOANS_STATEMENT, Loan.class)
                .configure(customerId)
                .fetchAll(Loan::new);
    }

    /**
     * Updates a loan.
     *
     * @param loan Loan instance to update.
     * @throws Exception if a general database error occurs.
     * @implNote This is a blocking operation.
     */
    private void updateLoan(Loan loan) throws Exception {
        logger.info("Updating loan...");
        database.update(UPDATE_LOAN_STATEMENT)
                .configure(loan.getCustomerId(), loan.getMediaItemId(), loan.getBorrowedAt(), loan.getReturnBy(), loan.getId())
                .executeQuery();
    }

    /**
     * Marks the loan of the specified media item id as returned.
     *
     * @param mediaItemId Id of the media item to return.
     * @throws Exception if the specified media item is not on loan, or if a general database error occurs.
     * @implNote This is a blocking operation.
     * @implNote Assumes that the database ensures that a media item cannot be loaned more than once at a time.
     */
    public void returnMediaItem(Long mediaItemId) throws Exception {
        logger.info("Returning media item " + mediaItemId + "...");
        database.update(RETURN_MEDIA_ITEM_STATEMENT)
                .configure(LocalDate.now(), mediaItemId)
                .execute();
    }

    /**
     * @return A list of all overdue loans.
     * @throws Exception if a general database error occurs.
     * @implNote This is a blocking operation.
     */
    public List<Loan> getLateLoans() throws Exception {
        logger.info("Getting loans that are past their return dates...");
        return database.select(SELECT_LATE_LOANS_STATEMENT, Loan.class)
                .configure(LocalDate.now())
                .fetchAll(Loan::new);
    }

}
