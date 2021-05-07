package org.library;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Book type of article
 */
public class Book extends Media {
    private String isbn;
    private String authors;

    public Book(Long id, String title, String category, String classification, String summary, String publisher, Date publishingDate, String isbn, String authors) {
        super(id, title, category, classification, summary, publisher, publishingDate);
        this.isbn = isbn;
        this.authors = authors;
    }

    public Book(ResultSet resultSet) throws SQLException {
        this(resultSet.getLong("id"), resultSet.getString("title"), resultSet.getString("category"), resultSet.getString("classification"), resultSet.getString("summary"), resultSet.getString("publisher"), resultSet.getDate("publishing_date"), "", "");
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }
}
