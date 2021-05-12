package org.library;

import java.util.ArrayList;
import java.util.List;

public class MediaManager {

    // Statements
    private static final String UPDATE_MEDIA_STATEMENT = "UPDATE media SET title = ?, classification = ?, summary = ?, location = ?, publishing_date = ? WHERE id = ? LIMIT 1";
    private static final String CREATE_MEDIA_STATEMENT = "INSERT INTO media (title, classification, summary, location, publishing_date) VALUES (?, ?, ?, ?, ?)";
    private static final String DELETE_MEDIA_STATEMENT = "DELETE FROM media WHERE id = ?";

    // Book statements
    private static final String SELECT_BOOK_BY_ID_STATEMENT = "SELECT *, (SELECT COUNT(*) FROM media_author WHERE media_id = media.id) AS author_count FROM media INNER JOIN book ON book.media_id = media.id INNER JOIN media_author ON media_author.media_id = media.id INNER JOIN author ON author.id = media_author.author_id WHERE id = ?";
    private static final String CREATE_BOOK_STATEMENT = "INSERT INTO book (media_id, isbn, publisher) VALUES (?, ?, ?)";
    private static final String UPDATE_BOOK_STATEMENT = "UPDATE book SET isbn = ?, publisher = ? WHERE media_id = ? LIMIT 1";
    private static final String SEARCH_BOOK_STATEMENT = "SELECT *, (SELECT COUNT(*) FROM media_author WHERE media_id = media.id) AS author_count FROM media INNER JOIN book ON book.media_id = media.id INNER JOIN media_author ON media_author.media_id = media.id INNER JOIN author ON author.id = media_author.author_id WHERE MATCH(media.title, media.summary, media.classification) AGAINST(? IN NATURAL LANGUAGE MODE) OR MATCH(book.isbn, book.publisher) AGAINST(? IN NATURAL LANGUAGE MODE) OR media.id IN (SELECT media_id FROM media_author INNER JOIN author ON author.id = media_author.author_id WHERE MATCH(author.given_name, author.family_name) AGAINST(? IN NATURAL LANGUAGE MODE))";

    // Movie statements
    private static final String SELECT_MOVIE_BY_ID_STATEMENT = "SELECT *, (SELECT COUNT(*) FROM media_actor WHERE media_id = media.id) AS actor_count FROM media INNER JOIN movie ON movie.media_id = media.id INNER JOIN media_actor ON media_actor.media_id = media.id INNER JOIN actor ON actor.id = media_actor.actor_id WHERE id = ?";
    private static final String CREATE_MOVIE_STATEMENT = "INSERT INTO movie (media_id, director, age_rating, production_country) VALUES (?, ?, ?)";
    private static final String UPDATE_MOVIE_STATEMENT = "UPDATE movie SET director = ?, age_rating = ?, production_country = ? WHERE media_id = ? LIMIT 1";
    private static final String SEARCH_MOVIE_STATEMENT = "SELECT *, (SELECT COUNT(*) FROM media_actor WHERE media_id = media.id) AS actor_count FROM media INNER JOIN movie ON movie.media_id = media.id INNER JOIN media_actor ON media_actor.media_id = media.id INNER JOIN actor ON actor.id = media_actor.actor_id WHERE MATCH(media.title, media.summary, media.classification) AGAINST(? IN NATURAL LANGUAGE MODE) OR MATCH(movie.director, movie.production_country) AGAINST(? IN NATURAL LANGUAGE MODE) OR media.id IN (SELECT media_id FROM media_actor INNER JOIN actor ON actor.id = media_actor.actor_id WHERE MATCH(actor.given_name, actor.family_name) AGAINST(? IN NATURAL LANGUAGE MODE))";

    // TODO: Author statements

    // Media item statements
    private static final String SELECT_MEDIA_ITEMS_BY_MEDIA_ID_STATEMENT = "SELECT * FROM media_item INNER JOIN media_type ON media_item.media_type_id = media_type.id WHERE media_id = ?";
    private static final String CREATE_MEDIA_ITEM_STATEMENT = "INSERT INTO media_item (media_id, media_type_id) VALUES (?, ?)";

    private final Database database;

    public MediaManager(Database database) {
        this.database = database;
    }

    private void createMedia(Media media) throws Exception {
        Long id = database.insert(CREATE_MEDIA_STATEMENT)
                .configure(media.getTitle(), media.getClassification(), media.getSummary(), media.getLocation(), media.getPublishingDate())
                .executeQuery();
        media.setId(id);
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

    // TODO: Get available media items.

    public void createMediaItem(MediaItem mediaItem) throws Exception {
        Long id = database.insert(CREATE_MEDIA_ITEM_STATEMENT)
                .configure(mediaItem.getMedia().getId(), mediaItem.getMediaType().getId())
                .executeQuery();
        mediaItem.setId(id);
    }

    public Book getBookById(Long id) throws Exception {
        return database.select(SELECT_BOOK_BY_ID_STATEMENT, Book.class)
                .configure(id)
                .fetch(Book::new);
    }

    public void createBook(Book book) throws Exception {
        this.createMedia(book);
        database.insert(CREATE_BOOK_STATEMENT)
                .configure(book.getId(), book.getIsbn(), book.getPublisher())
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
                .configure(query, query, query)
                .fetchAll(Book::new);
    }

    public void deleteMediaById(Long id) throws Exception {
        database.delete(DELETE_MEDIA_STATEMENT)
                .configure(id)
                .execute();
    }

    public Movie getMovieById(Long id) throws Exception {
        return database.select(SELECT_MOVIE_BY_ID_STATEMENT, Movie.class)
                .configure(id)
                .fetch(Movie::new);
    }

    public void createMovie(Movie movie) throws Exception {
        this.createMedia(movie);
        database.insert(CREATE_MOVIE_STATEMENT)
                .configure(movie.getId(), movie.getDirector(), movie.getAgeRating(), movie.getProductionCountry())
                .execute();
    }

    public void updateMovie(Movie movie) throws Exception {
        this.updateMedia(movie);
        database.update(UPDATE_MOVIE_STATEMENT)
                .configure(movie.getDirector(), movie.getAgeRating(), movie.getProductionCountry(), movie.getId())
                .execute();
    }

    public List<Movie> searchMovie(String query) throws Exception {
        return database.select(SEARCH_MOVIE_STATEMENT, Movie.class)
                .configure(query, query, query)
                .fetchAll(Movie::new);
    }

    public List<Media> searchMedia(String query) throws Exception {
        List<Media> mediaList = new ArrayList<>();
        mediaList.addAll(
                this.searchBook(query)
        );
        mediaList.addAll(
                this.searchMovie(query)
        );
        return mediaList;
    }

}
