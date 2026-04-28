package com.game;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;



public class GamePanel extends JPanel implements Runnable{
    
    public InputHandler inputHandler;
    public Mouse mouse;
    public ClientPlayer clientPlayer;
    public ClientZombie zombie;
    public HashMap<Integer, WorldPlayer> worldPlayers;
    TileManager tileManager = new TileManager();
    private final AssaultRifle rifle = new AssaultRifle();
    private final List<Bullet> bullets = new ArrayList<>();


    private Thread gameThread;
    final int FPS = 60;

    private GameClient gameClient;

  

    public GamePanel(){
         inputHandler = new InputHandler();
       //  player = new Player(inputHandler);
         mouse = new Mouse();
         addKeyListener(inputHandler);
         addMouseListener(mouse);
         addMouseMotionListener(mouse);
         setCursor(UITheme.blankCursor());
         
    }

    public void initGameEntities(){ 
        clientPlayer = new ClientPlayer();
        worldPlayers = new HashMap<>();
        zombie = new ClientZombie();
    }

    public void initNetworkThread(String serverIP){
        gameClient = new GameClient(serverIP, clientPlayer, worldPlayers, zombie);
        gameClient.initThread();
    }


    public synchronized void initGameThread(){

        gameThread = new Thread(this);
        gameThread.start();

    }

    public void updateGame(){
       // fixed timestep for now (matches current render loop)
       float dt = 1f / FPS;

       if (clientPlayer == null) return;

       rifle.update(dt);

       boolean triggerHeld = Mouse.isLeftDown();
       List<Bullet> spawned = rifle.tryFire(triggerHeld, clientPlayer.getX(), clientPlayer.getY(), clientPlayer.getRotation());
       if (!spawned.isEmpty()) bullets.addAll(spawned);

       for (Iterator<Bullet> it = bullets.iterator(); it.hasNext();) {
            Bullet b = it.next();
            b.update(dt);
            if (b.isDead()) it.remove();
       }
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
        UITheme.enableAntialias(g2);

    if(tileManager != null){
        tileManager.render(g2, clientPlayer);
    }

    // bullets first so player + flash reads cleanly on top
    for (Bullet b : bullets) {
        b.render(g2, tileManager);
    }

    if(clientPlayer != null){
       clientPlayer.render(g2, tileManager);
    }

    // simple "light" / muzzle flash on top of the player
    rifle.renderMuzzleFlash(g2, tileManager);
        
    if(worldPlayers != null){
       for(WorldPlayer wp : worldPlayers.values()){
            wp.render(g2, tileManager);
       }
        }

    if(zombie != null){
        zombie.render(g2, tileManager);
    }

        renderHud(g2);
        renderCrosshair(g2);


        g2.dispose();
    }

    private void renderHud(Graphics2D g2) {
        int pad = 14;
        int w = getWidth();
        int h = getHeight();

        // Bottom-right minimal "status block" (placeholders)
        int boxW = 220;
        int boxH = 74;
        int x = w - boxW - pad;
        int y = h - boxH - pad;

        var oldComp = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.72f));
        g2.setColor(new Color(10, 10, 12));
        g2.fill(new RoundRectangle2D.Float(x, y, boxW, boxH, 14, 14));
        g2.setComposite(oldComp);

        g2.setColor(new Color(200, 24, 24, 160));
        g2.draw(new RoundRectangle2D.Float(x, y, boxW, boxH, 14, 14));

        g2.setFont(new Font("Consolas", Font.PLAIN, 14));
        g2.setColor(new Color(235, 235, 235));
        String ammoLine;
        if (rifle.isReloading()) {
            ammoLine = String.format("AMMO  %d/%d  (RELOADING)", rifle.getMagAmmo(), rifle.getMagSize());
        } else {
            ammoLine = String.format("AMMO  %d/%d  |  %d", rifle.getMagAmmo(), rifle.getMagSize(), rifle.getReserveAmmo());
        }
        g2.drawString(ammoLine, x + 14, y + 26);
        g2.drawString("GREN  --",    x + 14, y + 46);
        g2.drawString("HP    --%",   x + 14, y + 66);

        // Top-left small "objective" strip
        int stripW = 260;
        int stripH = 28;
        int sx = pad;
        int sy = pad;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.55f));
        g2.setColor(new Color(10, 10, 12));
        g2.fill(new RoundRectangle2D.Float(sx, sy, stripW, stripH, 12, 12));
        g2.setComposite(oldComp);
        g2.setColor(new Color(255, 255, 255, 170));
        g2.setFont(new Font("Agency FB", Font.PLAIN, 16));
        g2.drawString("CONTAINMENT: ACTIVE", sx + 12, sy + 19);
    }

    private void renderCrosshair(Graphics2D g2) {
        int mx = Mouse.getMouseX();
        int my = Mouse.getMouseY();
        if (mx < 0 || my < 0) return;

        Stroke oldStroke = g2.getStroke();
        g2.setStroke(UITheme.CROSSHAIR_STROKE);

        // outer glow
        g2.setColor(new Color(0, 0, 0, 170));
        g2.drawLine(mx - 14, my, mx - 6, my);
        g2.drawLine(mx + 6, my, mx + 14, my);
        g2.drawLine(mx, my - 14, mx, my - 6);
        g2.drawLine(mx, my + 6, mx, my + 14);
        g2.drawOval(mx - 10, my - 10, 20, 20);

        // inner accent
        g2.setColor(new Color(255, 60, 60, 210));
        g2.drawLine(mx - 13, my, mx - 7, my);
        g2.drawLine(mx + 7, my, mx + 13, my);
        g2.drawLine(mx, my - 13, mx, my - 7);
        g2.drawLine(mx, my + 7, mx, my + 13);
        g2.drawOval(mx - 9, my - 9, 18, 18);

        // center dot
        g2.setColor(new Color(255, 255, 255, 220));
        g2.fillOval(mx - 1, my - 1, 3, 3);

        g2.setStroke(oldStroke);
    }

}
