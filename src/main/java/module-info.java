module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.naming;
    requires bcrypt;
    requires c3p0;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;

    opens org.example to javafx.fxml;
    exports org.example;
    exports org.library;
}