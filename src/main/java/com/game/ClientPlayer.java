package com.game;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class ClientPlayer {  //This will be visual mirror of the server side player

    private int clientID;
    private float x;
    private float y;
    private double rotation;
    private BufferedImage playerImg;
    TileManager tilemgr;

    
    private final int draw_width = 100;
    private final int draw_height = 100;


    public ClientPlayer(int clientID, float x, float y){ // this constructor is not used rn
        this.clientID = clientID;
        this.x = x;
        this.y = y;
    }

    public ClientPlayer(){ // player should have its own method to load images and animations
        playerImg = ImageManager.loadImage("/images/PlayerRIGHT.png");
    }




    public void render(Graphics2D g2, TileManager tilemgr){

        float playerScreenX = x - tilemgr.cameraX;
        float playerScreenY = y - tilemgr.cameraY;

        AffineTransform oldTransform = g2.getTransform(); 

        double angle = Math.atan2(Mouse.getMouseY() - playerScreenY, Mouse.getMouseX() - playerScreenX); //Already in radians

        g2.rotate(angle, playerScreenX+32, playerScreenY+32);

        if(playerImg != null){
        g2.drawImage(playerImg, (int)playerScreenX, (int)playerScreenY, draw_width, draw_height, null);
        }
        else{
            System.out.println("Player Image is null");
            g2.fillRect((int)x, (int)y, 20,20);
        }

        g2.setTransform(oldTransform);
    }


    public void setX(float x){
        this.x = x;
    }

    public void setY(float y){
        this.y = y;
    }

    public float getX(){
        return this.x;
    }

    public float getY(){
        return this.y;
    }

    public void setID(int id){
        this.clientID = id;
    }

    public int getID(){
        return this.clientID;
    }

    public void setRotation(double rotation){
        this.rotation = rotation;
    }

    public double getRotation(){
        return this.rotation;
    }


}
