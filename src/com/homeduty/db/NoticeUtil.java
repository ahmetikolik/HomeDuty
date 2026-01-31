package com.homeduty.db;

import java.sql.SQLWarning;
import java.sql.Statement;

public class NoticeUtil {

    // Trigger RAISE NOTICE mesajlarını (varsa) toplar
    public static String collectNotices(Statement stmt) {
        StringBuilder sb = new StringBuilder();
        try {
            SQLWarning w = stmt.getWarnings();
            while (w != null) {
                String msg = w.getMessage();
                if (msg != null && !msg.isBlank()) {
                    sb.append(msg).append("\n");
                }
                w = w.getNextWarning();
            }
            stmt.clearWarnings();
        } catch (Exception ignored) {}
        return sb.toString().trim();
    }

    private NoticeUtil() {}
}
	