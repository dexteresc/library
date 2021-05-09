package org.library;

import java.time.LocalDate;
import java.util.List;

public class MediaManager {

    // Statements
    private static final String SELECT_MEDIA_STATEMENT = "SELECT * FROM media WHERE id = ? LIMIT 1";
    private static final String UPDATE_MEDIA_STATEMENT = "UPDATE media SET title = ?, classification = ?, summary = ?, location = ?, publishing_date = ? WHERE id = ? LIMIT 1";
    private static final String CREATE_MEDIA_STATEMENT = "INSERT INTO media (title, classification, summary, location, publishing_date) VALUES (?, ?, ?, ?, ?)";

    // Book statements
    private static final String SELECT_BOOK_BY_ID_STATEMENT = "SELECT * FROM media INNER JOIN book ON book.media_id = media.id WHERE id = ? LIMIT 1";
    private static final String CREATE_BOOK_STATEMENT = "INSERT INTO book (media_id, isbn, publisher) VALUES (?, ?, ?)";
    private static final String UPDATE_BOOK_STATEMENT = "UPDATE book SET isbn = ?, publisher = ? WHERE id = ? LIMIT 1";
    private static final String SEARCH_BOOK_STATEMENT = "SELECT * FROM media INNER JOIN book ON book.media_id = media.id WHERE media.title LIKE ? LIMIT 10";

    // Media item statements
    private static final String SELECT_MEDIA_ITEMS_BY_MEDIA_ID_STATEMENT = "SELECT * FROM media_item INNER JOIN media_type ON media_item.media_type_id = media_type.id WHERE media_id = ?";
    private static final String CREATE_MEDIA_ITEM_STATEMENT = "INSERT INTO media_item (media_id, media_type_id) VALUES (?, ?)";

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

    public List<MediaItem> getMediaItems(Media media) throws Exception {
        return database.select(SELECT_MEDIA_ITEMS_BY_MEDIA_ID_STATEMENT, MediaItem.class)
                .configure(media.getId())
                .fetchAll(resultSet -> new MediaItem(resultSet.getLong("id"), media, new MediaType(resultSet)));
    }

    public Long createMediaItem(Media media, MediaType mediaType) throws Exception {
        return database.insert(CREATE_MEDIA_ITEM_STATEMENT)
                .configure(media.getId(), mediaType.getId())
                .executeQuery();
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

    public List<Book> searchBook(String query) throws Exception {
        return database.select(SEARCH_BOOK_STATEMENT, Book.class)
                .configure("%" + query + "%")
                .fetchAll(Book::new);
    }

}
