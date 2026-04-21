package com.game;

import java.awt.Graphics2D;


public class TileManager {

    
    public GameMap gameMap = new GameMap();

    public int[][] mapGrid = gameMap.getMapGrid();

    final int max_row_size = 24;
    final int max_col_size = 32;
    final int tile_size = 48;

    float deadzoneLeft = Constants.screen_width * 0.35f;
    float deadzoneRight = Constants.screen_width * 0.65f;
    float deadzoneTop = Constants.screen_height * 0.35f;
    float deadzoneBottom = Constants.screen_height * 0.65f;

    float cameraX = 0;
    float cameraY = 0;
    boolean cameraInitialised = false;

    float playerX;
    float playerY;

    Tile tileMetal;
    Tile tileBrick;


    public TileManager(){
        
         tileMetal = new Tile(TileType.METAL);
         tileBrick = new Tile(TileType.BRICK);
    }


    public void render(Graphics2D g2, ClientPlayer clientPlayer){

        int row = 0;
        int col = 0;
        int worldX = 0;    
        int worldY = 0;
        playerX = clientPlayer.getX();       // all of this will be moved somewhere else
        playerY = clientPlayer.getY();
        float screenX, screenY;

        if (!cameraInitialised) {
            cameraX = (max_col_size * tile_size / 2f) - (Constants.screen_width  / 2f);
            cameraY = (max_row_size * tile_size / 2f) - (Constants.screen_height / 2f);
            cameraInitialised = true;
        }
        
        float playerScreenX = playerX - cameraX;
        float playerScreenY = playerY - cameraY;

        if(playerScreenX < deadzoneLeft){
            cameraX -= (deadzoneLeft - playerScreenX);
        }
         if(playerScreenX > deadzoneRight){
            cameraX += (playerScreenX - deadzoneRight);
        }
         if(playerScreenY < deadzoneTop){
            cameraY -= (deadzoneTop - playerScreenY);
        }
         if(playerScreenY > deadzoneBottom){
            cameraY += (playerScreenY - deadzoneBottom);
        }


        while(row < max_row_size && col < max_col_size){

            screenX = worldX - cameraX;
            screenY = worldY - cameraY;

            if(mapGrid[row][col] == 0){
                g2.drawImage(tileMetal.getImage(), (int)screenX, (int)screenY, 48, 48, null);
            }
            
            if(mapGrid[row][col] == 1){
                g2.drawImage(tileBrick.getImage(), (int)screenX, (int)screenY, 48, 48, null); 
            }
        

            col++;
            worldX = col * tile_size;

            if(col == max_col_size){
                col = 0;
                worldX = 0;
                row++;
                worldY = row * tile_size; 
            }
        }

        

    }


    public int[][] getMapGrid() {
    return mapGrid;
}

    public int getMaxRowSize() {
        return max_row_size;
    }

    public int getMaxColSize() {
        return max_col_size;
    }

    public int getTileSize() {
        return tile_size;
    }





}
