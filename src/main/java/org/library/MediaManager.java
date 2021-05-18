package org.library;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MediaManager {
    private static final Logger logger = LogManager.getLogger();

    // Statements
    private static final String UPDATE_MEDIA_STATEMENT = "UPDATE media SET title = ?, classification = ?, summary = ?, location = ?, publishing_date = ? WHERE id = ? LIMIT 1";
    private static final String CREATE_MEDIA_STATEMENT = "INSERT INTO media (title, classification, summary, location, publishing_date) VALUES (?, ?, ?, ?, ?)";
    private static final String DELETE_MEDIA_STATEMENT = "DELETE FROM media WHERE id = ?";

    // Book statements
    private static final String SELECT_BOOK_BY_ID_STATEMENT = "SELECT *, (SELECT COUNT(*) FROM media_author WHERE media_id = media.id) AS author_count, (SELECT COUNT(*) FROM media_item INNER JOIN media_type ON media_item.media_type_id = media_type.id WHERE media_id = media.id AND status IS NULL AND media_type.loan_period > 0) AS loanable_item_count FROM media INNER JOIN book ON book.media_id = media.id INNER JOIN media_author ON media_author.media_id = media.id INNER JOIN author ON author.id = media_author.author_id WHERE id = ?";
    private static final String CREATE_BOOK_STATEMENT = "INSERT INTO book (media_id, isbn, publisher) VALUES (?, ?, ?)";
    private static final String UPDATE_BOOK_STATEMENT = "UPDATE book SET isbn = ?, publisher = ? WHERE media_id = ? LIMIT 1";
    private static final String SEARCH_BOOK_STATEMENT = "SELECT *, (SELECT COUNT(*) FROM media_author WHERE media_id = media.id) AS author_count, (SELECT COUNT(*) FROM media_item INNER JOIN media_type ON media_item.media_type_id = media_type.id WHERE media_id = media.id AND status IS NULL AND media_type.loan_period > 0) AS loanable_item_count FROM media INNER JOIN book ON book.media_id = media.id INNER JOIN media_author ON media_author.media_id = media.id INNER JOIN author ON author.id = media_author.author_id WHERE MATCH(media.title, media.summary, media.classification) AGAINST(? IN NATURAL LANGUAGE MODE) OR MATCH(book.isbn, book.publisher) AGAINST(? IN NATURAL LANGUAGE MODE) OR media.id IN (SELECT media_id FROM media_author INNER JOIN author ON author.id = media_author.author_id WHERE MATCH(author.given_name, author.family_name) AGAINST(? IN NATURAL LANGUAGE MODE))";

    // Movie statements
    private static final String SELECT_MOVIE_BY_ID_STATEMENT = "SELECT *, (SELECT COUNT(*) FROM media_actor WHERE media_id = media.id) AS actor_count, (SELECT COUNT(*) FROM media_item INNER JOIN media_type ON media_item.media_type_id = media_type.id WHERE media_id = media.id AND status IS NULL AND media_type.loan_period > 0) AS loanable_item_count FROM media INNER JOIN movie ON movie.media_id = media.id INNER JOIN media_actor ON media_actor.media_id = media.id INNER JOIN actor ON actor.id = media_actor.actor_id WHERE id = ?";
    private static final String CREATE_MOVIE_STATEMENT = "INSERT INTO movie (media_id, director, age_rating, production_country) VALUES (?, ?, ?)";
    private static final String UPDATE_MOVIE_STATEMENT = "UPDATE movie SET director = ?, age_rating = ?, production_country = ? WHERE media_id = ? LIMIT 1";
    private static final String SEARCH_MOVIE_STATEMENT = "SELECT *, (SELECT COUNT(*) FROM media_actor WHERE media_id = media.id) AS actor_count, (SELECT COUNT(*) FROM media_item INNER JOIN media_type ON media_item.media_type_id = media_type.id WHERE media_id = media.id AND status IS NULL AND media_type.loan_period > 0) AS loanable_item_count FROM media INNER JOIN movie ON movie.media_id = media.id INNER JOIN media_actor ON media_actor.media_id = media.id INNER JOIN actor ON actor.id = media_actor.actor_id WHERE MATCH(media.title, media.summary, media.classification) AGAINST(? IN NATURAL LANGUAGE MODE) OR MATCH(movie.director, movie.production_country) AGAINST(? IN NATURAL LANGUAGE MODE) OR media.id IN (SELECT media_id FROM media_actor INNER JOIN actor ON actor.id = media_actor.actor_id WHERE MATCH(actor.given_name, actor.family_name) AGAINST(? IN NATURAL LANGUAGE MODE))";

    // Author statements
    private static final String CREATE_AUTHOR_STATEMENT = "INSERT INTO author (given_name, family_name) VALUES (?, ?)";
    private static final String UPDATE_AUTHOR_STATEMENT = "UPDATE author SET given_name = ?, family_name = ? WHERE id = ?";
    private static final String DELETE_AUTHOR_STATEMENT = "DELETE FROM author WHERE id = ?";
    private static final String SELECT_AUTHOR_BY_ID_STATEMENT = "SELECT * FROM author WHERE id = ?";
    private static final String SEARCH_AUTHOR_STATEMENT = "SELECT * FROM author WHERE MATCH(given_name, family_name) AGAINST(? IN NATURAL LANGUAGE MODE)";

    // Actor statements
    private static final String CREATE_ACTOR_STATEMENT = "INSERT INTO actor (given_name, family_name) VALUES (?, ?)";
    private static final String UPDATE_ACTOR_STATEMENT = "UPDATE actor SET given_name = ?, family_name = ? WHERE id = ?";
    private static final String DELETE_ACTOR_STATEMENT = "DELETE FROM actor WHERE id = ?";
    private static final String SELECT_ACTOR_BY_ID_STATEMENT = "SELECT * FROM actor WHERE id = ?";
    private static final String SEARCH_ACTOR_STATEMENT = "SELECT * FROM actor WHERE MATCH(given_name, family_name) AGAINST(? IN NATURAL LANGUAGE MODE)";

    // Media item statements
    private static final String SELECT_MEDIA_ITEMS_BY_MEDIA_ID_STATEMENT = "SELECT *, (SELECT COUNT(*) FROM loan WHERE loan.media_item_id = id AND returned_at IS NULL) AS currently_on_loan FROM media_item INNER JOIN media_type ON media_item.media_type_id = media_type.id WHERE media_id = ?";
    private static final String SELECT_AVAILABLE_MEDIA_ITEMS_BY_MEDIA_ID_STATEMENT = "SELECT *, (SELECT 0) AS currently_on_loan FROM media_item INNER JOIN media_type ON media_item.media_type_id = media_type.id WHERE media_id = ? AND status IS NULL AND media_type.loan_period > 0";
    private static final String SELECT_FIRST_AVAILABLE_MEDIA_ITEMS_BY_MEDIA_ID_STATEMENT = "SELECT *, (SELECT 0) AS currently_on_loan FROM media_item INNER JOIN media_type ON media_item.media_type_id = media_type.id WHERE media_id = ? AND status IS NULL LIMIT 1";
    private static final String CREATE_MEDIA_ITEM_STATEMENT = "INSERT INTO media_item (media_id, media_type_id, status) VALUES (?, ?, ?)";
    private static final String UPDATE_MEDIA_ITEM_STATEMENT = "UPDATE media_item SET media_type_id = ?, status = ? WHERE id = ?";

    private final Database database;

    public MediaManager(Database database) {
        this.database = database;
    }

    private void createMedia(Media media) throws Exception {
        logger.info("Creating media...");
        Long id = database.insert(CREATE_MEDIA_STATEMENT)
                .configure(media.getTitle(), media.getClassification(), media.getSummary(), media.getLocation(), media.getPublishingDate())
                .executeQuery();
        media.setId(id);
    }

    private void updateMedia(Media media) throws Exception {
        logger.info("Updating media...");
        database.update(UPDATE_MEDIA_STATEMENT)
                .configure(media.getTitle(), media.getClassification(), media.getSummary(), media.getLocation(), media.getPublishingDate(), media.getId())
                .execute();
    }

    public List<MediaItem> getAllMediaItems(Media media) throws Exception {
        logger.info("Getting all media items...");
        return database.select(SELECT_MEDIA_ITEMS_BY_MEDIA_ID_STATEMENT, MediaItem.class)
                .configure(media.getId())
                .fetchAll(resultSet -> new MediaItem(resultSet.getLong("id"), media, new MediaType(resultSet), resultSet.getInt("currently_on_loan") == 1, MediaItem.Status.valueOf(resultSet.getString("status"))));
    }

    public List<MediaItem> getAvailableMediaItems(Media media) throws Exception {
        logger.info("Getting all available media items...");
        return database.select(SELECT_AVAILABLE_MEDIA_ITEMS_BY_MEDIA_ID_STATEMENT, MediaItem.class)
                .configure(media.getId())
                .fetchAll(resultSet -> new MediaItem(resultSet.getLong("id"), media, new MediaType(resultSet), resultSet.getInt("currently_on_loan") == 1, MediaItem.Status.NONE));
    }

    public MediaItem getFirstAvailableMediaItem(Media media) throws Exception {
        logger.info("Getting first available media item...");
        return database.select(SELECT_FIRST_AVAILABLE_MEDIA_ITEMS_BY_MEDIA_ID_STATEMENT, MediaItem.class)
                .configure(media.getId())
                .fetch(resultSet -> new MediaItem(resultSet.getLong("id"), media, new MediaType(resultSet), resultSet.getInt("currently_on_loan") == 1, MediaItem.Status.NONE));
    }

    public void updateMediaItem(MediaItem mediaItem) throws Exception {
        logger.info("Updating media item...");
        database.update(UPDATE_MEDIA_ITEM_STATEMENT)
                .configure(mediaItem.getMediaType().getId(), mediaItem.getStatus().getRawValue(), mediaItem.getId())
                .execute();
    }

    public void createMediaItem(MediaItem mediaItem) throws Exception {
        logger.info("Creating media item...");
        Long id = database.insert(CREATE_MEDIA_ITEM_STATEMENT)
                .configure(mediaItem.getMedia().getId(), mediaItem.getMediaType().getId(), mediaItem.getStatus())
                .executeQuery();
        mediaItem.setId(id);
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

    public void deleteMediaById(Long id) throws Exception {
        logger.info("Deleting media by id...");
        database.delete(DELETE_MEDIA_STATEMENT)
                .configure(id)
                .execute();
    }

    public Movie getMovieById(Long id) throws Exception {
        logger.info("Getting movie by id...");
        return database.select(SELECT_MOVIE_BY_ID_STATEMENT, Movie.class)
                .configure(id)
                .fetch(Movie::new);
    }

    public void createMovie(Movie movie) throws Exception {
        logger.info("Creating movie...");
        this.createMedia(movie);
        database.insert(CREATE_MOVIE_STATEMENT)
                .configure(movie.getId(), movie.getDirector(), movie.getAgeRating(), movie.getProductionCountry())
                .execute();
    }

    public void updateMovie(Movie movie) throws Exception {
        logger.info("Updating movie...");
        this.updateMedia(movie);
        database.update(UPDATE_MOVIE_STATEMENT)
                .configure(movie.getDirector(), movie.getAgeRating(), movie.getProductionCountry(), movie.getId())
                .execute();
    }

    public CompletableFuture<List<Book>> searchBook(String query) throws Exception {
        logger.debug("Searching books... (using query ”" + query + "”)");
        return database.select(SEARCH_BOOK_STATEMENT, Book.class)
                .configure(query, query, query)
                .asyncFetchAll(Book::new);
    }

    public CompletableFuture<List<Movie>> searchMovie(String query) throws Exception {
        logger.debug("Searching movies... (using query ”" + query + "”)");
        return database.select(SEARCH_MOVIE_STATEMENT, Movie.class)
                .configure(query, query, query)
                .asyncFetchAll(Movie::new);
    }

    public CompletableFuture<List<Media>> searchMedia(String query) throws Exception {
        logger.info("Searching media... (using query ”" + query + "”)");
        return CompletableFuture.supplyAsync(() -> {
            List<Media> mediaList = new ArrayList<>();

            try {
                mediaList.addAll(this.searchBook(query).get());
                mediaList.addAll(this.searchMovie(query).get());
                return mediaList;
            } catch (Exception exception) {
                exception.printStackTrace();
                return mediaList;
            }
        });
    }

}
