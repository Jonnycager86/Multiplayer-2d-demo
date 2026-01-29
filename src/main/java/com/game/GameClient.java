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

    Kryo kryo = KryoFactory.create();

   

   public GameClient(String ipAddr, int port){
        this.ipAddr = ipAddr;
        this.port = port;

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
    Output output = new Output(sock.getOutputStream()); //Client also needs to recv data
    Input input = new Input(sock.getInputStream());
    while(running){

            MovePacket movepkt = new MovePacket();
            
            mapInputsToPacket(inputHandler, movepkt);

            serializePacket(movepkt, output);

            output.flush();

    }

     }
    catch(IOException e){
        System.err.println(e);
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

        public void deserializePacket(Input input){
            kryo.readObject(input, MovePacket.class);
        }


        public void mapInputsToPacket(InputHandler input, MovePacket movepkt){
           movepkt.up = input.upPressed;
           movepkt.down = input.downPressed;
           movepkt.left = input.leftPressed;
           movepkt.right = input.rightPressed;
        }



    }


    

       
   


