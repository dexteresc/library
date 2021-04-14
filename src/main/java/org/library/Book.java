package org.library;

public class Book extends Article {
    String isbn = "01010101010101010101";
    String[] authors;
    Double physical_location = 13.01;
    int inStock = 1;

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
