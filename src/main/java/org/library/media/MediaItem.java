package org.library.media;

public class MediaItem {
    private Long id;
    private Media media;
    private MediaType mediaType;
    private Boolean currentlyOnLoan;
    private Status status;
    public MediaItem(Long id, Media media, MediaType mediaType, Boolean currentlyOnLoan, Status status) {
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
    }
}
