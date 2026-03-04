package com.game;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MenuPanel extends JPanel {

    private final CardLayout cardLayout;
    private final JPanel container;
    private final GameWindow gameWindow;

    public MenuPanel(CardLayout cardLayout, JPanel container, GameWindow gameWindow) {
        this.cardLayout = cardLayout;
        this.container = container;
        this.gameWindow = gameWindow;

        setBackground(new Color(20, 20, 20));
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(800, 800));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 0, 12, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Title
        JLabel title = new JLabel("ICLONE", SwingConstants.CENTER);
        title.setFont(new Font("Agency FB", Font.BOLD, 72));
        title.setForeground(new Color(220, 30, 30));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 40, 0);
        add(title, gbc);

        // Subtitle
        JLabel subtitle = new JLabel("IClone", SwingConstants.CENTER);
        subtitle.setFont(new Font("Agency FB", Font.PLAIN, 22));
        subtitle.setForeground(new Color(160, 160, 160));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 60, 0);
        add(subtitle, gbc);

        // Host button
        JButton hostBtn = makeButton("HOST GAME");
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 100, 10, 100);
        add(hostBtn, gbc);

        // Join button
        JButton joinBtn = makeButton("JOIN GAME");
        gbc.gridy = 3;
        add(joinBtn, gbc);

        // Exit button
        JButton exitBtn = makeButton("EXIT");
        exitBtn.setBackground(new Color(60, 60, 60));
        gbc.gridy = 4;
        gbc.insets = new Insets(30, 150, 10, 150);
        add(exitBtn, gbc);

        // ---- Button Actions ----

        hostBtn.addActionListener(e -> {
            // Start the server in the background
           // GameServer server = new GameServer();
           // server.initThread();

            // Show the host waiting screen
            gameWindow.showHostWaiting();
        });

        joinBtn.addActionListener(e -> {
            cardLayout.show(container, "JOIN");
        });

        exitBtn.addActionListener(e -> System.exit(0));
    }

    // ---- Utility: detect Tailscale IP (100.x.x.x) ----
    public static String getTailscaleIP() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                if (!ni.isUp() || ni.isLoopback()) continue;
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (addr instanceof Inet4Address && addr.getHostAddress().startsWith("100.")) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            final Color normal = btn.getBackground();
            final Color hover = normal.brighter();
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(hover); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(normal); }
        });

        return btn;
    }
}

