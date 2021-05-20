package org.library.media;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.library.util.Database;

public class MediaManager {
    private static final Logger logger = LogManager.getLogger();

    // Statements
    private static final String UPDATE_MEDIA_STATEMENT = "UPDATE media SET title = ?, classification = ?, summary = ?, location = ?, publishing_date = ? WHERE id = ? LIMIT 1";
    private static final String CREATE_MEDIA_STATEMENT = "INSERT INTO media (title, classification, summary, location, publishing_date) VALUES (?, ?, ?, ?, ?)";
    private static final String DELETE_MEDIA_STATEMENT = "DELETE FROM media WHERE id = ?";

    protected final Database database;

    public MediaManager(Database database) {
        this.database = database;
    }

    protected void createMedia(Media media) throws Exception {
        logger.info("Creating media...");
        Long id = database.insert(CREATE_MEDIA_STATEMENT)
                .configure(media.getTitle(), media.getClassification(), media.getSummary(), media.getLocation(), media.getPublishingDate())
                .executeQuery();
        media.setId(id);
    }

    protected void updateMedia(Media media) throws Exception {
        logger.info("Updating media...");
        database.update(UPDATE_MEDIA_STATEMENT)
                .configure(media.getTitle(), media.getClassification(), media.getSummary(), media.getLocation(), media.getPublishingDate(), media.getId())
                .execute();
    }

    protected void deleteMediaById(Long id) throws Exception {
        logger.info("Deleting media by id...");
        database.delete(DELETE_MEDIA_STATEMENT)
                .configure(id)
                .execute();
    }

}
