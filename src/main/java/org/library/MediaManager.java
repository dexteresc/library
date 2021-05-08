package org.library;

import java.time.LocalDate;

public class MediaManager {

    // Statements
    private static final String SELECT_MEDIA_STATEMENT = "SELECT * FROM media WHERE id = ? LIMIT 1";
    private static final String UPDATE_MEDIA_STATEMENT = "UPDATE media SET title = ?, classification = ?, summary = ?, location = ?, publishing_date = ? WHERE id = ? LIMIT 1";
    private static final String CREATE_MEDIA_STATEMENT = "INSERT INTO media (title, classification, summary, location, publishing_date) VALUES (?, ?, ?, ?, ?)";

    // Book statements
    private static final String SELECT_BOOK_BY_ID_STATEMENT = "SELECT * FROM media INNER JOIN book ON book.media_id = media.id WHERE id = ? LIMIT 1";
    private static final String CREATE_BOOK_STATEMENT = "INSERT INTO book (media_id, isbn, publisher) VALUES (?, ?, ?)";
    private static final String UPDATE_BOOK_STATEMENT = "UPDATE book SET isbn = ?, publisher = ? WHERE id = ?";

    private final Database database;

    public MediaManager(Database database) {
        this.database = database;
    }

    private Long createMedia(String title, String classification, String summary, String location, LocalDate publishingDate) throws Exception {
        return database.insert(CREATE_MEDIA_STATEMENT)
                .configure(title, classification, summary, location, publishingDate)
                .executeQuery();
    }

    private void updateMedia(Media media) throws Exception {
        database.update(UPDATE_MEDIA_STATEMENT)
                .configure(media.getTitle(), media.getClassification(), media.getSummary(), media.getLocation(), media.getPublishingDate(), media.getId())
                .execute();
    }

    public Book getBookById(Long id) throws Exception {
        return database.select(SELECT_BOOK_BY_ID_STATEMENT, Book.class)
                .configure(id)
                .fetch(Book::new);
    }

    public void createBook(String title, String classification, String summary, String location, LocalDate publishingDate, String isbn, String publisher) throws Exception {
        Long id = this.createMedia(title, classification, summary, location, publishingDate);
        database.insert(CREATE_BOOK_STATEMENT)
                .configure(id, isbn, publisher)
                .execute();
    }

    // TODO: This is inefficient. We should batch-process the book update alongside the media update.
    public void updateBook(Book book) throws Exception {
        this.updateMedia(book);
        database.update(UPDATE_BOOK_STATEMENT)
                .configure(book.getIsbn(), book.getPublisher(), book.getId())
                .execute();
    }
}
