package org.library;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Movie extends Media {
    private String director;
    private String ageRating;
    private String productionCountry;

    public Movie(ResultSet resultSet) throws SQLException {
        super(resultSet);

        this.director = resultSet.getString("director");
        this.ageRating = resultSet.getString("age_rating");
        this.productionCountry = resultSet.getString("production_country");
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

    public void setDirector(String director) {
        this.director = director;
    }

    public void setAgeRating(String ageRating) {
        this.ageRating = ageRating;
    }

    public void setProductionCountry(String productionCountry) {
        this.productionCountry = productionCountry;
    }
}
