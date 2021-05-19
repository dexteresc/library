package org.library.media;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.library.util.Database;

import java.util.List;

public class MediaItemManager extends MediaManager {
    private static final Logger logger = LogManager.getLogger();

    // Media item statements
    private static final String SELECT_MEDIA_ITEMS_BY_MEDIA_ID_STATEMENT = "SELECT *, (SELECT COUNT(*) FROM loan WHERE loan.media_item_id = id AND returned_at IS NULL) AS currently_on_loan FROM media_item INNER JOIN media_type ON media_item.media_type_id = media_type.id WHERE media_id = ?";
    private static final String SELECT_AVAILABLE_MEDIA_ITEMS_BY_MEDIA_ID_STATEMENT = "SELECT *, (SELECT 0) AS currently_on_loan FROM media_item INNER JOIN media_type ON media_item.media_type_id = media_type.id WHERE media_id = ? AND status IS NULL AND media_type.loan_period > 0";
    private static final String SELECT_FIRST_AVAILABLE_MEDIA_ITEMS_BY_MEDIA_ID_STATEMENT = "SELECT *, (SELECT 0) AS currently_on_loan FROM media_item INNER JOIN media_type ON media_item.media_type_id = media_type.id WHERE media_id = ? AND status IS NULL LIMIT 1";
    private static final String CREATE_MEDIA_ITEM_STATEMENT = "INSERT INTO media_item (media_id, media_type_id, status) VALUES (?, ?, ?)";
    private static final String UPDATE_MEDIA_ITEM_STATEMENT = "UPDATE media_item SET media_type_id = ?, status = ? WHERE id = ?";

    public MediaItemManager(Database database) {
        super(database);
    }

    public List<MediaItem> getAllMediaItems(Media media) throws Exception {
        logger.info("Getting all media items...");
        return database.select(SELECT_MEDIA_ITEMS_BY_MEDIA_ID_STATEMENT, MediaItem.class)
                .configure(media.getId())
                .fetchAll(resultSet -> new MediaItem(resultSet.getLong("id"), media, new MediaType(resultSet), resultSet.getInt("currently_on_loan") == 1, MediaItem.Status.valueOf(resultSet.getString("status"))));
    }

    public List<MediaItem> getAvailableMediaItems(Media media) throws Exception {
        logger.info("Getting all available media items...");
        return database.select(SELECT_AVAILABLE_MEDIA_ITEMS_BY_MEDIA_ID_STATEMENT, MediaItem.class)
                .configure(media.getId())
                .fetchAll(resultSet -> new MediaItem(resultSet.getLong("id"), media, new MediaType(resultSet), resultSet.getInt("currently_on_loan") == 1, MediaItem.Status.NONE));
    }

    public MediaItem getFirstAvailableMediaItem(Media media) throws Exception {
        logger.info("Getting first available media item...");
        return database.select(SELECT_FIRST_AVAILABLE_MEDIA_ITEMS_BY_MEDIA_ID_STATEMENT, MediaItem.class)
                .configure(media.getId())
                .fetch(resultSet -> new MediaItem(resultSet.getLong("id"), media, new MediaType(resultSet), resultSet.getInt("currently_on_loan") == 1, MediaItem.Status.NONE));
    }

    public void updateMediaItem(MediaItem mediaItem) throws Exception {
        logger.info("Updating media item...");
        database.update(UPDATE_MEDIA_ITEM_STATEMENT)
                .configure(mediaItem.getMediaType().getId(), mediaItem.getStatus().getRawValue(), mediaItem.getId())
                .execute();
    }

    public void createMediaItem(MediaItem mediaItem) throws Exception {
        logger.info("Creating media item...");
        Long id = database.insert(CREATE_MEDIA_ITEM_STATEMENT)
                .configure(mediaItem.getMedia().getId(), mediaItem.getMediaType().getId(), mediaItem.getStatus())
                .executeQuery();
        mediaItem.setId(id);
    }
}
