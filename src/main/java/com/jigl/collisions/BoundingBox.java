/**
 * @author Tristan Gaeta
 * @version 09-11-2023
 * 
 */
package com.jigl.collisions;

import org.joml.Math;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.jigl.Scratch;

public class BoundingBox extends BoundingVolume {
    protected Vector3f halfWidths;
    protected Quaternionf orientation;

    public BoundingBox(Vector3f anchor, Quaternionf focus, float halfWidth, float halfHeight, float halfDepth) {
        this.center = anchor;
        this.orientation = focus;
        this.halfWidths = new Vector3f(halfWidth, halfHeight, halfDepth);
        this.volume = 8 * halfWidth * halfHeight * halfDepth;
    }

    @Override
    public boolean intersectsGeneric(Intersectable other) {
        return other.intersects(this);
    }

    @Override
    public boolean intersects(BoundingSphere other) {
        return BoundingVolume.intersects(other, this);
    }

    @Override
    public boolean intersects(BoundingBox other) {
        return BoundingVolume.intersects(this, other);
    }

    public Vector3f closestPoint(Vector3f pt, Vector3f dest) {
        Vector3f translation = pt.sub(this.center, Scratch.VEC3.next());
        Matrix3f rotation = this.orientation.get(Scratch.MAT3.next());
        dest.set(this.center);
        Vector3f axis = Scratch.VEC3.next();

        for (int i = 0; i < 3; i++) {
            rotation.getColumn(i, axis);
            float dist = translation.dot(axis);
            dist = Math.clamp(-this.halfWidths.get(i), this.halfWidths.get(i), dist);
            dest.fma(dist, axis);
        }

        Scratch.VEC3.free(translation, axis);
        Scratch.MAT3.free(rotation);

        return dest;
    }
}
