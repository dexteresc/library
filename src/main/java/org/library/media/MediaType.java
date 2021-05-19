package org.library.media;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MediaType {
    private Long id;
    private String name;
    private Integer loanPeriod;

    public MediaType(Long id, String name, Integer loanPeriod) {
        this.id = id;
        this.name = name;
        this.loanPeriod = loanPeriod;
    }

    public MediaType(ResultSet resultSet) throws SQLException {
        this(resultSet.getLong("id"), resultSet.getString("type_name"), resultSet.getInt("loan_period"));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getLoanPeriod() {
        return loanPeriod;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLoanPeriod(Integer loanPeriod) {
        this.loanPeriod = loanPeriod;
    }
}
