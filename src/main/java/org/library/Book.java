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

    public Book(Long id, String title, String category, String classification, String summary, LocalDate publishingDate, String publisher, String isbn) {
        super(id, title, category, classification, summary, publishingDate);
        this.isbn = isbn;
        this.publisher = publisher;
    }

    public Book(ResultSet resultSet) throws SQLException {
        this(resultSet.getLong("id"), resultSet.getString("title"), resultSet.getString("category"), resultSet.getString("classification"), resultSet.getString("summary"), resultSet.getObject("publishing_date", LocalDate.class), resultSet.getString("isbn"), resultSet.getString("publisher"));
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
