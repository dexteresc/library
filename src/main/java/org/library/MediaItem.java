package org.library;

public class MediaItem {
    private Long id;
    private Media media;
    private MediaType mediaType;

    public MediaItem(Long id, Media media, MediaType mediaType) {
        this.id = id;
        this.media = media;
        this.mediaType = mediaType;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }
}
