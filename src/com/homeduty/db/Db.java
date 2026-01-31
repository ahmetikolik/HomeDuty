package com.homeduty.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Db {

    // ✅ Bu metot şart: AuthService burayı çağırıyor
    public static Connection connect(String user, String password) throws SQLException {
        return DriverManager.getConnection(DbConfig.URL, user, password);
    }

    private Db() {}
}
