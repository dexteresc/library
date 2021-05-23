package org.library.media;

import java.util.Objects;

/**
 * Media item representing a physical (or digital) copy of a given work (media) in a specific format (media type).
 *
 * @see Media
 */
public class MediaItem {
    private Long id;
    private Media media;
    private MediaType mediaType;
    private Boolean currentlyOnLoan;
    private Status status;

    /**
     * Creates a new, empty, media item instance.
     */
    public MediaItem() {
        this.currentlyOnLoan = false;
    }

    /**
     * Creates a new media item instance.
     */
    protected MediaItem(Long id, Media media, MediaType mediaType, Boolean currentlyOnLoan, Status status) {
        this.id = id;
        this.media = media;
        this.mediaType = mediaType;
        this.currentlyOnLoan = currentlyOnLoan;
        this.status = status;
    }

    // The media item id also functions as barcode
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public Boolean getCurrentlyOnLoan() {
        return currentlyOnLoan;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MediaItem)) return false;

        MediaItem mediaItem = (MediaItem) o;

        return Objects.equals(id, mediaItem.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public enum Status {
        NONE(),
        ARCHIVED("archived"),
        MISSING("missing"),
        UNAVAILABLE("unavailable");

        private final String rawValue;

        Status() {
            this.rawValue = null;
        }

        Status(String rawValue) {
            this.rawValue = rawValue;
        }

        public String getRawValue() {
            return rawValue;
        }

        public static Status resolve(String rawValue) {
            if (rawValue == null) { return Status.NONE; }

            for (Status status : Status.values()) {
                if (status.rawValue == null) { continue; }

                if (status.rawValue.equals(rawValue)) {
                    return status;
                }
            }

            return Status.NONE;
        }
    }
}
