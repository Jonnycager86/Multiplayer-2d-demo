package com.game;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class WorldPlayer extends ClientPlayer{



    public WorldPlayer(){
        super();
    }


    @Override
    public void render(Graphics2D g2, TileManager tilemgr){

        float playerScreenX = getX() - tilemgr.cameraX;
        float playerScreenY = getY() - tilemgr.cameraY;

        AffineTransform oldTransform = g2.getTransform(); 

        g2.rotate(getRotation(), playerScreenX+32, playerScreenY+32);

        if(getImage() != null){
        g2.drawImage(getImage(), (int)playerScreenX, (int)playerScreenY, 100, 100, null);
        }
        else{
            System.out.println("Player Image is null");
            g2.fillRect((int)getX(), (int)getY(), 20,20);
        }

        g2.setTransform(oldTransform);
    }

}
