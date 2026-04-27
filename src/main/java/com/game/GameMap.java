package com.game;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class GameMap {  // Main function of this class is to load the map

    private int[][] mapGrid; 

    int maxRows = Constants.max_row_size;
    int maxCols = Constants.max_col_size;

    public GameMap(){

         mapGrid = new int[maxRows][maxCols];
         loadMap("/images/mapfile.txt");
    }


    public void loadMap(String filePath){

        String[] numbers;
        String line;

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(filePath)));

           for(int i = 0; i < maxRows; i++){

                line = br.readLine();
                numbers = line.split("");

            for(int j = 0; j < maxCols; j++){

                mapGrid[i][j] = Integer.parseInt(numbers[j]);
            }
           }
            

           br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int[][] getMapGrid(){
        return mapGrid;
    }

}