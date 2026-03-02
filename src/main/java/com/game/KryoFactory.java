package com.game;

import java.util.ArrayList;

import com.esotericsoftware.kryo.Kryo;

public class KryoFactory {

    // Register classes that will be serialized(packets) the order is important hence the IDs
   // public static final int BASE_ID = 10;
    public static final int MOVE_ID = 11;
    public static final int SNAPSHOT_ID = 12;
    public static final int LOGIN_ID = 13;
    public static final int ARRAY_ID = 14;
    public static final int ENTITYSTATE_ID = 15;

  

    public static Kryo create(){

    Kryo kryo = new Kryo();

    kryo.setReferences(false);
    kryo.setRegistrationRequired(true);

    //Registration

    kryo.register(MoveIntentPacket.class, MOVE_ID);
    kryo.register(SnapshotPacket.class, SNAPSHOT_ID);
    kryo.register(LoginResponsePacket.class, LOGIN_ID);
    kryo.register(ArrayList.class, ARRAY_ID);
    kryo.register(EntityState.class, ENTITYSTATE_ID);

   


        return kryo;
    }

}
