package com.game;

import java.awt.geom.AffineTransform;


public class ServerPlayer{

    /*(this is the true player's position) */

    private int EntityID;
    private Vector2D position;
    private Vector2D velocity;
    private AffineTransform transform;

   

    public ServerPlayer(){
        position = new Vector2D(10, 10);
        velocity = new Vector2D(0,0);
    }


    public Vector2D getPosition(){
        return position;
    }

    public Vector2D getVelocity(){
        return velocity;
    }

    public void setPosition(float x, float y){
        this.position.x = x;
        this.position.y = y;
    }
    public void setPositionX(float x){
        this.position.x = x;
    }
    public void setPositionY(float y){
        this.position.y = y;
    }

    public void setVelocity(float x, float y){
        this.velocity.x = x;
        this.velocity.y = y;
    }

    public void setVelocityX(float x){
        this.velocity.x = x;
    }

    public void setVelocityY(float y){
        this.velocity.y = y;
    }

    public void setID(int ID){
        this.EntityID = ID;
    }

    public int getID(){
        return this.EntityID;
    }

}
