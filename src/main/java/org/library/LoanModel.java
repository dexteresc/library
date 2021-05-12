package org.library;

import java.util.ArrayList;
import java.util.List;

public class LoanModel {

    private LoanManager loanManager;
    private List<Media> mediaList = new ArrayList<>();

    public LoanModel(LoanManager loanManager) {
        this.loanManager = loanManager;
    }

}
