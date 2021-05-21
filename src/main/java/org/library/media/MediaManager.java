package org.library.media;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.library.util.Database;

/**
 * Abstract Manager for media instances.
 *
 * @see Media
 * @see MediaItemManager
 * @see BookManager
 * @see MovieManager
 */
public abstract class MediaManager {
    private static final Logger logger = LogManager.getLogger();

    // Statements
    private static final String UPDATE_MEDIA_STATEMENT = "UPDATE media SET title = ?, classification = ?, summary = ?, location = ?, publishing_date = ? WHERE id = ? LIMIT 1";
    private static final String CREATE_MEDIA_STATEMENT = "INSERT INTO media (title, classification, summary, location, publishing_date) VALUES (?, ?, ?, ?, ?)";
    private static final String DELETE_MEDIA_STATEMENT = "DELETE FROM media WHERE id = ?";

    protected final Database database;

    /**
     * Creates a new media manager instance.
     *
     * @param database A database instance.
     */
    public MediaManager(Database database) {
        this.database = database;
    }

    /**
     * Creates a media.
     *
     * @param media Media instance to create.
     * @throws Exception if the media already exists, or if a general database error occurs.
     * @implNote This is a blocking operation.
     */
    protected void createMedia(Media media) throws Exception {
        logger.info("Creating media...");
        Long id = database.insert(CREATE_MEDIA_STATEMENT)
                .configure(media.getTitle(), media.getClassification(), media.getSummary(), media.getLocation(), media.getPublishingDate())
                .executeQuery();
        media.setId(id);
    }

    /**
     * Updates an existing media.
     *
     * @param media Media instance to update.
     * @throws Exception if the media cannot be found, or if a general database error occurs.
     * @implNote This is a blocking operation.
     */
    protected void updateMedia(Media media) throws Exception {
        logger.info("Updating media with id " + media.getId() + "...");
        database.update(UPDATE_MEDIA_STATEMENT)
                .configure(media.getTitle(), media.getClassification(), media.getSummary(), media.getLocation(), media.getPublishingDate(), media.getId())
                .execute();
    }

    /**
     * Deletes an existing media.
     *
     * @param media Media instance to delete.
     * @throws Exception if the media cannot be found, or if a general database error occurs.
     * @implNote This is a blocking operation.
     */
    protected void deleteMedia(Media media) throws Exception {
        logger.info("Deleting media with id " + media.getId() + "...");
        database.delete(DELETE_MEDIA_STATEMENT)
                .configure(media.getId())
                .execute();
    }

}
