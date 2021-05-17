package org.library;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoanModel {

    private LoanManager loanManager;
    private MediaManager mediaManager;
    private ObservableList<MediaItem> mediaItemList = FXCollections.observableList(new ArrayList<>());
    private Customer customer;

    public LoanModel(LoanManager loanManager, MediaManager mediaManager) {
        this.loanManager = loanManager;
        this.mediaManager = mediaManager;
    }

    public void addItem(MediaItem mediaItem) throws Exception {
        // Check that the media item is not already on loan
        if (mediaItem.getCurrentlyOnLoan()) {
            throw new Exception("Cannot borrow an item that is already on loan.");
        }

        // Check that the item can be loaned
        if (mediaItem.getMediaType().getLoanPeriod() < 1) {
            throw new Exception("Cannot borrow an item that has a loan period of less than 1 day.");
        }
        this.mediaItemList.add(mediaItem);
    }

    public void removeItem(MediaItem mediaItem) {
        this.mediaItemList.remove(mediaItem);
    }

    public void add(Media media) throws Exception {
        MediaItem mediaItem = this.mediaManager.getFirstAvailableMediaItem(media);
        this.addItem(mediaItem);
    }

    public void remove(Media media) {
        Optional<MediaItem> mediaItem = this.mediaItemList.stream().filter(item -> item.getMedia().getId().equals(media.getId())).findFirst();
        mediaItem.ifPresent(item -> this.mediaItemList.remove(item));
    }

    public void validate() throws Exception {
        // Check if loan has a customer
        if (this.customer == null) {
            throw new Exception("You need to be logged in to loan items.");
        }

        // Check if the new loan would exceed the number of items that the customer has loaned
        if (this.customer.getNumberOfActiveLoans() + this.mediaItemList.size() > this.customer.getCustomerType().getMaxNumberLoans()) {
            throw new Exception("Exceeds number of concurrent loans.");
        }
    }

    // TODO: Return receipt
    public void registerLoan() throws Exception {

        // Validate the loan
        this.validate();

        // Create loans
        List<Loan> loanList = loanManager.createLoan(this.customer.getId(), this.mediaItemList);
    }

    public Customer getCustomer() {
        return customer;
    }

    public boolean hasCustomer() {
        return this.customer != null;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void reset() {
        this.customer = null;
        this.mediaItemList.clear();
    }

    public ObservableList<MediaItem> getMediaItemList() {
        return mediaItemList;
    }
}
