package com.PUB.dao;

import com.PUB.db.DB;
import com.PUB.model.Employee;
import javafx.collections.*;
import java.sql.*;

public class EmployeeDAO {

    public ObservableList<Employee> getAll() throws SQLException {
        ObservableList<Employee> list = FXCollections.observableArrayList();
        String sql = "SELECT e.*, d.name as dept_name FROM employees e " +
                     "LEFT JOIN departments d ON e.department_id = d.id ORDER BY e.emp_type, e.name";
        try (PreparedStatement ps = DB.get().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public ObservableList<Employee> getByType(String type) throws SQLException {
        ObservableList<Employee> list = FXCollections.observableArrayList();
        String sql = "SELECT e.*, d.name as dept_name FROM employees e " +
                     "LEFT JOIN departments d ON e.department_id = d.id " +
                     "WHERE e.emp_type = ? ORDER BY e.name";
        try (PreparedStatement ps = DB.get().prepareStatement(sql)) {
            ps.setString(1, type);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public ObservableList<Employee> search(String keyword) throws SQLException {
        ObservableList<Employee> list = FXCollections.observableArrayList();
        String sql = "SELECT e.*, d.name as dept_name FROM employees e " +
                     "LEFT JOIN departments d ON e.department_id = d.id " +
                     "WHERE e.name LIKE ? OR e.emp_id LIKE ? OR e.designation LIKE ? ORDER BY e.name";
        try (PreparedStatement ps = DB.get().prepareStatement(sql)) {
            String k = "%" + keyword + "%";
            ps.setString(1, k); ps.setString(2, k); ps.setString(3, k);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public void add(Employee e) throws SQLException {
        String sql = "INSERT INTO employees (emp_id,name,email,phone,nid,designation,emp_type," +
                     "department_id,status,join_date,salary,blood_group,address) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = DB.get().prepareStatement(sql)) {
            ps.setString(1, e.getEmpId());
            ps.setString(2, e.getName());
            ps.setString(3, e.getEmail());
            ps.setString(4, e.getPhone());
            ps.setString(5, e.getNid());
            ps.setString(6, e.getDesignation());
            ps.setString(7, e.getEmpType());
            ps.setInt(8, e.getDeptId());
            ps.setString(9, e.getStatus());
            ps.setString(10, e.getJoinDate());
            ps.setDouble(11, e.getSalary());
            ps.setString(12, e.getBlood());
            ps.setString(13, e.getAddress());
            ps.executeUpdate();
        }
    }

    public void update(Employee e) throws SQLException {
        String sql = "UPDATE employees SET emp_id=?,name=?,email=?,phone=?,nid=?,designation=?," +
                     "emp_type=?,department_id=?,status=?,join_date=?,salary=?,blood_group=?,address=? WHERE id=?";
        try (PreparedStatement ps = DB.get().prepareStatement(sql)) {
            ps.setString(1, e.getEmpId());
            ps.setString(2, e.getName());
            ps.setString(3, e.getEmail());
            ps.setString(4, e.getPhone());
            ps.setString(5, e.getNid());
            ps.setString(6, e.getDesignation());
            ps.setString(7, e.getEmpType());
            ps.setInt(8, e.getDeptId());
            ps.setString(9, e.getStatus());
            ps.setString(10, e.getJoinDate());
            ps.setDouble(11, e.getSalary());
            ps.setString(12, e.getBlood());
            ps.setString(13, e.getAddress());
            ps.setInt(14, e.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (PreparedStatement ps = DB.get().prepareStatement("DELETE FROM employees WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Employee map(ResultSet rs) throws SQLException {
        Employee e = new Employee();
        e.setId(rs.getInt("id"));
        e.setEmpId(rs.getString("emp_id"));
        e.setName(rs.getString("name"));
        e.setEmail(rs.getString("email"));
        e.setPhone(rs.getString("phone"));
        e.setNid(rs.getString("nid"));
        e.setDesignation(rs.getString("designation"));
        e.setEmpType(rs.getString("emp_type"));
        e.setDeptId(rs.getInt("department_id"));
        e.setDeptName(rs.getString("dept_name"));
        e.setStatus(rs.getString("status"));
        e.setJoinDate(rs.getString("join_date"));
        e.setSalary(rs.getDouble("salary"));
        e.setBlood(rs.getString("blood_group"));
        e.setAddress(rs.getString("address"));
        return e;
    }
}