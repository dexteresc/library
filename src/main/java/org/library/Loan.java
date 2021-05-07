package org.library;

import java.util.Date;

public class Loan {
    private Long id;
    private Date loanDate;
    private Date returnDate;
    private Boolean returned;
    private Long customerID;
    private Long articleID;

    public Loan(Long id, Date loanDate, Date returnDate, Boolean returned, Long customerID, Long articleID) {
        this.id = id;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
        this.returned = returned;
        this.customerID = customerID;
        this.articleID = articleID;
    }

    public Loan(Customer customer, Article article) {
        this.loanDate = new Date();
        this.returned = false;
        this.customerID = customer.getId();
        this.articleID = article.getId();
    }
}
