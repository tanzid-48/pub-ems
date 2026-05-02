package com.PUB.model;

import javafx.beans.property.*;

public class BusSchedule {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty busNo = new SimpleStringProperty();
    private final StringProperty routeName = new SimpleStringProperty();
    private final StringProperty fromLoc = new SimpleStringProperty();
    private final StringProperty toLoc = new SimpleStringProperty();
    private final StringProperty departTime = new SimpleStringProperty();
    private final StringProperty arriveTime = new SimpleStringProperty();
    private final StringProperty days = new SimpleStringProperty();
    private final IntegerProperty capacity = new SimpleIntegerProperty();
    private final StringProperty driverName = new SimpleStringProperty();
    private final StringProperty driverPhone = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty();

    public int getId() { return id.get(); }
    public void setId(int v) { id.set(v); }

    public String getBusNo() { return busNo.get(); }
    public void setBusNo(String v) { busNo.set(v); }
    public StringProperty busNoProperty() { return busNo; }

    public String getRouteName() { return routeName.get(); }
    public void setRouteName(String v) { routeName.set(v); }
    public StringProperty routeNameProperty() { return routeName; }

    public String getFromLoc() { return fromLoc.get(); }
    public void setFromLoc(String v) { fromLoc.set(v); }
    public StringProperty fromLocProperty() { return fromLoc; }

    public String getToLoc() { return toLoc.get(); }
    public void setToLoc(String v) { toLoc.set(v); }

    public String getDepartTime() { return departTime.get(); }
    public void setDepartTime(String v) { departTime.set(v); }
    public StringProperty departTimeProperty() { return departTime; }

    public String getArriveTime() { return arriveTime.get(); }
    public void setArriveTime(String v) { arriveTime.set(v); }
    public StringProperty arriveTimeProperty() { return arriveTime; }

    public String getDays() { return days.get(); }
    public void setDays(String v) { days.set(v); }
    public StringProperty daysProperty() { return days; }

    public int getCapacity() { return capacity.get(); }
    public void setCapacity(int v) { capacity.set(v); }
    public IntegerProperty capacityProperty() { return capacity; }

    public String getDriverName() { return driverName.get(); }
    public void setDriverName(String v) { driverName.set(v); }
    public StringProperty driverNameProperty() { return driverName; }

    public String getDriverPhone() { return driverPhone.get(); }
    public void setDriverPhone(String v) { driverPhone.set(v); }

    public String getStatus() { return status.get(); }
    public void setStatus(String v) { status.set(v); }
    public StringProperty statusProperty() { return status; }
}