package com.game;

import java.util.ArrayList;

public class SnapshotPacket implements Packet{

    public int tickNo;
    public ArrayList<EntityState> state = new ArrayList<>();
    
    
    public SnapshotPacket(){}
    

}
