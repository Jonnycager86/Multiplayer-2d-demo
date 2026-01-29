package com.game;

public class MovementSystem{

    private ServerPlayer player;
    private MovePacket movepkt;
    private final float speed = 4;    



    public MovementSystem(MovePacket movepkt, ServerPlayer player){ //should also have a unique player id or entity id
        this.movepkt = movepkt;
        this.player = player;
    }

    public void update(){

        player.getVelocity().x = 0;
        player.getVelocity().y = 0;

        if(movepkt.up){
            player.getVelocity().y = -1;
        }
        if(movepkt.down){
            player.getVelocity().y = 1;
        }
        if(movepkt.left){
            player.getVelocity().x = -1;
        }
        if(movepkt.right){
            player.getVelocity().x = 1;
        }

        player.getVelocity().normalize();
        
        player.getVelocity().scale(speed);

        player.getPosition().add(player.getVelocity());


    }

}
