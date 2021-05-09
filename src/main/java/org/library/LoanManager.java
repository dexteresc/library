package org.library;

import java.time.LocalDate;
import java.util.List;

public class LoanManager {

    // Statements
    private static final String CREATE_LOAN_STATEMENT = "INSERT INTO loan (customer_id, media_item_id, borrowed_at, return_by) VALUES (?, ?, ?, ?)";

    private final Database database;

    public LoanManager(Database database) {
        this.database = database;
    }

    private Long createLoan(Customer customer, MediaItem mediaItem) throws Exception {
        LocalDate borrowedAt = LocalDate.now();
        LocalDate returnBy = LocalDate.now().plusDays(mediaItem.getMediaType().getLoanPeriod());
        return database.insert(CREATE_LOAN_STATEMENT)
                .configure(customer.getId(), mediaItem.getId(), borrowedAt, returnBy)
                .executeQuery();
    }

    public void createLoan(Customer customer, List<MediaItem> mediaItems) throws Exception {
        for (MediaItem mediaItem : mediaItems) {
            this.createLoan(customer, mediaItem);
        }
    }

}
