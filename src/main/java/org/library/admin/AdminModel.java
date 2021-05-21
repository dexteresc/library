package org.library.admin;

import org.controllers.EditingMode;
import org.controllers.MediaItemConfiguration;
import org.library.media.*;

public class AdminModel {
    private BookManager bookManager;
    private MovieManager movieManager;
    private MediaItemManager mediaItemManager;

    private Object object;
    private EditingMode editingMode = EditingMode.CREATE;

    public AdminModel(BookManager bookManager, MovieManager movieManager, MediaItemManager mediaItemManager) {
        this.bookManager = bookManager;
        this.movieManager = movieManager;
        this.mediaItemManager = mediaItemManager;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Boolean hasObject() {
        return this.object != null;
    }

    public void setEditingMode(EditingMode editingMode) {
        this.editingMode = editingMode;
    }

    public void save() throws Exception {
        if (object instanceof Book) {
            this.save((Book) object);
        } else if (object instanceof Movie) {
            this.save((Movie) object);
        }
    }

    private void save(Book book) throws Exception {
        switch (this.editingMode) {
            case CREATE: this.bookManager.createBook(book); break;
            case UPDATE: this.bookManager.updateBook(book); break;
        }
    }

    private void save(Movie movie) throws Exception {
        switch (this.editingMode) {
            case CREATE: this.movieManager.createMovie(movie); break;
            case UPDATE: this.movieManager.updateMovie(movie); break;
        }
    }

    public void delete() throws Exception {
        if (object instanceof Book) {
            this.bookManager.deleteBook((Book) object);
        } else if (object instanceof Movie) {
            this.movieManager.deleteMovie((Movie) object);
        }
    }

    public void reset() {
        this.object = null;
        this.setEditingMode(EditingMode.CREATE);
    }
}
