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

        setBackground(UITheme.BG_0);
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(768, 576));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 0, 12, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Background chrome
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

        GridBagConstraints inner = new GridBagConstraints();
        inner.insets = new Insets(10, 0, 10, 0);
        inner.fill = GridBagConstraints.HORIZONTAL;
        inner.gridx = 0;

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(UITheme.PANEL);
        content.setOpaque(true);
        content.setBorder(UITheme.panelBorder());

        // Title
        JLabel title = new JLabel("ICLONE", SwingConstants.CENTER);
        title.setFont(UITheme.TITLE_FONT);
        title.setForeground(UITheme.ACCENT_HI);
        inner.gridy = 0;
        inner.insets = new Insets(0, 0, 18, 0);
        content.add(title, inner);

        // Subtitle
        JLabel subtitle = new JLabel("IClone", SwingConstants.CENTER);
        subtitle.setFont(UITheme.H2_FONT);
        subtitle.setForeground(UITheme.MUTED);
        inner.gridy = 1;
        inner.insets = new Insets(0, 0, 22, 0);
        content.add(subtitle, inner);

        JLabel tag = new JLabel("BIOHAZARD PROTOCOL", SwingConstants.CENTER);
        tag.setFont(new Font("Agency FB", Font.BOLD, 18));
        tag.setForeground(new Color(255, 255, 255, 140));
        inner.gridy = 2;
        inner.insets = new Insets(0, 0, 28, 0);
        content.add(tag, inner);

        // Host button
        JButton hostBtn = makeButton("HOST GAME");
        inner.gridy = 3;
        inner.insets = new Insets(8, 80, 8, 80);
        content.add(hostBtn, inner);

        // Join button
        JButton joinBtn = makeButton("JOIN GAME");
        inner.gridy = 4;
        content.add(joinBtn, inner);

        // Exit button
        JButton exitBtn = makeButton("EXIT");
        UITheme.styleButton(exitBtn, true);
        inner.gridy = 5;
        inner.insets = new Insets(22, 130, 0, 130);
        content.add(exitBtn, inner);

        GridBagConstraints wrap = new GridBagConstraints();
        wrap.gridx = 0;
        wrap.gridy = 0;
        wrap.insets = new Insets(40, 70, 40, 70);
        wrap.fill = GridBagConstraints.BOTH;
        bg.add(content, wrap);

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
        UITheme.styleButton(btn, false);
        btn.setPreferredSize(new Dimension(300, 55));
        return btn;
    }
}

