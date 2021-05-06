module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires bcrypt;

    opens org.example to javafx.fxml;
    exports org.example;
    exports org.library;
}