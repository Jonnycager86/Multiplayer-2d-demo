package com.game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.border.Border;

public final class UITheme {
    private UITheme() {}

    public static final Color BG_0 = new Color(10, 10, 12);
    public static final Color BG_1 = new Color(20, 18, 20);
    public static final Color PANEL = new Color(18, 18, 20, 210);
    public static final Color PANEL_EDGE = new Color(140, 20, 20, 120);
    public static final Color TEXT = new Color(225, 225, 225);
    public static final Color MUTED = new Color(165, 165, 165);
    public static final Color ACCENT = new Color(200, 24, 24);
    public static final Color ACCENT_HI = new Color(255, 60, 60);
    public static final Color OK = new Color(110, 220, 120);
    public static final Color WARN = new Color(220, 180, 90);
    public static final Color BAD = new Color(230, 90, 90);

    public static final Font TITLE_FONT = new Font("Agency FB", Font.BOLD, 72);
    public static final Font H1_FONT = new Font("Agency FB", Font.BOLD, 52);
    public static final Font H2_FONT = new Font("Agency FB", Font.PLAIN, 22);
    public static final Font BODY_FONT = new Font("Agency FB", Font.PLAIN, 20);
    public static final Font BUTTON_FONT = new Font("Agency FB", Font.BOLD, 24);
    public static final Font MONO_BIG = new Font("Consolas", Font.BOLD, 36);
    public static final Font MONO = new Font("Consolas", Font.PLAIN, 28);

    public static final BasicStroke CROSSHAIR_STROKE = new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    public static Border panelBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PANEL_EDGE, 2, true),
            BorderFactory.createEmptyBorder(18, 22, 18, 22)
        );
    }

    public static void styleButton(JButton btn, boolean secondary) {
        btn.setFont(BUTTON_FONT);
        btn.setBackground(secondary ? new Color(45, 45, 48) : new Color(140, 16, 16));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            final Color normal = btn.getBackground();
            final Color hover = secondary ? new Color(60, 60, 64) : new Color(175, 20, 20);
            @Override public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(hover); }
            @Override public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(normal); }
        });
    }

    public static void enableAntialias(java.awt.Graphics2D g2) {
        g2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
    }

    public static void setScaryChrome(JComponent c) {
        c.setOpaque(false);
    }

    public static Cursor blankCursor() {
        BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        return Toolkit.getDefaultToolkit().createCustomCursor(img, new java.awt.Point(0, 0), "blank");
    }
}

