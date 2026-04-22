package com.game;

import java.awt.Graphics2D;

public class ClientZombie {

    private float x;
    private float y;

    public void render(Graphics2D g2, TileManager tilemgr){
        float zombieScreenX = x - tilemgr.cameraX;
        float zombieScreenY = y - tilemgr.cameraY;
        g2.fillRect((int)zombieScreenX, (int)zombieScreenY, 20, 20);
        
    }


    public void setX(float x){
        this.x = x;
    }

    public void setY(float y){
        this.y = y;
    }
    

}
