package org.library.media;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.library.util.Database;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Manager for media item instances.
 *
 * @see MediaItem
 * @see MediaManager
 */
public class MediaItemManager extends MediaManager {
    private static final Logger logger = LogManager.getLogger();

    // Media item statements
    private static final String SELECT_MEDIA_ITEMS_BY_MEDIA_ID_STATEMENT = "SELECT *, (SELECT COUNT(*) FROM loan WHERE loan.media_item_id = media_item.id AND returned_at IS NULL) AS currently_on_loan FROM media_item INNER JOIN media_type ON media_item.media_type_id = media_type.id WHERE media_id = ?";
    private static final String SELECT_AVAILABLE_MEDIA_ITEMS_BY_MEDIA_ID_STATEMENT = "SELECT *, (SELECT 0) AS currently_on_loan FROM media_item INNER JOIN media_type ON media_item.media_type_id = media_type.id WHERE media_id = ? AND status IS NULL AND media_type.loan_period > 0 AND media_item.id NOT IN (SELECT media_item_id FROM loan WHERE media_item_id = media_item.media_id)";
    private static final String SELECT_FIRST_AVAILABLE_MEDIA_ITEMS_BY_MEDIA_ID_STATEMENT = "SELECT *, (SELECT 0) AS currently_on_loan FROM media_item INNER JOIN media_type ON media_item.media_type_id = media_type.id WHERE media_id = ? AND status IS NULL AND media_type.loan_period > 0 AND media_item.id NOT IN (SELECT media_item_id FROM loan WHERE media_item_id = media_item.media_id) LIMIT 1";
    private static final String CREATE_MEDIA_ITEM_STATEMENT = "INSERT INTO media_item (media_id, media_type_id, status) VALUES (?, ?, ?)";
    private static final String UPDATE_MEDIA_ITEM_STATEMENT = "UPDATE media_item SET media_type_id = ?, status = ? WHERE id = ?";
    private static final String DELETE_MEDIA_ITEM_STATEMENT = "DELETE FROM media_item WHERE id = ?";

    private static final String SELECT_ALL_MEDIA_TYPES_STATEMENT = "SELECT id AS media_type_id, type_name, loan_period FROM media_type";
    private static final String CREATE_MEDIA_TYPE_STATEMENT = "INSERT INTO media_type (type_name, loan_period) VALUES (?, ?)";
    private static final String UPDATE_MEDIA_TYPE_STATEMENT = "UPDATE media_type SET type_name = ?, loan_period = ? WHERE id = ?";
    private static final String DELETE_MEDIA_TYPE_STATEMENT = "DELETE FROM media_type WHERE id = ?";

    /**
     * Creates a new media item manager instance.
     *
     * @param database A database instance.
     */
    public MediaItemManager(Database database) {
        super(database);
    }

    /**
     * Gets all media items for the provided media.
     *
     * @param media Media instance to get media items for.
     * @throws Exception if a general database error occurs.
     * @implNote This is a blocking operation.
     */
    public List<MediaItem> getAllMediaItems(Media media) throws Exception {
        logger.info("Getting all media items for media id " + media.getId() + "...");
        return database.select(SELECT_MEDIA_ITEMS_BY_MEDIA_ID_STATEMENT, MediaItem.class)
                .configure(media.getId())
                .fetchAll(resultSet -> new MediaItem(resultSet.getLong("id"), media, new MediaType(resultSet), resultSet.getInt("currently_on_loan") == 1, MediaItem.Status.resolve(resultSet.getString("status"))));
    }

    /**
     * Gets available media items for the provided media.
     *
     * @param media Media instance to get media items for.
     * @throws Exception if a general database error occurs.
     * @implNote This is a blocking operation.
     */
    public List<MediaItem> getAvailableMediaItems(Media media) throws Exception {
        logger.info("Getting all available media items for media with id " + media.getId() + "...");
        return database.select(SELECT_AVAILABLE_MEDIA_ITEMS_BY_MEDIA_ID_STATEMENT, MediaItem.class)
                .configure(media.getId())
                .fetchAll(resultSet -> new MediaItem(resultSet.getLong("id"), media, new MediaType(resultSet), resultSet.getInt("currently_on_loan") == 1, MediaItem.Status.NONE));
    }

    /**
     * Gets the first available media item for the provided media (if one exists).
     *
     * @param media Media instance to get media item for.
     * @throws Exception if a general database error occurs.
     * @implNote This is a blocking operation.
     */
    public MediaItem getFirstAvailableMediaItem(Media media) throws Exception {
        logger.info("Getting first available media item for media with id " + media.getId() + "...");
        return database.select(SELECT_FIRST_AVAILABLE_MEDIA_ITEMS_BY_MEDIA_ID_STATEMENT, MediaItem.class)
                .configure(media.getId())
                .fetch(resultSet -> new MediaItem(resultSet.getLong("id"), media, new MediaType(resultSet), resultSet.getInt("currently_on_loan") == 1, MediaItem.Status.NONE));
    }

