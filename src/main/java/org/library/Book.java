package org.library;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Book type of article
 */
public class Book extends Media {
    private String isbn;
    private String publisher;
    private List<Author> authors;

    public Book(Long id, String title, String classification, String summary, String location, LocalDate publishingDate, String publisher, String isbn, List<Author> authors) {
        super(id, title, classification, summary, location, publishingDate);

        this.isbn = isbn;
        this.publisher = publisher;
        this.authors = authors;
    }

    public Book(ResultSet resultSet) throws SQLException {
        super(resultSet);

        this.isbn = resultSet.getString("isbn");
        this.publisher = resultSet.getString("publisher");
        this.authors = new ArrayList<>();

        int numberOfAuthors = resultSet.getInt("author_count");
        if (numberOfAuthors > 1) {
            while (numberOfAuthors > 0) {
                this.authors.add(new Author(resultSet));
                resultSet.next();
                numberOfAuthors--;
            }
        } else {
            this.authors.add(new Author(resultSet));
        }
    }

    public String getIsbn() {
        return isbn;
    }

    public String getPublisher() {
        return publisher;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }
}
