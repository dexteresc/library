package org.library.media;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

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
        this(resultSet.getLong("media_type_id"), resultSet.getString("type_name"), resultSet.getInt("loan_period"));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLoanPeriod() {
        return loanPeriod;
    }

    public void setLoanPeriod(Integer loanPeriod) {
        this.loanPeriod = loanPeriod;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MediaType)) return false;

        MediaType mediaType = (MediaType) o;

        return Objects.equals(id, mediaType.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
