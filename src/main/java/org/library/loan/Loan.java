package org.library.loan;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Loan representing a lease-type relationship between a media item and a customer.
 *
 * @see org.library.media.MediaItem
 * @see org.library.account.Customer
 */
public class Loan {
    private Long id;
    private Long customerId;
    private Long mediaItemId;
    private LocalDate borrowedAt;
    private LocalDate returnBy;
    private LocalDate returnedAt;

    private Metadata metadata;

    /**
     * Creates a new loan instance.
     */
    protected Loan(Long id, Long customerId, Long mediaItemId, LocalDate borrowedAt, LocalDate returnBy, LocalDate returnedAt) {
        this.id = id;
        this.customerId = customerId;
        this.mediaItemId = mediaItemId;
        this.borrowedAt = borrowedAt;
        this.returnBy = returnBy;
        this.returnedAt = returnedAt;
    }

    /**
     * Creates a new loan instance from a result set.
     *
     * @param resultSet A ResultSet instance.
     * @throws SQLException if the ResultSet instance methods throw an exception.
     */
    public Loan(ResultSet resultSet) throws SQLException {
        this(resultSet.getLong("id"), resultSet.getLong("customer_id"), resultSet.getLong("media_item_id"), resultSet.getObject("borrowed_at", LocalDate.class), resultSet.getObject("return_by", LocalDate.class), resultSet.getObject("returned_at", LocalDate.class));

        this.setMetadata(new Metadata(resultSet));
    }

    public Long getId() {
        return id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Long getMediaItemId() {
        return mediaItemId;
    }

    public LocalDate getBorrowedAt() {
        return borrowedAt;
    }

    public LocalDate getReturnBy() {
        return returnBy;
    }

    public LocalDate getReturnedAt() {
        return returnedAt;
    }

    public boolean hasMetadata() {
        return this.metadata != null;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    private void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    /**
     * Metadata content for loan.
     */
    class Metadata {
        private String mediaTitle;

        /**
         * Creates a new metadata instance.
         */
        private Metadata(String mediaTitle) {
            this.mediaTitle = mediaTitle;
        }

        /**
         * Creates a new metadata instance from a result set.
         *
         * @param resultSet A ResultSet instance.
         * @throws SQLException if the ResultSet instance methods throw an exception.
         */
        protected Metadata(ResultSet resultSet) throws SQLException {
            this(resultSet.getString("title"));
        }

        public String getMediaTitle() {
            return mediaTitle;
        }
    }
}
