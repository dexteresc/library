package org.library.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.library.media.Media;
import org.library.media.MediaItem;
import org.library.media.MediaItemManager;
import org.library.media.MediaType;

import java.util.ArrayList;
import java.util.List;

public class MediaItemsEditModel extends EditModel {

    private MediaItemManager mediaItemManager;
    private Media media;
    private ObservableList<MediaItem> mediaItemList;
    private ObservableList<MediaType> mediaTypeList;

    public MediaItemsEditModel(MediaItemManager mediaItemManager) {
        super();

        this.mediaItemManager = mediaItemManager;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public ObservableList<MediaItem> getMediaItemList() throws Exception {
        if (this.mediaItemList == null) {
            this.mediaItemList = FXCollections.observableList(this.mediaItemManager.getAllMediaItems(media));
        }
        return this.mediaItemList;
    }

    public ObservableList<MediaType> getMediaTypeList() throws Exception {
        if (this.mediaTypeList == null) {
            this.mediaTypeList = FXCollections.observableList(this.mediaItemManager.getAllMediaTypes());
        }
        return this.mediaTypeList;
    }

    @Override
    public void save() throws Exception {
        this.mediaItemManager.updateMediaItems(this.media, this.mediaItemList);

        // Reload media items list
        this.reloadMediaItemList();
    }

    private void reloadMediaItemList() {
        for (int i = 0; i < this.mediaItemList.size(); i++) {
            this.mediaItemList.set(i, this.mediaItemList.get(i));
        }
    }

    @Override
    public void delete() {
        // Can't delete.
    }

}
