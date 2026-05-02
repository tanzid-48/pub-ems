package com.PUB.dao;

import com.PUB.db.DB;
import com.PUB.model.BusSchedule;
import javafx.collections.*;
import java.sql.*;

public class BusDAO {

    public ObservableList<BusSchedule> getAll() throws SQLException {
        ObservableList<BusSchedule> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM bus_schedule ORDER BY bus_no";
        try (PreparedStatement ps = DB.get().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                BusSchedule b = new BusSchedule();
                b.setId(rs.getInt("id"));
                b.setBusNo(rs.getString("bus_no"));
                b.setRouteName(rs.getString("route_name"));
                b.setFromLoc(rs.getString("from_loc"));
                b.setToLoc(rs.getString("to_loc"));
                b.setDepartTime(rs.getString("depart_time"));
                b.setArriveTime(rs.getString("arrive_time"));
                b.setDays(rs.getString("days"));
                b.setCapacity(rs.getInt("capacity"));
                b.setDriverName(rs.getString("driver_name"));
                b.setDriverPhone(rs.getString("driver_phone"));
                b.setStatus(rs.getString("status"));
                list.add(b);
            }
        }
        return list;
    }

    public void add(BusSchedule b) throws SQLException {
        String sql = "INSERT INTO bus_schedule (bus_no,route_name,from_loc,to_loc,depart_time," +
                     "arrive_time,days,capacity,driver_name,driver_phone,status) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = DB.get().prepareStatement(sql)) {
            ps.setString(1, b.getBusNo());
            ps.setString(2, b.getRouteName());
            ps.setString(3, b.getFromLoc());
            ps.setString(4, b.getToLoc());
            ps.setString(5, b.getDepartTime());
            ps.setString(6, b.getArriveTime());
            ps.setString(7, b.getDays());
            ps.setInt(8, b.getCapacity());
            ps.setString(9, b.getDriverName());
            ps.setString(10, b.getDriverPhone());
            ps.setString(11, b.getStatus());
            ps.executeUpdate();
        }
    }

    public void update(BusSchedule b) throws SQLException {
        String sql = "UPDATE bus_schedule SET bus_no=?,route_name=?,from_loc=?,to_loc=?,depart_time=?," +
                     "arrive_time=?,days=?,capacity=?,driver_name=?,driver_phone=?,status=? WHERE id=?";
        try (PreparedStatement ps = DB.get().prepareStatement(sql)) {
            ps.setString(1, b.getBusNo());
            ps.setString(2, b.getRouteName());
            ps.setString(3, b.getFromLoc());
            ps.setString(4, b.getToLoc());
            ps.setString(5, b.getDepartTime());
            ps.setString(6, b.getArriveTime());
            ps.setString(7, b.getDays());
            ps.setInt(8, b.getCapacity());
            ps.setString(9, b.getDriverName());
            ps.setString(10, b.getDriverPhone());
            ps.setString(11, b.getStatus());
            ps.setInt(12, b.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (PreparedStatement ps = DB.get().prepareStatement(
                "DELETE FROM bus_schedule WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}