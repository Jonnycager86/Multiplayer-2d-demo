package com.game;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;



public class GamePanel extends JPanel implements Runnable{
    
    public InputHandler inputHandler;
    ClientPlayer player;

    private Thread gameThread;
    private Thread serverThread;
    private Thread clientThread;
    final int FPS = 60;

    private GameClient gameClient;
    private GameServer gameServer;
  

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
        player = new ClientPlayer();
       
    }

    public void initNetworkThread(){
        gameServer = new GameServer();
        gameClient = new GameClient("127.0.0.1", 5000, player);
        gameServer.initThread();
        gameClient.initThread();
        System.out.println("Network started");
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

        // Draw game elements here
      //   g2.fillRect(50, 50, 100, 100);
       // if(player != null){
         //  player.drawPlayer(g2);

       player.render(g2);
        


        g2.dispose();
    }

}
