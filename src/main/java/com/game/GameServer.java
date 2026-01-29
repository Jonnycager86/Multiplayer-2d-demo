package com.game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class GameServer implements Runnable{   //refactor to have physics handling in another class

    private Socket s = null;
    private ServerSocket ss = null;
    private final int SERVER_PORT = 5000; 
    ServerPlayer player;
    private Thread serverThread;
    
    private final float speed = 4; 


    Kryo kryo = KryoFactory.create();
    

    public GameServer(){
    }

    public void initEntities(){ //all actual game entity objects will me moved into game world class
         player = new ServerPlayer(); 
    }

    public void initThread(){
        serverThread = new Thread(this);
        serverThread.start();
    }

    @Override
    public void run(){

        boolean running = true;

        try{
            ss = new ServerSocket(SERVER_PORT);
            System.out.println("Server Socket created!");
            System.out.println("Waiting for client");


            s = ss.accept(); 
            System.out.println("Client Accepted");
            Input input = new Input(s.getInputStream());
            Output output =  new Output(s.getOutputStream());

            while(running){

                initEntities();

                /*Method in here to actually process move packet
                (and any other kind of packet) using deserializePacket method
                using Movement class. (Need to figure out how to handle different
                types of packets like login etc)  */

            }


            

             {
        } 

        }
        catch(IOException e){
            System.out.println(e);
        }

        // try{
        //     s.close();
        //     ss.close();
        // }
        // catch(IOException e){
        //     System.out.println(e);
        // }
    }

    public MovePacket deserializePacket(Input input){        // recv the packet from the input stream
        MovePacket deserializedpkt = kryo.readObject(input, MovePacket.class);
        
        return deserializedpkt;
    }

    public void serializePacket(Output output, PositionPacket pospkt){
        kryo.writeObject(output, pospkt);

    }


    
}
