package com.PUB.dao;

import com.PUB.db.DB;
import java.sql.*;

public class AttendanceDAO {

    public void saveOrUpdate(int empId, String date, String status, String checkIn, String checkOut) throws SQLException {
        String sql = "INSERT INTO attendance (employee_id, att_date, status, check_in, check_out) " +
                     "VALUES (?,?,?,?,?) ON DUPLICATE KEY UPDATE status=?, check_in=?, check_out=?";
        try (PreparedStatement ps = DB.get().prepareStatement(sql)) {
            ps.setInt(1, empId);
            ps.setString(2, date);
            ps.setString(3, status);
            ps.setString(4, checkIn);
            ps.setString(5, checkOut);
            ps.setString(6, status);
            ps.setString(7, checkIn);
            ps.setString(8, checkOut);
            ps.executeUpdate();
        }
    }

    public int[] getTodayStats(String date) throws SQLException {
        int[] stats = {0, 0, 0, 0};
        String sql = "SELECT status, COUNT(*) as cnt FROM attendance WHERE att_date=? GROUP BY status";
        try (PreparedStatement ps = DB.get().prepareStatement(sql)) {
            ps.setString(1, date);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int cnt = rs.getInt("cnt");
                    switch (rs.getString("status")) {
                        case "PRESENT"  -> stats[0] += cnt;
                        case "ABSENT"   -> stats[1] += cnt;
                        case "LATE"     -> stats[2] += cnt;
                        case "HALF_DAY" -> stats[3] += cnt;
                    }
                }
            }
        }
        return stats;
    }
}