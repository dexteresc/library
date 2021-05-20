package org.library.media;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.library.util.Database;

/**
 * Manager for book media instances.
 *
 * @see Book
 * @see MediaManager
 */
public class BookManager extends MediaManager {
    private static final Logger logger = LogManager.getLogger();

    // Book statements
    private static final String SELECT_BOOK_BY_ID_STATEMENT = "SELECT *, (SELECT COUNT(*) FROM media_author WHERE media_id = media.id) AS author_count, (SELECT COUNT(*) FROM media_item INNER JOIN media_type ON media_item.media_type_id = media_type.id WHERE media_id = media.id AND status IS NULL AND media_type.loan_period > 0 AND media_item.id NOT IN (SELECT media_item_id FROM loan WHERE media_item_id = media_item.id)) AS loanable_item_count FROM media INNER JOIN book ON book.media_id = media.id INNER JOIN media_author ON media_author.media_id = media.id INNER JOIN author ON author.id = media_author.author_id WHERE media.id = ?";
    private static final String CREATE_BOOK_STATEMENT = "INSERT INTO book (media_id, isbn, publisher) VALUES (?, ?, ?)";
    private static final String UPDATE_BOOK_STATEMENT = "UPDATE book SET isbn = ?, publisher = ? WHERE media_id = ? LIMIT 1";

    // Author statements
    private static final String CREATE_AUTHOR_STATEMENT = "INSERT INTO author (given_name, family_name) VALUES (?, ?)";
    private static final String UPDATE_AUTHOR_STATEMENT = "UPDATE author SET given_name = ?, family_name = ? WHERE id = ?";
    private static final String DELETE_AUTHOR_STATEMENT = "DELETE FROM author WHERE id = ?";
    private static final String SELECT_AUTHOR_BY_ID_STATEMENT = "SELECT * FROM author WHERE id = ?";
    private static final String SELECT_FIRST_AUTHOR_BY_NAME_STATEMENT = "SELECT * FROM author WHERE given_name = ? AND family_name = ? LIMIT 1";

    /**
     * Creates a new book manager instance.
     *
     * @param database A database instance.
     */
    public BookManager(Database database) {
        super(database);
    }

    public Book getBookById(Long id) throws Exception {
        logger.info("Getting book by id...");
        return database.select(SELECT_BOOK_BY_ID_STATEMENT, Book.class)
                .configure(id)
                .fetch(Book::new);
    }

    /**
     * Creates a book.
     *
     * @param book Book instance to create.
     * @throws Exception if the book already exists, or if a general database error occurs.
     * @implNote This is a blocking operation.
     */
    public void createBook(Book book) throws Exception {
        logger.info("Creating book...");
        this.createMedia(book);
        database.insert(CREATE_BOOK_STATEMENT)
                .configure(book.getId(), book.getIsbn(), book.getPublisher())
                .execute();
    }

    /**
     * Updates an existing book.
     *
     * @param book Book instance to update.
     * @throws Exception if the book cannot be found, or if a general database error occurs.
     * @implNote This is a blocking operation.
     */
    public void updateBook(Book book) throws Exception {
        logger.info("Updating book...");
        this.updateMedia(book);
        database.update(UPDATE_BOOK_STATEMENT)
                .configure(book.getIsbn(), book.getPublisher(), book.getId())
                .execute();
    }

    /**
     * Deletes an existing book.
     *
     * @param book Book instance to delete.
     * @throws Exception if the book cannot be found, or if a general database error occurs.
     * @implNote This is a blocking operation.
     */
    public void deleteBook(Book book) throws Exception {
        logger.info("Deleting book...");
        this.deleteMedia(book);
    }

    // Author

    public Author getAuthor(Author author) throws Exception {
        return database.select(SELECT_FIRST_AUTHOR_BY_NAME_STATEMENT, Author.class)
                .configure(author.getGivenName(), author.getFamilyName())
                .fetch(Author::new);
    }

    public void createAuthor(Author author) throws Exception {
        Long authorId = database.insert(CREATE_AUTHOR_STATEMENT)
                .configure(author.getGivenName(), author.getFamilyName())
                .executeQuery();
        author.setId(authorId);
    }

    public Author getOrCreateAuthor(Author author) throws Exception {
        try {
            return this.getAuthor(author);
        } catch (Exception exception) {
            this.createAuthor(author);
            return author;
        }
    }

    // TODO: Author book relationships

    private void removeAuthorFromBook(Author author, Book book) {

    }
}
