package com.game;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class GameWindow extends JFrame {

    // ---- Card names ----
    private static final String CARD_MENU    = "MENU";
    private static final String CARD_JOIN    = "JOIN";
    private static final String CARD_HOST    = "HOST";
    private static final String CARD_GAME    = "GAME";

    private final CardLayout cardLayout;
    private final JPanel container;

    // Game screen components (built once, reused)
    private GamePanel gamePanel;
    private JPanel infoPanel;
    private JLabel ammoLabel;
    private JLabel zombieCountLabel;
    private JLabel scoreLabel;

    public GameWindow() {
        // ---- Basic window setup ----
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("IClone");
        setPreferredSize(new Dimension(800, 800));

        // ---- Card layout container — fills the whole window ----
        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);
        container.setPreferredSize(new Dimension(800, 800));

        // ---- Build each screen ----
        MenuPanel menuPanel       = new MenuPanel(cardLayout, container, this);
        JoinPanel joinPanel       = new JoinPanel(cardLayout, container, this);
        HostWaitingPanel hostPanel = new HostWaitingPanel(cardLayout, container, this);
        JPanel gameScreen         = buildGameScreen();   // wraps GamePanel + HUD

        container.add(menuPanel,  CARD_MENU);
        container.add(joinPanel,  CARD_JOIN);
        container.add(hostPanel,  CARD_HOST);
        container.add(gameScreen, CARD_GAME);

        add(container);

        // ---- Show menu first ----
        cardLayout.show(container, CARD_MENU);

        // ---- Final window setup ----
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ---- Called by MenuPanel when "Host Game" is clicked ----
    public void showHostWaiting() {
        cardLayout.show(container, CARD_HOST);
    }

    // ---- Called by both JoinPanel (friend's IP) and HostWaitingPanel ("127.0.0.1") ----
    public void startGameAsClient(String serverIP) {
        SwingUtilities.invokeLater(() -> {
            // Init entities and connect using the given IP
            gamePanel.initGameEntities();
            gamePanel.initNetworkThread(serverIP);

            // Switch to game screen
            cardLayout.show(container, CARD_GAME);

            // Give keyboard focus to the game panel
            gamePanel.setFocusable(true);
            gamePanel.requestFocusInWindow();
        });
    }

    // ---- Builds the game screen: HUD on top, game panel in centre ----
    private JPanel buildGameScreen() {
        JPanel screen = new JPanel(new java.awt.BorderLayout(0, 0));
        screen.setBackground(Color.BLACK);

        // HUD / info bar
        infoPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        infoPanel.setPreferredSize(new Dimension(800, 40));
        infoPanel.setBackground(Color.DARK_GRAY);

        Font labelFont = new Font("Agency FB", Font.BOLD, 16);
        zombieCountLabel = new JLabel("Zombies: 0", SwingConstants.CENTER);
        zombieCountLabel.setFont(labelFont);
        zombieCountLabel.setForeground(Color.WHITE);

        ammoLabel = new JLabel("Ammo: 10", SwingConstants.CENTER);
        ammoLabel.setFont(labelFont);
        ammoLabel.setForeground(Color.WHITE);

        scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        scoreLabel.setFont(labelFont);
        scoreLabel.setForeground(Color.WHITE);

        infoPanel.add(zombieCountLabel);
        infoPanel.add(ammoLabel);
        infoPanel.add(scoreLabel);

        // Game panel (the actual game rendering + input)
        gamePanel = new GamePanel();
        gamePanel.setBackground(Color.BLACK);
        gamePanel.initGameThread();   // start the render loop now; network starts later

        screen.add(infoPanel, java.awt.BorderLayout.NORTH);
        screen.add(gamePanel, java.awt.BorderLayout.CENTER);

        return screen;
    }

    // ---- Convenience getters so other classes can update HUD labels if needed ----
    public JLabel getAmmoLabel()        { return ammoLabel; }
    public JLabel getZombieCountLabel() { return zombieCountLabel; }
    public JLabel getScoreLabel()       { return scoreLabel; }
}
