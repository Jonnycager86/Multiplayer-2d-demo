package com.game;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;
import java.util.Random;

import javax.swing.JPanel;

public class BackgroundPanel extends JPanel {
    private final Random rng = new Random(1337);

    public BackgroundPanel() {
        setBackground(UITheme.BG_0);
        setOpaque(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        UITheme.enableAntialias(g2);

        int w = getWidth();
        int h = getHeight();

        // base gradient
        g2.setPaint(new GradientPaint(0, 0, UITheme.BG_1, 0, h, UITheme.BG_0));
        g2.fillRect(0, 0, w, h);

        // subtle red hazard glow on left
        g2.setPaint(new GradientPaint(0, 0, new Color(180, 24, 24, 80), w * 0.55f, 0, new Color(0, 0, 0, 0)));
        g2.fillRect(0, 0, w, h);

        // faint diagonal "hazard" bars
        g2.setColor(new Color(255, 255, 255, 10));
        int barW = 120;
        for (int x = -h; x < w + h; x += barW) {
            g2.fillPolygon(
                new int[] { x, x + 70, x + 110, x + 40 },
                new int[] { 0, 0, h, h },
                4
            );
        }

        // film grain
        int dots = Math.max(3000, (w * h) / 90);
        for (int i = 0; i < dots; i++) {
            int x = rng.nextInt(Math.max(1, w));
            int y = rng.nextInt(Math.max(1, h));
            int a = 6 + rng.nextInt(18);
            g2.setColor(new Color(255, 255, 255, a));
            g2.fillRect(x, y, 1, 1);
        }

        // vignette
        float radius = Math.max(w, h) * 0.85f;
        RadialGradientPaint vignette = new RadialGradientPaint(
            new Point2D.Float(w / 2f, h / 2f),
            radius,
            new float[] { 0.0f, 0.7f, 1.0f },
            new Color[] { new Color(0, 0, 0, 0), new Color(0, 0, 0, 90), new Color(0, 0, 0, 200) }
        );
        g2.setPaint(vignette);
        g2.fillRect(0, 0, w, h);

        g2.dispose();
    }
}

