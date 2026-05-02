package com.PUB.dao;

import com.PUB.db.DB;
import com.PUB.model.Department;
import javafx.collections.*;
import java.sql.*;

public class DepartmentDAO {

    public ObservableList<Department> getAll() throws SQLException {
        ObservableList<Department> list = FXCollections.observableArrayList();
        String sql = "SELECT d.*, COUNT(e.id) as emp_count FROM departments d " +
                     "LEFT JOIN employees e ON e.department_id = d.id GROUP BY d.id ORDER BY d.name";
        try (PreparedStatement ps = DB.get().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Department d = new Department();
                d.setId(rs.getInt("id"));
                d.setName(rs.getString("name"));
                d.setCode(rs.getString("code"));
                d.setFaculty(rs.getString("faculty"));
                d.setHeadName(rs.getString("head_name"));
                d.setEmpCount(rs.getInt("emp_count"));
                list.add(d);
            }
        }
        return list;
    }

    public void add(Department d) throws SQLException {
        String sql = "INSERT INTO departments (name, code, faculty, head_name) VALUES (?,?,?,?)";
        try (PreparedStatement ps = DB.get().prepareStatement(sql)) {
            ps.setString(1, d.getName());
            ps.setString(2, d.getCode());
            ps.setString(3, d.getFaculty());
            ps.setString(4, d.getHeadName());
            ps.executeUpdate();
        }
    }

    public void update(Department d) throws SQLException {
        String sql = "UPDATE departments SET name=?, code=?, faculty=?, head_name=? WHERE id=?";
        try (PreparedStatement ps = DB.get().prepareStatement(sql)) {
            ps.setString(1, d.getName());
            ps.setString(2, d.getCode());
            ps.setString(3, d.getFaculty());
            ps.setString(4, d.getHeadName());
            ps.setInt(5, d.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (PreparedStatement ps = DB.get().prepareStatement("DELETE FROM departments WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}