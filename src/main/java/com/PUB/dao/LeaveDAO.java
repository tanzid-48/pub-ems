package com.PUB.dao;

import com.PUB.db.DB;
import com.PUB.model.LeaveRequest;
import javafx.collections.*;
import java.sql.*;

public class LeaveDAO {

    public ObservableList<LeaveRequest> getAll() throws SQLException {
        ObservableList<LeaveRequest> list = FXCollections.observableArrayList();
        String sql = "SELECT l.*, e.name as emp_name FROM leave_requests l " +
                     "LEFT JOIN employees e ON l.employee_id = e.id ORDER BY l.applied_date DESC";
        try (PreparedStatement ps = DB.get().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                LeaveRequest l = new LeaveRequest();
                l.setId(rs.getInt("id"));
                l.setEmployeeId(rs.getInt("employee_id"));
                l.setEmpName(rs.getString("emp_name"));
                l.setLeaveType(rs.getString("leave_type"));
                l.setFromDate(rs.getString("from_date"));
                l.setToDate(rs.getString("to_date"));
                l.setDays(rs.getInt("days"));
                l.setReason(rs.getString("reason"));
                l.setStatus(rs.getString("status"));
                list.add(l);
            }
        }
        return list;
    }

    public void add(LeaveRequest l) throws SQLException {
        String sql = "INSERT INTO leave_requests (employee_id, leave_type, from_date, to_date, days, reason) " +
                     "VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = DB.get().prepareStatement(sql)) {
            ps.setInt(1, l.getEmployeeId());
            ps.setString(2, l.getLeaveType());
            ps.setString(3, l.getFromDate());
            ps.setString(4, l.getToDate());
            ps.setInt(5, l.getDays());
            ps.setString(6, l.getReason());
            ps.executeUpdate();
        }
    }

    public void updateStatus(int id, String status) throws SQLException {
        try (PreparedStatement ps = DB.get().prepareStatement(
                "UPDATE leave_requests SET status=? WHERE id=?")) {
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (PreparedStatement ps = DB.get().prepareStatement(
                "DELETE FROM leave_requests WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}