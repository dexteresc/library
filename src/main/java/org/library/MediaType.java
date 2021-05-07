package org.library;

public class MediaType {
    private Long id;
    private String name;
    private Integer loanPeriod;

    public MediaType(Long id, String name, Integer loanPeriod) {
        this.id = id;
        this.name = name;
        this.loanPeriod = loanPeriod;
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
