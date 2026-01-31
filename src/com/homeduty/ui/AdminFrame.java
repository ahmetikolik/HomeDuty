package com.homeduty.ui;

import com.homeduty.app.AppSession;

import javax.swing.*;
import java.awt.*;

public class AdminFrame extends JFrame {

    public AdminFrame(AppSession session) {
        super("HomeDuty - Yönetici Paneli");
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

        tabs.addTab("Gorevler", new DutyPanel(session, true));
        tabs.addTab("Kisi Arama", new PersonSearchPanel(session));
        tabs.addTab("Raporlar", new ReportPanel(session));

        mainPanel.add(tabs, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private JPanel createHeaderPanel(AppSession session) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.PRIMARY_DARK);
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel("HomeDuty - Yonetici Paneli");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);

        JLabel lblUser = new JLabel(session.getDbUser() + " | ADMIN");
        lblUser.setFont(UITheme.FONT_NORMAL);
        lblUser.setForeground(UITheme.PRIMARY_LIGHT);

        JButton btnLogout = UITheme.createDangerButton("Çıkış");
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
