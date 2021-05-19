package org.library.loan;

public class ReturnsModel {

    private LoanManager loanManager;

    public ReturnsModel(LoanManager loanManager) {
        this.loanManager = loanManager;
    }

    public void returnById(Long mediaItemId) throws Exception {
        loanManager.returnMediaItem(mediaItemId);

        // TODO: Indicate what media item was returned
    }

}
