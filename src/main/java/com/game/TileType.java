package com.game;

import java.awt.image.BufferedImage;


public enum TileType {

    METAL("/images/Metal_15-128x128.png"),
    BRICK("/images/Brick_10-128x128.png");

    private final BufferedImage image;

     TileType(String path){
        this.image = ImageManager.loadImage(path);
    }

    public BufferedImage getImage(){
        return image;
    }
}
