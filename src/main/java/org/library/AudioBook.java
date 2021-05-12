package org.library;

import java.time.LocalDate;

/**
 * Audiobook type of article
 */
public class AudioBook extends Media {
    private Integer runtime;

    /**
     * @param runtime Duration of audiobook in minutes.
     */
    public AudioBook(Long id, String title, String classification, String summary, LocalDate publishingDate, Integer runtime) {
        super(id, title, classification, summary, null, publishingDate);
        this.runtime = runtime;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }
}
