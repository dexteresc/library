package org.library.media;

import java.util.List;
import java.util.stream.Collectors;
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
    private static final String SELECT_BOOK_BY_ID_STATEMENT =
            "SELECT *, (SELECT COUNT(*) FROM media_author WHERE media_id = media.id) AS author_count, (SELECT COUNT(*) FROM media_item INNER JOIN media_type ON media_item.media_type_id = media_type.id WHERE media_id = media.id AND status IS NULL AND media_type.loan_period > 0 AND media_item.id NOT IN (SELECT media_item_id FROM loan WHERE media_item_id = media_item.id AND loan.returned_at IS NULL)) AS loanable_item_count FROM media INNER JOIN book ON book.media_id = media.id INNER JOIN media_author ON media_author.media_id = media.id INNER JOIN author ON author.id = media_author.author_id WHERE media.id = ?";
    private static final String CREATE_BOOK_STATEMENT =
            "INSERT INTO book (media_id, isbn, publisher) VALUES (?, ?, ?)";
    private static final String UPDATE_BOOK_STATEMENT =
            "UPDATE book SET isbn = ?, publisher = ? WHERE media_id = ? LIMIT 1";

    // Author statements
    private static final String CREATE_AUTHOR_STATEMENT =
            "INSERT INTO author (given_name, family_name) VALUES (?, ?)";
    private static final String UPDATE_AUTHOR_STATEMENT =
            "UPDATE author SET given_name = ?, family_name = ? WHERE id = ?";
    private static final String DELETE_AUTHOR_STATEMENT = "DELETE FROM author WHERE id = ?";
    private static final String SELECT_FIRST_AUTHOR_BY_NAME_STATEMENT =
            "SELECT * FROM author WHERE given_name = ? AND family_name = ? LIMIT 1";

    // Author media relationship statements
    private static final String CREATE_AUTHOR_MEDIA_STATEMENT =
            "INSERT INTO media_author (author_id, media_id) VALUES (?, ?)";
    private static final String SELECT_ALL_AUTHORS_FOR_MEDIA_STATEMENT =
            "SELECT * FROM media_author INNER JOIN author ON author.id = media_author.author_id WHERE media_id = ?";
    private static final String DELETE_AUTHOR_MEDIA_STATEMENT =
            "DELETE FROM media_author WHERE author_id = ? AND media_id = ?";

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

        // Get or create authors
        for (Author author : book.getAuthors()) {
            author.setId(this.getOrCreateAuthor(author).getId());
        }

        // Create author book relationships
        for (Author author : book.getAuthors()) {
            this.addAuthorToBook(author, book);
        }
    }

    /**
     * Updates an existing book.
     *
     * @param book Book instance to update.
     * @throws Exception if the book cannot be found, or if a general database error occurs.
     * @implNote This is a blocking operation.
     */
    public void updateBook(Book book) throws Exception {
        logger.info("Updating book with id " + book.getId() + "...");
        this.updateMedia(book);
        database.update(UPDATE_BOOK_STATEMENT)
                .configure(book.getIsbn(), book.getPublisher(), book.getId())
                .execute();

        // Update book authors
        this.updateAuthorsForBook(book);
    }

    private void updateAuthorsForBook(Book book) throws Exception {
        // Get authors for book before update
        List<Author> authorsBeforeUpdate = this.getAllAuthorsForMedia(book);

        // Get or create authors without ids
        for (Author author :
                book.getAuthors().stream()
                        .filter(author -> author.getId() == null)
                        .collect(Collectors.toList())) {
            author.setId(this.getOrCreateAuthor(author).getId());
        }

        // Filter authors to add
        List<Author> authorsToAdd =
                book.getAuthors().stream()
                        .filter(author -> !authorsBeforeUpdate.contains(author))
                        .collect(Collectors.toList());

        // Filter authors to remove
        List<Author> authorsToRemove =
                authorsBeforeUpdate.stream()
                        .filter(author -> !book.getAuthors().contains(author))
                        .collect(Collectors.toList());

        // Create author book relationships for added authors
        for (Author author : authorsToAdd) {
            this.addAuthorToBook(author, book);
        }

        // Delete author book relationships for removed authors
        for (Author author : authorsToRemove) {
            this.removeAuthorFromBook(author, book);
        }
    }

    /**
     * Deletes an existing book.
     *
     * @param book Book instance to delete.
     * @throws Exception if the book cannot be found, or if a general database error occurs.
     * @implNote This is a blocking operation.
     */
    public void deleteBook(Book book) throws Exception {
        logger.info("Deleting book with id " + book.getId() + "...");
        this.deleteMedia(book);
    }

    // Author

    /**
     * Gets an existing author.
     *
     * @param author Author instance to use for searching.
     * @throws Exception if the author cannot be found, or if a general database error occurs.
     * @implNote This is a blocking operation.
     */
    public Author getAuthor(Author author) throws Exception {
        logger.info(
                "Getting author (given name: "
                        + author.getGivenName()
                        + ", family name: "
                        + author.getFamilyName()
                        + ")...");
        return database.select(SELECT_FIRST_AUTHOR_BY_NAME_STATEMENT, Author.class)
                .configure(author.getGivenName(), author.getFamilyName())
                .fetch(Author::new);
    }

    /**
     * Creates an author.
     *
     * @param author Author instance to create.
     * @throws Exception if a general database error occurs.
     * @implNote This is a blocking operation.
     */
    public void createAuthor(Author author) throws Exception {
        logger.info("Creating author...");
        Long authorId =
                database.insert(CREATE_AUTHOR_STATEMENT)
                        .configure(author.getGivenName(), author.getFamilyName())
                        .executeQuery();
        author.setId(authorId);
    }

    /**
     * Updates an existing author.
     *
     * @param author Author instance to update.
     * @throws Exception if a general database error occurs.
     * @implNote This is a blocking operation.
     */
    public void updateAuthor(Author author) throws Exception {
        logger.info("Updating author with id " + author.getId() + "...");
        database.update(UPDATE_AUTHOR_STATEMENT)
                .configure(author.getGivenName(), author.getFamilyName(), author.getId())
                .execute();
    }

    /**
     * Deletes an existing author.
     *
     * @param author Author instance to create.
     * @throws Exception if a general database error occurs.
     * @implNote This is a blocking operation.
     */
    public void deleteActor(Author author) throws Exception {
        logger.info("Deleting author with id " + author.getId() + "...");
        database.update(DELETE_AUTHOR_STATEMENT).configure(author.getId()).execute();
    }

    /**
     * Checks if an author matching the provided fields exists, and creates one if it does not.
     *
     * @param author Author instance to find, or create.
     * @throws Exception if a general database error occurs.
     * @implNote This is a blocking operation.
     */
    public Author getOrCreateAuthor(Author author) throws Exception {
        logger.info("Get or create author...");
        try {
            return this.getAuthor(author);
        } catch (Exception exception) {
            this.createAuthor(author);
            return author;
        }
    }

    // Author book relationships

    /**
     * Get all authors for media.
     *
     * @param media Media instance to get authors for.
     * @throws Exception if a general database error occurs.
     * @implNote This is a blocking operation.
     */
    private List<Author> getAllAuthorsForMedia(Media media) throws Exception {
        logger.info("Getting all authors for media with id " + media.getId() + "...");
        return database.select(SELECT_ALL_AUTHORS_FOR_MEDIA_STATEMENT, Author.class)
                .configure(media.getId())
                .fetchAll(Author::new);
    }

    /**
     * Associates an author with a book.
     *
     * @param author Author instance to associate with book.
     * @param book Book instance author should be associated with.
     * @throws Exception if a general database error occurs.
     * @implNote This is a blocking operation.
     */
    private void addAuthorToBook(Author author, Book book) throws Exception {
        logger.info(
                "Adding author with id "
                        + author.getId()
                        + " to book with id "
                        + book.getId()
                        + "...");
        database.insert(CREATE_AUTHOR_MEDIA_STATEMENT)
                .configure(author.getId(), book.getId())
                .execute();
    }

    /**
     * Disassociates an author from a book.
     *
     * @param author Author instance to disassociate from book.
     * @param book Book instance author should be disassociated from.
     * @throws Exception if a general database error occurs.
     * @implNote This is a blocking operation.
     */
    private void removeAuthorFromBook(Author author, Book book) throws Exception {
        logger.info(
                "Removing author with id "
                        + author.getId()
                        + " from book with id "
                        + book.getId()
                        + "...");
        database.delete(DELETE_AUTHOR_MEDIA_STATEMENT)
                .configure(author.getId(), book.getId())
                .execute();
    }
}
