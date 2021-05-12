package org.library;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class SearchModel {

    private MediaManager mediaManager;
    private ObservableList<Media> searchResultsList = FXCollections.observableList(new ArrayList<>());

    public SearchModel(MediaManager mediaManager) {
        this.mediaManager = mediaManager;
    }

    public void search(String query) {
        try {
            this.searchResultsList.setAll(this.mediaManager.searchMedia(query));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Media> getSearchResultsList() {
        return searchResultsList;
    }

}
