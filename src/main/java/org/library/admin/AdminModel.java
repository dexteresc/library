package org.library.admin;

import org.library.media.BookManager;
import org.library.media.Media;
import org.library.media.MediaItemManager;
import org.library.media.MovieManager;

public class AdminModel {
    private BookManager bookManager;
    private MovieManager movieManager;
    private MediaItemManager mediaItemManager;

    private Media media;

    public AdminModel(BookManager bookManager, MovieManager movieManager, MediaItemManager mediaItemManager) {
        this.bookManager = bookManager;
        this.movieManager = movieManager;
        this.mediaItemManager = mediaItemManager;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }
}
