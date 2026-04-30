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

        // Match ClientPlayer: 100x100 sprite. Gun is on the character's right; offset muzzle along forward + "right" = (-sin, cos)
        final float spriteW = 100f;
        final float spriteH = 100f;
        float pivotX = playerWorldX + spriteW * 0.5f;
        float pivotY = playerWorldY + spriteH * 0.5f;
        float cos = (float) Math.cos(angleRadians);
        float sin = (float) Math.sin(angleRadians);
        float forward = 18f;
        float gunSide = 12f; // toward sprite right (e.g. east when facing north)
        float muzzleX = pivotX + cos * forward - sin * gunSide;
        float muzzleY = pivotY + sin * forward + cos * gunSide;

        lastMuzzleX = muzzleX;
        lastMuzzleY = muzzleY;
        muzzleFlashTimer = 0.09f;

        float vx = (float) Math.cos(angleRadians) * bulletSpeed;
        float vy = (float) Math.sin(angleRadians) * bulletSpeed;

        List<Bullet> spawned = new ArrayList<>(1);
        spawned.add(new Bullet(muzzleX, muzzleY, vx, vy, bulletLifeSeconds));
        return spawned;
    }

    public void renderMuzzleFlash(Graphics2D g2, TileManager tilemgr) {
        if (muzzleFlashTimer <= 0f) return;

        final float flashDuration = 0.09f;
        float t = muzzleFlashTimer / flashDuration; // 1..0
        float sx = lastMuzzleX - tilemgr.cameraX;
        float sy = lastMuzzleY - tilemgr.cameraY;

        Object oldAA = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Composite old = g2.getComposite();

        float r = 38f + (1f - t) * 22f;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.min(1f, 0.92f * t)));
        RadialGradientPaint paint = new RadialGradientPaint(
            new Point2D.Float(sx, sy),
            r,
            new float[] { 0f, 0.35f, 0.7f, 1f },
            new Color[] {
                new Color(255, 255, 240, 255),
                new Color(255, 210, 90, 230),
                new Color(255, 120, 40, 140),
                new Color(255, 40, 20, 0)
            }
        );

        var oldPaint = g2.getPaint();
        g2.setPaint(paint);
        g2.fillOval((int) (sx - r), (int) (sy - r), (int) (r * 2f), (int) (r * 2f));

        float coreR = 14f + (1f - t) * 6f;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.min(1f, t)));
        g2.setPaint(new RadialGradientPaint(
            new Point2D.Float(sx, sy),
            coreR,
            new float[] { 0f, 1f },
            new Color[] { new Color(255, 255, 255, 250), new Color(255, 200, 120, 0) }
        ));
        g2.fillOval((int) (sx - coreR), (int) (sy - coreR), (int) (coreR * 2f), (int) (coreR * 2f));
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

