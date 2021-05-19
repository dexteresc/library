package org.library.media;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.library.util.Database;

public class BookManager extends MediaManager {
    private static final Logger logger = LogManager.getLogger();

    // Book statements
    private static final String SELECT_BOOK_BY_ID_STATEMENT = "SELECT *, (SELECT COUNT(*) FROM media_author WHERE media_id = media.id) AS author_count, (SELECT COUNT(*) FROM media_item INNER JOIN media_type ON media_item.media_type_id = media_type.id WHERE media_id = media.id AND status IS NULL AND media_type.loan_period > 0) AS loanable_item_count FROM media INNER JOIN book ON book.media_id = media.id INNER JOIN media_author ON media_author.media_id = media.id INNER JOIN author ON author.id = media_author.author_id WHERE id = ?";
    private static final String CREATE_BOOK_STATEMENT = "INSERT INTO book (media_id, isbn, publisher) VALUES (?, ?, ?)";
    private static final String UPDATE_BOOK_STATEMENT = "UPDATE book SET isbn = ?, publisher = ? WHERE media_id = ? LIMIT 1";

    // Author statements
    private static final String CREATE_AUTHOR_STATEMENT = "INSERT INTO author (given_name, family_name) VALUES (?, ?)";
    private static final String UPDATE_AUTHOR_STATEMENT = "UPDATE author SET given_name = ?, family_name = ? WHERE id = ?";
    private static final String DELETE_AUTHOR_STATEMENT = "DELETE FROM author WHERE id = ?";
    private static final String SELECT_AUTHOR_BY_ID_STATEMENT = "SELECT * FROM author WHERE id = ?";
    private static final String SEARCH_AUTHOR_STATEMENT = "SELECT * FROM author WHERE MATCH(given_name, family_name) AGAINST(? IN NATURAL LANGUAGE MODE)";

    public BookManager(Database database) {
        super(database);
    }

    public Book getBookById(Long id) throws Exception {
        logger.info("Getting book by id...");
        return database.select(SELECT_BOOK_BY_ID_STATEMENT, Book.class)
                .configure(id)
                .fetch(Book::new);
    }

    public void createBook(Book book) throws Exception {
        logger.info("Creating book...");
        this.createMedia(book);
        database.insert(CREATE_BOOK_STATEMENT)
                .configure(book.getId(), book.getIsbn(), book.getPublisher())
                .execute();
    }

    public void updateBook(Book book) throws Exception {
        logger.info("Updating book...");
        this.updateMedia(book);
        database.update(UPDATE_BOOK_STATEMENT)
                .configure(book.getIsbn(), book.getPublisher(), book.getId())
                .execute();
    }
}
