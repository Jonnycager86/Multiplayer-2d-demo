package com.game;

import java.io.IOException;
import java.net.Socket;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class GameClient implements Runnable{

    private Socket sock = null;
    private final String ipAddr;
    private final int port;
    private Thread clientThread;
    private ClientPlayer player;
    private Input input;
    private Output output;

    Kryo kryo = KryoFactory.create();

   

   public GameClient(String ipAddr, int port, ClientPlayer player){
        this.ipAddr = ipAddr;
        this.port = port;
        this.player = player;

   }

    public void initThread(){
        clientThread = new Thread(this);
        clientThread.start();
    }

   @Override
   public void run(){

    boolean running = true;

    InputHandler inputHandler = new InputHandler();
    try{
    sock = new Socket(ipAddr, port);
     output = new Output(sock.getOutputStream()); 
     input = new Input(sock.getInputStream());

    while(running){

            MovePacket movepkt = new MovePacket();
            
            mapInputsToPacket(inputHandler, movepkt);

            serializePacket(movepkt, output);

            output.flush();

            PositionPacket pospkt = deserializePacket(input);

            applyInputToPlayer(player, pospkt);
           

            //ClientGameWorld is where working log of all the players will be to apply position packets

            Thread.sleep(16);

    }

     }
    catch(IOException e){
        System.err.println(e);
    } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

}

        //  try{
        //     sock.close();
        // }
        // catch(IOException e){
        //     System.out.println(e);
        // }

        public void serializePacket(MovePacket movepkt, Output output){
            kryo.writeObject(output, movepkt);
        }

        public PositionPacket deserializePacket(Input input){    
            PositionPacket deserializedpkt = kryo.readObject(input, PositionPacket.class);
        
            return deserializedpkt;
    }

        public void applyInputToPlayer(ClientPlayer player, PositionPacket pospkt){

            player.setX(pospkt.x);
            player.setY(pospkt.y);
        }


        public void mapInputsToPacket(InputHandler input, MovePacket movepkt){
           movepkt.up = input.upPressed;
           movepkt.down = input.downPressed;
           movepkt.left = input.leftPressed;
           movepkt.right = input.rightPressed;
        }



    }


    

       
   


