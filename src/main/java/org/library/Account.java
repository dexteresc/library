package org.library;


public class Account {

    private int id;
    private String givenName;
    private String familyName;
    private String email;

    /**
     * Create a new account instance.
     * @param givenName account holder's given name.
     * @param familyName account holder's family name.
     * @param email account holder's email.
     */
    public Account(String givenName, String familyName, String email) {
        this.givenName = givenName;
        this.familyName = familyName;
        this.email = email;
    }

    /**
     * Create a new account instance with id.
     * @param id account id.
     * @param givenName account holder's given name.
     * @param familyName account holder's family name.
     * @param email account holder's email.
     */
    public Account(int id, String givenName, String familyName, String email) {
        this(givenName, familyName, email);
        this.id = id;
    }

    public int getID() {
        return id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
