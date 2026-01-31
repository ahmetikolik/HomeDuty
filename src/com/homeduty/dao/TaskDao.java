package com.homeduty.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TaskDao {
    private final Connection conn;

    public TaskDao(Connection conn) {
        this.conn = conn;
    }

    // Dropdown/Seçim için görev adlarını getirir
    public List<String> gorevAdlariniGetir() throws Exception {
        List<String> list = new ArrayList<>();
        String sql = "SELECT name FROM homeduty.task ORDER BY name";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("name"));
            }
        }
        return list;
    }
}
