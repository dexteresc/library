package org.library;

import java.util.Date;
import java.sql.ResultSet;
import java.util.ArrayList;

public class LoanRepository extends Repository<Loan> {

    /**
     * Creates a new instance of repository.
     *
     * @param database A database instance used to submit queries to the database.
     */
    public LoanRepository(Database database) {
        super("lan", database);
    }

    private Loan loanFrom(ResultSet resultSet) throws Exception {
        return new Loan(
                resultSet.getInt("lanID"),
                resultSet.getDate("landatum"),
                resultSet.getDate("returdatum"),
                resultSet.getBoolean("returnerad"),
                resultSet.getInt("kund_kundID"),
                resultSet.getInt("artikel_artikelID")
        );
    }

    public ArrayList<Loan> getCustomerLoans(Customer customer) throws Exception {
        return this.findAll(
                "kund_kundID = ? AND returnerad = ?",
                "landatum",
                customer.getCustomerType().getMaxNumberLoans(),
                0,
                ps -> {
                    ps.setInt(1, customer.getID());
                    ps.setBoolean(2, false);
                },
                this::loanFrom
        );
    }

    public ArrayList<Loan> getArticleLoans(int articleID) throws Exception {
        return this.findAll(
                "artikel_artikelID = ? AND returnerad = ?",
                "landatum",
                100 /* TODO: Replace with number of copies */,
                0,
                ps -> {
                    ps.setInt(1, articleID);
                    ps.setBoolean(2, false);
                },
                this::loanFrom
        );
    }

    public ArrayList<Loan> getOverdueLoans() throws Exception {
        return this.findAll(
                "returdatum < ? AND returnerad = ?",
                "returdatum DESC",
                100 /* TODO: Replace with number of copies */,
                0,
                ps -> {
                    ps.setDate(1, new java.sql.Date(new Date().getTime()));
                    ps.setBoolean(2, false);
                },
                this::loanFrom
        );
    }

    public void create(Customer customer, Article article) {
        // TODO: Implement
    }
}
