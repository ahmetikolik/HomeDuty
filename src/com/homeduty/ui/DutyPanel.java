package com.homeduty.ui;

import com.homeduty.app.AppSession;
import com.homeduty.dao.DutyDao;
import com.homeduty.dao.TaskDao;
import com.homeduty.model.DutyViewRow;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class DutyPanel extends JPanel {
    private final AppSession session;
    private final boolean adminModu;

    private final DutyDao dutyDao;
    private final TaskDao taskDao;

    private final JComboBox<String> cmbGorevler;
    private final JTextField txtBitisTarihi;
    private final JTextField txtDutyId;

    private final DefaultTableModel model = new DefaultTableModel(
            new Object[] { "Gorev ID", "Aile", "Gorev", "Atanan Kisi", "Bitis Tarihi", "Durum" }, 0);
    private final JTable table = new JTable(model);

    public DutyPanel(AppSession session, boolean adminModu) {
        this.session = session;
        this.adminModu = adminModu;

        this.dutyDao = new DutyDao(session.getConnection());
        this.taskDao = new TaskDao(session.getConnection());

        // Initialize styled components
        cmbGorevler = UITheme.createComboBox();
        txtBitisTarihi = UITheme.createTextField(LocalDate.now().toString());
        txtDutyId = UITheme.createTextField("");

        setLayout(new BorderLayout(15, 15));
        setBackground(UITheme.BG_LIGHT);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(createTopPanel(), BorderLayout.NORTH);

        UITheme.styleTable(table);
        add(UITheme.createScrollPane(table), BorderLayout.CENTER);

        gorevleriYukle();
        tabloYenile();
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setOpaque(false);

        // INSERT Panel
        JPanel insertPanel = UITheme.createCardPanel("Gorev Ata (INSERT - Fonksiyon)");
        insertPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 1
        gbc.gridx = 0;
        gbc.gridy = 0;
        insertPanel.add(UITheme.createLabel("Gorev Sec:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        cmbGorevler.setPreferredSize(new Dimension(200, 35));
        insertPanel.add(cmbGorevler, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        insertPanel.add(UITheme.createLabel("Bitis Tarihi:"), gbc);
        gbc.gridx = 3;
        gbc.weightx = 1;
        txtBitisTarihi.setPreferredSize(new Dimension(150, 35));
        insertPanel.add(txtBitisTarihi, gbc);

        // Buttons
        gbc.gridx = 4;
        gbc.weightx = 0;
        JButton btnAta = UITheme.createSuccessButton("Gorev Ata");
        btnAta.setEnabled(adminModu);
        btnAta.addActionListener(e -> gorevAta());
        insertPanel.add(btnAta, gbc);

        gbc.gridx = 5;
        JButton btnYenile = UITheme.createSecondaryButton("Yenile");
        btnYenile.addActionListener(e -> tabloYenile());
        insertPanel.add(btnYenile, gbc);

        topPanel.add(insertPanel);
        topPanel.add(Box.createVerticalStrut(15));

        // UPDATE/DELETE Panel
        JPanel actionPanel = UITheme.createCardPanel("Guncelle / Sil");
        actionPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));

        actionPanel.add(UITheme.createLabel("Gorev ID:"));
        txtDutyId.setPreferredSize(new Dimension(100, 35));
        actionPanel.add(txtDutyId);

        JButton btnTamamla = UITheme.createPrimaryButton("DONE Yap (Trigger)");
        btnTamamla.addActionListener(e -> tamamla());
        actionPanel.add(btnTamamla);

        JButton btnSil = UITheme.createDangerButton("Sil");
        btnSil.setEnabled(adminModu);
        btnSil.addActionListener(e -> sil());
        actionPanel.add(btnSil);

        // Info label
        JLabel lblInfo = new JLabel();
        lblInfo.setFont(UITheme.FONT_SMALL);
        lblInfo.setForeground(UITheme.TEXT_LIGHT);
        if (adminModu) {
            lblInfo.setText("Admin: Tum islemler aktif");
            lblInfo.setForeground(UITheme.PRIMARY);
        } else {
            lblInfo.setText("User: Sadece DONE yapabilirsiniz");
            lblInfo.setForeground(UITheme.WARNING);
        }
        actionPanel.add(Box.createHorizontalStrut(20));
        actionPanel.add(lblInfo);

        topPanel.add(actionPanel);

        return topPanel;
    }

    private void gorevleriYukle() {
        try {
            cmbGorevler.removeAllItems();
            List<String> tasks = taskDao.gorevAdlariniGetir();
            for (String t : tasks)
                cmbGorevler.addItem(t);
            if (cmbGorevler.getItemCount() > 0)
                cmbGorevler.setSelectedIndex(0);
        } catch (Exception ex) {
            showError("Görev listesi yüklenemedi!", ex);
        }
    }

    private void gorevAta() {
        if (!adminModu) {
            showWarning("Yetkiniz yok!", "Görev atama sadece ADMIN içindir.");
            return;
        }

        try {
            String gorevAdi = (String) cmbGorevler.getSelectedItem();
            if (gorevAdi == null || gorevAdi.isBlank()) {
                showWarning("Uyarı", "Lütfen bir görev seçin.");
                return;
            }

            LocalDate ld = LocalDate.parse(txtBitisTarihi.getText().trim());
            int dutyId = dutyDao.gorevAtaIsmeGore(session.getFamilyId(), gorevAdi, Date.valueOf(ld));

            showSuccess("Başarılı!", "Görev atandı! Görev ID = " + dutyId);
            tabloYenile();

        } catch (Exception ex) {
            showError("Görev atama başarısız!", ex);
        }
    }

    private void tamamla() {
        try {
            int dutyId = Integer.parseInt(txtDutyId.getText().trim());
            String notices = dutyDao.goreviTamamlaVeBildirimAl(dutyId);

            String msg = "✅ Görev DONE yapıldı!";
            if (notices != null && !notices.isBlank()) {
                msg += "\n\n🔔 Trigger Mesajları:\n" + notices;
            }

            showSuccess("Başarılı!", msg);
            tabloYenile();

        } catch (Exception ex) {
            showError("Güncelleme başarısız!", ex);
        }
    }

    private void sil() {
        if (!adminModu) {
            showWarning("Yetkiniz yok!", "Silme sadece ADMIN içindir.");
            return;
        }

        try {
            int dutyId = Integer.parseInt(txtDutyId.getText().trim());

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Görev #" + dutyId + " silinecek. Emin misiniz?",
                    "Silme Onayı",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                int silinen = dutyDao.gorevSil(dutyId);
                showSuccess("Silindi!", "Silinen kayıt sayısı: " + silinen);
                tabloYenile();
            }

        } catch (Exception ex) {
            showError("Silme başarısız!", ex);
        }
    }

    private void tabloYenile() {
        try {
            List<DutyViewRow> rows = dutyDao.acikGorevleriListele();
            model.setRowCount(0);
            for (DutyViewRow r : rows) {
                model.addRow(new Object[] {
                        r.dutyId, r.family, r.task, r.assignedTo, r.dueDate, r.status
                });
            }
        } catch (Exception ex) {
            showError("Listeleme başarısız!", ex);
        }
    }

    private void showError(String title, Exception ex) {
        JOptionPane.showMessageDialog(this, title + "\n\n" + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
    }

    private void showWarning(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.WARNING_MESSAGE);
    }

    private void showSuccess(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
