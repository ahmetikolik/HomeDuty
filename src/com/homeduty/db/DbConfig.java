package com.homeduty.db;

public class DbConfig {
    public static final String URL =
            "jdbc:postgresql://localhost:5432/homeduty?currentSchema=homeduty";

    // Uygulamanın DB'ye bağlandığı teknik kullanıcı:
    public static final String APP_DB_USER = "postgres";
    public static final String APP_DB_PASS = "12345";

    private DbConfig() {}
}
