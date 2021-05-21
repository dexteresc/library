package org.controllers;

import javafx.fxml.Initializable;

public interface EditController extends Initializable {
    void setObjectToEdit(Object object);

    void configure();
}
