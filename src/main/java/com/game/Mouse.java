package com.game;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Mouse implements MouseListener, MouseMotionListener{

    private static  int mouseX = -1;
    private static  int mouseY = -1;
    private static boolean leftDown = false;
    private static boolean rightDown = false;


    @Override
    public void mouseDragged(MouseEvent e) {
         mouseX = e.getX();
         mouseY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // intentionally unused for now
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) leftDown = true;
        if (e.getButton() == MouseEvent.BUTTON3) rightDown = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) leftDown = false;
        if (e.getButton() == MouseEvent.BUTTON3) rightDown = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }


    public static int getMouseX(){
        return mouseX;
    }

    public static int getMouseY(){
        return mouseY;
    }

    public static boolean isLeftDown() {
        return leftDown;
    }

    public static boolean isRightDown() {
        return rightDown;
    }

}
