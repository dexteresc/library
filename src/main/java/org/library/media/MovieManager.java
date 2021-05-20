package org.library.media;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.library.util.Database;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Manager for movie media instances.
 *
 * @see Movie
 * @see MediaManager
 */
public class MovieManager extends MediaManager {
    private static final Logger logger = LogManager.getLogger();

    // Movie statements
    private static final String SELECT_MOVIE_BY_ID_STATEMENT = "SELECT *, (SELECT COUNT(*) FROM media_actor WHERE media_id = media.id) AS actor_count, (SELECT COUNT(*) FROM media_item INNER JOIN media_type ON media_item.media_type_id = media_type.id WHERE media_id = media.id AND status IS NULL AND media_type.loan_period > 0 AND media_item.id NOT IN (SELECT media_item_id FROM loan WHERE media_item_id = media_item.id)) AS loanable_item_count FROM media INNER JOIN movie ON movie.media_id = media.id INNER JOIN media_actor ON media_actor.media_id = media.id INNER JOIN actor ON actor.id = media_actor.actor_id WHERE id = ?";
    private static final String CREATE_MOVIE_STATEMENT = "INSERT INTO movie (media_id, director, age_rating, production_country) VALUES (?, ?, ?)";
    private static final String UPDATE_MOVIE_STATEMENT = "UPDATE movie SET director = ?, age_rating = ?, production_country = ? WHERE media_id = ? LIMIT 1";

    // Actor statements
    private static final String CREATE_ACTOR_STATEMENT = "INSERT INTO actor (given_name, family_name) VALUES (?, ?)";
    private static final String UPDATE_ACTOR_STATEMENT = "UPDATE actor SET given_name = ?, family_name = ? WHERE id = ?";
    private static final String DELETE_ACTOR_STATEMENT = "DELETE FROM actor WHERE id = ?";
    private static final String SELECT_FIRST_ACTOR_BY_NAME_STATEMENT = "SELECT * FROM actor WHERE given_name = ? AND family_name = ? LIMIT 1";

    // Author media relationship statements
    private static final String CREATE_ACTOR_MEDIA_STATEMENT = "INSERT INTO media_actor (actor_id, media_id) VALUES (?, ?)";
    private static final String SELECT_ALL_ACTORS_FOR_MEDIA_STATEMENT = "SELECT * FROM media_actor INNER JOIN actor ON actor.id = media_actor.actor_id WHERE media_id = ?";
    private static final String DELETE_ACTOR_MEDIA_STATEMENT = "DELETE FROM media_actor WHERE actor_id = ?, media_id = ?";

    /**
     * Creates a new movie manager instance.
     *
     * @param database A database instance.
     */
    public MovieManager(Database database) {
        super(database);
    }

    public Movie getMovieById(Long id) throws Exception {
        logger.info("Getting movie by id...");
        return database.select(SELECT_MOVIE_BY_ID_STATEMENT, Movie.class)
                .configure(id)
                .fetch(Movie::new);
    }

    /**
     * Creates a movie.
     *
     * @param movie Movie instance to create.
     * @throws Exception if the movie already exists, or if a general database error occurs.
     * @implNote This is a blocking operation.
     */
    public void createMovie(Movie movie) throws Exception {
        logger.info("Creating movie...");
        this.createMedia(movie);
        database.insert(CREATE_MOVIE_STATEMENT)
                .configure(movie.getId(), movie.getDirector(), movie.getAgeRating(), movie.getProductionCountry())
                .execute();

        // Get or create actors
        for (Actor actor : movie.getActors()) {
            actor.setId(this.getOrCreateActor(actor).getId());
        }

        // Create actor movie relationships
        for (Actor actor : movie.getActors()) {
            this.addActorToMovie(actor, movie);
        }
    }

    /**
     * Updates an existing movie.
     *
     * @param movie Movie instance to update.
     * @throws Exception if the movie cannot be found, or if a general database error occurs.
     * @implNote This is a blocking operation.
     */
    public void updateMovie(Movie movie) throws Exception {
        logger.info("Updating movie...");
        this.updateMedia(movie);
        database.update(UPDATE_MOVIE_STATEMENT)
                .configure(movie.getDirector(), movie.getAgeRating(), movie.getProductionCountry(), movie.getId())
                .execute();

        // Update movie actors
        this.updateActorsForMovie(movie);
    }

    private void updateActorsForMovie(Movie movie) throws Exception {
        // Get actors for movie before update
        List<Actor> authorsBeforeUpdate = this.getAllActorsForMedia(movie);

        // Get or create actors without ids
        for (Actor actor : movie.getActors().stream().filter(actor -> actor.getId() == null).collect(Collectors.toList())) {
            actor.setId(this.getOrCreateActor(actor).getId());
        }

        // Filter actors to add
        List<Actor> actorsToAdd = movie.getActors().stream().filter(actor -> !authorsBeforeUpdate.contains(actor)).collect(Collectors.toList());

        // Filter actors to remove
        List<Actor> actorsToRemove = authorsBeforeUpdate.stream().filter(actor -> !movie.getActors().contains(actor)).collect(Collectors.toList());

        // Create actor movie relationships for added actors
        for (Actor actor : actorsToAdd) {
            this.addActorToMovie(actor, movie);
        }

        // Delete actor movie relationships for removed actors
        for (Actor actor : actorsToRemove) {
            this.removeActorFromMovie(actor, movie);
        }
    }

    /**
     * Deletes an existing movie.
     *
     * @param movie Movie instance to delete.
     * @throws Exception if the movie cannot be found, or if a general database error occurs.
     * @implNote This is a blocking operation.
     */
    public void deleteMovie(Movie movie) throws Exception {
        this.deleteMedia(movie);
    }

    // Actor

    public Actor getActor(Actor actor) throws Exception {
        return database.select(SELECT_FIRST_ACTOR_BY_NAME_STATEMENT, Actor.class)
                .configure(actor.getGivenName(), actor.getFamilyName())
                .fetch(Actor::new);
    }

    public void createActor(Actor actor) throws Exception {
        Long actorId = database.insert(CREATE_ACTOR_STATEMENT)
                .configure(actor.getGivenName(), actor.getFamilyName())
                .executeQuery();
        actor.setId(actorId);
    }

    public void updateActor(Actor actor) throws Exception {
        database.update(UPDATE_ACTOR_STATEMENT)
                .configure(actor.getGivenName(), actor.getFamilyName(), actor.getId())
                .execute();
    }

    public void deleteActor(Actor actor) throws Exception {
        database.update(DELETE_ACTOR_STATEMENT)
                .configure(actor.getId())
                .execute();
    }

    public Actor getOrCreateActor(Actor actor) throws Exception {
        try {
            return this.getActor(actor);
        } catch (Exception exception) {
            this.createActor(actor);
            return actor;
        }
    }

    // Actor movie relationships

    private List<Actor> getAllActorsForMedia(Media media) throws Exception {
        return database.select(SELECT_ALL_ACTORS_FOR_MEDIA_STATEMENT, Actor.class)
                .configure(media.getId())
                .fetchAll(Actor::new);
    }

    private void addActorToMovie(Actor actor, Movie movie) throws Exception {
        database.insert(CREATE_ACTOR_MEDIA_STATEMENT)
                .configure(actor.getId(), movie.getId())
                .execute();
    }

    private void removeActorFromMovie(Actor actor, Movie movie) throws Exception {
        database.delete(DELETE_ACTOR_MEDIA_STATEMENT)
                .configure(actor.getId(), movie.getId())
                .execute();
    }
}
