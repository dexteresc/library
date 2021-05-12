package org.library;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class SearchModel {

    private MediaManager mediaManager;
    private ObservableList<Media> searchResultsList = FXCollections.observableList(new ArrayList<>());
    private String previousQuery;

    public SearchModel(MediaManager mediaManager) {
        this.mediaManager = mediaManager;
    }

    public void search(String query) {
        if (this.previousQuery != null && this.previousQuery.equalsIgnoreCase(query)) {
            return; // No need to run the same query again.
        }
        this.previousQuery = query;

        try {
            this.mediaManager.searchMedia(query).thenAcceptAsync(result -> Platform.runLater(() -> this.searchResultsList.setAll(result)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Media> getSearchResultsList() {
        return searchResultsList;
    }

    public String getPreviousQuery() {
        return previousQuery;
    }
}
