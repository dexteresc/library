package org.library.media;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * A common base for all media types.
 */
public abstract class Media {
    private Long id;
    private String title;
    private String classification;
    private String summary;
    private String location;
    private LocalDate publishingDate;
    private Long numberOfLoanableItems;

    public Media() {
    }

    public Media(Long id, String title, String classification, String summary, String location, LocalDate publishingDate, Long numberOfLoanableItems) {
        this.id = id;
        this.title = title;
        this.classification = classification;
        this.summary = summary;
        this.location = location;
        this.publishingDate = publishingDate;
        this.numberOfLoanableItems = numberOfLoanableItems;
    }

    public Media(ResultSet resultSet) throws SQLException {
        this(resultSet.getLong("id"), resultSet.getString("title"), resultSet.getString("classification"), resultSet.getString("summary"), resultSet.getString("location"), resultSet.getObject("publishing_date", LocalDate.class), resultSet.getLong("loanable_item_count"));
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public LocalDate getPublishingDate() {
        return publishingDate;
    }

    public void setPublishingDate(LocalDate publishingDate) {
        this.publishingDate = publishingDate;
    }

    public Long getNumberOfLoanableItems() {
        return numberOfLoanableItems;
    }

    public void setNumberOfLoanableItems(Long numberOfLoanableItems) {
        this.numberOfLoanableItems = numberOfLoanableItems;
    }

    public boolean hasItemsAvailableForLoan() {
        return numberOfLoanableItems > 0;
    }

}
