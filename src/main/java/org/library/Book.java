package org.library;

/**
 * Book type of article
 */
public class Book extends Article {
    String isbn;
    String[] authors;
    Double physical_location;
    int inStock;

    /**
     * Book Constructor
     *
     * @param id                Id of article
     * @param title             Title of article
     * @param year              Year of article
     * @param isbn              The books isbn
     * @param authors           The books author(s)
     * @param physical_location The physical location of the book inside the library
     * @param inStock           The amount of books in the library
     */
    public Book(int id, String title, int year, String isbn, String[] authors, Double physical_location, int inStock) {
        super(id, title, year);
        this.isbn = isbn;
        this.authors = authors;
        this.physical_location = physical_location;
        this.inStock = inStock;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String[] getAuthors() {
        return authors;
    }

    public void setAuthors(String[] authors) {
        this.authors = authors;
    }

    public Double getPhysical_location() {
        return physical_location;
    }

    public void setPhysical_location(Double physical_location) {
        this.physical_location = physical_location;
    }

    public int getInStock() {
        return inStock;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
    }
}
