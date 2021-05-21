package org.library.search;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.library.media.Book;
import org.library.media.Media;
import org.library.media.MediaManager;
import org.library.media.Movie;
import org.library.util.Database;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SearchModel {
    private static final Logger logger = LogManager.getLogger();

    private static final String SEARCH_BOOK_STATEMENT = "SELECT *, (SELECT COUNT(*) FROM media_author WHERE media_id = media.id) AS author_count, (SELECT COUNT(*) FROM media_item INNER JOIN media_type ON media_item.media_type_id = media_type.id WHERE media_id = media.id AND status IS NULL AND media_type.loan_period > 0 AND media_item.id NOT IN (SELECT media_item_id FROM loan WHERE media_item_id = media_item.id)) AS loanable_item_count FROM media INNER JOIN book ON book.media_id = media.id INNER JOIN media_author ON media_author.media_id = media.id INNER JOIN author ON author.id = media_author.author_id WHERE MATCH(media.title, media.summary, media.classification) AGAINST(? IN NATURAL LANGUAGE MODE) OR MATCH(book.isbn, book.publisher) AGAINST(? IN NATURAL LANGUAGE MODE) OR media.id IN (SELECT media_id FROM media_author INNER JOIN author ON author.id = media_author.author_id WHERE MATCH(author.given_name, author.family_name) AGAINST(? IN NATURAL LANGUAGE MODE))";
    private static final String SEARCH_MOVIE_STATEMENT = "SELECT *, (SELECT COUNT(*) FROM media_actor WHERE media_id = media.id) AS actor_count, (SELECT COUNT(*) FROM media_item INNER JOIN media_type ON media_item.media_type_id = media_type.id WHERE media_id = media.id AND status IS NULL AND media_type.loan_period > 0 AND media_item.id NOT IN (SELECT media_item_id FROM loan WHERE media_item_id = media_item.id)) AS loanable_item_count FROM media INNER JOIN movie ON movie.media_id = media.id INNER JOIN media_actor ON media_actor.media_id = media.id INNER JOIN actor ON actor.id = media_actor.actor_id WHERE MATCH(media.title, media.summary, media.classification) AGAINST(? IN NATURAL LANGUAGE MODE) OR MATCH(movie.director, movie.production_country) AGAINST(? IN NATURAL LANGUAGE MODE) OR media.id IN (SELECT media_id FROM media_actor INNER JOIN actor ON actor.id = media_actor.actor_id WHERE MATCH(actor.given_name, actor.family_name) AGAINST(? IN NATURAL LANGUAGE MODE))";

    private Database database;
    private MediaManager mediaManager;
    private ObservableList<Media> searchResultsList = FXCollections.observableList(new ArrayList<>());
    private SimpleBooleanProperty noSearchResults = new SimpleBooleanProperty();
    private String previousQuery;

    public SearchModel(Database database) {
        this.database = database;
    }

    public void search(String query) {
        if (this.previousQuery != null && this.previousQuery.equalsIgnoreCase(query)) {
            logger.info("Ignoring unchanged search query.");
            return; // No need to run the same query again.
        }
        this.previousQuery = query;

        try {
            this.searchMedia(query).thenAcceptAsync(result -> Platform.runLater(() -> {
                this.searchResultsList.setAll(result);
                this.noSearchResults.set(result.size() < 1);
            }));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Media> getSearchResultsList() {
        return searchResultsList;
    }

    public SimpleBooleanProperty getNoSearchResults() {
        return this.noSearchResults;
    }

    public String getPreviousQuery() {
        return previousQuery;
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
