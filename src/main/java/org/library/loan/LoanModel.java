package org.library.loan;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.library.account.Customer;
import org.library.media.Media;
import org.library.media.MediaItem;
import org.library.media.MediaItemManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoanModel {

    private final LoanManager loanManager;
    private final MediaItemManager mediaItemManager;
    private final ObservableList<MediaItem> mediaItemList = FXCollections.observableList(new ArrayList<>());
    private Customer customer;

    public LoanModel(LoanManager loanManager, MediaItemManager mediaItemManager) {
        this.loanManager = loanManager;
        this.mediaItemManager = mediaItemManager;
    }

    public void addItem(MediaItem mediaItem) throws Exception {
        // Check that the media item is not already on loan
        if (mediaItem.getCurrentlyOnLoan()) {
            throw new Exception("Cannot borrow an item that is already on loan.");
        }

        if (mediaItemList.stream().anyMatch(item -> item.getId().equals(mediaItem.getId()))) {
            throw new Exception("Cannot borrow the same media item twice.");
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
        MediaItem mediaItem = this.mediaItemManager.getFirstAvailableMediaItem(media);
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

        // Update number of active loans for customer in case it has become desynchronized
        this.customer.setNumberOfActiveLoans(
                this.loanManager.getNumberOfActiveCustomerLoans(this.customer.getId())
        );

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
        List<Loan> loanList = loanManager.createLoan(this.customer, this.mediaItemList);
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public boolean hasCustomer() {
        return this.customer != null;
    }

    public void reset() {
        this.customer = null;
        this.mediaItemList.clear();
    }

    public ObservableList<MediaItem> getMediaItemList() {
        return mediaItemList;
    }
}
