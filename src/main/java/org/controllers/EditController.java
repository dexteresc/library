package org.controllers;

import javafx.fxml.Initializable;
import org.library.admin.EditModel;

public interface EditController extends Initializable {
    void setEditModel(EditModel editModel);

    void configure();
}
