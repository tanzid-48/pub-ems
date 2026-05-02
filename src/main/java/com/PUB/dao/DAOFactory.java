package com.PUB.dao;

public class DAOFactory {
    private static final EmployeeDAO   EMP   = new EmployeeDAO();
    private static final DepartmentDAO DEPT  = new DepartmentDAO();
    private static final BusDAO        BUS   = new BusDAO();
    private static final LeaveDAO      LEAVE = new LeaveDAO();
    private static final AttendanceDAO ATT   = new AttendanceDAO();

    public static EmployeeDAO   emp()   { return EMP; }
    public static DepartmentDAO dept()  { return DEPT; }
    public static BusDAO        bus()   { return BUS; }
    public static LeaveDAO      leave() { return LEAVE; }
    public static AttendanceDAO att()   { return ATT; }
}