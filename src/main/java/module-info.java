module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.naming;
    requires bcrypt;
    requires c3p0;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;

    exports org.library.account;
    exports org.library.admin;
    exports org.library.loan;
    exports org.library.media;
    exports org.library.search;
    exports org.library.security;
    exports org.library.util;

    opens org.controllers to javafx.fxml;
    exports org.controllers;
}