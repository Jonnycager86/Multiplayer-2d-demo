package com.game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ClientPlayer {  //This will be visual mirror of the server side player

    private int clientID;
    private float x;
    private float y;
    private BufferedImage playerImg;


    public ClientPlayer(int clientID, float x, float y){ // this constructor is not used rn
        this.clientID = clientID;
        this.x = x;
        this.y = y;
    }

    public ClientPlayer(){ // player should have its own method to load images and animations
        playerImg = ImageManager.loadImage("/images/PlayerRIGHT.png");
    }


    public void render(Graphics2D g2){

        
        if(playerImg != null){
        g2.drawImage(playerImg, (int)x, (int)y, 80,80, null);
        }
        else{
            System.out.println("Player Image is null");
            g2.fillRect((int)x, (int)y, 20,20);
        }
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


}