    /**
     * Updates an existing media item.
     *
     * @param mediaItem Media item to update.
     * @throws Exception if a general database error occurs.
     * @implNote This is a blocking operation.
     */
    public void updateMediaItem(MediaItem mediaItem) throws Exception {
        logger.info("Updating media item with id " + mediaItem.getId() + "...");
        database.update(UPDATE_MEDIA_ITEM_STATEMENT)
                .configure(mediaItem.getMediaType().getId(), mediaItem.getStatus().getRawValue(), mediaItem.getId())
                .execute();
    }

    /**
     * Updates media items for the provided media.
     *
     * @param media      Media that the media items belong to.
     * @param mediaItems New list of media items for the media.
     * @throws Exception if a general database error occurs.
     * @implNote This is a blocking operation.
     */
    public void updateMediaItems(Media media, List<MediaItem> mediaItems) throws Exception {
        logger.info("Updating media items...");

        // Get media items before update
        List<MediaItem> mediaItemsBeforeUpdate = this.getAllMediaItems(media);

        // Filter media items to add
        List<MediaItem> mediaItemsToAdd = mediaItems.stream().filter(mediaItem -> !mediaItemsBeforeUpdate.contains(mediaItem)).collect(Collectors.toList());

        // Filter media items to update
        List<MediaItem> mediaItemsToUpdate = mediaItems.stream().filter(mediaItemsBeforeUpdate::contains).collect(Collectors.toList());

        // Filter media items to delete
        List<MediaItem> mediaItemsToDelete = mediaItemsBeforeUpdate.stream().filter(mediaItem -> !mediaItems.contains(mediaItem)).collect(Collectors.toList());

        // Add added media items
        for (MediaItem mediaItem : mediaItemsToAdd) {
            this.createMediaItem(mediaItem);
        }

        // Update modified media items
        for (MediaItem mediaItem : mediaItemsToUpdate) {
            this.updateMediaItem(mediaItem);
        }

        // Delete removed media items
        for (MediaItem mediaItem : mediaItemsToDelete) {
            this.deleteMediaItem(mediaItem);
        }
    }

    /**
     * Creates a media item.
     *
     * @param mediaItem Media item to create.
     * @throws Exception if a general database error occurs.
     * @implNote This is a blocking operation.
     */
    public void createMediaItem(MediaItem mediaItem) throws Exception {
        logger.info("Creating media item...");
        Long id = database.insert(CREATE_MEDIA_ITEM_STATEMENT)
                .configure(mediaItem.getMedia().getId(), mediaItem.getMediaType().getId(), mediaItem.getStatus().getRawValue())
                .executeQuery();
        mediaItem.setId(id);
    }

    /**
     * Deletes a media item.
     *
     * @param mediaItem Media item to delete.
     * @throws Exception if a general database error occurs.
     * @implNote This is a blocking operation.
     */
    public void deleteMediaItem(MediaItem mediaItem) throws Exception {
        logger.info("Deleting media item with id " + mediaItem.getId() + "...");
        database.delete(DELETE_MEDIA_ITEM_STATEMENT)
                .configure(mediaItem.getId())
                .execute();
    }

    // Media types

    /**
     * Gets all registered media types.
     *
     * @throws Exception if a general database error occurs.
     * @implNote This is a blocking operation.
     */
    public List<MediaType> getAllMediaTypes() throws Exception {
        logger.info("Getting all media types...");
        return database.select(SELECT_ALL_MEDIA_TYPES_STATEMENT, MediaType.class)
                .fetchAll(MediaType::new);
    }

    /**
     * Creates a media type.
     *
     * @param mediaType Media type to create.
     * @throws Exception if a general database error occurs.
     * @implNote This is a blocking operation.
     */
    public void createMediaType(MediaType mediaType) throws Exception {
        logger.info("Creating media type...");
        Long id = database.insert(CREATE_MEDIA_TYPE_STATEMENT)
                .configure(mediaType.getName(), mediaType.getLoanPeriod())
                .executeQuery();
        mediaType.setId(id);
    }

    /**
     * Updates an existing media type.
     *
     * @param mediaType Media type to update.
     * @throws Exception if a general database error occurs.
     * @implNote This is a blocking operation.
     */
    public void updateMediaType(MediaType mediaType) throws Exception {
        logger.info("Updating media type with id " + mediaType.getId() + "...");
        database.update(UPDATE_MEDIA_TYPE_STATEMENT)
                .configure(mediaType.getName(), mediaType.getLoanPeriod(), mediaType.getId())
                .execute();
    }

    /**
     * Deletes an existing media type.
     *
     * @param mediaType Media type to delete.
     * @throws Exception if a general database error occurs.
     * @implNote This is a blocking operation.
     */
    public void deleteMediaType(MediaType mediaType) throws Exception {
        logger.info("Deleting media type...");
        database.delete(DELETE_MEDIA_TYPE_STATEMENT)
                .configure(mediaType.getId())
                .executeQuery();
    }

}
