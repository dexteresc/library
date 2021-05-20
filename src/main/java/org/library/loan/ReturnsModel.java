package org.library.loan;

public class ReturnsModel {

    private LoanManager loanManager;

    public ReturnsModel(LoanManager loanManager) {
        this.loanManager = loanManager;
    }

    public String returnById(Long mediaItemId) throws Exception {
        Loan loan = loanManager.returnMediaItem(mediaItemId);

        return loan.getMetadata().getMediaTitle() + " was returned successfully.";
    }

}
