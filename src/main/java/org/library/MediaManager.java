package org.library;

import java.util.Date;

public class MediaManager {

    // Statements
    private static final String SELECT_MEDIA_STATEMENT = "SELECT * FROM media WHERE id = ? LIMIT 1";
    private static final String UPDATE_MEDIA_STATEMENT = "UPDATE media SET title = ?, category = ?, classification = ?, summary = ?, publisher = ?, publishing_date = ? WHERE id = ? LIMIT 1";
    private static final String CREATE_MEDIA_STATEMENT = "INSERT INTO media (title, category, classification, summary, publisher, publishing_date) VALUES (?, ?, ?, ?, ?, ?)";

    private final Database database;

    public MediaManager(Database database) {
        this.database = database;
    }

    public void createMedia(String title, String category, String classification, String summary, String publisher, Date publishingDate) throws Exception {
        database.insert(CREATE_MEDIA_STATEMENT)
                .configure(title, category, classification, summary, publisher, publishingDate)
                .execute();
    }

    public void updateMedia(Media media) throws Exception {
        database.update(UPDATE_MEDIA_STATEMENT)
                .configure(media.getTitle(), media.getCategory(), media.getClassification(), media.getSummary(), media.getPublisher(), media.getPublishingDate(), media.getId())
                .execute();
    }

}
