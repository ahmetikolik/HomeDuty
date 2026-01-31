package com.homeduty.ui;

import com.homeduty.app.AppSession;
import com.homeduty.dao.PersonDao;
import com.homeduty.model.Person;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PersonSearchPanel extends JPanel {
    private final PersonDao personDao;

    private final JTextField txtArama;
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[] { "Kişi ID", "Ad Soyad", "E-posta", "Rol", "Toplam Puan" }, 0);
    private final JTable table = new JTable(model);

    public PersonSearchPanel(AppSession session) {
        this.personDao = new PersonDao(session.getConnection());

        setLayout(new BorderLayout(15, 15));
        setBackground(UITheme.BG_LIGHT);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Üst arama paneli
        JPanel searchPanel = UITheme.createCardPanel("Kisi Arama (fn_search_person + INDEX)");
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));

        searchPanel.add(UITheme.createLabel("Ad/Soyad ile Ara:"));

        txtArama = UITheme.createTextField("Ali");
        txtArama.setPreferredSize(new Dimension(250, 35));
        searchPanel.add(txtArama);

        JButton btnAra = UITheme.createPrimaryButton("Ara");
        btnAra.addActionListener(e -> ara());
        searchPanel.add(btnAra);

        // Enter tuşu ile arama
        txtArama.addActionListener(e -> ara());

        add(searchPanel, BorderLayout.NORTH);

        // Tablo
        UITheme.styleTable(table);
        add(UITheme.createScrollPane(table), BorderLayout.CENTER);

        // Bilgi paneli
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setOpaque(false);
        JLabel lblInfo = new JLabel(
                "Bu arama idx_person_fullname INDEX'i kullanir. ILIKE ile case-insensitive arama yapar.");
        lblInfo.setFont(UITheme.FONT_SMALL);
        lblInfo.setForeground(UITheme.TEXT_LIGHT);
        infoPanel.add(lblInfo);
        add(infoPanel, BorderLayout.SOUTH);
    }

    private void ara() {
        try {
            String searchTerm = txtArama.getText().trim();
            List<Person> list = personDao.adaGoreAra(searchTerm);
            model.setRowCount(0);

            for (Person p : list) {
                String rolText = p.roleType.equals("admin") ? "ADMIN" : "USER";
                model.addRow(new Object[] {
                        p.personId,
                        p.fullName,
                        p.email,
                        rolText,
                        p.totalPoints + " puan"
                });
            }

            if (list.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "\"" + searchTerm + "\" için sonuç bulunamadı.",
                        "Sonuç Yok",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Arama başarısız!\n\n" + ex.getMessage(),
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
