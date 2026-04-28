package com.game;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public final class AssaultRifle {
    private final int magSize;
    private final float roundsPerSecond;
    private final float reloadSeconds;
    private final float bulletSpeed;
    private final float bulletLifeSeconds;

    private int magAmmo;
    private int reserveAmmo;

    private float shotCooldown;
    private float reloadTimer;

    private float muzzleFlashTimer;
    private float lastMuzzleX;
    private float lastMuzzleY;

    public AssaultRifle() {
        this.magSize = 30;
        this.roundsPerSecond = 10.0f;  // 600 RPM
        this.reloadSeconds = 1.55f;
        this.bulletSpeed = 1550f;      // world units/sec
        this.bulletLifeSeconds = 0.55f;

        this.magAmmo = magSize;
        this.reserveAmmo = 180;
    }

    public void update(float dtSeconds) {
        if (shotCooldown > 0f) shotCooldown = Math.max(0f, shotCooldown - dtSeconds);
        if (muzzleFlashTimer > 0f) muzzleFlashTimer = Math.max(0f, muzzleFlashTimer - dtSeconds);

        if (reloadTimer > 0f) {
            reloadTimer = Math.max(0f, reloadTimer - dtSeconds);
            if (reloadTimer == 0f) finishReload();
        }

        if (magAmmo == 0 && reloadTimer == 0f && reserveAmmo > 0) {
            startReload();
        }
    }

    public List<Bullet> tryFire(boolean triggerHeld, float playerWorldX, float playerWorldY, double angleRadians) {
        if (!triggerHeld) return List.of();
        if (reloadTimer > 0f) return List.of();
        if (shotCooldown > 0f) return List.of();
        if (magAmmo <= 0) return List.of();

        magAmmo--;
        shotCooldown = 1f / roundsPerSecond;

        float pivotX = playerWorldX + 32f;
        float pivotY = playerWorldY + 32f;
        float muzzleX = pivotX + (float) Math.cos(angleRadians) * 42f;
        float muzzleY = pivotY + (float) Math.sin(angleRadians) * 42f;

        lastMuzzleX = muzzleX;
        lastMuzzleY = muzzleY;
        muzzleFlashTimer = 0.07f;

        float vx = (float) Math.cos(angleRadians) * bulletSpeed;
        float vy = (float) Math.sin(angleRadians) * bulletSpeed;

        List<Bullet> spawned = new ArrayList<>(1);
        spawned.add(new Bullet(muzzleX, muzzleY, vx, vy, bulletLifeSeconds));
        return spawned;
    }

    public void renderMuzzleFlash(Graphics2D g2, TileManager tilemgr) {
        if (muzzleFlashTimer <= 0f) return;

        float t = muzzleFlashTimer / 0.07f; // 1..0
        float sx = lastMuzzleX - tilemgr.cameraX;
        float sy = lastMuzzleY - tilemgr.cameraY;

        Object oldAA = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Composite old = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.min(1f, 0.55f * t)));

        float r = 26f + (1f - t) * 10f;
        RadialGradientPaint paint = new RadialGradientPaint(
            new Point2D.Float(sx, sy),
            r,
            new float[] { 0f, 0.45f, 1f },
            new Color[] {
                new Color(255, 245, 180, 220),
                new Color(255, 150, 60, 120),
                new Color(255, 60, 60, 0)
            }
        );

        var oldPaint = g2.getPaint();
        g2.setPaint(paint);
        g2.fillOval((int) (sx - r), (int) (sy - r), (int) (r * 2f), (int) (r * 2f));
        g2.setPaint(oldPaint);

        g2.setComposite(old);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAA);
    }

    private void startReload() {
        if (reserveAmmo <= 0) return;
        if (magAmmo == magSize) return;
        reloadTimer = reloadSeconds;
    }

    private void finishReload() {
        int needed = magSize - magAmmo;
        if (needed <= 0) return;
        int take = Math.min(needed, reserveAmmo);
        magAmmo += take;
        reserveAmmo -= take;
    }

    public boolean isReloading() {
        return reloadTimer > 0f;
    }

    public float getReloadTimer() {
        return reloadTimer;
    }

    public int getMagAmmo() {
        return magAmmo;
    }

    public int getMagSize() {
        return magSize;
    }

    public int getReserveAmmo() {
        return reserveAmmo;
    }
}

