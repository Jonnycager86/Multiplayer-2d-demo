package com.game;

public class MovementSystem{ // for now this will only deal with players

    private final float speed = 6; 
    
     //should also have a unique player id or entity id

    public void update(MoveIntentPacket movepkt, ServerPlayer player){            //Needs work


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
