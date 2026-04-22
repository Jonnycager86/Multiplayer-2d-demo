package com.game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ClientZombie {

    private float x;
    private float y;
    private BufferedImage zombieImg;
    
    public ClientZombie(){
        zombieImg = ImageManager.loadImage("/images/zombie1.png");
    }

    public void render(Graphics2D g2, TileManager tilemgr){
        float zombieScreenX = x - tilemgr.cameraX;
        float zombieScreenY = y - tilemgr.cameraY;
        g2.drawImage(zombieImg, (int)zombieScreenX, (int)zombieScreenY, 40, 40, null);
        
    }


    public void setX(float x){
        this.x = x;
    }

    public void setY(float y){
        this.y = y;
    }
    

}
