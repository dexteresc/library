package org.library;

public class MediaItem {
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

    public Long getId() {
        return id;
    }

    public Media getMedia() {
        return media;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public Boolean getCurrentlyOnLoan() {
        return currentlyOnLoan;
    }

    public Status getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
