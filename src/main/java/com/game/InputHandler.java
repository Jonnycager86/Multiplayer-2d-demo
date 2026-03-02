package com.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class InputHandler implements KeyListener{

    //key variables
    public static boolean upPressed, downPressed, leftPressed, rightPressed;


    public InputHandler(){
    }


    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
    }

    @Override
    public void keyPressed(KeyEvent e) {
       int key = e.getKeyCode();

        switch (key){

        case KeyEvent.VK_W -> {
            upPressed = true;
           // System.out.println("up");
        }
        case KeyEvent.VK_S -> {
            downPressed = true;
          //   System.out.println("down");
        }
        case KeyEvent.VK_A -> {
            leftPressed = true; 
            // System.out.println("left");
        } 
        case KeyEvent.VK_D -> {
            rightPressed = true;
            // System.out.println("right");
        }
       }
    }

    @Override
    public void keyReleased(KeyEvent e) {
         int key = e.getKeyCode();

        switch (key){

        case KeyEvent.VK_W -> upPressed = false;
        case KeyEvent.VK_S -> downPressed = false;
        case KeyEvent.VK_A -> leftPressed = false;  
        case KeyEvent.VK_D -> rightPressed = false;
       }
    }



    // removing for now
//       public boolean isUpPressed() {
//         return upPressed;
//     }
//     public boolean isDownPressed() {
//         return downPressed;
//     }
//     public boolean isLeftPressed() {
//         return leftPressed;
//     }
//     public boolean isRightPressed() {
//         return rightPressed;
    
// }


}

