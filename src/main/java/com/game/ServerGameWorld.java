package com.game;

import java.util.ArrayList;


public class ServerGameWorld {
  
    //holds all the actual entities

    ArrayList<ServerPlayer> players = new ArrayList<>();


    public ServerPlayer createPlayer(){
        return new ServerPlayer();
    }


}
