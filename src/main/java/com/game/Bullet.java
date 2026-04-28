package com.game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

public final class Bullet {
    private float x;
    private float y;
    private final float vx;
    private final float vy;
    private float lifeSeconds;

    public Bullet(float x, float y, float vx, float vy, float lifeSeconds) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.lifeSeconds = lifeSeconds;
    }

    public void update(float dtSeconds) {
        x += vx * dtSeconds;
        y += vy * dtSeconds;
        lifeSeconds -= dtSeconds;
    }

    public boolean isDead() {
        return lifeSeconds <= 0f;
    }

    public void render(Graphics2D g2, TileManager tilemgr) {
        float sx = x - tilemgr.cameraX;
        float sy = y - tilemgr.cameraY;

        Stroke oldStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        
        float speed = (float) Math.sqrt(vx * vx + vy * vy);
        float nx = speed > 0.0001f ? (vx / speed) : 1f;
        float ny = speed > 0.0001f ? (vy / speed) : 0f;
        float tail = 10f;

        g2.setColor(new Color(0, 0, 0, 170));
        g2.drawLine((int) (sx - nx * (tail + 2f)), (int) (sy - ny * (tail + 2f)), (int) sx, (int) sy);

        g2.setColor(new Color(255, 235, 130, 220));
        g2.drawLine((int) (sx - nx * tail), (int) (sy - ny * tail), (int) sx, (int) sy);

        g2.setStroke(oldStroke);
    }
}

