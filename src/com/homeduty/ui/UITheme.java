package com.homeduty.ui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

/**
 * HomeDuty Modern Tema - Tüm UI bileşenlerini güzelleştiren yardımcı sınıf
 */
public class UITheme {

    // Renk Paleti - Modern Mavi Tonları
    public static final Color PRIMARY = new Color(41, 128, 185); // Ana mavi
    public static final Color PRIMARY_DARK = new Color(31, 97, 141); // Koyu mavi
    public static final Color PRIMARY_LIGHT = new Color(174, 214, 241); // Açık mavi
    public static final Color ACCENT = new Color(46, 204, 113); // Yeşil accent
    public static final Color WARNING = new Color(243, 156, 18); // Turuncu uyarı
    public static final Color DANGER = new Color(231, 76, 60); // Kırmızı tehlike
    public static final Color BG_DARK = new Color(44, 62, 80); // Koyu arka plan
    public static final Color BG_LIGHT = new Color(248, 249, 250); // Açık arka plan
    public static final Color TEXT_DARK = Color.BLACK; // TAM SIYAH - EN NET
    public static final Color TEXT_LIGHT = new Color(108, 117, 125); // Açık metin
    public static final Color BORDER_COLOR = new Color(222, 226, 230); // Kenar rengi

    // Fontlar
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 13);

    /**
     * Uygulamayı başlatırken çağır - Look and Feel ayarları
     */
    public static void applyTheme() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Global UI ayarları
            UIManager.put("Button.font", FONT_BUTTON);
            UIManager.put("Label.font", FONT_NORMAL);
            UIManager.put("TextField.font", FONT_NORMAL);
            UIManager.put("Table.font", FONT_NORMAL);
            UIManager.put("TableHeader.font", FONT_SUBTITLE);
            UIManager.put("TabbedPane.font", FONT_SUBTITLE);
            UIManager.put("ComboBox.font", FONT_NORMAL);
            UIManager.put("TitledBorder.font", FONT_SUBTITLE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Birincil stilize buton oluştur
     */
    public static JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text);
        styleButton(btn, PRIMARY, Color.WHITE);
        return btn;
    }

    /**
     * Başarı (yeşil) butonu oluştur
     */
    public static JButton createSuccessButton(String text) {
        JButton btn = new JButton(text);
        styleButton(btn, ACCENT, Color.WHITE);
        return btn;
    }

    /**
     * Tehlike (kırmızı) butonu oluştur
     */
    public static JButton createDangerButton(String text) {
        JButton btn = new JButton(text);
        styleButton(btn, DANGER, Color.WHITE);
        return btn;
    }

    /**
     * İkincil (gri) buton oluştur
     */
    public static JButton createSecondaryButton(String text) {
        JButton btn = new JButton(text);
        styleButton(btn, new Color(108, 117, 125), Color.WHITE);
        return btn;
    }

    private static void styleButton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(FONT_BUTTON);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Hover efekti
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            Color original = bg;

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(original.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(original);
            }
        });
    }

    /**
     * Stilize text field oluştur
     */
    public static JTextField createTextField(String placeholder) {
        JTextField field = new JTextField(placeholder);
        field.setFont(FONT_NORMAL);
        field.setForeground(TEXT_DARK); // KOYU YAZI
        field.setBackground(new Color(248, 249, 250)); // ACIK GRI ARKAPLAN
        field.setCaretColor(TEXT_DARK); // IMLEÇ RENGI
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        return field;
    }

    /**
     * Stilize password field oluştur
     */
    public static JPasswordField createPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField(placeholder);
        field.setFont(FONT_NORMAL);
        field.setForeground(TEXT_DARK); // KOYU YAZI
        field.setBackground(new Color(248, 249, 250)); // ACIK GRI ARKAPLAN
        field.setCaretColor(TEXT_DARK); // IMLEÇ RENGI
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        return field;
    }

    /**
     * Stilize combobox oluştur
     */
    public static <T> JComboBox<T> createComboBox() {
        JComboBox<T> combo = new JComboBox<>();
        combo.setFont(FONT_NORMAL);
        combo.setForeground(TEXT_DARK); // KOYU YAZI
        combo.setBackground(Color.WHITE); // BEYAZ ARKAPLAN
        return combo;
    }

    /**
     * Stilize label oluştur
     */
    public static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_NORMAL);
        label.setForeground(TEXT_DARK); // KOYU YAZI
        label.setOpaque(false); // SEFFAF ARKAPLAN - altindaki renk gorunsun
        return label;
    }

    /**
     * Başlık label oluştur
     */
    public static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_TITLE);
        label.setForeground(PRIMARY_DARK);
        return label;
    }

    /**
     * Stilize panel oluştur
     */
    public static JPanel createPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BG_LIGHT);
        return panel;
    }

    /**
     * Kart stili panel oluştur (gölgeli kenar)
     */
    public static JPanel createCardPanel(String title) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        if (title != null && !title.isEmpty()) {
            panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder(
                            BorderFactory.createLineBorder(PRIMARY, 2),
                            " " + title + " ",
                            TitledBorder.LEFT,
                            TitledBorder.TOP,
                            FONT_SUBTITLE,
                            PRIMARY_DARK),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        }
        return panel;
    }

    /**
     * Tabloyu stilize et
     */
    public static void styleTable(JTable table) {
        table.setFont(FONT_NORMAL);
        table.setRowHeight(35);
        table.setGridColor(BORDER_COLOR);
        table.setSelectionBackground(PRIMARY_LIGHT);
        table.setSelectionForeground(TEXT_DARK);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));

        // Header stilizasyonu
        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_SUBTITLE);
        header.setBackground(Color.WHITE); // BEYAZ ARKAPLAN - OKUNUR
        header.setForeground(Color.BLACK); // SIYAH YAZI - KESINLIKLE OKUNUR
        header.setOpaque(true); // ARKAPLAN RENGINI GOSTER - KRITIK!
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));

        // Satır renkleri (zebra)
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 249, 250));
                }
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return c;
            }
        });
    }

    /**
     * TabbedPane stilize et
     */
    public static void styleTabbedPane(JTabbedPane tabbedPane) {
        tabbedPane.setFont(FONT_SUBTITLE);
        tabbedPane.setBackground(BG_LIGHT);
    }

    /**
     * Scroll pane stilize et
     */
    public static JScrollPane createScrollPane(JComponent component) {
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        return scrollPane;
    }
}
