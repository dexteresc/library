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
}
