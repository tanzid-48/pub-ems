package com.PUB.model;

import javafx.beans.property.*;

public class LeaveRequest {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty employeeId = new SimpleIntegerProperty();
    private final StringProperty empName = new SimpleStringProperty();
    private final StringProperty leaveType = new SimpleStringProperty();
    private final StringProperty fromDate = new SimpleStringProperty();
    private final StringProperty toDate = new SimpleStringProperty();
    private final IntegerProperty days = new SimpleIntegerProperty();
    private final StringProperty reason = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty();

    public int getId() { return id.get(); }
    public void setId(int v) { id.set(v); }

    public int getEmployeeId() { return employeeId.get(); }
    public void setEmployeeId(int v) { employeeId.set(v); }

    public String getEmpName() { return empName.get(); }
    public void setEmpName(String v) { empName.set(v); }
    public StringProperty empNameProperty() { return empName; }

    public String getLeaveType() { return leaveType.get(); }
    public void setLeaveType(String v) { leaveType.set(v); }
    public StringProperty leaveTypeProperty() { return leaveType; }

    public String getFromDate() { return fromDate.get(); }
    public void setFromDate(String v) { fromDate.set(v); }
    public StringProperty fromDateProperty() { return fromDate; }

    public String getToDate() { return toDate.get(); }
    public void setToDate(String v) { toDate.set(v); }
    public StringProperty toDateProperty() { return toDate; }

    public int getDays() { return days.get(); }
    public void setDays(int v) { days.set(v); }
    public IntegerProperty daysProperty() { return days; }

    public String getReason() { return reason.get(); }
    public void setReason(String v) { reason.set(v); }

    public String getStatus() { return status.get(); }
    public void setStatus(String v) { status.set(v); }
    public StringProperty statusProperty() { return status; }
}
