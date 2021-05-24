package org.library.media;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Book representing a cinematic work.
 *
 * @see Media
 */
public class Movie extends Media {

    private String director;
    private String ageRating;
    private String productionCountry;
    private List<Actor> actors;

    /** Creates a new, empty, movie instance. */
    public Movie() {
        this.actors = new ArrayList<>();
    }

    /**
     * Creates a new movie instance from a result set.
     *
     * @param resultSet A ResultSet instance.
     * @throws SQLException if the ResultSet instance methods throw an exception.
     */
    public Movie(ResultSet resultSet) throws SQLException {
        super(resultSet);

        this.director = resultSet.getString("director");
        this.ageRating = resultSet.getString("age_rating");
        this.productionCountry = resultSet.getString("production_country");
        this.actors = new ArrayList<>();

        int numberOfActors = resultSet.getInt("actor_count");
        this.actors.add(new Actor(resultSet));
        while (numberOfActors > 1) {
            this.actors.add(new Actor(resultSet));
            resultSet.next();
            numberOfActors--;
        }
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getAgeRating() {
        return ageRating;
    }

    public void setAgeRating(String ageRating) {
        this.ageRating = ageRating;
    }

    public String getProductionCountry() {
        return productionCountry;
    }

    public void setProductionCountry(String productionCountry) {
        this.productionCountry = productionCountry;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }
}
