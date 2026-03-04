package com.game;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class JoinPanel extends JPanel {

    private final CardLayout cardLayout;
    private final JPanel container;
    private final GameWindow gameWindow;

    private JTextField ipField;
    private JLabel statusLabel;

    public JoinPanel(CardLayout cardLayout, JPanel container, GameWindow gameWindow) {
        this.cardLayout = cardLayout;
        this.container = container;
        this.gameWindow = gameWindow;

        setBackground(new Color(20, 20, 20));
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(800, 800));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel title = new JLabel("JOIN GAME", SwingConstants.CENTER);
        title.setFont(new Font("Agency FB", Font.BOLD, 52));
        title.setForeground(new Color(220, 30, 30));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 50, 0);
        add(title, gbc);

        // Instruction
        JLabel instruction = new JLabel("Enter the Host's Tailscale IP address:", SwingConstants.CENTER);
        instruction.setFont(new Font("Agency FB", Font.PLAIN, 20));
        instruction.setForeground(new Color(180, 180, 180));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 12, 0);
        add(instruction, gbc);

        // IP text field
        ipField = new JTextField("100.x.x.x");
        ipField.setFont(new Font("Consolas", Font.PLAIN, 28));
        ipField.setHorizontalAlignment(JTextField.CENTER);
        ipField.setBackground(new Color(40, 40, 40));
        ipField.setForeground(new Color(100, 200, 100));
        ipField.setCaretColor(Color.WHITE);
        ipField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 80), 2),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        ipField.setPreferredSize(new Dimension(400, 55));

        // Clear placeholder on first click
        ipField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (ipField.getText().equals("100.x.x.x")) {
                    ipField.setText("");
                }
            }
        });

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 100, 20, 100);
        add(ipField, gbc);

        // Status label (for errors / connecting message)
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Agency FB", Font.PLAIN, 18));
        statusLabel.setForeground(new Color(220, 100, 100));
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 20, 0);
        add(statusLabel, gbc);

        // Connect button
        JButton connectBtn = makeButton("CONNECT");
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 150, 12, 150);
        add(connectBtn, gbc);

        // Back button
        JButton backBtn = makeButton("BACK");
        backBtn.setBackground(new Color(60, 60, 60));
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 200, 0, 200);
        add(backBtn, gbc);

        // ---- Button Actions ----

        connectBtn.addActionListener(e -> {
            String ip = ipField.getText().trim();

            if (ip.isEmpty() || ip.equals("100.x.x.x")) {
                statusLabel.setText("Please enter a valid IP address.");
                return;
            }

            statusLabel.setForeground(new Color(100, 200, 100));
            statusLabel.setText("Connecting to " + ip + "...");
            connectBtn.setEnabled(false);

            // Connect on a background thread so UI doesn't freeze
            new Thread(() -> {
                try {
                    // Small delay so user sees "Connecting..." message
                    Thread.sleep(300);
                    SwingUtilities.invokeLater(() -> {
                        gameWindow.startGameAsClient(ip);
                    });
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }).start();
        });

        backBtn.addActionListener(e -> {
            statusLabel.setText(" ");
            ipField.setText("100.x.x.x");
            connectBtn.setEnabled(true);
            cardLayout.show(container, "MENU");
        });
    }

    private JButton makeButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Agency FB", Font.BOLD, 24));
        btn.setBackground(new Color(180, 20, 20));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(300, 55));
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            final Color normal = btn.getBackground();
            final Color hover = normal.brighter();
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(hover); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(normal); }
        });

        return btn;
    }
}

