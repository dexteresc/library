package org.library;

public enum CustomerType {
    STUDENT(5),
    RESEARCHER(20),
    UNIVERSITY_EMPLOYEE(10),
    INDIVIDUAL(1),
    OTHER(0);

    private int maxNumberOfLoans;

    CustomerType(int maxNumberOfLoans) {
        this.maxNumberOfLoans = maxNumberOfLoans;
    }

    private CustomerType withMaxNumberOfLoans(int maxNumberOfLoans) {
        this.maxNumberOfLoans = maxNumberOfLoans;
        return this;
    }

    public static CustomerType OTHER(int maxNumberOfLoans) {
        return CustomerType.OTHER.withMaxNumberOfLoans(maxNumberOfLoans);
    }

    public int getMaxNumberLoans() {
        return maxNumberOfLoans;
    }
}
