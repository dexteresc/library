package org.library;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class LoanModel {

    private LoanManager loanManager;
    private ObservableList<MediaItem> mediaItemList = FXCollections.observableList(new ArrayList<>());
    private Customer customer;

    public LoanModel(LoanManager loanManager) {
        this.loanManager = loanManager;
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

    public void validate() throws Exception {
        // Check if loan has a customer
        if (this.customer == null) {
            throw new Exception("No customer.");
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

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void reset() {
        this.customer = null;
        this.mediaItemList.clear();
    }

    public List<MediaItem> getMediaItemList() {
        return mediaItemList;
    }
}
