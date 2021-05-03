package org.library;

import java.util.Date;

public class Loan {
    private int id;
    private Date loanDate;
    private Date returnDate;
    private boolean returned;
    private int customerID;
    private int articleID;

    public Loan(int id, Date loanDate, Date returnDate, boolean returned, int customerID, int articleID) {
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
        this.customerID = customer.getID();
        this.articleID = article.getID();
    }
}
