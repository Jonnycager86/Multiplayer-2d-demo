package com.game;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;


public class ServerPlayer{

    /*This class is where all server related physics processing is handled
    (this is the true player's position) */

    private Vector2D position;
    private Vector2D velocity;
    private AffineTransform transform;

   

    public ServerPlayer(){
    }


    public void renderUpdate(Graphics2D g2){


         // transform = g2.getTransform();

        //  transform.translate(position.x, position.y);
         // transform.rotate(rotation ,position.x, position.y);

          //g2.setTransform(transform);
            

         // g2.fillRect((int)position.x-40, (int)position.y-40, 120,120);
            

            
    }


    public Vector2D getPosition(){
        return position;
    }

    public Vector2D getVelocity(){
        return velocity;
    }

}
