package com.game;

public class ServerMain {

    public static void main(String[] args){

        GameServer gameServer = new GameServer();
        gameServer.initThread();
    }
}
