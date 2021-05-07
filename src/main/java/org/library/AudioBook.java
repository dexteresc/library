package org.library;

import java.util.Date;

/**
 * Audiobook type of article
 */
public class AudioBook extends Media {
    private Integer runtime;

    /**
     * @param runtime Duration of audiobook in minutes.
     */
    public AudioBook(Long id, String title, String category, String classification, String summary, String publisher, Date publishingDate, Integer runtime) {
        super(id, title, category, classification, summary, publisher, publishingDate);
        this.runtime = runtime;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }
}
