package com.homeduty.dao;

import com.homeduty.db.NoticeUtil;
import com.homeduty.model.DutyViewRow;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DutyDao {
    private final Connection conn;

    public DutyDao(Connection conn) {
        this.conn = conn;
    }

    // fn_assign_duty_by_name(p_family_id int, p_task_name text, p_due date) returns int
    public int gorevAtaIsmeGore(int familyId, String gorevAdi, Date bitisTarihi) throws SQLException {
        String sql = "SELECT fn_assign_duty_by_name(?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, familyId);
            ps.setString(2, gorevAdi);
            ps.setDate(3, bitisTarihi);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    // Trigger NOTICE mesajlarını yakalamak için Statement ile update
    public String goreviTamamlaVeBildirimAl(int dutyId) throws SQLException {
        String sql = "UPDATE duty SET status='DONE' WHERE duty_id=" + dutyId;
        try (Statement st = conn.createStatement()) {
            st.executeUpdate(sql);
            return NoticeUtil.collectNotices(st);
        }
    }

    public int gorevSil(int dutyId) throws SQLException {
        String sql = "DELETE FROM duty WHERE duty_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, dutyId);
            return ps.executeUpdate();
        }
    }

    // v_open_duties view
    public List<DutyViewRow> acikGorevleriListele() throws SQLException {
        List<DutyViewRow> list = new ArrayList<>();
        String sql =
                "SELECT duty_id, family, task, assigned_to, due_date, status " +
                "FROM v_open_duties " +
                "ORDER BY due_date";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new DutyViewRow(
                        rs.getInt("duty_id"),
                        rs.getString("family"),
                        rs.getString("task"),
                        rs.getString("assigned_to"),
                        rs.getDate("due_date"),
                        rs.getString("status")
                ));
            }
        }
        return list;
    }
}
