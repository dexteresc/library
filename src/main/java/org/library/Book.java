package org.library;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Book type of article
 */
public class Book extends Media {
    private String isbn;
    private String publisher;

    public Book(Long id, String title, String classification, String summary, String location, LocalDate publishingDate, String publisher, String isbn) {
        super(id, title, classification, summary, location, publishingDate);
        this.isbn = isbn;
        this.publisher = publisher;
    }

    public Book(ResultSet resultSet) throws SQLException {
        this(resultSet.getLong("id"), resultSet.getString("title"), resultSet.getString("classification"), resultSet.getString("summary"), resultSet.getString("location"), resultSet.getObject("publishing_date", LocalDate.class), resultSet.getString("isbn"), resultSet.getString("publisher"));
    }

    public String getIsbn() {
        return isbn;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
