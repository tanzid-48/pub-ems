package com.PUB.model;

import javafx.beans.property.*;

public class Employee {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty empId = new SimpleStringProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty nameBn = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty phone = new SimpleStringProperty();
    private final StringProperty nid = new SimpleStringProperty();
    private final StringProperty designation = new SimpleStringProperty();
    private final StringProperty empType = new SimpleStringProperty();
    private final IntegerProperty deptId = new SimpleIntegerProperty();
    private final StringProperty deptName = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty();
    private final StringProperty joinDate = new SimpleStringProperty();
    private final DoubleProperty salary = new SimpleDoubleProperty();
    private final StringProperty blood = new SimpleStringProperty();
    private final StringProperty address = new SimpleStringProperty();

    public int getId() { return id.get(); }
    public void setId(int v) { id.set(v); }
    public IntegerProperty idProperty() { return id; }

    public String getEmpId() { return empId.get(); }
    public void setEmpId(String v) { empId.set(v); }
    public StringProperty empIdProperty() { return empId; }

    public String getName() { return name.get(); }
    public void setName(String v) { name.set(v); }
    public StringProperty nameProperty() { return name; }

    public String getNameBn() { return nameBn.get(); }
    public void setNameBn(String v) { nameBn.set(v); }

    public String getEmail() { return email.get(); }
    public void setEmail(String v) { email.set(v); }
    public StringProperty emailProperty() { return email; }

    public String getPhone() { return phone.get(); }
    public void setPhone(String v) { phone.set(v); }
    public StringProperty phoneProperty() { return phone; }

    public String getNid() { return nid.get(); }
    public void setNid(String v) { nid.set(v); }

    public String getDesignation() { return designation.get(); }
    public void setDesignation(String v) { designation.set(v); }
    public StringProperty designationProperty() { return designation; }

    public String getEmpType() { return empType.get(); }
    public void setEmpType(String v) { empType.set(v); }
    public StringProperty empTypeProperty() { return empType; }

    public int getDeptId() { return deptId.get(); }
    public void setDeptId(int v) { deptId.set(v); }

    public String getDeptName() { return deptName.get(); }
    public void setDeptName(String v) { deptName.set(v); }
    public StringProperty deptNameProperty() { return deptName; }

    public String getStatus() { return status.get(); }
    public void setStatus(String v) { status.set(v); }
    public StringProperty statusProperty() { return status; }

    public String getJoinDate() { return joinDate.get(); }
    public void setJoinDate(String v) { joinDate.set(v); }
    public StringProperty joinDateProperty() { return joinDate; }

    public double getSalary() { return salary.get(); }
    public void setSalary(double v) { salary.set(v); }
    public DoubleProperty salaryProperty() { return salary; }

    public String getBlood() { return blood.get(); }
    public void setBlood(String v) { blood.set(v); }

    public String getAddress() { return address.get(); }
    public void setAddress(String v) { address.set(v); }

    @Override
    public String toString() { return name.get(); }
}