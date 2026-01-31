package com.homeduty.ui;

import com.homeduty.app.AppSession;

import javax.swing.*;
import java.awt.*;

public class UserFrame extends JFrame {

    public UserFrame(AppSession session) {
        super("HomeDuty - Kullanıcı Paneli");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);

        // Ana panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.BG_LIGHT);

        // Üst başlık barı
        JPanel headerPanel = createHeaderPanel(session);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Sekme paneli
        JTabbedPane tabs = new JTabbedPane();
        UITheme.styleTabbedPane(tabs);

        tabs.addTab("Gorevler", new DutyPanel(session, false));
        tabs.addTab("Kisi Arama", new PersonSearchPanel(session));
        tabs.addTab("Raporlar", new ReportPanel(session));

        mainPanel.add(tabs, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private JPanel createHeaderPanel(AppSession session) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.ACCENT);
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel("HomeDuty - Kullanici Paneli");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);

        JLabel lblUser = new JLabel(session.getDbUser() + " | USER");
        lblUser.setFont(UITheme.FONT_NORMAL);
        lblUser.setForeground(Color.WHITE);

        JButton btnLogout = UITheme.createSecondaryButton("Çıkış");
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        rightPanel.add(lblUser);
        rightPanel.add(Box.createHorizontalStrut(15));
        rightPanel.add(btnLogout);

        header.add(lblTitle, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);

        return header;
    }
}
