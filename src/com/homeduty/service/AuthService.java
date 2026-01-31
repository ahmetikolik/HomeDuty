package com.homeduty.service;

import com.homeduty.app.AppSession;
import com.homeduty.db.Db;

import java.sql.Connection;

public class AuthService {

    public AppSession girisYap(String dbUser, String dbPassword, int familyId) throws Exception {

        // DB bağlantısı (senin verdiğin kullanıcı/şifre ile)
        Connection conn = Db.connect(dbUser, dbPassword);

        // Rol belirleme: admin/user
        String roleType = dbUser.equalsIgnoreCase("homeduty_admin") ? "admin" : "user";

        return new AppSession(conn, dbUser, familyId, roleType);
    }
}
