package com.game;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class AstarSearch {

    final int max_rows = 24;
    final int max_cols = 32;
    final int maxRows;
    final int maxCols;
    final int tileSize = 48;


    Node[][] nodeGrid = new Node[max_cols][max_rows];
    int[][] mapGrid;
    Node startNode, currNode, targetNode;
    boolean targetReached = false;

   
    final PriorityQueue<Node> openList = new PriorityQueue<>();
    final ArrayList<Node> checkedList = new ArrayList<>();
    final ArrayList<Node> pathList = new ArrayList<>();

     public AstarSearch(int[][] mapGrid, int maxRows, int maxCols) {
        this.mapGrid = mapGrid;
        this.maxRows = maxRows;
        this.maxCols = maxCols;
        this.nodeGrid = new Node[max_cols][max_rows];
        initNodes();
    }

     private void initNodes() {

        for (int row = 0; row < max_rows; row++) {
            for (int col = 0; col < max_cols; col++) {

                nodeGrid[col][row] = new Node(col, row);

                if (mapGrid[row][col] == 1) {
                    nodeGrid[col][row].solid = true;
                }
            }
        }
    }

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

        targetReached = false;
        openList.clear();
        checkedList.clear();
        pathList.clear();

        int col = 0;
        int row = 0;

        while(row < max_rows && col < max_cols){
            Node curr = nodeGrid[col][row];
            curr.fCost = 0;
            curr.gCost = 0;
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


    public void setupNodes(int startCol, int startRow, int targetCol, int targetRow){

        resetNodes();

        startNode = nodeGrid[startCol][startRow];
        targetNode = nodeGrid[targetCol][targetRow];
        currNode = startNode;

        startNode.setAsStart();
        targetNode.setAsTarget();

        int col = 0;
        int row = 0;

        while(row < max_rows && col < max_cols){
            getCost(nodeGrid[col][row]);
            col++;

            if(col >= max_cols){
                col = 0;
                row++;

            }

             openList.offer(startNode);
             startNode.setAsOpen();

        }

    }

    public void openNode(Node node){

        if(node.isOpen == false && node.checked == false && node.solid == false){

            node.setAsOpen();
            node.parent = currNode;
            openList.offer(node);
        }
    }

   public boolean search(int startCol, int startRow, int targetCol, int targetRow) {
        if (!isValidTile(startCol, startRow) || !isValidTile(targetCol, targetRow)) {
            return false;
        }
        if (nodeGrid[targetCol][targetRow].solid) { // check over this
            return false;
        }
        setupNodes(startCol, startRow, targetCol, targetRow);
        while (!targetReached && !openList.isEmpty()) {
            currNode = openList.poll();
            currNode.setAsChecked();
            checkedList.add(currNode);
            int col = currNode.col;
            int row = currNode.row;
            if (currNode == targetNode) {
                targetReached = true;
                backTrack();
                return true;
            }
            if (row - 1 >= 0) {
                openNode(nodeGrid[col][row - 1]);
            }
            if (row + 1 < maxRows) {
                openNode(nodeGrid[col][row + 1]);
            }
            if (col - 1 >= 0) {
                openNode(nodeGrid[col - 1][row]);
            }
            if (col + 1 < maxCols) {
                openNode(nodeGrid[col + 1][row]);
            }
        }
        return false;
    }

    public void backTrack(){

        Node curr = targetNode;
        pathList.clear();

        while(curr != startNode && curr != startNode){
            pathList.add(0, curr); 
           curr = curr.parent;
        }
    }

    public ArrayList<Node> getPathList() {
        return pathList;
    }

    public int worldToCol(float worldX) {
        return (int)(worldX / tileSize);
   }

    public int worldToRow(float worldY) {
        return (int)(worldY / tileSize);
   }

   public float colToWorldCenterX(int col) {
        return col * tileSize + tileSize / 2.0f;
   }

   public float rowToWorldCenterY(int row) {
        return row * tileSize + tileSize / 2.0f;
   }

    private boolean isValidTile(int col, int row) {
        return col >= 0 && col < maxCols && row >= 0 && row < maxRows;
    }

}
