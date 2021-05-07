package org.library;

/**
 * Audiobook type of article
 */
public class AudioBook extends Article { // Not prioritizing
    // Should we add this to db?
    String narrator;
    // Feedback needed
    int hours;
    int minutes;

    /**
     * @param id       The id of the article
     * @param title    The title of the article
     * @param year     The year of the article
     * @param narrator The narrator of the audiobook
     * @param hours    Hour length
     * @param minutes  Minute length
     */
    public AudioBook(Long id, String title, int year, String narrator, int hours, int minutes) {
        super(id, title, year);
        this.narrator = narrator;
        this.hours = hours;
        this.minutes = minutes;
    }

    public String getNarrator() {
        return narrator;
    }

    public void setNarrator(String narrator) {
        this.narrator = narrator;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }
}
