package com.homeduty.ui;

import com.homeduty.app.AppSession;
import com.homeduty.service.AuthService;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final JTextField txtKullanici;
    private final JPasswordField txtSifre;
    private final JTextField txtFamilyId;

    public LoginFrame() {
        super("HomeDuty - Giris");
        UITheme.applyTheme();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 450);
        setLocationRelativeTo(null);
        setResizable(false);

        // Ana panel - gradient arka plan
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, UITheme.PRIMARY, 0, getHeight(), UITheme.PRIMARY_DARK);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        // Beyaz kart panel
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        cardPanel.setMaximumSize(new Dimension(400, 360));

        // Logo/Baslik - EMOJI KALDIRILDI
        JLabel lblLogo = new JLabel("HomeDuty");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblLogo.setForeground(UITheme.PRIMARY_DARK);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitle = new JLabel("Ev Ici Gorev Yonetim Sistemi");
        lblSubtitle.setFont(UITheme.FONT_SMALL);
        lblSubtitle.setForeground(UITheme.TEXT_LIGHT);
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Form alanlari - GORULUR HALE GETIRILDI
        txtKullanici = createVisibleTextField("homeduty_admin");
        txtSifre = createVisiblePasswordField("admin123");
        txtFamilyId = createVisibleTextField("3");

        // Butonlar
        JButton btnGiris = UITheme.createPrimaryButton("Giris Yap");
        btnGiris.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnGiris.addActionListener(e -> giris());

        // Panel'e ekle
        cardPanel.add(lblLogo);
        cardPanel.add(Box.createVerticalStrut(5));
        cardPanel.add(lblSubtitle);
        cardPanel.add(Box.createVerticalStrut(25));

        cardPanel.add(createFormRow("Veritabani Kullanici:", txtKullanici));
        cardPanel.add(Box.createVerticalStrut(12));
        cardPanel.add(createFormRow("Sifre:", txtSifre));
        cardPanel.add(Box.createVerticalStrut(12));
        cardPanel.add(createFormRow("Aile ID:", txtFamilyId));
        cardPanel.add(Box.createVerticalStrut(25));
        cardPanel.add(btnGiris);

        // Karti ortala
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(cardPanel);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Alt bilgi
        JLabel lblFooter = new JLabel("Veritabani Lab 2526 Projesi", SwingConstants.CENTER);
        lblFooter.setFont(UITheme.FONT_SMALL);
        lblFooter.setForeground(Color.WHITE);
        lblFooter.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(lblFooter, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private JTextField createVisibleTextField(String defaultValue) {
        JTextField field = new JTextField(defaultValue);
        field.setFont(UITheme.FONT_NORMAL);
        field.setForeground(UITheme.TEXT_DARK); // KOYU YAZI RENGI
        field.setBackground(new Color(248, 249, 250)); // ACIK GRI ARKAPLAN
        field.setCaretColor(UITheme.TEXT_DARK); // IMLEÇ RENGI
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 2),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        field.setPreferredSize(new Dimension(200, 38));
        return field;
    }

    private JPasswordField createVisiblePasswordField(String defaultValue) {
        JPasswordField field = new JPasswordField(defaultValue);
        field.setFont(UITheme.FONT_NORMAL);
        field.setForeground(UITheme.TEXT_DARK); // KOYU YAZI RENGI
        field.setBackground(new Color(248, 249, 250)); // ACIK GRI ARKAPLAN
        field.setCaretColor(UITheme.TEXT_DARK); // IMLEÇ RENGI
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 2),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        field.setPreferredSize(new Dimension(200, 38));
        return field;
    }

    private JPanel createFormRow(String labelText, JComponent field) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(340, 65));

        JLabel label = UITheme.createLabel(labelText);
        label.setPreferredSize(new Dimension(160, 38));
        label.setForeground(UITheme.TEXT_DARK); // LABEL RENGI KOYU

        row.add(label, BorderLayout.WEST);
        row.add(field, BorderLayout.CENTER);

        return row;
    }

    private void giris() {
        try {
            String u = txtKullanici.getText().trim();
            String pw = new String(txtSifre.getPassword());
            int familyId = Integer.parseInt(txtFamilyId.getText().trim());

            AuthService auth = new AuthService();
            AppSession session = auth.girisYap(u, pw, familyId);

            dispose();

            if (session.isAdmin())
                new AdminFrame(session).setVisible(true);
            else
                new UserFrame(session).setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Giris basarisiz!\n\n" + ex.getMessage(),
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
