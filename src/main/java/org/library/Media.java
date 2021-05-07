package org.library;

/**
 * A common base for all media types.
 */
public abstract class Media {
    private Long id;
    private String title;
    private String category;
    private String classification;
    private String summary;
    private String publisher;

    public Media(Long id, String title, String category, String classification, String summary, String publisher) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.classification = classification;
        this.summary = summary;
        this.publisher = publisher;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getClassification() {
        return classification;
    }

    public String getSummary() {
        return summary;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
