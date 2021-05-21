package org.library.media;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Book representing a literary work.
 *
 * @see Media
 */
public class Book extends Media {

    private String isbn;
    private String publisher;
    private List<Author> authors;

    /**
     * Creates a new, empty, book instance.
     */
    public Book() {
        this.authors = new ArrayList<>();
    }

    /**
     * Creates a new book instance.
     */
    protected Book(Long id, String title, String classification, String summary, String location, LocalDate publishingDate, String publisher, String isbn, List<Author> authors) {
        super(id, title, classification, summary, location, publishingDate, (long) 0);

        this.isbn = isbn;
        this.publisher = publisher;
        this.authors = authors;
    }

    /**
     * Creates a new book instance from a result set.
     *
     * @param resultSet A ResultSet instance.
     * @throws SQLException if the ResultSet instance methods throw an exception.
     */
    public Book(ResultSet resultSet) throws SQLException {
        super(resultSet);

        this.isbn = resultSet.getString("isbn");
        this.publisher = resultSet.getString("publisher");
        this.authors = new ArrayList<>();

        int numberOfAuthors = resultSet.getInt("author_count");
        this.authors.add(new Author(resultSet));
        while (numberOfAuthors > 1) {
            numberOfAuthors--;
            resultSet.next();
            this.authors.add(new Author(resultSet));
        }
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

}
