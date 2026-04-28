package com.game;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class ClientZombie {

    private float x;
    private float y;
    private final BufferedImage zombieImg;
    private double rotation;
    
    public ClientZombie(){
        zombieImg = ImageManager.loadImage("/images/zombie1.png");
    }

    public void render(Graphics2D g2, TileManager tilemgr){
        float zombieScreenX = x - tilemgr.cameraX;
        float zombieScreenY = y - tilemgr.cameraY;

        AffineTransform oldTransform = g2.getTransform();

        g2.rotate(rotation, zombieScreenX + 32, zombieScreenY + 32);
        g2.drawImage(zombieImg, (int)zombieScreenX, (int)zombieScreenY, 40, 40, null);

        g2.setTransform(oldTransform);
        
    }


    public void setX(float x){
        this.x = x;
    }

    public void setY(float y){
        this.y = y;
    }

    public void setRotation(double rotation){
        this.rotation = rotation;
    }

    public double getRotation(){
        return this.rotation;
    }
    

}
