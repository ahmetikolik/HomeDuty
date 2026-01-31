package com.homeduty.dao;

import com.homeduty.model.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonDao {
    private final Connection conn;

    public PersonDao(Connection conn) {
        this.conn = conn;
    }

    // fn_search_person(p_name text)
    public List<Person> adaGoreAra(String adParcasi) throws SQLException {
        List<Person> list = new ArrayList<>();

        String sql =
        		  "SELECT person_id, full_name, email, role_type, total_points " +
        		  "FROM homeduty.fn_search_person(?) ORDER BY full_name";


        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, adParcasi);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Person(
                            rs.getInt("person_id"),
                            rs.getString("full_name"),
                            rs.getString("email"),
                            rs.getString("role_type"),
                            rs.getInt("total_points")
                    ));
                }
            }
        }

        return list;
    }
}
