package com.game;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class AstarSearch {

    final int maxRows;
    final int maxCols;
    final int tileSize = 48;


    Node[][] nodeGrid;
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
        this.nodeGrid = new Node[maxCols][maxRows];
        initNodes();
    }

     private void initNodes() {

        for (int row = 0; row < maxRows; row++) {
            for (int col = 0; col < maxCols; col++) {

                nodeGrid[col][row] = new Node(col, row);

                if (mapGrid[row][col] == 1) {
                    nodeGrid[col][row].solid = true;
                }
            }
        }
    }

    private int heuristic(Node a, Node b) {
        return Math.abs(a.col - b.col) + Math.abs(a.row - b.row);
    }

    public void resetNodes(){

        targetReached = false;
        openList.clear();
        checkedList.clear();
        pathList.clear();

        for (int row = 0; row < maxRows; row++) {
            for (int col = 0; col < maxCols; col++) {
                Node curr = nodeGrid[col][row];
                curr.fCost = 0;
                curr.gCost = Integer.MAX_VALUE;
                curr.hCost = 0;
                curr.checked = false;
                curr.isOpen = false;
                curr.parent = null;
                curr.isStart = false;
                curr.isTarget = false;
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
        startNode.gCost = 0;
        startNode.hCost = heuristic(startNode, targetNode);
        startNode.fCost = startNode.gCost + startNode.hCost;
        openList.offer(startNode);
        startNode.setAsOpen();
    }

    public void openNode(Node node){

        if(node.checked || node.solid){
            return;
        }

        int tentativeG = currNode.gCost + 1;
        if (!node.isOpen || tentativeG < node.gCost) {
            node.parent = currNode;
            node.gCost = tentativeG;
            node.hCost = heuristic(node, targetNode);
            node.fCost = node.gCost + node.hCost;

            if (!node.isOpen) {
                node.setAsOpen();
                openList.offer(node);
            } else {
                // refresh node position
                openList.remove(node);
                openList.offer(node);
            }
        }
    }

   public boolean search(int startCol, int startRow, int targetCol, int targetRow){

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

        while(curr != null && curr != startNode){
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
