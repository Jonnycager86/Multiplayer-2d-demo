package com.game;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class HostWaitingPanel extends JPanel {

    private final CardLayout cardLayout;
    private final JPanel container;
    private final GameWindow gameWindow;

    public HostWaitingPanel(CardLayout cardLayout, JPanel container, GameWindow gameWindow) {
        this.cardLayout = cardLayout;
        this.container = container;
        this.gameWindow = gameWindow;

        setBackground(new Color(20, 20, 20));
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(768, 576));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel title = new JLabel("HOSTING GAME", SwingConstants.CENTER);
        title.setFont(new Font("Agency FB", Font.BOLD, 52));
        title.setForeground(new Color(220, 30, 30));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 50, 0);
        add(title, gbc);

        // Server status
        JLabel serverStatus = new JLabel("✓  Server is running", SwingConstants.CENTER);
        serverStatus.setFont(new Font("Agency FB", Font.PLAIN, 22));
        serverStatus.setForeground(new Color(100, 220, 100));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 40, 0);
        add(serverStatus, gbc);

        // Instruction
        JLabel instruction = new JLabel("Send this IP to your friend:", SwingConstants.CENTER);
        instruction.setFont(new Font("Agency FB", Font.PLAIN, 20));
        instruction.setForeground(new Color(180, 180, 180));
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 10, 0);
        add(instruction, gbc);

        // Detect Tailscale IP
        String tailscaleIP = MenuPanel.getTailscaleIP();
        String displayIP = (tailscaleIP != null) ? tailscaleIP : "Tailscale not detected";
        Color ipColor = (tailscaleIP != null) ? new Color(100, 220, 100) : new Color(220, 100, 100);

        // IP display label (big and obvious)
        JLabel ipLabel = new JLabel(displayIP, SwingConstants.CENTER);
        ipLabel.setFont(new Font("Consolas", Font.BOLD, 36));
        ipLabel.setForeground(ipColor);
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 50, 10, 50);
        add(ipLabel, gbc);

        // Copy to clipboard button (only if IP was found)
        if (tailscaleIP != null) {
            String finalIP = tailscaleIP;
            JButton copyBtn = new JButton("📋  Copy IP");
            copyBtn.setFont(new Font("Agency FB", Font.PLAIN, 18));
            copyBtn.setBackground(new Color(50, 50, 50));
            copyBtn.setForeground(Color.WHITE);
            copyBtn.setFocusPainted(false);
            copyBtn.setBorderPainted(false);
            copyBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            copyBtn.addActionListener(e -> {
                Toolkit.getDefaultToolkit()
                    .getSystemClipboard()
                    .setContents(new StringSelection(finalIP), null);
                copyBtn.setText("✓  Copied!");
            });
            gbc.gridy = 4;
            gbc.insets = new Insets(0, 250, 30, 250);
            add(copyBtn, gbc);
        } else {
            JLabel hint = new JLabel("Make sure Tailscale is running", SwingConstants.CENTER);
            hint.setFont(new Font("Agency FB", Font.PLAIN, 16));
            hint.setForeground(new Color(150, 150, 150));
            gbc.gridy = 4;
            gbc.insets = new Insets(0, 0, 30, 0);
            add(hint, gbc);
        }

        // Port info (for reference)
        JLabel portInfo = new JLabel("Port: 5000  (your friend doesn't need to type this)", SwingConstants.CENTER);
        portInfo.setFont(new Font("Agency FB", Font.PLAIN, 15));
        portInfo.setForeground(new Color(100, 100, 100));
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 50, 0);
        add(portInfo, gbc);

        // Waiting label
        JLabel waitingLabel = new JLabel("Waiting for players to join...", SwingConstants.CENTER);
        waitingLabel.setFont(new Font("Agency FB", Font.PLAIN, 20));
        waitingLabel.setForeground(new Color(180, 180, 100));
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 30, 0);
        add(waitingLabel, gbc);

        // Play button (host joins their own game as client connecting to localhost)
        JButton playBtn = makeButton("PLAY");
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 150, 12, 150);
        add(playBtn, gbc);

        playBtn.addActionListener(e -> {
            // Host connects to their own server via localhost
            gameWindow.startGameAsClient("127.0.0.1");
        });

        // Back button
        JButton backBtn = makeButton("BACK TO MENU");
        backBtn.setBackground(new Color(60, 60, 60));
        gbc.gridy = 8;
        gbc.insets = new Insets(0, 200, 0, 200);
        add(backBtn, gbc);

        backBtn.addActionListener(e -> cardLayout.show(container, "MENU"));
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

