package org.example;

public enum Destination {
    HOME("primary"),
    LOGIN("login"),
    REGISTER("register"),
    MY_PAGES("mypages"),
    ADMIN("admin"),
    CUSTOM("");

    String resourceName;

    Destination(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceName() {
        return resourceName + ".fxml";
    }

    private void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public static Destination CUSTOM(String resourceName) {
        Destination destination = Destination.CUSTOM;
        destination.setResourceName(resourceName);
        return destination;
    }

    public static Destination resolve(String value) {
        for (Destination destination : Destination.values()) {
            if (destination.resourceName.equals(value)) {
                return destination;
            }
        }
        return Destination.CUSTOM(value);
    }
}
