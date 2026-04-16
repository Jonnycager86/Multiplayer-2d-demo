package com.game;

public class Node implements Comparable<Node> {

    public int gCost;
    public int fCost;
    public Node parent;
    public boolean checked;
    public int col;
    public int row;
    public boolean isOpen;
    public boolean solid;
    public boolean isTarget;
    public boolean isStart;

    public Node(int col, int row){
        this.col = col;
        this.row = row;
    }


    public void setAsOpen(){
        isOpen = true;
    }

    public void setAsTarget(){
        isTarget = true;
    }

     public void setAsStart(){
        isStart = true;
    }

    public void setAsChecked(){
        checked = true;
    }



    @Override
    public int compareTo(Node node) {
        return Integer.compare(this.fCost, node.fCost);
    }


    

}
