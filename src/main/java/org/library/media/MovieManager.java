package org.library.media;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.library.util.Database;

public class MovieManager extends MediaManager {
    private static final Logger logger = LogManager.getLogger();

    // Movie statements
    private static final String SELECT_MOVIE_BY_ID_STATEMENT = "SELECT *, (SELECT COUNT(*) FROM media_actor WHERE media_id = media.id) AS actor_count, (SELECT COUNT(*) FROM media_item INNER JOIN media_type ON media_item.media_type_id = media_type.id WHERE media_id = media.id AND status IS NULL AND media_type.loan_period > 0) AS loanable_item_count FROM media INNER JOIN movie ON movie.media_id = media.id INNER JOIN media_actor ON media_actor.media_id = media.id INNER JOIN actor ON actor.id = media_actor.actor_id WHERE id = ?";
    private static final String CREATE_MOVIE_STATEMENT = "INSERT INTO movie (media_id, director, age_rating, production_country) VALUES (?, ?, ?)";
    private static final String UPDATE_MOVIE_STATEMENT = "UPDATE movie SET director = ?, age_rating = ?, production_country = ? WHERE media_id = ? LIMIT 1";

    // Actor statements
    private static final String CREATE_ACTOR_STATEMENT = "INSERT INTO actor (given_name, family_name) VALUES (?, ?)";
    private static final String UPDATE_ACTOR_STATEMENT = "UPDATE actor SET given_name = ?, family_name = ? WHERE id = ?";
    private static final String DELETE_ACTOR_STATEMENT = "DELETE FROM actor WHERE id = ?";
    private static final String SELECT_ACTOR_BY_ID_STATEMENT = "SELECT * FROM actor WHERE id = ?";
    private static final String SEARCH_ACTOR_STATEMENT = "SELECT * FROM actor WHERE MATCH(given_name, family_name) AGAINST(? IN NATURAL LANGUAGE MODE)";

    public MovieManager(Database database) {
        super(database);
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

}