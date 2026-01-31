package com.homeduty.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportDao {
    private final Connection conn;

    public ReportDao(Connection conn) {
        this.conn = conn;
    }

    // UNION view: v_today_and_overdue_open
    public List<String[]> bugunVeGecikmisler() throws SQLException {
        List<String[]> rows = new ArrayList<>();
        String sql =
                "SELECT duty_id, bucket, due_date, full_name, task " +
                "FROM v_today_and_overdue_open " +
                "ORDER BY bucket, due_date";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rows.add(new String[] {
                        String.valueOf(rs.getInt("duty_id")),
                        rs.getString("bucket"),
                        String.valueOf(rs.getDate("due_date")),
                        rs.getString("full_name"),
                        rs.getString("task")
                });
            }
        }
        return rows;
    }

    // HAVING + aggregate view: v_high_performers
    public List<String[]> yuksekPerformans() throws SQLException {
        List<String[]> rows = new ArrayList<>();
        String sql =
                "SELECT person_id, full_name, sum_points, done_count " +
                "FROM v_high_performers " +
                "ORDER BY sum_points DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rows.add(new String[] {
                        String.valueOf(rs.getInt("person_id")),
                        rs.getString("full_name"),
                        String.valueOf(rs.getInt("sum_points")),
                        String.valueOf(rs.getInt("done_count"))
                });
            }
        }
        return rows;
    }
}
