package com.PUB.model;

import javafx.beans.property.*;

public class Department {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty code = new SimpleStringProperty();
    private final StringProperty faculty = new SimpleStringProperty();
    private final StringProperty headName = new SimpleStringProperty();
    private final IntegerProperty empCount = new SimpleIntegerProperty();

    public int getId() { return id.get(); }
    public void setId(int v) { id.set(v); }

    public String getName() { return name.get(); }
    public void setName(String v) { name.set(v); }
    public StringProperty nameProperty() { return name; }

    public String getCode() { return code.get(); }
    public void setCode(String v) { code.set(v); }
    public StringProperty codeProperty() { return code; }

    public String getFaculty() { return faculty.get(); }
    public void setFaculty(String v) { faculty.set(v); }
    public StringProperty facultyProperty() { return faculty; }

    public String getHeadName() { return headName.get(); }
    public void setHeadName(String v) { headName.set(v); }
    public StringProperty headNameProperty() { return headName; }

    public int getEmpCount() { return empCount.get(); }
    public void setEmpCount(int v) { empCount.set(v); }
    public IntegerProperty empCountProperty() { return empCount; }

    @Override
    public String toString() { return name.get(); }
}