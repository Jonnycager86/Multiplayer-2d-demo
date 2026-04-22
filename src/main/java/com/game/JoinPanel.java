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

        setBackground(UITheme.BG_0);
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(768, 576));

        setOpaque(false);
        BackgroundPanel bg = new BackgroundPanel();
        bg.setLayout(new GridBagLayout());
        bg.setPreferredSize(new Dimension(768, 576));
        GridBagConstraints bgc = new GridBagConstraints();
        bgc.gridx = 0;
        bgc.gridy = 0;
        bgc.fill = GridBagConstraints.BOTH;
        bgc.weightx = 1.0;
        bgc.weighty = 1.0;
        add(bg, bgc);

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(UITheme.PANEL);
        content.setOpaque(true);
        content.setBorder(UITheme.panelBorder());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel title = new JLabel("JOIN GAME", SwingConstants.CENTER);
        title.setFont(UITheme.H1_FONT);
        title.setForeground(UITheme.ACCENT_HI);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 18, 0);
        content.add(title, gbc);

        // Instruction
        JLabel instruction = new JLabel("Enter the Host's Tailscale IP address:", SwingConstants.CENTER);
        instruction.setFont(UITheme.BODY_FONT);
        instruction.setForeground(UITheme.MUTED);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 12, 0);
        content.add(instruction, gbc);

        // IP text field
        ipField = new JTextField("100.x.x.x");
        ipField.setFont(UITheme.MONO);
        ipField.setHorizontalAlignment(JTextField.CENTER);
        ipField.setBackground(new Color(40, 40, 40));
        ipField.setForeground(new Color(100, 200, 100));
        ipField.setCaretColor(Color.WHITE);
        ipField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(90, 20, 20, 140), 2),
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
        content.add(ipField, gbc);

        // Status label (for errors / connecting message)
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Agency FB", Font.PLAIN, 18));
        statusLabel.setForeground(UITheme.BAD);
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 20, 0);
        content.add(statusLabel, gbc);

        // Connect button
        JButton connectBtn = makeButton("CONNECT");
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 150, 12, 150);
        content.add(connectBtn, gbc);

        // Back button
        JButton backBtn = makeButton("BACK");
        UITheme.styleButton(backBtn, true);
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 200, 0, 200);
        content.add(backBtn, gbc);

        GridBagConstraints wrap = new GridBagConstraints();
        wrap.gridx = 0;
        wrap.gridy = 0;
        wrap.insets = new Insets(44, 70, 44, 70);
        wrap.fill = GridBagConstraints.BOTH;
        bg.add(content, wrap);

        // ---- Button Actions ----

        connectBtn.addActionListener(e -> {
            String ip = ipField.getText().trim();

            if (ip.isEmpty() || ip.equals("100.x.x.x")) {
                statusLabel.setText("Please enter a valid IP address.");
                return;
            }

            statusLabel.setForeground(UITheme.OK);
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
        UITheme.styleButton(btn, false);
        btn.setPreferredSize(new Dimension(300, 55));
        return btn;
    }
}

