package org.library;

/**
 * An article
 */
public class Article {
    // Need feedback
    Long id;
    String title;
    int year;

    /**
     * @param id    The id of the article
     * @param title The title of the article
     * @param year Year of article
     */
    public Article(Long id, String title, int year) {
        this.id = id;
        this.title = title;
        this.year = year;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
