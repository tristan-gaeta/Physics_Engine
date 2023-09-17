package com.jigl.bounding;

import org.joml.Math;
import org.joml.Vector3f;

/**
 * @author Tristan Gaeta
 * @version 09-11-2023
 * 
 */
public class BoundingSphere extends BoundingVolume {
    protected static final float VOLUME_CONSTANT = Math.toRadians(240f);
    protected float radius;

    public BoundingSphere(float x, float y, float z, float radius) {
        this.center = new Vector3f(x, y, z);
        this.radius = radius;
        this.volume = VOLUME_CONSTANT * radius * radius * radius;
    }

    public BoundingSphere(final Vector3f anchor, float radius) {
        this.center = anchor;
        this.radius = radius;
        this.volume = VOLUME_CONSTANT * radius * radius * radius;
    }

    @Override
    public boolean intersectsGeneric(Intersectable other) {
        return other.intersects(this);
    }

    @Override
    public boolean intersects(BoundingSphere other) {
        return BoundingVolume.intersects(this, other);
    }

    @Override
    public boolean intersects(BoundingBox other) {
        return BoundingVolume.intersects(this, other);
    }
}
