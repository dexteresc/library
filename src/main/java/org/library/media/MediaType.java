package org.library.media;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Media type representing a category of media. Dictates whether or not a media item can be loaned and if so, for how long.
 *
 * @see MediaItem
 */
public class MediaType {
    private Long id;
    private String name;
    private Integer loanPeriod;

    /**
     * Creates a new, empty, media type instance.
     */
    public MediaType() {
        this.loanPeriod = 0;
    }

    /**
     * Creates a new media type instance.
     */
    MediaType(Long id, String name, Integer loanPeriod) {
        this.id = id;
        this.name = name;
        this.loanPeriod = loanPeriod;
    }

    /**
     * Creates a new media type instance from a result set.
     *
     * @param resultSet A ResultSet instance.
     * @throws SQLException if the ResultSet instance methods throw an exception.
     */
    public MediaType(ResultSet resultSet) throws SQLException {
        this(resultSet.getLong("media_type_id"), resultSet.getString("type_name"), resultSet.getInt("loan_period"));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLoanPeriod() {
        return loanPeriod;
    }

    public void setLoanPeriod(Integer loanPeriod) {
        this.loanPeriod = loanPeriod;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MediaType)) return false;

        MediaType mediaType = (MediaType) o;

        return Objects.equals(id, mediaType.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
