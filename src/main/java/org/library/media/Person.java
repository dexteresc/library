package org.library.media;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public abstract class Person {
    private Long id;
    private String givenName;
    private String familyName;

    public Person() {
    }

    protected Person(Long id, String givenName, String familyName) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;

        Person person = (Person) o;

        return Objects.equals(id, person.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
