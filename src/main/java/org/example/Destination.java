package org.example;

/**
 * Destination
 *
 * A type-safe representation of FXML resources.
 */
public enum Destination {
    HOME("primary"),
    LOGIN("login"),
    REGISTER("register"),
    MY_PAGES("mypages"),
    ADMIN("admin"),
    NEW_LOAN("loan"),
    RETURNS("returns"),
    PREVIOUS("internal:previous"),
    CUSTOM("");

    private String resourceName;

    Destination(String resourceName) {
        this.resourceName = resourceName;
    }

    /**
     * @return The name of the underlying FXML resource.
     */
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

    /**
     * @param value The value that should be resolved to a destination.
     * @return An existing destination (if it exists) or a custom one.
     */
    public static Destination resolve(String value) {
        for (Destination destination : Destination.values()) {
            if (destination.resourceName.equals(value)) {
                return destination;
            }
        }
        return Destination.CUSTOM(value);
    }
}
