package com.game;

import java.util.ArrayDeque;
import java.util.Queue;

public class MovementSystem{ // for now this will only deal with players

    //private ServerPlayer player;
     private MovePacket movepkt;
    private final float speed = 4; 
    
    Queue<MovePacket> inputQueue = new ArrayDeque<>();

    



     //should also have a unique player id or entity id

    public void update(MovePacket movepkt, ServerPlayer player){            //Needs work


        player.setVelocity(0,0);

        if(movepkt.up){
            player.setVelocityY(-1); // setting the intial direction of the vector
        }
        if(movepkt.down){
            player.setVelocityY(1);
        }
        if(movepkt.left){
            player.setVelocityX(-1);
        }
        if(movepkt.right){
            player.setVelocityX(1);
        }

        player.getVelocity().normalize();
        
        player.getVelocity().scale(speed);

        player.getPosition().add(player.getVelocity());


    }

}
