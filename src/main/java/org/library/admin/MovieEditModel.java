package org.library.admin;

import org.library.media.Movie;
import org.library.media.MovieManager;

public class MovieEditModel extends EditModel {

    private MovieManager movieManager;
    private Movie movie;

    public MovieEditModel(MovieManager movieManager) {
        super();

        this.movieManager = movieManager;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    @Override
    public void save() throws Exception {
        if (this.movie.getId() == null) {
            this.movieManager.createMovie(movie);
        } else {
            this.movieManager.updateMovie(movie);
        }
    }

    @Override
    public void delete() {

    }

}
