package org.library;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Movie extends Media {
    private String director;
    private String ageRating;
    private String productionCountry;
    private List<Actor> actors;

    public Movie(ResultSet resultSet) throws SQLException {
        super(resultSet);

        this.director = resultSet.getString("director");
        this.ageRating = resultSet.getString("age_rating");
        this.productionCountry = resultSet.getString("production_country");
        this.actors = new ArrayList<>();

        int numberOfActors = resultSet.getInt("actor_count");
        if (numberOfActors > 1) {
            while (numberOfActors > 0) {
                this.actors.add(new Actor(resultSet));
                resultSet.next();
                numberOfActors--;
            }
        } else {
            this.actors.add(new Actor(resultSet));
        }
    }

    public String getDirector() {
        return director;
    }

    public String getAgeRating() {
        return ageRating;
    }

    public String getProductionCountry() {
        return productionCountry;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setAgeRating(String ageRating) {
        this.ageRating = ageRating;
    }

    public void setProductionCountry(String productionCountry) {
        this.productionCountry = productionCountry;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }
}
