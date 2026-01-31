package com.homeduty.app;

import java.sql.Connection;

public class AppSession {
    private final Connection connection;
    private final String dbUser;
    private final int familyId;
    private final String roleType;

    public AppSession(Connection connection, String dbUser, int familyId, String roleType) {
        this.connection = connection;
        this.dbUser = dbUser;
        this.familyId = familyId;
        this.roleType = roleType;
    }

    public Connection getConnection() { return connection; }
    public String getDbUser() { return dbUser; }
    public int getFamilyId() { return familyId; }
    public String getRoleType() { return roleType; }

    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(roleType);
    }
}
