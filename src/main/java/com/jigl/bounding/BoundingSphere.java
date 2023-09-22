package com.jigl.bounding;

import org.joml.Math;

import com.jigl.geometry.Intersectable;
import com.jigl.math.Vec3;

/**
 * @author Tristan Gaeta
 * @version 09-11-2023
 * 
 */
public class BoundingSphere extends BoundingVolume {
    protected static final float VOLUME_CONSTANT = Math.toRadians(240f);
    protected float radius;

    public BoundingSphere(float x, float y, float z, float radius) {
        this.center = new Vec3(x, y, z);
        this.radius = radius;
        this.volume = VOLUME_CONSTANT * radius * radius * radius;
    }

    public BoundingSphere(final Vec3 anchor, float radius) {
        this.center = anchor;
        this.radius = radius;
        this.volume = VOLUME_CONSTANT * radius * radius * radius;
    }
}
