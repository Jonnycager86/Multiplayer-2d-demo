package com.game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class GameServer implements Runnable{   
    
    private final int SERVER_PORT = 5000;
    private final int TICK_RATE = 30;
    private Thread serverThread;
    MovementSystem movement;
    GameMap gameMap = new GameMap();
    ServerZombie zombie = new ServerZombie(gameMap);
    private int clientID = 0;

    private final ArrayList<ServerPlayer> players = new ArrayList<>(); // actual players will be moved to server game world class
    private final HashMap<Integer, Socket> clients = new HashMap<>();
    private final ConcurrentHashMap<Integer, LinkedBlockingQueue<MoveIntentPacket>> inputQueues = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, Output> outputs = new ConcurrentHashMap<>();
    private final ArrayList<ServerZombie> zombies  = new ArrayList<>();

   


    Kryo kryo = KryoFactory.create();
    

    public GameServer(){
    }

    public void initEntities(){ //all actual game entity objects will me moved into game world class
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

        new Thread(() -> runMainServerLoop()).start(); 

        try{

            ServerSocket ss = new ServerSocket(SERVER_PORT);
            System.out.println("Server Socket created!");
            System.out.println("Waiting for client");

        while(running){

            Socket clientSocket = ss.accept(); 
            clients.put(clientID, clientSocket);
            System.out.println("Client Accepted " + clientSocket);
            
            Input input = new Input(clientSocket.getInputStream());
            Output output =  new Output(clientSocket.getOutputStream());

            outputs.put(clientID, output); // for broadcasting
            inputQueues.put(clientID, new LinkedBlockingQueue<>());

            ServerPlayer player = new ServerPlayer();
            player.setID(clientID);
            players.add(player);

            LoginResponsePacket loginpkt = new LoginResponsePacket();
            loginpkt.assignedClientID = clientID;
            loginpkt.x = player.getPosition().x; 
            loginpkt.y = player.getPosition().y;

            kryo.writeClassAndObject(output, loginpkt);
    

            new Thread(() -> getClientInput(clientID, player, input, output)).start(); 

               Thread.sleep(16);

               clientID++;

             }

             
            

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

    public void runMainServerLoop(){

        Kryo kryo = KryoFactory.create();

        boolean running = true;
        double tickInterval = 1000000000/TICK_RATE;
        long lastTickTime = System.nanoTime();
        double delta = 0;

        while(running){

            long currentTickTime = System.nanoTime();

            delta += (currentTickTime - lastTickTime) / tickInterval;

            lastTickTime = currentTickTime;

            if(delta >= 1){
                serverUpdate();
                delta--;
            }
        }
    }

    public void serverUpdate(){

        MoveIntentPacket movepkt;
        SnapshotPacket snapshot = new SnapshotPacket();

        for(Map.Entry<Integer, LinkedBlockingQueue<MoveIntentPacket>> entry : inputQueues.entrySet()){

            ServerPlayer player = getServerPlayerByID(entry.getKey());

            while((movepkt = entry.getValue().poll()) != null){
               
                movement.update(movepkt, player);
            }
        }

        if (!players.isEmpty()) {
            zombieUpdate(players, zombie);
        }

        // Build snapshot every tick
        for (ServerPlayer player : players) {
            EntityState statePacketP = getPlayerStatePacket(player);
            if (statePacketP != null) {
                snapshot.state.add(statePacketP);
            }
        }
        EntityState statePacketZ = getZombieStatePacket(zombie);
        if (statePacketZ != null) {
            snapshot.state.add(statePacketZ);
        }

        broadcastSnapshotToClients(snapshot);
    }

    
    public void broadcastSnapshotToClients(SnapshotPacket snapshot){

        for(Map.Entry<Integer, Output> entry : outputs.entrySet()){
            serializePacket(entry.getValue(), snapshot);
            entry.getValue().flush();
        }

    
        

    }

    public ServerPlayer getServerPlayerByID(int id){ 
        for(ServerPlayer p : players){
            if(p.getID() == id){
                return p;
            }
        }
        return null;
    }

    public MoveIntentPacket deserializePacket(Input input){        
        MoveIntentPacket deserializedpkt = kryo.readObject(input, MoveIntentPacket.class);
        
        return deserializedpkt;
    }
        //fix this to be able to send any type up of packet
    public void serializePacket(Output output, SnapshotPacket snapshot){
        kryo.writeClassAndObject(output, snapshot); 

    }

    public EntityState getPlayerStatePacket(ServerPlayer player){
       EntityState entityState = new EntityState();

       entityState.EntityID = player.getID();
       entityState.x = player.getPosition().x;
       entityState.y = player.getPosition().y;
       entityState.playerRotation = player.getRotation();
       entityState.entityType = 0;

        return entityState;
    }

    public EntityState getZombieStatePacket(ServerZombie zombie){
       EntityState entityState = new EntityState();

       entityState.x = zombie.getPosition().x;
       entityState.y = zombie.getPosition().y;
       entityState.zombieRotation = zombie.getRotationAngle();
       System.out.println("Rotation: " + zombie.getRotationAngle());
       entityState.entityType = 1;

        return entityState;
    }


    public void getClientInput(int clientID, ServerPlayer player, Input input, Output output){

            Kryo kryo = KryoFactory.create();

            boolean connected = true;

            try{

              while(connected){

               MoveIntentPacket movepkt = deserializePacket(input);

               inputQueues.get(clientID).offer(movepkt);

               

                }

            }
            catch(Exception e){
                System.out.println("Client disconnected");
            }


    }


     public void initZombies(){           // this will be moved into another class later
    
    //    for(int i = 0; i < 10; i++){
    //     zombies.add(new Zombie(gameMap));  // testing one zombie for now
    //    }
        
   }

   public void zombieUpdate(ArrayList<ServerPlayer> players, ServerZombie zombie){

        zombie.updatePathToTarget(players);

        zombie.followPath();
   }


    
}
