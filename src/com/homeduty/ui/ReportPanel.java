package com.homeduty.ui;

import com.homeduty.app.AppSession;
import com.homeduty.dao.ReportDao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReportPanel extends JPanel {
    private final ReportDao reportDao;

    private final DefaultTableModel modelUnion = new DefaultTableModel(
            new Object[] { "Görev ID", "Durum", "Tarih", "Kişi", "Görev" }, 0);
    private final DefaultTableModel modelHaving = new DefaultTableModel(
            new Object[] { "Kişi ID", "Ad Soyad", "Toplam Puan", "Tamamlanan Görev" }, 0);

    private final JTable tableUnion = new JTable(modelUnion);
    private final JTable tableHaving = new JTable(modelHaving);

    public ReportPanel(AppSession session) {
        this.reportDao = new ReportDao(session.getConnection());

        setLayout(new GridLayout(2, 1, 15, 15));
        setBackground(UITheme.BG_LIGHT);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(createUnionPanel());
        add(createHavingPanel());
    }

    private JPanel createUnionPanel() {
        JPanel panel = UITheme.createCardPanel("Bugun + Gecikmis Acik Gorevler (UNION View)");
        panel.setLayout(new BorderLayout(10, 10));

        // Buton paneli
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.setOpaque(false);

        JButton btnListele = UITheme.createPrimaryButton("Listele");
        btnListele.addActionListener(e -> unionYukle());
        btnPanel.add(btnListele);

        JLabel lblInfo = new JLabel("v_today_and_overdue_open view'ını kullanır");
        lblInfo.setFont(UITheme.FONT_SMALL);
        lblInfo.setForeground(UITheme.TEXT_LIGHT);
        btnPanel.add(Box.createHorizontalStrut(15));
        btnPanel.add(lblInfo);

        panel.add(btnPanel, BorderLayout.NORTH);

        UITheme.styleTable(tableUnion);
        panel.add(UITheme.createScrollPane(tableUnion), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createHavingPanel() {
        JPanel panel = UITheme.createCardPanel("Yuksek Performans (Aggregate + HAVING View)");
        panel.setLayout(new BorderLayout(10, 10));

        // Buton paneli
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.setOpaque(false);

        JButton btnListele = UITheme.createSuccessButton("Listele");
        btnListele.addActionListener(e -> havingYukle());
        btnPanel.add(btnListele);

        JLabel lblInfo = new JLabel("v_high_performers view'ını kullanır (SUM + COUNT + HAVING)");
        lblInfo.setFont(UITheme.FONT_SMALL);
        lblInfo.setForeground(UITheme.TEXT_LIGHT);
        btnPanel.add(Box.createHorizontalStrut(15));
        btnPanel.add(lblInfo);

        panel.add(btnPanel, BorderLayout.NORTH);

        UITheme.styleTable(tableHaving);
        panel.add(UITheme.createScrollPane(tableHaving), BorderLayout.CENTER);

        return panel;
    }

    private void unionYukle() {
        try {
            List<String[]> rows = reportDao.bugunVeGecikmisler();
            modelUnion.setRowCount(0);

            for (String[] r : rows) {
                // Durum text ile goster
                String durumText = r[1].equals("TODAY") ? "BUGUN" : "GECIKMIS";
                modelUnion.addRow(new Object[] { r[0], durumText, r[2], r[3], r[4] });
            }

            if (rows.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Bugun veya gecikmis acik gorev bulunmuyor.",
                        "Bilgi",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Rapor yüklenemedi!\n\n" + ex.getMessage(),
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void havingYukle() {
        try {
            List<String[]> rows = reportDao.yuksekPerformans();
            modelHaving.setRowCount(0);

            for (String[] r : rows) {
                modelHaving.addRow(new Object[] {
                        r[0],
                        r[1],
                        r[2] + " puan",
                        r[3] + " gorev"
                });
            }

            if (rows.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "20+ puan toplayan kisi bulunmuyor.\n(HAVING SUM >= 20)",
                        "Bilgi",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Rapor yüklenemedi!\n\n" + ex.getMessage(),
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
