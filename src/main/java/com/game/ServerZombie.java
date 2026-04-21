package com.game;

import java.util.ArrayList;

public class ServerZombie extends ServerPlayer {

    private final float speed = 2.0f;
    private final AstarSearch pathfinder;
    private ArrayList<Node> currentPath = new ArrayList<>();

    public ServerZombie(GameMap gameMap) {
        super();
        this.pathfinder = new AstarSearch(
            gameMap.getMapGrid(), // most important line here
            Constants.max_row_size,
            Constants.max_col_size
        );
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

        this.setPosition(
            this.getPosition().x + moveX,
            this.getPosition().y + moveY
        );
    }


    public ArrayList<Node> getCurrentPath() {
        return currentPath;
    }
}