package com.game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class GameServer implements Runnable{   /*Stuff like MovementSystem and other systems will me moved into GameWorld class */

    private Socket s = null;
    private ServerSocket ss = null;
    private final int SERVER_PORT = 5000; 
    ServerPlayer player;
    private Thread serverThread;
    MovementSystem movement;


    Kryo kryo = KryoFactory.create();
    

    public GameServer(){
    }

    public void initEntities(){ //all actual game entity objects will me moved into game world class
         player = new ServerPlayer(); 
         movement = new MovementSystem();
    }

    public void initThread(){
        serverThread = new Thread(this);
        serverThread.start();
    }

    @Override
    public void run(){

        boolean running = true;

        initEntities();

        try{
            ss = new ServerSocket(SERVER_PORT);
            System.out.println("Server Socket created!");
            System.out.println("Waiting for client");


            s = ss.accept(); 
            System.out.println("Client Accepted");
            Input input = new Input(s.getInputStream());
            Output output =  new Output(s.getOutputStream());

            while(running){

                /*Method in here to actually process move packet
                (and any other kind of packet) using deserializePacket method
                using Movement class. (Need to figure out how to handle different
                types of packets like login etc)  */

               MovePacket movepkt = deserializePacket(input);

               movement.update(movepkt, player);

               PositionPacket pospkt = getPositionPacket(player);
               
              serializePacket(output, pospkt); 
               
               output.flush();

               Thread.sleep(16);
             }

             // need handling for packets going wrong or else the kryo reading and writing wil block( need some more exception handling)
            

             {
        } 

        }
        catch(IOException e){
            System.out.println(e);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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

    public PositionPacket getPositionPacket(ServerPlayer player){
        PositionPacket pospkt = new PositionPacket();

       pospkt.x = player.getPosition().x;
       pospkt.y = player.getPosition().y;

        return pospkt;
    }


    
}
