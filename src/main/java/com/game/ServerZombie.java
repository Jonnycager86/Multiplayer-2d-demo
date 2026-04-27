package com.game;

import java.util.ArrayList;

public class ServerZombie extends ServerPlayer {

    private final float speed = 2.0f;
    private final AstarSearch pathfinder;
    private ArrayList<Node> currentPath = new ArrayList<>();
    private final int[][] mapGrid;
    private final int tileSize = 48;
    private final int maxRows = Constants.max_row_size;
    private final int maxCols = Constants.max_col_size;

    public ServerZombie(GameMap gameMap) {
        super();
        this.mapGrid = gameMap.getMapGrid();
        this.pathfinder = new AstarSearch(
            gameMap.getMapGrid(), // most important line here
            Constants.max_row_size,
            Constants.max_col_size
        );
    }

    private boolean isSolidWorld(float worldX, float worldY) {
        int col = (int)(worldX / tileSize);
        int row = (int)(worldY / tileSize);
        if (col < 0 || col >= maxCols || row < 0 || row >= maxRows) {
            return true;
        }
        return mapGrid[row][col] == 1;
    }

    public void updatePathToTarget(ServerPlayer target) {
        int startCol = pathfinder.worldToCol(this.getPosition().x);
        int startRow = pathfinder.worldToRow(this.getPosition().y);

        int targetCol = pathfinder.worldToCol(target.getPosition().x);
        int targetRow = pathfinder.worldToRow(target.getPosition().y);

        boolean foundPath = pathfinder.search(startCol, startRow, targetCol, targetRow);

        if (foundPath) {
            currentPath = new ArrayList<>(pathfinder.getPathList());
        } else {
            currentPath.clear();
        }
    }

    public void followPath() {
        if (currentPath.isEmpty()) {
            return;
        }

        Node nextNode = currentPath.get(0); // this could maybe be a stack instead

        float targetX = pathfinder.colToWorldCenterX(nextNode.col);
        float targetY = pathfinder.rowToWorldCenterY(nextNode.row);

        float dx = targetX - this.getPosition().x;
        float dy = targetY - this.getPosition().y;

        float distance = (float)Math.sqrt(dx * dx + dy * dy);

        if (distance < 2.0f) {
            currentPath.remove(0);
            return;
        }

        float moveX = (dx / distance) * speed;
        float moveY = (dy / distance) * speed;

        float currX = this.getPosition().x;
        float currY = this.getPosition().y;

        float proposedX = currX + moveX;
        float proposedY = currY + moveY;

        
        float minX = 0;
        float minY = 0;
        float maxX = maxCols * tileSize - 1;
        float maxY = maxRows * tileSize - 1;

        proposedX = Math.max(minX, Math.min(maxX, proposedX));
        proposedY = Math.max(minY, Math.min(maxY, proposedY));


        if (!isSolidWorld(proposedX, proposedY)) {
            this.setPosition(proposedX, proposedY);
            return;
        }

        // axis separation slide to avoid clipping into walls
        float slideX = Math.max(minX, Math.min(maxX, currX + moveX));
        float slideY = Math.max(minY, Math.min(maxY, currY + moveY));

        boolean moved = false;
        if (!isSolidWorld(slideX, currY)) {
            currX = slideX;
            moved = true;
        }
        if (!isSolidWorld(currX, slideY)) {
            currY = slideY;
            moved = true;
        }

        if (moved) {
            this.setPosition(currX, currY);
        } else {
            // Stuck against geometry, force a re-path the next tick.
            currentPath.clear();
        }
    }


    public ArrayList<Node> getCurrentPath() {
        return currentPath;
    }
}