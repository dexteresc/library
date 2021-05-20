package org.library.media;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Person {
    private Long id;
    private String givenName;
    private String familyName;

    public Person() {
    }

    public Person(Long id, String givenName, String familyName) {
        this.givenName = givenName;
        this.familyName = familyName;
    }

    public Person(ResultSet resultSet) throws SQLException {
        this(resultSet.getLong("id"), resultSet.getString("given_name"), resultSet.getString("family_name"));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }
}
