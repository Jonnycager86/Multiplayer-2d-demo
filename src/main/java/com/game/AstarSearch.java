package com.game;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class AstarSearch {

    final int max_rows = 24;
    final int max_cols = 32;


    Node[][] nodeGrid = new Node[max_cols][max_rows];
    Node startNode, currNode, targetNode;
    boolean targetReached = false;

    // Need something to store paths for the zombie to follow after each search

    PriorityQueue<Node> openList = new PriorityQueue<>();
    ArrayList<Node> checkedList = new ArrayList<>();
    


    public void getCost(Node node){

        // Find G cost
        int xGDist = Math.abs(node.col - startNode.col);
        int yGDist = Math.abs(node.row - startNode.row);

        int gCost = xGDist + yGDist;

        // Find H cost
        int xHDist = Math.abs(node.col - targetNode.col);
        int yHDist = Math.abs(node.row - targetNode.row);

        int hCost = xHDist + yHDist;


        
        node.fCost = hCost + gCost;
    }

    public void resetNodes(){

        int col = 0;
        int row = 0;

        while(row < max_rows && col < max_cols){
            Node curr = nodeGrid[col][row];
            curr.fCost = 0;
            curr.checked = false;
            curr.isOpen = false;
            curr.parent = null;
            col++;
        

            if(col == max_cols){
                col = 0;
                row++;
        }
    }
    }


    public void setupNodes(){

        int col = 0;
        int row = 0;

        while(row < max_rows && col < max_cols){
            getCost(nodeGrid[col][row]);
            col++;

            if(col >= max_cols){
                col = 0;
                row++;

            }

        }

    }

    public void openNode(Node node){

        if(node.isOpen == false && node.checked == false && node.solid == false){

            node.setAsOpen();
            node.parent = currNode;
            openList.offer(node);
        }
    }

    public void search(){

        resetNodes();

        setupNodes();

        int col = currNode.col;
        int row = currNode.row;

        while(targetReached == false){

            if(row-1 >= 0){
                openNode(nodeGrid[col][row-1]);
            }
             if(row+1 <= max_rows){
                openNode(nodeGrid[col][row+1]);
            }
             if(col-1 >= 0){
                openNode(nodeGrid[col-1][row]);
            }
             if(col+1 <= max_cols){
                openNode(nodeGrid[col+1][row]);
            }

            currNode.setAsChecked();

            checkedList.add(currNode);

            currNode = openList.poll();

            if(currNode == targetNode){
                targetReached = true;
                backTrack();
            }
            
        }

    }

    public void backTrack(){

        Node curr = targetNode;

        while(curr != startNode){
           curr = curr.parent;
        }
    }

}
