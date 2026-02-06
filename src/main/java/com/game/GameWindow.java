package com.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class GameWindow extends JFrame {
    
     JPanel infoPanel;
     JLabel ammoLabel;
     JLabel zombieCountLabel;
     JLabel scoreLabel;
     JLabel gameOverLabel;

    private JButton startB;
	  private JButton pauseB;
	  private JButton endB;
    private JButton exitB;

    private JPanel mainPanel;
    private GamePanel gamePanel;
    
   
    private boolean gameStarted = false;


            public GameWindow() {
                // Basic window setup
                this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                this.setResizable(true);
                this.setTitle("IClone");
                this.setPreferredSize(new Dimension(800, 800));
                this.setLayout(new BorderLayout(0, 0)); // No gaps between components
                
                // Create main panel to hold everything
                mainPanel = new JPanel(new BorderLayout(0, 0));
                mainPanel.setPreferredSize(new Dimension(800, 800));
                
                // Create game panel with fixed size
                gamePanel = new GamePanel();
                gamePanel.setPreferredSize(new Dimension(700, 500)); 
                
               
                
                // Create info panel
                infoPanel = new JPanel(new GridLayout(1, 3, 10, 0));
                infoPanel.setPreferredSize(new Dimension(600, 50));
                infoPanel.setBackground(Color.LIGHT_GRAY);
                
                Font labelFont = new Font("Agency FB", Font.BOLD, 16);
                // Initialize labels
                zombieCountLabel = new JLabel("Zombies: 0", SwingConstants.CENTER);
                zombieCountLabel.setFont(labelFont);
                ammoLabel = new JLabel("Ammo: 10", SwingConstants.CENTER);
                ammoLabel.setFont(labelFont);
                scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
                scoreLabel.setFont(labelFont);
                
                // Add labels to info panel
                infoPanel.add(zombieCountLabel);
                infoPanel.add(ammoLabel);
                infoPanel.add(scoreLabel);
                
                // Create button panel
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
                buttonPanel.setPreferredSize(new Dimension(800, 50));
                buttonPanel.setBackground(Color.DARK_GRAY);
                
                // Initialize buttons
                startB = new JButton("Start Game");
                pauseB = new JButton("Pause Game");
                endB = new JButton("End Game");
                exitB = new JButton("Exit");
                
                
                // Set initial button states
                pauseB.setEnabled(false);
                endB.setEnabled(false);
                
                // Add buttons to button panel
                buttonPanel.add(startB);
                buttonPanel.add(pauseB);
                buttonPanel.add(endB);
                buttonPanel.add(exitB);
                
                // Create game over label (overlay)
              //  gameOverLabel = new JLabel("GAME OVER GG", SwingConstants.CENTER);
               // gameOverLabel.setFont(new Font("Arial", Font.BOLD, 48));
               // gameOverLabel.setForeground(Color.RED);
               // gameOverLabel.setVisible(false);
                
                // Add components to main panel
                mainPanel.add(infoPanel, BorderLayout.NORTH);
                mainPanel.add(gamePanel, BorderLayout.CENTER);
                mainPanel.add(buttonPanel, BorderLayout.SOUTH);
                
                // Add main panel to frame
                this.add(mainPanel);

                gamePanel.setFocusable(true);
                gamePanel.requestFocusInWindow();
                gamePanel.initGameThread();
                gamePanel.initGameEntities();
                gamePanel.initNetworkThread();
                
                // Final window setup
                this.setVisible(true);
                this.pack();
                this.setLocationRelativeTo(null); // Center on screen
                
                 
                
               
            }


}


