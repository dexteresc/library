package org.controllers;

import org.library.admin.EditModel;

public interface EditController extends Controller {
    void setEditModel(EditModel editModel);

    void configure();
}
