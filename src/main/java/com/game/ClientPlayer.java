package com.game;

import java.awt.Graphics2D;

public class ClientPlayer {  //This will be visual mirror of the server side player

    private float x;
    private float y;


    public void render(Graphics2D g2){
        
        g2.fillRect((int)x, (int)y, 20,20);
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
}
