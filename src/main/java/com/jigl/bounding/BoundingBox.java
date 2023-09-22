package com.jigl.bounding;

import org.joml.Math;

import com.jigl.math.Mat3;
import com.jigl.math.Quat;
import com.jigl.math.Scratch;
import com.jigl.math.Vec3;

/**
 * 
 * 
 * @author Tristan Gaeta
 * @version 09-11-2023
 * 
 */
public class BoundingBox extends BoundingVolume {
    protected Vec3 halfWidths;
    protected Quat orientation;

    public BoundingBox(Vec3 anchor, Quat focus, Vec3 halfWidths) {
        this.center = anchor;
        this.orientation = focus;
        this.halfWidths = halfWidths;
        this.volume = 8f * halfWidths.x * halfWidths.y * halfWidths.z;
    }

    public Vec3 closestPoint(Vec3 pt, Vec3 dest) {
        try (Vec3 translation = Scratch.VEC3.next();
                Mat3 rotation = Scratch.MAT3.next();
                Vec3 axis = Scratch.VEC3.next();) {
            pt.sub(this.center, translation);
            this.orientation.get(rotation);
            dest.set(this.center);

            for (int i = 0; i < 3; i++) {
                rotation.getColumn(i, axis);
                float dist = translation.dot(axis);
                dist = Math.clamp(-this.halfWidths.get(i), this.halfWidths.get(i), dist);
                dest.fma(dist, axis);
            }
        }

        return dest;
    }
}
