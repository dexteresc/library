package org.controllers;

import org.library.admin.EditModel;

public interface EditController extends Controller {
    default void setAdminController(AdminController adminController) {
    }

    void setEditModel(EditModel editModel);

    void configure();
}
