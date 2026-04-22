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
        JLabel title = new JLabel("HOSTING GAME", SwingConstants.CENTER);
        title.setFont(UITheme.H1_FONT);
        title.setForeground(UITheme.ACCENT_HI);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 18, 0);
        content.add(title, gbc);

        // Server status
        JLabel serverStatus = new JLabel("✓  Server is running", SwingConstants.CENTER);
        serverStatus.setFont(new Font("Agency FB", Font.PLAIN, 22));
        serverStatus.setForeground(UITheme.OK);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 18, 0);
        content.add(serverStatus, gbc);

        // Instruction
        JLabel instruction = new JLabel("Send this IP to your friend:", SwingConstants.CENTER);
        instruction.setFont(new Font("Agency FB", Font.PLAIN, 20));
        instruction.setForeground(UITheme.MUTED);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 10, 0);
        content.add(instruction, gbc);

        // Detect Tailscale IP
        String tailscaleIP = MenuPanel.getTailscaleIP();
        String displayIP = (tailscaleIP != null) ? tailscaleIP : "Tailscale not detected";
        Color ipColor = (tailscaleIP != null) ? UITheme.OK : UITheme.BAD;

        // IP display label (big and obvious)
        JLabel ipLabel = new JLabel(displayIP, SwingConstants.CENTER);
        ipLabel.setFont(UITheme.MONO_BIG);
        ipLabel.setForeground(ipColor);
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 50, 10, 50);
        content.add(ipLabel, gbc);

        // Copy to clipboard button (only if IP was found)
        if (tailscaleIP != null) {
            String finalIP = tailscaleIP;
            JButton copyBtn = new JButton("📋  Copy IP");
            copyBtn.setFont(new Font("Agency FB", Font.PLAIN, 18));
            copyBtn.setBackground(new Color(40, 40, 44));
            copyBtn.setForeground(UITheme.TEXT);
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
            content.add(copyBtn, gbc);
        } else {
            JLabel hint = new JLabel("Make sure Tailscale is running", SwingConstants.CENTER);
            hint.setFont(new Font("Agency FB", Font.PLAIN, 16));
            hint.setForeground(new Color(255, 255, 255, 120));
            gbc.gridy = 4;
            gbc.insets = new Insets(0, 0, 30, 0);
            content.add(hint, gbc);
        }

        // Port info (for reference)
        JLabel portInfo = new JLabel("Port: 5000  (your friend doesn't need to type this)", SwingConstants.CENTER);
        portInfo.setFont(new Font("Agency FB", Font.PLAIN, 15));
        portInfo.setForeground(new Color(255, 255, 255, 90));
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 50, 0);
        content.add(portInfo, gbc);

        // Waiting label
        JLabel waitingLabel = new JLabel("Waiting for players to join...", SwingConstants.CENTER);
        waitingLabel.setFont(new Font("Agency FB", Font.PLAIN, 20));
        waitingLabel.setForeground(UITheme.WARN);
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 30, 0);
        content.add(waitingLabel, gbc);

        // Play button (host joins their own game as client connecting to localhost)
        JButton playBtn = makeButton("PLAY");
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 150, 12, 150);
        content.add(playBtn, gbc);

        playBtn.addActionListener(e -> {
            // Host connects to their own server via localhost
            gameWindow.startGameAsClient("127.0.0.1");
        });

        // Back button
        JButton backBtn = makeButton("BACK TO MENU");
        UITheme.styleButton(backBtn, true);
        gbc.gridy = 8;
        gbc.insets = new Insets(0, 200, 0, 200);
        content.add(backBtn, gbc);

        backBtn.addActionListener(e -> cardLayout.show(container, "MENU"));

        GridBagConstraints wrap = new GridBagConstraints();
        wrap.gridx = 0;
        wrap.gridy = 0;
        wrap.insets = new Insets(34, 70, 34, 70);
        wrap.fill = GridBagConstraints.BOTH;
        bg.add(content, wrap);
    }

    private JButton makeButton(String text) {
        JButton btn = new JButton(text);
        UITheme.styleButton(btn, false);
        btn.setPreferredSize(new Dimension(300, 55));
        return btn;
    }
}

