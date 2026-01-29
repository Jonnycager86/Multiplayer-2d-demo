package com.game;

public class Vector2D {
    float x;
    float y;

    public Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }


    public void scale(float scalar){
        this.x *= scalar;
        this.y *= scalar;
    }

    public void add(Vector2D vec2){

         this.x += vec2.x;
         this.y += vec2.y;
    }

    public void normalize(){

        //find length of a vector first
        float length = getLength();

        if(length != 0){
            x/=length;
            y/=length;

        }
        
    }

    public float getLength(){

        return (float) Math.sqrt(x * x + y * y);

    }

    




}

