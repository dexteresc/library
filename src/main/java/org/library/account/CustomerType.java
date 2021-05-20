package org.library.account;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerType {
    private Long id;
    private String typeName;
    private int maxNumberOfLoans;

    public CustomerType(Long id, String typeName, int maxNumberOfLoans) {
        this.id = id;
        this.typeName = typeName;
        this.maxNumberOfLoans = maxNumberOfLoans;
    }

    public CustomerType(ResultSet resultSet) throws SQLException {
        this(resultSet.getLong("id"), resultSet.getString("type_name"), resultSet.getInt("max_concurrent_loans"));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getMaxNumberLoans() {
        return maxNumberOfLoans;
    }

    public void setMaxNumberOfLoans(int maxNumberOfLoans) {
        this.maxNumberOfLoans = maxNumberOfLoans;
    }

    @Override
    public String toString() {
        return this.getTypeName();
    }
}
