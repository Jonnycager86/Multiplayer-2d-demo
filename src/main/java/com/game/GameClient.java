package com.game;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class GameClient implements Runnable{

    
    private Socket sock = null;
    private final String ipAddr;
    private final int PORT = 5000;
    private Thread clientThread;
    private final ClientPlayer clientPlayer;
    private Input input;
    private Output output;

    HashMap<Integer, WorldPlayer> worldPlayers = new HashMap<>();

    Kryo kryo = KryoFactory.create();

   

   public GameClient(String ipAddr, ClientPlayer clientPlayer, HashMap<Integer, WorldPlayer> worldPlayers){
        this.ipAddr = ipAddr;
        this.clientPlayer = clientPlayer;
        this.worldPlayers = worldPlayers;
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
     sock = new Socket(ipAddr, PORT);
     output = new Output(sock.getOutputStream()); 
     input = new Input(sock.getInputStream());

    while(running){

            // Packet sending

            MoveIntentPacket movepkt = new MoveIntentPacket();
            
            mapInputsToPacket(inputHandler, movepkt);

            serializePacket(movepkt, output);

            output.flush();

            //Packet receiving

            Object packet;

            packet = kryo.readClassAndObject(input);

            if(packet instanceof LoginResponsePacket){
                LoginResponsePacket loginpkt = (LoginResponsePacket) packet;
                clientPlayer.setID(loginpkt.assignedClientID);
                clientPlayer.setX(loginpkt.x);
                clientPlayer.setY(loginpkt.y);
                System.out.println("Successful Login " + clientPlayer.getID());
            }

            if(packet instanceof SnapshotPacket){
                
                SnapshotPacket snapshot = (SnapshotPacket) packet; 

                processWorldPlayerState(snapshot);
                    
                processClientPlayerMovement(snapshot);

            }

            


            Thread.sleep(16);

    }

     }
    catch(IOException e){
        System.err.println(e);
    }catch (InterruptedException e) {
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

        public void processClientPlayerMovement(SnapshotPacket snapshot){

            for(EntityState statepkt : snapshot.state){
            
            if(clientPlayer.getID() == statepkt.EntityID){
              clientPlayer.setX(statepkt.x);
              clientPlayer.setY(statepkt.y);
            }

           }
        }



        public void processWorldPlayerState(SnapshotPacket snapshot){
            for(EntityState statepkt : snapshot.state){

                if(statepkt.EntityID == clientPlayer.getID()){
                    continue;
                }

                WorldPlayer existingPlayer = worldPlayers.get(statepkt.EntityID);

                if(!(worldPlayers.containsKey(statepkt.EntityID))){
                    WorldPlayer worldPlayer = new WorldPlayer();
                    worldPlayer.setID(statepkt.EntityID);
                    worldPlayer.setX(statepkt.x);
                    worldPlayer.setY(statepkt.y);
                    worldPlayers.put(statepkt.EntityID, worldPlayer);
                }
                else{
                    existingPlayer.setX(statepkt.x);
                    existingPlayer.setY(statepkt.y);

                }
            }
        }



        public void serializePacket(MoveIntentPacket movepkt, Output output){
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


        public void mapInputsToPacket(InputHandler input, MoveIntentPacket movepkt){
           movepkt.up = input.upPressed;
           movepkt.down = input.downPressed;
           movepkt.left = input.leftPressed;
           movepkt.right = input.rightPressed;
        }



    }


    

       
   


