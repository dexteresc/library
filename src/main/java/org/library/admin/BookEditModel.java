package org.library.admin;

import org.library.media.Book;
import org.library.media.BookManager;

public class BookEditModel extends EditModel {

    private BookManager bookManager;
    private Book book;

    public BookEditModel(BookManager bookManager) {
        super();

        this.bookManager = bookManager;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public void save() throws Exception {
        if (this.book.getId() == null) {
            this.bookManager.createBook(book);
        } else {
            this.bookManager.updateBook(book);
        }
    }

    @Override
    public void delete() {

    }

}
