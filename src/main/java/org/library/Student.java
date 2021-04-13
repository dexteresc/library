package org.library;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Student {
    String[] studentList = {"Jonathan", "Erik", "Axel","Jonathan", "Erik", "Axel","Jonathan", "Erik", "Axel","Jonathan", "Erik", "Axel","Jonathan", "Erik", "Axel","Jonathan", "Erik", "Axel","Jonathan", "Erik", "Axel","Jonathan", "Erik", "Axel","Jonathan", "Erik", "Axel",};

    public String[] getStudentList() {
        return studentList;
    }

    public ObservableList<String> something(){
        ObservableList<String> items = FXCollections.observableArrayList();
        items.addAll(getStudentList());
        return items;
    }
}
