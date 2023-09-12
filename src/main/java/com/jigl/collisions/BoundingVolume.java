/**
 * @author Tristan Gaeta
 * @version 09-11-2023
 * 
 */
package com.jigl.collisions;

import org.joml.Math;
import org.joml.Matrix3f;
import org.joml.Vector3f;

import com.jigl.Scratch;

public abstract class BoundingVolume implements Intersectable {
    protected Vector3f center;
    protected float volume;

    public float getVolume() {
        return this.volume;
    }

    public Vector3f getCenter(Vector3f dest) {
        return dest.set(this.center);
    }

    public float distance(BoundingVolume other) {
        return this.center.distance(other.center);
    }

    public float distanceSquared(BoundingVolume other) {
        return this.center.distanceSquared(other.center);
    }

    public static boolean intersects(BoundingSphere s1, BoundingSphere s2) {
        float SqDist = s1.distanceSquared(s2);
        float minDist = (s1.radius + s2.radius) * (s1.radius + s2.radius);
        return SqDist <= minDist;
    }

    public static boolean intersects(BoundingBox b1, BoundingBox b2) {
        // perform separation of axes
        float r1, r2; // projected radii
        Matrix3f rotation = Scratch.MAT3.next().rotation(b2.orientation).transpose().rotate(b1.orientation);
        Matrix3f absRotation = Scratch.MAT3.next();
        Vector3f tmp = Scratch.VEC3.next();
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                absRotation.set(i, j, Math.abs(rotation.get(i, j)) + 1e-12f);
        Vector3f translation = b2.getCenter(Scratch.VEC3.next()).sub(b1.getCenter(tmp))
                .rotate(b1.orientation);
        // axes of b1
        for (int i = 0; i < 3; i++) {
            r1 = b1.halfWidths.get(i);
            r2 = b2.halfWidths.dot(absRotation.getColumn(i, tmp));
            if (Math.abs(translation.get(i)) > r1 + r2)
                return false;
        }
        // axes of b2
        for (int i = 0; i < 3; i++) {
            r1 = b1.halfWidths.dot(absRotation.getRow(i, tmp));
            r2 = b2.halfWidths.get(i);
            if (Math.abs(translation.dot(rotation.getRow(i, tmp))) > r1 + r2)
                return false;
        }
        // 9 axes orthogonal each pair from b1 & b2
        r1 = b1.halfWidths.y * absRotation.m20 + b1.halfWidths.z * absRotation.m10;
        r2 = b2.halfWidths.y * absRotation.m02 + b2.halfWidths.z * absRotation.m01;
        if (Math.abs(translation.z * rotation.m10 - translation.y * rotation.m20) > r1 + r2)
            return false;

        r1 = b1.halfWidths.y * absRotation.m21 + b1.halfWidths.z * absRotation.m11;
        r2 = b2.halfWidths.x * absRotation.m02 + b2.halfWidths.z * absRotation.m00;
        if (Math.abs(translation.z * rotation.m11 - translation.y * rotation.m21) > r1 + r2)
            return false;

        r1 = b1.halfWidths.y * absRotation.m22 + b1.halfWidths.z * absRotation.m12;
        r2 = b2.halfWidths.x * absRotation.m01 + b2.halfWidths.y * absRotation.m00;
        if (Math.abs(translation.z * rotation.m12 - translation.y * rotation.m22) > r1 + r2)
            return false;

        r1 = b1.halfWidths.x * absRotation.m20 + b1.halfWidths.z * absRotation.m00;
        r2 = b2.halfWidths.y * absRotation.m12 + b2.halfWidths.z * absRotation.m11;
        if (Math.abs(translation.x * rotation.m20 - translation.z * rotation.m00) > r1 + r2)
            return false;

        r1 = b1.halfWidths.x * absRotation.m21 + b1.halfWidths.z * absRotation.m01;
        r2 = b2.halfWidths.x * absRotation.m12 + b2.halfWidths.y * absRotation.m10;
        if (Math.abs(translation.x * rotation.m21 - translation.z * rotation.m01) > r1 + r2)
            return false;

        r1 = b1.halfWidths.x * absRotation.m22 + b1.halfWidths.z * absRotation.m02;
        r2 = b2.halfWidths.x * absRotation.m11 + b2.halfWidths.y * absRotation.m10;
        if (Math.abs(translation.x * rotation.m22 - translation.z * rotation.m02) > r1 + r2)
            return false;

        r1 = b1.halfWidths.x * absRotation.m10 + b1.halfWidths.y * absRotation.m00;
        r2 = b2.halfWidths.y * absRotation.m22 + b2.halfWidths.z * absRotation.m21;
        if (Math.abs(translation.y * rotation.m00 - translation.x * rotation.m10) > r1 + r2)
            return false;

        r1 = b1.halfWidths.x * absRotation.m11 + b1.halfWidths.y * absRotation.m01;
        r2 = b2.halfWidths.x * absRotation.m22 + b2.halfWidths.z * absRotation.m20;
        if (Math.abs(translation.y * rotation.m01 - translation.x * rotation.m11) > r1 + r2)
            return false;

        r1 = b1.halfWidths.x * absRotation.m12 + b1.halfWidths.y * absRotation.m02;
        r2 = b2.halfWidths.x * absRotation.m21 + b2.halfWidths.y * absRotation.m20;
        if (Math.abs(translation.y * rotation.m02 - translation.x * rotation.m12) > r1 + r2)
            return false;

        Scratch.MAT3.free(rotation, absRotation);
        Scratch.VEC3.free(translation, tmp);

        return true;
    }

    public static boolean intersects(BoundingSphere s, BoundingBox b) {
        Vector3f tmp = Scratch.VEC3.next();
        float d2 = b.closestPoint(s.center, tmp).distanceSquared(s.center);
        Scratch.VEC3.free(tmp);
        return d2 <= s.radius * s.radius;
    }
}
