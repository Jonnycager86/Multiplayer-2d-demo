package com.game;

import java.awt.geom.AffineTransform;


public class ServerPlayer{

    /*(this is the player's true position) */

    private int EntityID;
    private Vector2D position;
    private Vector2D velocity;
    private AffineTransform transform;
    private double rotation;

   

    public ServerPlayer(){
        position = new Vector2D(Constants.screen_width, Constants.screen_height);
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

    public double getRotation(){
        return this.rotation;
    }

    public void setRotation(double rotation){
        this.rotation = rotation;
    }

}
