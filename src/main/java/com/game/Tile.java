package com.game;

import java.awt.image.BufferedImage;

public class Tile {

    public BufferedImage image;
    public boolean collision;
    public TileType type;

    public Tile(TileType type){
       this.type = type;
    }

    public BufferedImage getImage(){
        return type.getImage();
    }

}
