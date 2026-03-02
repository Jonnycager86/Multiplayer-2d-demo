package com.game;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;

import javax.swing.JPanel;



public class GamePanel extends JPanel implements Runnable{
    
    public InputHandler inputHandler;
    public ClientPlayer clientPlayer;
    public HashMap<Integer, WorldPlayer> worldPlayers;


    private Thread gameThread;
    final int FPS = 60;

    private GameClient gameClient;

  

    public GamePanel(){
        // Game state variables should be initialized here
         inputHandler = new InputHandler();
       //  player = new Player(inputHandler);
        // mouse = new Mouse(player);
         addKeyListener(inputHandler);
        // addMouseListener(mouse);
         //addMouseMotionListener(mouse);
         
    }

    public void initGameEntities(){ 
        clientPlayer = new ClientPlayer();
        worldPlayers = new HashMap<>();
    }

    public void initNetworkThread(){
        gameClient = new GameClient("127.0.0.1", 5000, clientPlayer, worldPlayers);
        gameClient.initThread();
    }


    public synchronized void initGameThread(){

        gameThread = new Thread(this);
        gameThread.start();

    }

    public void updateGame(){
       
    }

    @Override
    public void run() {
    
        double drawInterval = 1000000000 / FPS;
        long lastTime = System.nanoTime();
        double delta = 0;
        long currentTime;

        while(gameThread != null){

            currentTime = System.nanoTime();
            
            delta += (currentTime-lastTime) / drawInterval;

            lastTime = currentTime;

            if(delta >= 1){
            updateGame();
            repaint();
            delta--;
            }
        }
    }

    @Override
    public void paintComponent(Graphics g){ 

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

       clientPlayer.render(g2);

       for(WorldPlayer wp : worldPlayers.values()){
            wp.render(g2);
       }
        


        g2.dispose();
    }

}
