package org.library.admin;

import org.library.media.*;

public class AdminModel {
    private final BookManager bookManager;
    private final MovieManager movieManager;
    private final MediaItemManager mediaItemManager;

    private EditModel editModel;

    public AdminModel(
            BookManager bookManager, MovieManager movieManager, MediaItemManager mediaItemManager) {
        this.bookManager = bookManager;
        this.movieManager = movieManager;
        this.mediaItemManager = mediaItemManager;
    }

    public boolean hasEditModel() {
        return this.editModel != null;
    }

    public EditModel getEditModel() {
        return editModel;
    }

    public void editBook(Book book) {
        BookEditModel bookEditModel = new BookEditModel(bookManager);
        bookEditModel.setBook(book);

        this.editModel = bookEditModel;
    }

    public void editMovie(Movie movie) {
        MovieEditModel movieEditModel = new MovieEditModel(movieManager);
        movieEditModel.setMovie(movie);

        this.editModel = movieEditModel;
    }

    public void editMediaItems(Media media) {
        MediaItemsEditModel mediaItemsEditModel = new MediaItemsEditModel(mediaItemManager);
        mediaItemsEditModel.setMedia(media);

        this.editModel = mediaItemsEditModel;
    }

    public void save() throws Exception {
        if (this.editModel == null) {
            return;
        }

        this.editModel.save();
    }

    public void delete() throws Exception {
        if (this.editModel == null) {
            return;
        }

        this.editModel.delete();
    }

    public void reset() {
        this.editModel = null;
    }
}
