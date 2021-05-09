package org.library;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerType {
    private String typeName;
    private int maxNumberOfLoans;

    public CustomerType(String typeName, int maxNumberOfLoans) {
        this.typeName = typeName;
        this.maxNumberOfLoans = maxNumberOfLoans;
    }

    CustomerType(ResultSet resultSet) throws SQLException {
        this(resultSet.getString("type_name"), resultSet.getInt("max_concurrent_loans"));
    }

    public String getTypeName() {
        return typeName;
    }

    public int getMaxNumberLoans() {
        return maxNumberOfLoans;
    }
}
