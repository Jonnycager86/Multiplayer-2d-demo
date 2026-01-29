package com.game;

import com.esotericsoftware.kryo.Kryo;

public class KryoFactory {

    // Register classes that will be serialized(packets) the order is important hence the IDs
    public static final int MOVE_ID = 10;
  

    public static Kryo create(){

    Kryo kryo = new Kryo();

    kryo.setReferences(false);
    kryo.setRegistrationRequired(true);

    //Registration

    kryo.register(MovePacket.class, MOVE_ID);


        return kryo;
    }

}
