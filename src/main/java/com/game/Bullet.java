package com.game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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

        Object oldAA = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Stroke oldStroke = g2.getStroke();
        float speed = (float) Math.sqrt(vx * vx + vy * vy);
        float nx = speed > 0.0001f ? (vx / speed) : 1f;
        float ny = speed > 0.0001f ? (vy / speed) : 0f;
        float tail = 24f;

        // Dark outer streak for contrast on bright tiles
        g2.setStroke(new BasicStroke(4.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(new Color(15, 10, 5, 200));
        g2.drawLine((int) (sx - nx * tail), (int) (sy - ny * tail), (int) sx, (int) sy);

        g2.setStroke(new BasicStroke(2.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(new Color(255, 140, 40, 255));
        g2.drawLine((int) (sx - nx * (tail - 4f)), (int) (sy - ny * (tail - 4f)), (int) sx, (int) sy);

        g2.setStroke(new BasicStroke(1.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(new Color(255, 255, 220, 255));
        g2.drawLine((int) (sx - nx * (tail * 0.35f)), (int) (sy - ny * (tail * 0.35f)), (int) sx, (int) sy);

        g2.setColor(new Color(255, 255, 255, 255));
        g2.fillOval((int) (sx - 2.5f), (int) (sy - 2.5f), 5, 5);

        g2.setStroke(oldStroke);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAA);
    }
}

