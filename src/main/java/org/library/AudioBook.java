package org.library;

public class AudioBook extends Article{ // Not prioritizing
    // Should we add this to db?
    String narrator = "John Smith";
    // Feedback needed
    int hours = 1;
    int minutes = 10;

    public AudioBook(int id, String title, int year, String narrator, int hours, int minutes) {
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
